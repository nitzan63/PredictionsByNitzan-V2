package engine.file.mapper.world.rules.rule.action;

import scheme.generated.PRDAction;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.impl.IncreaseAction;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.Map;

public class IncreaseActionMapper {
    public static IncreaseAction mapIncreaseAction(PRDAction jaxbAction, SecondaryEntity secondaryEntity) {
        String propertyName = jaxbAction.getProperty();
        String byExpression = jaxbAction.getBy();
        String entityName = jaxbAction.getEntity();
        return new IncreaseAction(propertyName, byExpression, entityName, secondaryEntity);
    }
}
