package world.generator;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.EntityProperties;
import world.entities.entity.properties.property.api.EntityProperty;
import world.grid.Grid;
import world.property.impl.PropertyBool;
import world.property.impl.PropertyDecimal;
import world.property.impl.PropertyFloat;
import world.property.impl.PropertyString;
import world.utils.range.Range;

import java.util.Random;

public class EntityGenerator {
    private Random rand = new Random();

    // Helper function to find an empty cell on the grid.
    private int[] findEmptyCell(Grid grid) {
        int row, col;
        do {
            row = rand.nextInt(grid.getRows());
            col = rand.nextInt(grid.getCols());
        } while (!grid.isCellFree(row, col));
        return new int[]{row, col};
    }

    public EntityInstance generateNewInstance(EntityInstance prototypeEntity, Grid grid, int serialNumber) {
        EntityProperties newProperties = new EntityProperties();
        for (EntityProperty prototypeProperty : prototypeEntity.getProperties().getProperties()) {

            // create new property template:
            EntityProperty newProperty = null;

            // gather properties from prototype:
            String name = prototypeProperty.getName();
            String type = prototypeProperty.getType();
            Range<?> range = prototypeProperty.getRange();
            boolean isRandom = prototypeProperty.isRandomInitialize();
            Object value = prototypeProperty.getValue();

            // create the new properties using the ctors (generates random values in ctor)
            switch (type) {
                case "string":
                    newProperty = new PropertyString(name, isRandom, (String) value);
                    break;
                case "boolean":
                    newProperty = new PropertyBool(name, isRandom, (Boolean) value);
                    break;
                case "decimal":
                    newProperty = new PropertyDecimal(name, isRandom, (Integer) value, (Range<Integer>) range);
                    break;
                case "float":
                    newProperty = new PropertyFloat(name, isRandom, (Float) value, (Range<Float>) range);
                    break;
            }

            // add the new property to the entity instance property list:
            newProperties.addProperty(newProperty);
        }

        // find empty cell for the entity:
        int [] emptyCell = findEmptyCell(grid);
        int row = emptyCell[0];
        int col = emptyCell[1];

        // generate the new entity!
        EntityInstance newInstance = new EntityInstance(serialNumber, newProperties, row, col);

        //place the entity on the grid:
        grid.addEntityToGrid(newInstance);

        return newInstance;

    }

    public EntityInstance replaceFromScratch(String entityName){

    }

    public EntityInstance replaceDerived (String entityName, EntityInstance baseInstance){

    }
}