package world.rules.rule.action.api;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.utils.expression.ExpressionEvaluator;

public abstract class AbstractAction implements Action {
    protected final ActionType actionType;
    protected final EntitiesDefinition entitiesDefinition;
    protected final String entityName;

    protected AbstractAction(ActionType actionType, EntitiesDefinition entitiesDefinition, String entityName) {
        this.actionType = actionType;
        this.entitiesDefinition = entitiesDefinition;
        this.entityName = entityName;
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public EntitiesDefinition getContextEntity() {
        return entitiesDefinition;
    }

    protected Object evaluateExpression(String expression, EntityInstance entityInstance) {
        return ExpressionEvaluator.evaluateExpression(expression, entityInstance);
    }

    @Override
    public String toString() {
        return "AbstractAction{" +
                "actionType=" + actionType +
                ", entitiesDefinition=" + entitiesDefinition +
                '}';
    }
}

