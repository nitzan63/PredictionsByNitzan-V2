package dto;

public class GridDTO {
    private final int cols;
    private final int rows;

    private final int gridSize;
    public GridDTO(int rows, int cols) {
        this.cols = cols;
        this.rows = rows;
        gridSize = rows*cols;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getGridSize() {
        return gridSize;
    }
}
