package world.rules.rule.action.impl;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.ActionType;

public class SetAction extends AbstractAction {
    private final String propertyName;
    private final String expression;
    private final String entityName;

    public SetAction(EntitiesDefinition entitiesDefinition, String propertyName, String expression, String entityName) {
        super(ActionType.SET, entitiesDefinition);
        this.propertyName = propertyName;
        this.expression = expression;
        this.entityName = entityName;
    }

    @Override
    public void invoke(EntityInstance entityInstance) {
        Object value = evaluateExpression(expression, entityInstance);
        entityInstance.getProperty(propertyName).setValue(value);
    }
}
