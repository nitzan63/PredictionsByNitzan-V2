package world.rules;

import world.World;
import world.entities.EntitiesDefinition;
import world.environment.Environment;
import world.environment.properties.EnvProperties;
import world.rules.rule.activation.Activation;
import world.rules.rule.api.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void simulateRules(Map<String,EntitiesDefinition> entitiesMap, int tickNumber, Environment environment) throws Exception{
            for (Rule rule : rules) {
                rule.performActions(entitiesMap, tickNumber, environment);
            }

    }

    @Override
    public String toString() {
        return "Rules{" +
                "rules=" + rules +
                '}';
    }
}
