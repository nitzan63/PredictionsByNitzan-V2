package engine.file.mapper.world.entities.entity.properties.property;

import engine.file.mapper.world.utils.range.RangeMapper;
import scheme.generated.PRDProperty;
import world.entities.entity.properties.property.api.EntityProperty;
import world.property.impl.PropertyBool;
import world.property.impl.PropertyDecimal;
import world.property.impl.PropertyFloat;
import world.property.impl.PropertyString;
import world.utils.range.Range;

public class EntityPropertyMapper {
    public static EntityProperty mapProperty(PRDProperty jaxbProperty) {
        EntityProperty property = null;
        // get the data from jaxb class:
        String name = jaxbProperty.getPRDName();
        String type = jaxbProperty.getType();
        String init = jaxbProperty.getPRDValue().getInit();
        boolean isRandom = jaxbProperty.getPRDValue().isRandomInitialize();

        switch (type) {
            case "string":
                if (isRandom) {
                    property = new PropertyString(name);
                } else {
                    property = new PropertyString(name, false, init);
                }
                break;
            case "boolean":
                if (isRandom) {
                    property = new PropertyBool(name);
                } else {
                    if (init.equals("false")) {
                        property = new PropertyBool(name, false, false);
                    } else if (init.equals("true")) {
                        property = new PropertyBool(name, false, true);
                    }  //TODO handle this exception - invalid boolean value
                }
                break;
            case "decimal": {
                Range<Integer> range = RangeMapper.decimalMapRange(jaxbProperty.getPRDRange());
                try {

                    if (isRandom) {
                        property = new PropertyDecimal(name, range);
                    } else {
                        Integer intInit = Integer.parseInt(init);
                        property = new PropertyDecimal(name, false, intInit, range);
                    }
                    //TODO make sure this works outside the try scope
                } catch (NumberFormatException e) {
                    throw e; //TODO handle this exception
                }
                break;
            }
            case "float":
                Range<Float> range = RangeMapper.floatMapRange(jaxbProperty.getPRDRange());
                try {
                    if (isRandom) {
                        property = new PropertyFloat(name, range);
                    } else {
                        Float floatInit = Float.parseFloat(init);
                        property = new PropertyFloat(name, false, floatInit, range);
                    }
                    //TODO make sure this works outside the try scope
                } catch (NumberFormatException e) {
                    throw e; //TODO handle this exception
                }
                break;
        }
        return property;
    }
}
