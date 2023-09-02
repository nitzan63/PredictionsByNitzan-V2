package engine.file.validator.world.environment;

import engine.file.validator.world.environment.properties.EnvPropertiesValidator;
import scheme.generated.PRDEnvironment;

import javax.xml.bind.ValidationException;

public class EnvironmentValidator {
    public static void validateEnvironment (PRDEnvironment environment) throws ValidationException {
        if (environment == null)
            throw new ValidationException("Environment is missing! ");
        else {
            EnvPropertiesValidator.validateEnvProperties(environment.getPRDEnvProperty());
        }

    }
}
