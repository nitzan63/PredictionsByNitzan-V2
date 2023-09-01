package engine.file.validator.world.utils;

import scheme.v1.generated.PRDRange;

import javax.xml.bind.ValidationException;

public class RangeValidator {
    public static void validateRange (PRDRange range) throws ValidationException{
        if (range != null) {
            if (range.getTo() < range.getFrom())
                throw new ValidationException("In Range, the TO value is bigger than the FROM value! -> ");
            if (range.getTo() == 0 && range.getFrom() == 0)
                throw new ValidationException("In range, TO and FROM equals 0 -> ");
        }
    }
}
