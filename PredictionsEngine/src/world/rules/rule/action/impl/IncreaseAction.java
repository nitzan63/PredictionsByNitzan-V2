package world.rules.rule.action.impl;

import world.rules.rule.action.api.ActionContext;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.Environment;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;
import world.rules.rule.action.secondary.SecondaryEntity;

import java.util.Map;

public class IncreaseAction extends AbstractAction {
    private final String propertyName;
    private final String byExpression;

    public IncreaseAction(String property, String byExpression, String entityName, SecondaryEntity secondaryEntity) {
        super(ActionType.INCREASE, entityName, secondaryEntity);
        this.propertyName = property;
        this.byExpression = byExpression;
    }

    public void invoke(EntityInstance entityInstance, ActionContext actionContext) {
        // check if there is a secondary entity:
        if (secondaryEntity != null){
            // if yes, check if the intended action is in the context of the secondary entity:
            if (secondaryEntity.getDefinitionEntityName().equals(entityName)){
                // if yes, get the map of the selected entities:
                Map<Integer, EntityInstance> secondaryEntities = secondaryEntity.getSelectedSecondaryInstancesMap(actionContext);
                // iterate over them and perform actions
                for (EntityInstance secondaryEntity : secondaryEntities.values()){
                    performAction(secondaryEntity, actionContext);
                }
                // if there is a secondary entity, but the action is performed on the primary entity:
            } else performAction(entityInstance, actionContext);
            // if there is no secondaryEntity, perform on the main entity.
        } else performAction(entityInstance, actionContext);
    }

    private void performAction (EntityInstance entityInstance, ActionContext actionContext){
        // Initialize the new value which will eventually store the increased property value
        double newValue;

        // Fetch the environment details from the action context
        Environment environment = actionContext.getEnvironment();

        // Retrieve the property of the entity instance that we wish to modify
        EntityProperty property = entityInstance.getProperty(propertyName);

        // Fetch the current value of the property
        Object valueObject = property.getValue();

        // Initialize the value variable which will store the current property value as a double
        Double value;

        // Type checking: Determine if the current value is of type Integer or Double
        if (valueObject instanceof Integer) {
            value = (((Integer) valueObject).doubleValue());
        } else if (valueObject instanceof Double) {
            value = (Double) valueObject;
        } else {
            // Throw an exception if the type is not Integer or Double
            throw new IllegalArgumentException("Unexpected type " + valueObject.getClass() + " in " + propertyName + " in " + entityInstance);
        }

        // Evaluate the expression to find out how much to increase the property by
        Double expression = (Double) evaluateExpression(byExpression, entityInstance, environment);

        // Perform the actual increase
        newValue = value + expression;

        // Check if the new value would go beyond the defined range for this property
        if (newValue < property.getRange().getTo().doubleValue()) {
            // If it's within the range, set the new value
            property.setValue(newValue);
        } else {
            // If it exceeds the maximum allowed value, set it to the maximum
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
