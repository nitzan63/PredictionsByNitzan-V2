package engine.file.mapper.world.rules.rule.action;

import scheme.generated.PRDAction;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.impl.DecreaseAction;
import world.rules.rule.action.secondary.SecondaryEntity;

public class DecreaseActionMapper {
    public static DecreaseAction mapDecreaseAction(PRDAction jaxbAction, SecondaryEntity secondaryEntity) {
        String propertyName = jaxbAction.getProperty();
        String byExpression = jaxbAction.getBy();
        String entityName = jaxbAction.getEntity();
        return new DecreaseAction(propertyName, byExpression, entityName, secondaryEntity);
    }
}
