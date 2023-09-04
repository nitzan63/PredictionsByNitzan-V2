package engine.file.mapper.world.rules.rule.action;

import scheme.generated.PRDAction;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.impl.SetAction;
import world.rules.rule.action.secondary.SecondaryEntity;

public class SetActionMapper {
    public static SetAction mapSetAction(PRDAction jaxbAction, SecondaryEntity secondaryEntity){
        String propertyName = jaxbAction.getProperty();
        String byExpression = jaxbAction.getBy();
        String entityName = jaxbAction.getEntity();

        return new SetAction(propertyName, byExpression, entityName, secondaryEntity);
    }
}
