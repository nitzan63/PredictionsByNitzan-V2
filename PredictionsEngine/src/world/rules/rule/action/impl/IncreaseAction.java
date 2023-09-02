package world.rules.rule.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.Environment;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;

import java.util.Map;

public class IncreaseAction extends AbstractAction {
    private final String propertyName;
    private final String byExpression;

    public IncreaseAction(Map<String,EntitiesDefinition> allEntitiesDefinition, String property, String byExpression, String entityName) {
        super(ActionType.INCREASE, allEntitiesDefinition, entityName);
        this.propertyName = property;
        this.byExpression = byExpression;
    }

    public void invoke(EntityInstance entityInstance, Environment environment) {
        double newValue;
        EntityProperty property = entityInstance.getProperty(propertyName);
        Object valueObject = property.getValue();
        Double value;
        if (valueObject instanceof Integer){
            value =(((Integer) valueObject).doubleValue());
        } else if (valueObject instanceof Double){
            value = (Double) valueObject;
        } else {
            throw new IllegalArgumentException("Unexpected type" + valueObject.getClass() + " in " + propertyName + " in " + entityInstance);
        }
        Double expression = (Double) evaluateExpression(byExpression, entityInstance, environment);
        newValue = value + expression;
        if (newValue < property.getRange().getTo().doubleValue())
            property.setValue(value + expression);
        else {
            entityInstance.getProperty(propertyName).setValue(entityInstance.getProperty(propertyName).getRange().getTo().doubleValue());
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
        return "IncreaseAction{" +
                "propertyName='" + propertyName + '\'' +
                ", byExpression='" + byExpression + '\'' +
                '}';
    }
}
