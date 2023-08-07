package engine.validator.world.environment.properties;

import engine.validator.world.environment.properties.property.EnvPropertyValidator;
import scheme.generated.PRDEnvProperty;

import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnvPropertiesValidator {
    public static void validateEnvProperties(List<PRDEnvProperty> properties) throws ValidationException {
        if (properties == null)
            throw new ValidationException("No environment properties found!");

        Set<String> propertyNames = new HashSet<>();

        for (PRDEnvProperty property : properties) {

            String propName = property.getPRDName();
            if (propertyNames.contains(propName))
                throw new ValidationException("Duplicate environment property name found: " + propName);
            propertyNames.add(propName);

            EnvPropertyValidator.validateEnvProperty(property);
        }
    }
}
