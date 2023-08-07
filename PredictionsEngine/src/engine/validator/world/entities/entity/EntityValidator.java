package engine.validator.world.entities.entity;

import engine.validator.world.entities.entity.population.PopulationValidator;
import engine.validator.world.entities.entity.properties.PropertiesValidator;
import engine.validator.world.utils.NameValidator;
import scheme.generated.PRDEntity;

import javax.xml.bind.ValidationException;

public class EntityValidator {
    public static void validateEntity(PRDEntity entity) throws ValidationException {
        try {
            PopulationValidator.validatePopulation(entity.getPRDPopulation());
            PropertiesValidator.validateProperties(entity.getPRDProperties());
            NameValidator.validateName(entity.getName());
        } catch (ValidationException e){
            throw new ValidationException(" Entity: " + entity.getName() + " -> ", e);
        }
    }
}
