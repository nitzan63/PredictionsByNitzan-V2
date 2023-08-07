package engine.validator.world.environment;

import scheme.generated.PRDEvironment;

import javax.xml.bind.ValidationException;

public class EnvironmentValidator {
    public static void validateEnvironment (PRDEvironment environment) throws ValidationException {
        if (environment == null)
            throw new ValidationException("Environment is missing! ");


    }
}
