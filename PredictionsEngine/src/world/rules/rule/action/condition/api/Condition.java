package world.rules.rule.action.condition.api;

import world.entities.entity.EntityInstance;

public interface Condition {
    boolean evaluate(EntityInstance entityInstance);


}
