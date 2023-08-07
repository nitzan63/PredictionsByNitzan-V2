package engine.validator;

import engine.validator.world.WorldValidator;
import scheme.generated.PRDWorld;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;

public class XMLValidator {
    public static boolean validateXML(PRDWorld world){
        List<ValidationException> exceptions = new ArrayList<>();
        try {
            WorldValidator.validateWorld(world);
        } catch (ValidationException e){
            exceptions.add(e);
        }

        if (!exceptions.isEmpty()){
            for (ValidationException e : exceptions){
                //TODO handle the exceptions
            }
            return false;
        }
        return true;
    }
}
