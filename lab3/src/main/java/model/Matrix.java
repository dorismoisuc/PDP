package model;

public class Matrix {
    private final int lines;
    private final int columns;
    private final Integer[][] matrix;

    public Matrix(int lines, int columns) {
        this.lines = lines;
        this.columns = columns;
        this.matrix = new Integer[lines][columns];
        for (int i = 0; i < lines; i++)
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = 0;
            }
    }

    public Matrix(int lines, int columns, Integer[] values) {
        this.lines = lines;
        this.columns = columns;
        this.matrix = new Integer[lines][columns];
        int valuesIndex = 0;
        for (int i = 0; i < lines; i++)
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = values[valuesIndex++];
            }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                result.append(matrix[i][j]).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public int getLines() {
        return lines;
    }

    public int getColumns() {
        return columns;
    }

    public Integer getSize() {
        return this.lines * this.columns;
    }

    public int getElementAt(int row, int column) {
        return this.matrix[row][column];
    }

    public void setValue(Integer row, Integer column, int result) {
        this.matrix[row][column] = result;
    }
}
