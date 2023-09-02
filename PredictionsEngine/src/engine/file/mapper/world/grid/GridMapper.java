package engine.file.mapper.world.grid;

import scheme.generated.PRDWorld;
import world.grid.Grid;

public class GridMapper {
    public static Grid mapGrid (PRDWorld.PRDGrid jaxbGrid){
        return new Grid(jaxbGrid.getRows(), jaxbGrid.getColumns());
    }
}
