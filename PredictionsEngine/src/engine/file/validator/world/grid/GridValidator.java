package engine.file.validator.world.grid;

import scheme.generated.PRDWorld;

import javax.xml.bind.ValidationException;

public class GridValidator {
    public static void validateGrid(PRDWorld.PRDGrid grid) throws ValidationException {
        if (grid == null)
            throw new ValidationException("Grid is Missing");
        int rows = grid.getRows();
        int cols = grid.getColumns();

        if (rows > 100 || rows < 10)
            throw new ValidationException("Rows number is invalid (10-100): " + rows);
        if (cols > 100 || cols < 10)
            throw new ValidationException("Cols number is invalid (10-100): " + cols);
    }
}
