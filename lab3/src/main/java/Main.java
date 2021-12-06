import model.Matrix;
import thread.ColumnMultiplicationThread;
import thread.KthMultiplicationThread;
import thread.RowMultiplicationThread;
import utils.ThreadRunner;

import java.util.Arrays;
import java.util.Scanner;

import static utils.Constants.MATRIX_DIMENSION;

public class Main {
    public static void main(String[] args) {
        Matrix firstMatrix = initialMatrix();
        Matrix secondMatrix = initialMatrix();
        Matrix resultMatrix = new Matrix(firstMatrix.getLines(), secondMatrix.getColumns());
        ThreadRunner rowThreadRunner = new ThreadRunner(firstMatrix, secondMatrix, resultMatrix, RowMultiplicationThread.class);
        ThreadRunner columnThreadRunner = new ThreadRunner(firstMatrix, secondMatrix, resultMatrix, ColumnMultiplicationThread.class);
        ThreadRunner kthThreadRunner = new ThreadRunner(firstMatrix, secondMatrix, resultMatrix, KthMultiplicationThread.class);
        try {
            Scanner console = new Scanner(System.in);
            System.out.println("Choose multiplication function:\n1.Row multiplication\n2.Column multiplication\n3.Kth multiplication\n>");
            String input = console.next();
            switch (input) {
                case "1": {
                    rowThreadRunner.runThreads();
                    rowThreadRunner.runThreadsPool();
                    break;
                }
                case "2": {
                    columnThreadRunner.runThreads();
                    columnThreadRunner.runThreadsPool();
                    break;
                }
                case "3": {
                    kthThreadRunner.runThreads();
                    kthThreadRunner.runThreadsPool();
                    break;
                }
                default: {
                    System.out.println("Invalid option!");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Matrix initialMatrix() {
        Integer[] initialValues = new Integer[MATRIX_DIMENSION * MATRIX_DIMENSION];
        Arrays.fill(initialValues, 1);
        return new Matrix(MATRIX_DIMENSION, MATRIX_DIMENSION, initialValues);
    }
}
