package engine.mapper.world.rules.rule.action;

import scheme.generated.PRDAction;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.impl.SetAction;

public class SetActionMapper {
    public static SetAction mapSetAction(PRDAction jaxbAction, EntitiesDefinition entitiesContext){
        String propertyName = jaxbAction.getProperty();
        String byExpression = jaxbAction.getBy();
        String entityName = jaxbAction.getEntity();

        return new SetAction(entitiesContext, propertyName, byExpression, entityName);
    }
}
