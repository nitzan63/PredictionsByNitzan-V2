package engine.validator.world.rules.rule.activation;

import scheme.generated.PRDActivation;

import javax.xml.bind.ValidationException;

public class ActivationValidator {
    public static void validateActivation(PRDActivation activation) throws ValidationException {
        // Ensure the activation object exists
        if (activation == null) {
            throw new ValidationException("Activation is missing.");
        }

        // Validate 'ticks' attribute
        if (activation.getTicks() != null) {
            if (activation.getTicks() < 0) {
                throw new ValidationException("Ticks value should not be negative.");
            }
            if (activation.getTicks() == 0) {
                throw new ValidationException("Ticks value should be greater than 0.");
            }
        }

        // Validate 'probability' attribute
        if (activation.getProbability() != null) {
            if (activation.getProbability() < 0.0 || activation.getProbability() > 1.0) {
                throw new ValidationException("Probability should be between 0 and 1 inclusive.");
            }
        }
    }
}
