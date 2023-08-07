package engine.validator.world.entities.entity.properties.property;

import engine.validator.world.utils.NameValidator;
import engine.validator.world.utils.RangeValidator;
import engine.validator.world.utils.ValueValidator;
import scheme.generated.PRDProperty;

import javax.xml.bind.ValidationException;

public class PropertyValidator {
    public static void validateProperty(PRDProperty property) throws ValidationException {
        // Check if type attribute is valid
        String propertyType = property.getType();
        String init = property.getPRDValue().getInit();

        if (!"boolean".equals(propertyType) && !"decimal".equals(propertyType) && !"float".equals(propertyType) && !"string".equals(propertyType)) {
            throw new ValidationException("Invalid type attribute: " + propertyType);
        }
        if (property.getPRDValue().getInit() != null)
            if (!isValueCompatibleWithPropertyType(propertyType, init))
                throw new ValidationException("init type does not match the property type! in: " + property.getPRDName());

        try {
            ValueValidator.validateValue(property.getPRDValue());
            RangeValidator.validateRange(property.getPRDRange());
            NameValidator.validateName(property.getPRDName());
        } catch (ValidationException e) {
            throw new ValidationException("Property: " + property.getPRDName() + " -> ", e);
        }

    }

    private static boolean isValueCompatibleWithPropertyType(String propertyType, String initValueInString) {
        switch (propertyType) {
            case "boolean":
                return "true".equalsIgnoreCase(initValueInString) || "false".equalsIgnoreCase(initValueInString);
            case "decimal":
                try {
                    Double.parseDouble(initValueInString);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case "float":
                try {
                    Float.parseFloat(initValueInString);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case "string":
                return initValueInString != null;
            default:
                return false; // Invalid property type
        }
    }

}
