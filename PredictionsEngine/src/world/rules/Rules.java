package world.rules;

import world.rules.rule.Rule;

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
}
