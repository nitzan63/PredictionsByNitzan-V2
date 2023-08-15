package engine.file.validator.world.utils;

import scheme.generated.PRDValue;

import javax.xml.bind.ValidationException;

public class ValueValidator {
    public static void validateValue (PRDValue value) throws ValidationException {
        if (value.isRandomInitialize() && value.getInit() != null)
            throw new ValidationException("property can't have init value and set to random at the same time!");
        if (!value.isRandomInitialize() && value.getInit() == null)
            throw new ValidationException("property can't be not random initialized and have no init value!");
    }
}
