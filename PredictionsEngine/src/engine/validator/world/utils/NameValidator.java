package engine.validator.world.utils;

import javax.xml.bind.ValidationException;

public class NameValidator {
    public static void validateName(String name) throws ValidationException {
        if (name.isEmpty())
            throw new ValidationException("Name can't be empty! -> ");
    }
}

