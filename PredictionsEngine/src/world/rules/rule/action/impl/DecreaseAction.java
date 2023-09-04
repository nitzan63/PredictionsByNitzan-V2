package world.rules.rule.action.impl;

import world.rules.rule.action.api.ActionContext;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.Environment;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;
import world.rules.rule.action.secondary.SecondaryEntity;

public class DecreaseAction extends AbstractAction {
    private final String propertyName;
    private final String byExpression;

    public DecreaseAction(String property, String byExpression, String entityName, SecondaryEntity secondaryEntity){
        super(ActionType.DECREASE,  entityName, secondaryEntity);
        this.propertyName = property;
        this.byExpression = byExpression;
    }

    public void invoke (EntityInstance entityInstance, ActionContext actionContext){
        double newValue;

        EntityProperty property = entityInstance.getProperty(propertyName);
        Double value = (Double) property.getValue();
        Double expression = (Double) evaluateExpression(byExpression, entityInstance, actionContext);
        newValue = value - expression;
        if (newValue > property.getRange().getFrom().doubleValue())
            property.setValue(value - expression);
        else {
            entityInstance.getProperty(propertyName).setValue(entityInstance.getProperty(propertyName).getRange().getFrom().doubleValue());
        }

    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String getByExpression() {
        return byExpression;
    }

    @Override
    public String toString() {
        return "DecreaseAction{" +
                "propertyName='" + propertyName + '\'' +
                ", byExpression='" + byExpression + '\'' +
                '}';
    }
}
