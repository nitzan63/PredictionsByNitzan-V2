package engine.mapper.world.rules.rule.action;

import scheme.generated.PRDAction;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.impl.DecreaseAction;
import world.rules.rule.action.impl.IncreaseAction;

public class IncreaseActionMapper {
    public static IncreaseAction mapIncreaseAction(PRDAction jaxbAction, EntitiesDefinition entitiesContext) {
        String propertyName = jaxbAction.getProperty();
        String byExpression = jaxbAction.getBy();
        String entityName = jaxbAction.getEntity();
        return new IncreaseAction(entitiesContext, propertyName, byExpression , entityName);
    }
}
