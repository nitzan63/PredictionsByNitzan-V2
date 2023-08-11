package world.rules.rule.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.rules.rule.action.api.CalculationAction;

public class DivideAction extends CalculationAction {

    public DivideAction(EntitiesDefinition entitiesDefinition, String entityName, String resultProp, String s1, String s2) {
        super(entitiesDefinition, entityName, resultProp, s1, s2);
    }

    @Override
    public void invoke(EntityInstance entityInstance) throws ArithmeticException{

        Double num1 = evaluateExpression(args1, entityInstance);
        Double num2 = evaluateExpression(args2, entityInstance);
        if (num2 == 0) {
            throw new ArithmeticException("can't divide by zero!");
        } else {
            entityInstance.getProperty(resProp).setValue(num1 / num2);
        }
        // TODO handle what happens when you divide by 0! maybe in the validation part.
    }

}
