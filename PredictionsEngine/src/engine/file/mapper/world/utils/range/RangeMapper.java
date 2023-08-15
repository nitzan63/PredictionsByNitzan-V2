package engine.file.mapper.world.utils.range;

import scheme.generated.PRDRange;
import world.utils.range.Range;

public class RangeMapper {
    public static Range<Integer> decimalMapRange(PRDRange jaxbRange) {
        if (jaxbRange.getTo()- ((int)jaxbRange.getTo()) > 0){
            //TODO case where the range is a float and not decimal
        }
        if (jaxbRange.getFrom()- ((int)jaxbRange.getFrom()) > 0){
            //TODO case where the range is a float and not decimal
        }
        Integer from = (int)jaxbRange.getFrom();
        Integer to = (int)jaxbRange.getTo();
        return new Range<Integer>(from, to);
    }

    public static Range<Float> floatMapRange(PRDRange jaxbRange) {
        Float from = (float)jaxbRange.getFrom();
        Float to = (float)jaxbRange.getTo();
        return new Range<Float>(from, to);
    }
}
