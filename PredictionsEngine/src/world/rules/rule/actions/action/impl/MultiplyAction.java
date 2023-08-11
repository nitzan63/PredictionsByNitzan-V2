package world.rules.rule.actions.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.rules.rule.actions.action.api.CalculationAction;

public class MultiplyAction extends CalculationAction {

    public MultiplyAction(EntitiesDefinition entitiesDefinition, String entityName, String resultProp, String s1, String s2) {
        super(entitiesDefinition, entityName, resultProp, s1, s2);
    }

    @Override
    public void invoke(EntityInstance entityInstance) {

        Double num1 = evaluateExpression(args1, entityInstance);
        Double num2 = evaluateExpression(args2, entityInstance);

        entityInstance.getProperty(resProp).setValue(num1 * num2);
    }
}
