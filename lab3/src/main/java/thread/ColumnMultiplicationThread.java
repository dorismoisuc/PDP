package thread;

import model.Matrix;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;

import static utils.Constants.NUMBER_OF_THREADS;

public class ColumnMultiplicationThread extends MultiplicationThread{
    public ColumnMultiplicationThread(Integer threadIndex, Matrix firstMatrix, Matrix secondMatrix, Matrix resultMatrix) throws Exception {
        super(threadIndex, firstMatrix, secondMatrix, resultMatrix);
    }

    @Override
    public List<Pair<Integer, Integer>> compute() {
        List<Pair<Integer, Integer>> positions = new ArrayList<>();
        int computations = this.computations;
        while (computations > 0) {
            if(this.rowStart == this.resultMatrix.getLines()) {
                this.rowStart = 0;
                this.columnStart += 1;
            }
            positions.add(new Pair<>(this.rowStart, this.columnStart));
            this.rowStart += 1;
            computations -= 1;
        }
        return positions;
    }

    @Override
    public Pair<Integer, Integer> getStart() {
        this.rowStart = this.computations * this.threadIndex % this.resultMatrix.getColumns();
        this.columnStart = this.computations * this.threadIndex / this.resultMatrix.getColumns();
        if (this.threadIndex + 1 == NUMBER_OF_THREADS) {
            this.computations += this.resultMatrix.getSize() % NUMBER_OF_THREADS;
        }
        return new Pair<>(this.rowStart, this.columnStart);
    }
}
