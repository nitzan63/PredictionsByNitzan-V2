package world.rules.rule.actions.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.Environment;
import world.rules.rule.actions.action.api.AbstractAction;
import world.rules.rule.actions.action.api.ActionType;

public class IncreaseAction extends AbstractAction {
    private final String propertyName;
    private final String byExpression;

    public IncreaseAction(EntitiesDefinition entitiesDefinition, String property, String byExpression){
        super(ActionType.INCREASE, entitiesDefinition);
        this.propertyName = property;
        this.byExpression = byExpression;
    }

    public void invoke (EntityInstance entityInstance){

        EntityProperty property = entityInstance.getProperty(propertyName);
        Double value = (Double) property.getValue();
        Double expression = evaluateExpression(byExpression);

        property.setValue(value + expression);

    }




}
