package world.rules;

import world.entities.EntitiesDefinition;
import world.rules.rule.activation.Activation;
import world.rules.rule.api.Rule;

import java.util.ArrayList;
import java.util.List;

public class Rules {
    private final List<Rule> rules;

    public Rules() {
        this.rules = new ArrayList<>();
    }

    public void addRule(Rule rule){
        rules.add(rule);
    }

    public List<Rule> getRules(){
        return rules;
    }

    public void simulateRules(EntitiesDefinition entitiesDefinition, int tickNumber){
        for (Rule rule : rules){
            rule.performActions(entitiesDefinition, tickNumber);
        }
    }
}
