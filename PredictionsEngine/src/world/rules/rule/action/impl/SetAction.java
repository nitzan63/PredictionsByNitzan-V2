package world.rules.rule.action.impl;

import world.rules.rule.action.api.ActionContext;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.Map;

public class SetAction extends AbstractAction {
    private final String propertyName;
    private final String expression;

    public SetAction(String propertyName, String expression, String entityName, SecondaryEntity secondaryEntity) {
        super(ActionType.SET, entityName, secondaryEntity);
        this.propertyName = propertyName;
        this.expression = expression;
    }

    @Override
    public void invoke(EntityInstance entityInstance, ActionContext actionContext) {
        // check if there is a secondary entity:
        if (secondaryEntity != null){
            // if yes, check if the intended action is in the context of the secondary entity:
            if (secondaryEntity.getDefinitionEntityName().equals(entityName)){
                // if yes, get the map of the selected entities:
                Map<Integer, EntityInstance> secondaryEntities = secondaryEntity.getSelectedSecondaryInstancesMap(actionContext);
                // iterate over them and perform actions
                for (EntityInstance secondaryEntity : secondaryEntities.values()){
                    performAction(secondaryEntity, actionContext);
                }
                // if there is a secondary entity, but the action is performed on the primary entity:
            } else performAction(entityInstance, actionContext);
            // if there is no secondaryEntity, perform on the main entity.
        } else performAction(entityInstance, actionContext);
    }

    public void performAction (EntityInstance entityInstance, ActionContext actionContext){
        Object value = evaluateExpression(expression, entityInstance, actionContext);
        if (value instanceof Double){ // handle the cases when trying to set value out of range.
            if ((double) value < entityInstance.getProperty(propertyName).getRange().getFrom().doubleValue()){
                entityInstance.getProperty(propertyName).setValue(entityInstance.getProperty(propertyName).getRange().getFrom().doubleValue());
            } else if ((double) value > entityInstance.getProperty(propertyName).getRange().getTo().doubleValue()){
                entityInstance.getProperty(propertyName).setValue(entityInstance.getProperty(propertyName).getRange().getTo());
            } else entityInstance.getProperty(propertyName).setValue(value);
        } else {
            entityInstance.getProperty(propertyName).setValue(value);
        }
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String getByExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "SetAction{" +
                "propertyName='" + propertyName + '\'' +
                ", expression='" + expression + '\'' +
                ", entityName='" + entityName + '\'' +
                '}';
    }
}
