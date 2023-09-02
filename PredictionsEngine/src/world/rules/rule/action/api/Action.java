package world.rules.rule.action.api;

import world.entities.entity.EntityInstance;

public interface Action {
    void invoke (EntityInstance entityInstance, ActionContext actionContext);
    ActionType getActionType();

    public String getEntityName();

    public String getPropertyName();

    public String getByExpression();
}
