package world.rules.rule.api;

import world.ActionContext;
import world.entities.EntitiesDefinition;
import world.environment.Environment;
import world.environment.properties.EnvProperties;
import world.rules.rule.action.api.Action;
import world.rules.rule.activation.Activation;

import java.util.List;
import java.util.Map;

public interface Rule {
    String getName();
    Activation getActivation();
    List<Action> getActionsToPerform();
    void addAction (Action action);
    public void performActions(int tickNumber, ActionContext actionContext) throws Exception;
}
