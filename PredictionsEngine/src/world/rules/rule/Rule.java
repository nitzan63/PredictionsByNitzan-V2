package world.rules.rule;

import world.rules.rule.actions.action.api.Action;
import world.rules.rule.activation.api.Activation;

import java.util.List;

public interface Rule {
    String getName();
    Activation getActivation();
    List<Action> getActionsToPerform();
    void addAction (Action action);
}
