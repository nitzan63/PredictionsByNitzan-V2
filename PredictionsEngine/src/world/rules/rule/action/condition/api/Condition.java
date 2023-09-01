package world.rules.rule.action.condition.api;

import world.entities.entity.EntityInstance;
import world.environment.Environment;

public interface Condition {
    boolean evaluate(EntityInstance entityInstance, Environment environment);

}
