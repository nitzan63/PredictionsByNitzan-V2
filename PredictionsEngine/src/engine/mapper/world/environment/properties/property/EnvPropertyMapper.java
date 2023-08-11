package engine.mapper.world.environment.properties.property;

import engine.mapper.world.utils.range.RangeMapper;
import scheme.generated.PRDEnvProperty;
import world.environment.properties.property.api.EnvProperty;
import world.property.impl.PropertyBool;
import world.property.impl.PropertyDecimal;
import world.property.impl.PropertyFloat;
import world.property.impl.PropertyString;
import world.utils.range.Range;

public class EnvPropertyMapper {
    public static EnvProperty mapEnvProperty (PRDEnvProperty jaxbProperty){
        String type = jaxbProperty.getType();
        String name = jaxbProperty.getPRDName();
        EnvProperty property = null;
        switch (type){
            case "string":
                property = new PropertyString(name);
                break;
            case "decimal":
                Range<Integer> decimalRange = RangeMapper.decimalMapRange(jaxbProperty.getPRDRange());
                property = new PropertyDecimal(name, decimalRange);
                break;
            case "float":
                Range<Float> floatRange = RangeMapper.floatMapRange(jaxbProperty.getPRDRange());
                property = new PropertyFloat(name, floatRange);
                break;
            case "boolean":
                property = new PropertyBool(name);
                break;
        }
        return property;
    }
}
