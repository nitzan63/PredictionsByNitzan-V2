package world.rules.rule.api;

import world.rules.rule.action.api.ActionContext;
import world.rules.rule.action.api.Action;
import world.rules.rule.activation.Activation;

import java.util.List;

public interface Rule {
    String getName();
    Activation getActivation();
    List<Action> getActionsToPerform();
    void addAction (Action action);
    public void performActions(int tickNumber, ActionContext actionContext) throws Exception;
}
