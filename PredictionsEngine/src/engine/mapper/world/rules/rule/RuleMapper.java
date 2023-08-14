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
    public static Rule mapRule(PRDRule jaxbRule, EntitiesDefinition entitesContext){
        String name = jaxbRule.getName();
        Activation activation = ActivationMapper.mapActivation(jaxbRule.getPRDActivation());
        Rule rule = new RuleImpl(name, activation);
        PRDActions jaxbActions = jaxbRule.getPRDActions();

        if (jaxbActions != null){
            List<Action> actions = new ArrayList<>();
            for (PRDAction jaxbAction: jaxbActions.getPRDAction()){
                Action action = ActionMapper.mapAction(jaxbAction, entitesContext);
                actions.add(action);
            }
            for (Action action : actions){
                rule.addAction(action);
            }
        }
        return rule;
    }
}
