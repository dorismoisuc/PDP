package thread;

import model.Matrix;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;

import static utils.Constants.NUMBER_OF_THREADS;

public class KthMultiplicationThread extends MultiplicationThread {
    public KthMultiplicationThread(Integer threadIndex, Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix) throws Exception {
        super(threadIndex, firstMatrix, secondMatrix, resultMatrix);
    }

    @Override
    public List<Pair<Integer, Integer>> compute() {
        List<Pair<Integer, Integer>> positions = new ArrayList<>();
        int computations = this.computations;
        while (computations > 0) {
            positions.add(new Pair<>(this.rowStart, this.columnStart));
            this.rowStart += (this.columnStart + NUMBER_OF_THREADS) / this.resultMatrix.getLines();
            this.columnStart = (this.columnStart + NUMBER_OF_THREADS) % this.resultMatrix.getLines();
            computations -= 1;
        }
        return positions;
    }

    @Override
    public Pair<Integer, Integer> getStart() {
        if (this.threadIndex < this.resultMatrix.getSize() % NUMBER_OF_THREADS) {
            this.computations += 1;
        }
        this.rowStart = this.threadIndex / this.resultMatrix.getLines();
        this.columnStart = this.threadIndex % this.resultMatrix.getLines();
        return new Pair<>(this.rowStart, this.columnStart);
    }
}
