package world.grid;

import world.entities.entity.EntityInstance;

public class Grid {
    private final int cols;
    private final int rows;
    private final EntityInstance[][] cells;

    public Grid(int columns, int rows) {
        this.cols = columns;
        this.rows = rows;
        this.cells = new EntityInstance[rows][columns];
    }

    public boolean placeEntityAt(EntityInstance entityInstance, int row, int col){
        if (cells[row][col] == null){
            cells[row][col] = entityInstance;
            return true;
        } else return false;
    }

    public boolean addEntityToGrid(EntityInstance entity){
        return placeEntityAt(entity, entity.getRow(), entity.getCol());
    }

    public boolean removeEntityFromGrid(EntityInstance entity) {
        int row = entity.getRow();
        int col = entity.getCol();
        if (cells[row][col] == entity) {
            cells[row][col] = null; // remove the entity
            return true; // Entity found and removed
        }
        return false; // Entity not found in grid
    }

    public boolean isCellFree(int row, int col){
        return (cells[row][col] == null);
    }

    public EntityInstance getEntityAt(int row, int col){
        return cells[row][col];
    }

    public EntityInstance removeEntityAt(int row, int col){
        EntityInstance temp = cells[row][col];
        cells[row][col] = null;
        return temp;
    }
    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public int getGridSize(){
        return rows* cols;
    }

    public enum Direction {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        private final int rowChange;
        private final int colChange;

        Direction(int rowChange, int colChange) {
            this.rowChange = rowChange;
            this.colChange = colChange;
        }

        public int getRowChange() {
            return rowChange;
        }

        public int getColChange() {
            return colChange;
        }
    }

}
