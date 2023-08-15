package engine.file.validator;

import engine.file.validator.world.WorldValidator;
import scheme.generated.PRDWorld;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;

public class XMLValidator {
    public static List<ValidationException> validateXML(PRDWorld world){
        List<ValidationException> exceptions = new ArrayList<>();
        try {
            WorldValidator.validateWorld(world);
        } catch (ValidationException e){
            exceptions.add(e);
        }

        return exceptions;
    }
}
