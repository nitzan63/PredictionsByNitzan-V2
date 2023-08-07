package engine.validator.world.entities.entity.properties;

import engine.validator.world.entities.entity.properties.property.PropertyValidator;
import scheme.generated.PRDProperties;
import scheme.generated.PRDProperty;

import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PropertiesValidator {
    public static void validateProperties(PRDProperties properties) throws ValidationException {
        List<PRDProperty> propertyList = properties.getPRDProperty();
        // check if there are properties at all:
        if (propertyList == null || propertyList.isEmpty())
            throw new ValidationException("property elements are missing.");

        // set for checking for name duplicates in properties
        Set<String> propertyNames = new HashSet<>();

        for (PRDProperty property : propertyList) {
            String propertyName = property.getPRDName();
            if (propertyNames.contains(propertyName)) {
                throw new ValidationException("Duplicate property name found: " + propertyName);
            }
            propertyNames.add(propertyName);

            PropertyValidator.validateProperty(property);
        }

    }
}
