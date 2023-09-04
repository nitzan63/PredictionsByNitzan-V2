package world.rules.rule.action.impl;

import world.rules.rule.action.api.ActionContext;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.CalculationAction;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.Map;

public class MultiplyAction extends CalculationAction {

    public MultiplyAction(String entityName, String resultProp, String s1, String s2, SecondaryEntity secondaryEntity) {
        super(entityName, resultProp, s1, s2, secondaryEntity);
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

    private void performAction (EntityInstance entityInstance, ActionContext actionContext){
        Double num1 = (Double) evaluateExpression(args1, entityInstance, actionContext);
        Double num2 = (Double) evaluateExpression(args2, entityInstance, actionContext);
        double newValue = num1 * num2;
        if (entityInstance.getProperty(resProp).getRange().getTo().doubleValue() > newValue)
            entityInstance.getProperty(resProp).setValue(num1 * num2);
        else {
            entityInstance.getProperty(resProp).setValue(entityInstance.getProperty(resProp).getRange().getTo().doubleValue());
        }
    }

    @Override
    public String toString() {
        return "MultiplyAction{" +
                "entityName='" + entityName + '\'' +
                ", resProp='" + resProp + '\'' +
                ", args1='" + args1 + '\'' +
                ", args2='" + args2 + '\'' +
                '}';
    }
}
