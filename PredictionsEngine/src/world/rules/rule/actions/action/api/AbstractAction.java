package world.rules.rule.actions.action.api;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;

public abstract class AbstractAction implements Action {
    private final ActionType actionType;
    private final EntitiesDefinition entitiesDefinition;

    protected AbstractAction(ActionType actionType, EntitiesDefinition entitiesDefinition) {
        this.actionType = actionType;
        this.entitiesDefinition = entitiesDefinition;
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public EntitiesDefinition getContextEntity() {
        return entitiesDefinition;
    }

    protected Double evaluateExpression(String expression, EntityInstance entityInstance) {
        //if the expression represents an env var:
        if (expression.startsWith("environment(") && expression.endsWith(")")) {
            String envVar = expression.substring(12, expression.length() - 1);
            return Environment.getEnvironmentPropValue(envVar);
        } else if (entityInstance.getProperty(expression) != null) { // if the action represents one of the entity prop:
            return (Double) entityInstance.getProperty(expression).getValue();
        } else return Double.parseDouble(expression); //just a number!
    }
}

