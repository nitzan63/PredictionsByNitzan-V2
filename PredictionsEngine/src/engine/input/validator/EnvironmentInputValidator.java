package engine.input.validator;

import dto.UserEnvironmentInputDTO;
import world.environment.properties.EnvProperties;
import world.environment.properties.property.api.EnvProperty;
import world.utils.range.Range;

import java.util.Map;

public class EnvironmentInputValidator {

    public static void validateInput(UserEnvironmentInputDTO input, EnvProperties properties) {
        for (Map.Entry<String, String> entry : input.getUserInputProperties().entrySet()) {
            String propertyName = entry.getKey();
            String stringValue = entry.getValue();

            EnvProperty property = properties.getProperty(propertyName);
            if (property == null) {
                throw new IllegalArgumentException("Invalid property name: " + propertyName);
            }

            String expectedType = property.getType();
            Object value;
            try {
                value = parseValue(stringValue, expectedType);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid value format for property " + propertyName + ": " + stringValue);
            }

            // Check if the value is within the allowed range (for decimals and floats)
            if (expectedType.equals("decimal") || expectedType.equals("float")) {
                Range<?> range = property.getRange();
                double numValue = ((Number) value).doubleValue();
                if (numValue < ((Number) range.getFrom()).doubleValue() || numValue > ((Number) range.getTo()).doubleValue()) {
                    throw new IllegalArgumentException("Invalid value for property " + propertyName + ": value out of range");
                }
            }
        }
    }

    public static Object parseValue(String stringValue, String expectedType) throws NumberFormatException {
        if (expectedType.equals("decimal")) {
            return Double.parseDouble(stringValue);
        } else if (expectedType.equals("float")) {
            return Float.parseFloat(stringValue);
        } else if (expectedType.equals("string")) {
            return stringValue;
        } else if (expectedType.equals("boolean")) {
            return Boolean.parseBoolean(stringValue.toLowerCase());
        }
        throw new IllegalArgumentException("Unsupported type: " + expectedType);
    }
}
