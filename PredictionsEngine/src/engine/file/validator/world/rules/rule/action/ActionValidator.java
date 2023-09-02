package engine.file.validator.world.rules.rule.action;

import engine.file.validator.world.WorldValidator;
import scheme.generated.PRDAction;

import javax.xml.bind.ValidationException;
import java.util.Arrays;
import java.util.List;

public class ActionValidator {

    // Static method to validate PRDAction
    public static void validateAction(PRDAction action) throws ValidationException {
        // Check if action type is valid
        if (action.getType() == null || action.getType().isEmpty()) {
            throw new ValidationException("Action type is missing");
        }

        // Check if action type value is in the list of accepted values
        List<String> validActionTypes = Arrays.asList("calculation", "condition", "decrease", "increase", "kill", "set");
        if (!validActionTypes.contains(action.getType().toLowerCase())) {
            throw new ValidationException("Invalid action type: " + action.getType());
        }

        // Check if entity is present, as it's required
        if (action.getEntity() == null || action.getEntity().isEmpty()) {
            throw new ValidationException("Action entity field is missing");
        }

        // Based on action type, perform specific validation
        switch (action.getType()) {
            case "calculation":
                // For calculation actions, ensure there's either a PRDDivide or PRDMultiply present
                if (action.getPRDDivide() == null && action.getPRDMultiply() == null) {
                    throw new ValidationException("Calculation action missing divide/multiply details");
                }
                break;
            case "condition":
                // For condition actions, ensure there's a PRDCondition, PRDThen, and optionally a PRDElse
                if (action.getPRDCondition() == null) {
                    throw new ValidationException("Condition action missing PRDCondition details");
                }
                if (action.getPRDThen() == null) {
                    throw new ValidationException("Condition action missing PRDThen details");
                }
                break;
            case "increase":
            case "decrease":
                if (action.getBy() == null)
                    throw new ValidationException("Action has missing 'byExpression' value");
                break;
        }

        // Check if action's entity exists
        if (!WorldValidator.entityExists(action.getEntity())) {
            throw new ValidationException("Entity '" + action.getEntity() + "' does not exist in world metadata");
        }

        // Check if action's property (if any) exists
        if (action.getProperty() != null && !action.getProperty().isEmpty() &&
                !WorldValidator.propertyExists(action.getEntity(), action.getProperty())) {
            throw new ValidationException("Property '" + action.getProperty() + "' does not exist for entity '"
                    + action.getEntity() + "' in world metadata");
        }
    }

}
