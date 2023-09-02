package engine.file.validator.world;

import engine.file.validator.world.entities.EntitiesValidator;
import engine.file.validator.world.environment.EnvironmentValidator;
import engine.file.validator.world.grid.GridValidator;
import engine.file.validator.world.rules.RulesValidator;
import engine.file.validator.world.termination.TerminationValidator;
import scheme.generated.PRDWorld;

import javax.xml.bind.ValidationException;

import static java.util.Arrays.stream;

public class WorldValidator {
    private static PRDWorld currentWorld;
    public static void validateWorld(PRDWorld world) throws ValidationException {
        currentWorld = world;
        EnvironmentValidator.validateEnvironment(world.getPRDEnvironment());
        EntitiesValidator.validateEntities(world.getPRDEntities());
        RulesValidator.validateRules(world.getPRDRules());
        TerminationValidator.validateTermination(world.getPRDTermination());
        GridValidator.validateGrid(world.getPRDGrid());
    }

    // Auxiliary method to check if an entity exists
    public static boolean entityExists(String entityName) {
        return currentWorld.getPRDEntities().getPRDEntity().stream()
                .anyMatch(entity -> entityName.equals(entity.getName()));
    }

    // Auxiliary method to check if a property of a given entity exists
    public static boolean propertyExists(String entityName, String propertyName) {
        return currentWorld.getPRDEntities().getPRDEntity().stream()
                .filter(entity -> entityName.equals(entity.getName()))
                .anyMatch(entity -> entity.getPRDProperties().getPRDProperty().stream()
                        .anyMatch(property -> propertyName.equals(property.getPRDName())));
    }

}
