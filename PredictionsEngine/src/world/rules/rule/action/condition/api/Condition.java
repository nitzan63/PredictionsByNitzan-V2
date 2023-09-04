package world.rules.rule.action.condition.api;

import world.entities.entity.EntityInstance;
import world.environment.Environment;
import world.rules.rule.action.api.ActionContext;

public interface Condition {
    boolean evaluate(EntityInstance primaryInstance, EntityInstance secondaryInstance, String secondaryName, ActionContext actionContext);

}
