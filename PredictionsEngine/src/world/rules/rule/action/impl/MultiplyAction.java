package world.rules.rule.action.impl;

import world.ActionContext;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.CalculationAction;

import java.util.Map;

public class MultiplyAction extends CalculationAction {

    public MultiplyAction(String entityName, String resultProp, String s1, String s2) {
        super(entityName, resultProp, s1, s2);
    }

    @Override
    public void invoke(EntityInstance entityInstance, ActionContext actionContext) {
        Environment environment = actionContext.getEnvironment();
        Double num1 = (Double) evaluateExpression(args1, entityInstance, environment);
        Double num2 = (Double) evaluateExpression(args2, entityInstance, environment);
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
