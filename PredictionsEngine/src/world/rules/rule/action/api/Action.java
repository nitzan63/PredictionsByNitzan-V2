package world.rules.rule.action.api;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.environment.Environment;

public interface Action {
    void invoke (EntityInstance entityInstance, Environment environment);
    ActionType getActionType();

    public String getEntityName();

    public String getPropertyName();

    public String getByExpression();
}
