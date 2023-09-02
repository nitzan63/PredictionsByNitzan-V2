package world.rules.rule.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.CalculationAction;

import java.util.Map;

public class DivideAction extends CalculationAction {

    public DivideAction(Map<String,EntitiesDefinition> allEntitiesDefinition, String entityName, String resultProp, String s1, String s2) {
        super(allEntitiesDefinition, entityName, resultProp, s1, s2);
    }

    @Override
    public void invoke(EntityInstance entityInstance, Environment environment) throws ArithmeticException{

        Double num1 = (Double) evaluateExpression(args1, entityInstance, environment);
        Double num2 = (Double) evaluateExpression(args2, entityInstance, environment);
        if (num2 == 0) {
            throw new ArithmeticException("can't divide by zero! in Entity number: " + entityInstance.getSerialNumber() + ", args2 is " + args2 + " that means number: " + num2);
        } else {
            double newValue = num1 / num2;
            if (entityInstance.getProperty(resProp).getRange().getFrom().doubleValue() < newValue)
                entityInstance.getProperty(resProp).setValue(num1 / num2);
            else {
                entityInstance.getProperty(resProp).setValue(entityInstance.getProperty(resProp).getRange().getFrom().doubleValue());
            }
        }
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
