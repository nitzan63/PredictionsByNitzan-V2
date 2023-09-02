package world.rules.rule.action.api;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.utils.expression.ExpressionEvaluator;

import java.util.Map;

public abstract class AbstractAction implements Action {
    protected final ActionType actionType;
    protected final Map<String, EntitiesDefinition> allEntitiesDefinitionMap;
    protected final String entityName;

    protected AbstractAction(ActionType actionType, Map<String, EntitiesDefinition> allEntitiesDefinition, String entityName) {
        this.actionType = actionType;
        this.allEntitiesDefinitionMap = allEntitiesDefinition;
        this.entityName = entityName;
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    protected Object evaluateExpression(String expression, EntityInstance entityInstance, Environment environment) {
        return ExpressionEvaluator.evaluateExpression(expression, entityInstance, environment);
    }

    public String getEntityName() {
        return entityName;
    }

    public Map<String, EntitiesDefinition> getAllEntitiesDefinitionMap() {
        return this.allEntitiesDefinitionMap;
    }


    @Override
    public String toString() {
        return "AbstractAction{" +
                "actionType=" + actionType +
                ", entitiesDefinition=" + allEntitiesDefinitionMap +
                '}';
    }
}

