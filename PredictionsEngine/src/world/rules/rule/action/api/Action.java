package world.rules.rule.action.api;

import world.ActionContext;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;

public interface Action {
    void invoke (EntityInstance entityInstance, ActionContext actionContext);
    ActionType getActionType();

    public String getEntityName();

    public String getPropertyName();

    public String getByExpression();
}
