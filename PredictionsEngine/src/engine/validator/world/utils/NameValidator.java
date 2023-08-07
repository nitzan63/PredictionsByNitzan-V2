package engine.validator.world.utils;

import javax.xml.bind.ValidationException;

public class NameValidator {
    public static void validateName (String name) throws ValidationException {
        if (name.isEmpty())
            throw new ValidationException("Name can't be empty! -> ");
        if (hasInValidChar(name))
            throw new ValidationException("Name has invalid characters! -> ");
    }

    private static boolean hasInValidChar(String name){
        return name.matches("[A-Za-z0-9]+");
    }
}
