package thread;

import model.Matrix;
import utils.Pair;

import java.util.List;

import static utils.Constants.NUMBER_OF_THREADS;

public abstract class MultiplicationThread extends Thread {
    protected final Integer threadIndex;
    protected final Matrix resultMatrix;
    private final Matrix firstMatrix;
    private final Matrix secondMatrix;
    private final Integer numberOfThreads;
    protected Integer computations;
    protected int rowStart;
    protected int columnStart;

    public MultiplicationThread(Integer threadIndex, Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix) throws Exception {
        this.threadIndex = threadIndex;
        this.firstMatrix = firstMatrix;
        this.secondMatrix = secondMatrix;
        this.resultMatrix = resultMatrix;
        this.numberOfThreads = NUMBER_OF_THREADS;
        this.computations = resultMatrix.getSize() / this.numberOfThreads;
        this.getStart();
    }

    public abstract List<Pair<Integer, Integer>> compute();

    public abstract Pair<Integer, Integer> getStart();

    @Override
    public void run() {
        compute().forEach(p -> multiply(p.getFirst(), p.getSecond()));
    }

    private Integer multiply(Integer row, Integer column) {
        int result = 0;
        for (int i = 0; i < this.firstMatrix.getColumns(); i++) {
            result += this.firstMatrix.getElementAt(row, i) * this.secondMatrix.getElementAt(i, column);
        }
        this.resultMatrix.setValue(row, column, result);
        return result;
    }
}
