package world.rules.rule.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.Environment;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;

public class DecreaseAction extends AbstractAction {
    private final String propertyName;
    private final String byExpression;

    public DecreaseAction(EntitiesDefinition entitiesDefinition, String property, String byExpression, String entityName){
        super(ActionType.DECREASE, entitiesDefinition, entityName);
        this.propertyName = property;
        this.byExpression = byExpression;
    }

    public void invoke (EntityInstance entityInstance, Environment environment){
        double newValue;
        EntityProperty property = entityInstance.getProperty(propertyName);
        Double value = (Double) property.getValue();
        Double expression = (Double) evaluateExpression(byExpression, entityInstance, environment);
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
