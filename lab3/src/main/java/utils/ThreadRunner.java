package utils;

import model.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static utils.Constants.MATRIX_DIMENSION;
import static utils.Constants.NUMBER_OF_THREADS;

public class ThreadRunner {
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;
    private final Matrix resultMatrix;
    private final Class threadClass;


    public ThreadRunner(Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix, Class threadClass) {
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
        this.threadClass = threadClass;
    }

    public void runThreads() throws Exception {
        long startTime = System.nanoTime();
        List<Thread> threads = new ArrayList<>();
        for (int index = 0; index < NUMBER_OF_THREADS; index++) {
            Thread t = (Thread) this.threadClass.getDeclaredConstructor(Integer.class, Matrix.class, Matrix.class, Matrix.class).newInstance(index, this.firstMatrix, this.secondMatrix, this.resultMatrix);
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
        checkResultMatrix();
        System.out.printf("Execution of threads took %d milliseconds%n", (System.nanoTime() - startTime) / 1000000);
    }

    public void runThreadsPool() throws Exception {
        long startTime = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        for (int index = 0; index < NUMBER_OF_THREADS; index++) {
            executorService.submit((Thread) this.threadClass.getDeclaredConstructor(Integer.class, Matrix.class, Matrix.class, Matrix.class).newInstance(index, this.firstMatrix, this.secondMatrix, this.resultMatrix));
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.MINUTES)) {
            executorService.shutdownNow();
        }
        checkResultMatrix();
        System.out.printf("Execution of threads with pool took %d milliseconds%n", (System.nanoTime() - startTime) / 1000000);
    }


    /**
     * Check result matrix for the case when the matrices are square, with 1 on every position
     *
     * @throws Exception
     */
    public void checkResultMatrix() throws Exception {
        for (int i = 0; i < this.resultMatrix.getLines(); i++)
            for (int j = 0; j < this.resultMatrix.getColumns(); j++)
                if (this.resultMatrix.getElementAt(i, j) != MATRIX_DIMENSION) {
                    throw new Exception(String.format("Oops! Invalid element %d!", this.resultMatrix.getElementAt(i, j)));
                }
        System.out.println("Valid multiplication result!");
    }
}
