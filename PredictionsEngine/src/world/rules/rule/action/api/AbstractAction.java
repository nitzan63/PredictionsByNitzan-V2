package world.rules.rule.action.api;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.secondary.SecondaryEntity;
import world.utils.expression.ExpressionEvaluator;

import java.util.Map;

public abstract class AbstractAction implements Action {
    protected final ActionType actionType;

    protected final String entityName;

    protected final SecondaryEntity secondaryEntity;


    protected AbstractAction(ActionType actionType, String entityName, SecondaryEntity secondaryEntity) {
        this.actionType = actionType;
        this.entityName = entityName;
        this.secondaryEntity = secondaryEntity;
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    protected Object evaluateExpression(String expression, EntityInstance entityInstance, ActionContext actionContext) {
        return ExpressionEvaluator.evaluateExpression(expression, entityInstance, actionContext);
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public String getSecondaryEntityName() {
        if (secondaryEntity != null)
            return secondaryEntity.getDefinitionEntityName();
        else return null;
    }

    @Override
    public String toString() {
        return "AbstractAction{" +
                "actionType=" + actionType +
                ", entitiesDefinition=" +
                '}';
    }
}

