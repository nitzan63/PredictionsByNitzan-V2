package world.rules.rule.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.property.api.EntityProperty;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;

public class IncreaseAction extends AbstractAction {
    private final String propertyName;
    private final String byExpression;

    public IncreaseAction(EntitiesDefinition entitiesDefinition, String property, String byExpression) {
        super(ActionType.INCREASE, entitiesDefinition);
        this.propertyName = property;
        this.byExpression = byExpression;
    }

    public void invoke(EntityInstance entityInstance) {
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
        // Double value = (Double) property.getValue();
        //System.out.println("\nbyExpression: " + byExpression + " after evaluation: " + evaluateExpression(byExpression, entityInstance));
        Double expression = (Double) evaluateExpression(byExpression, entityInstance);
        newValue = value + expression;
        if (newValue < property.getRange().getTo().doubleValue())
            property.setValue(value + expression);
        else {
            entityInstance.getProperty(propertyName).setValue(entityInstance.getProperty(propertyName).getRange().getTo().doubleValue());
        }

    }

    @Override
    public String toString() {
        return "IncreaseAction{" +
                "propertyName='" + propertyName + '\'' +
                ", byExpression='" + byExpression + '\'' +
                '}';
    }
}
