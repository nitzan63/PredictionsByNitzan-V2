package engine.file.validator;

import engine.file.validator.world.WorldValidator;
import scheme.generated.PRDWorld;

import javax.xml.bind.ValidationException;

public class XMLValidator {
    public static void validateXML(PRDWorld world) throws ValidationException{
            WorldValidator.validateWorld(world);
    }
}
