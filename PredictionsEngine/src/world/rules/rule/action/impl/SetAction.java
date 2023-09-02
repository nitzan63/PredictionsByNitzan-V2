package world.rules.rule.action.impl;

import world.rules.rule.action.api.ActionContext;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;

public class SetAction extends AbstractAction {
    private final String propertyName;
    private final String expression;

    public SetAction(String propertyName, String expression, String entityName) {
        super(ActionType.SET, entityName);
        this.propertyName = propertyName;
        this.expression = expression;
    }

    @Override
    public void invoke(EntityInstance entityInstance, ActionContext actionContext) {
        Environment environment = actionContext.getEnvironment();
        Object value = evaluateExpression(expression, entityInstance, environment);
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
