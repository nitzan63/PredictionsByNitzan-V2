package engine.validator.world;

import engine.validator.world.entities.EntitiesValidator;
import engine.validator.world.environment.EnvironmentValidator;
import scheme.generated.PRDWorld;
import world.environment.Environment;

import javax.xml.bind.ValidationException;

public class WorldValidator {
    public static void validateWorld(PRDWorld world) throws ValidationException {
        EnvironmentValidator.validateEnvironment(world.getPRDEvironment());
        EntitiesValidator.validateEntities(world.getPRDEntities());
        //RulesValidator.validateRules(world.getPRDRules());
        //TerminationValidator.validateTermination(world.getPRDTermination()));
    }
}
