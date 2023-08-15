package engine.mapper.world.rules.rule.action;

import scheme.generated.PRDAction;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.impl.DecreaseAction;

public class DecreaseActionMapper {
    public static DecreaseAction mapDecreaseAction(PRDAction jaxbAction, EntitiesDefinition entitiesContext) {
        String propertyName = jaxbAction.getProperty();
        String byExpression = jaxbAction.getBy();
        String entityName = jaxbAction.getEntity();
        return new DecreaseAction(entitiesContext, propertyName, byExpression, entityName);
    }
}
