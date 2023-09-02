package engine.file.validator.world.entities;

import engine.file.validator.world.entities.entity.EntityValidator;
import scheme.generated.PRDEntities;
import scheme.generated.PRDEntity;

import javax.xml.bind.ValidationException;

public class EntitiesValidator {
    public static void validateEntities(PRDEntities entities) throws ValidationException {
        for (PRDEntity entity : entities.getPRDEntity())
            EntityValidator.validateEntity(entity);
    }
}
