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
        Double value = (Double) property.getValue();
        Double expression = evaluateExpression(byExpression, entityInstance);
        newValue = value + expression;
        if (newValue > property.getRange().getTo().doubleValue())
            property.setValue(value + expression);
        else {
            //TODO throw exception regarding actions.
        }

    }


}
