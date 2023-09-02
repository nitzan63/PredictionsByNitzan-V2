package world.rules.rule.action.impl;

import world.ActionContext;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.AbstractAction;
import world.rules.rule.action.api.Action;
import world.rules.rule.action.api.ActionType;
import world.rules.rule.action.condition.api.Condition;
import java.util.List;
import java.util.Map;

public class ConditionAction extends AbstractAction {
    private final Condition condition;
    private final List<Action> thenActions;
    private final List<Action> elseActions;
    public ConditionAction (String entityName, List<Action> thenActions, List<Action> elseActions, Condition condition){
        super(ActionType.CONDITION, entityName);
        this.elseActions = elseActions;
        this.thenActions = thenActions;
        this.condition = condition;
    }

    @Override
    public void invoke(EntityInstance entityInstance, ActionContext actionContext) {
        Environment environment = actionContext.getEnvironment();
        if (condition.evaluate(entityInstance, environment)) {
            performActions(thenActions, entityInstance, actionContext);
        } else if (elseActions != null){
            performActions(elseActions, entityInstance, actionContext);
        }
    }

    @Override
    public String getPropertyName() {
        return null;
    }


    @Override
    public String getByExpression() {
        return null;
    }

    private void performActions(List<Action> actions, EntityInstance entityInstance, ActionContext actionContext){
        for (Action action : actions)
            action.invoke(entityInstance, actionContext);
    }

    public List<Action> getThenActions() {
        return thenActions;
    }

    public List<Action> getElseActions() {
        return elseActions;
    }

    @Override
    public String toString() {
        return "ConditionAction{" +
                "condition=" + condition +
                ", entityName='" + entityName + '\'' +
                ", thenActions=" + thenActions +
                ", elseActions=" + elseActions +
                '}';
    }
}
