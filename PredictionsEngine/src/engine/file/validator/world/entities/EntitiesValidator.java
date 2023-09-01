package engine.file.validator.world.entities;

import engine.file.validator.world.entities.entity.EntityValidator;
import scheme.v1.generated.PRDEntities;
import scheme.v1.generated.PRDEntity;

import javax.xml.bind.ValidationException;

public class EntitiesValidator {
    public static void validateEntities(PRDEntities entities) throws ValidationException {
        for (PRDEntity entity : entities.getPRDEntity())
            EntityValidator.validateEntity(entity);
    }
}
