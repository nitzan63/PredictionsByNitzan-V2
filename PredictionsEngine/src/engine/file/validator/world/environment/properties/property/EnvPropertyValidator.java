package engine.file.validator.world.environment.properties.property;

import engine.file.validator.world.utils.NameValidator;
import engine.file.validator.world.utils.RangeValidator;
import scheme.generated.PRDEnvProperty;

import javax.xml.bind.ValidationException;

public class EnvPropertyValidator {
    public static void validateEnvProperty (PRDEnvProperty property) throws ValidationException {

        String propertyType = property.getType();

        if (!"boolean".equals(propertyType) && !"decimal".equals(propertyType) && !"float".equals(propertyType) && !"string".equals(propertyType)) {
            throw new ValidationException("Invalid type attribute: " + propertyType);
        }

        try {
            RangeValidator.validateRange(property.getPRDRange());
            NameValidator.validateName(property.getPRDName());
        } catch (ValidationException e) {
            throw new ValidationException("EnvProperty: " + property.getPRDName() + " -> ", e);
        }
    }
}
