package world.rules;

import world.rules.rule.action.api.ActionContext;
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

    public void simulateRules(int tickNumber, ActionContext actionContext) throws Exception{
            for (Rule rule : rules) {
                rule.performActions(tickNumber, actionContext);
            }

    }

    @Override
    public String toString() {
        return "Rules{" +
                "rules=" + rules +
                '}';
    }
}
