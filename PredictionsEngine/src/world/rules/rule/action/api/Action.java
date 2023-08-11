package world.rules.rule.action.api;

import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;

public interface Action {
    void invoke (EntityInstance entityInstance);
    ActionType getActionType();
    EntitiesDefinition getContextEntity();
}
