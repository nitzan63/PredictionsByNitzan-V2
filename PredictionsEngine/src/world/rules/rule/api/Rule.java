package world.rules.rule.api;

import world.entities.EntitiesDefinition;
import world.environment.Environment;
import world.environment.properties.EnvProperties;
import world.rules.rule.action.api.Action;
import world.rules.rule.activation.Activation;

import java.util.List;

public interface Rule {
    String getName();
    Activation getActivation();
    List<Action> getActionsToPerform();
    void addAction (Action action);
    public void performActions(EntitiesDefinition entitiesDefinition, int tickNumber, Environment environment) throws Exception;
}
