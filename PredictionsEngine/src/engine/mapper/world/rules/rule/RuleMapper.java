package engine.mapper.world.rules.rule;

import engine.mapper.world.rules.rule.action.ActionMapper;
import engine.mapper.world.rules.rule.activation.ActivationMapper;
import scheme.generated.PRDAction;
import scheme.generated.PRDActions;
import scheme.generated.PRDRule;
import world.entities.EntitiesDefinition;
import world.rules.rule.action.api.Action;
import world.rules.rule.activation.Activation;
import world.rules.rule.api.Rule;
import world.rules.rule.impl.RuleImpl;

import java.util.ArrayList;
import java.util.List;

public class RuleMapper {
    public static Rule mapRule(PRDRule jaxbRule, EntitiesDefinition entitiesContext){
        String name = jaxbRule.getName();
        if (jaxbRule.getPRDActivation() != null) { // if there is an activation section:
            Activation activation = ActivationMapper.mapActivation(jaxbRule.getPRDActivation());
            Rule rule = new RuleImpl(name, activation);
        }
        // else - activation default - every tick , probability = 1.
        Rule rule = new RuleImpl(name, new Activation(1,1));
        PRDActions jaxbActions = jaxbRule.getPRDActions();

        if (jaxbActions != null){
            List<Action> actions = new ArrayList<>();
            for (PRDAction jaxbAction: jaxbActions.getPRDAction()){
                Action action = ActionMapper.mapAction(jaxbAction, entitiesContext);
                actions.add(action);
            }
            for (Action action : actions){
                rule.addAction(action);
            }
        }
        return rule;
    }
}
