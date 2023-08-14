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

        Double num1 = (Double) evaluateExpression(args1, entityInstance);
        Double num2 = (Double) evaluateExpression(args2, entityInstance);
        if (num2 == 0) {
            throw new ArithmeticException("can't divide by zero!");
        } else {
            double newValue = num1 / num2;
            if (entityInstance.getProperty(resProp).getRange().getFrom().doubleValue() < newValue)
                entityInstance.getProperty(resProp).setValue(num1 / num2);
            else {
                entityInstance.getProperty(resProp).setValue(entityInstance.getProperty(resProp).getRange().getFrom().doubleValue());
            }
        }
        // TODO handle what happens when you divide by 0! maybe in the validation part.
    }

    @Override
    public String toString() {
        return "DivideAction{" +
                "entityName='" + entityName + '\'' +
                ", resProp='" + resProp + '\'' +
                ", args1='" + args1 + '\'' +
                ", args2='" + args2 + '\'' +
                '}';
    }
}
