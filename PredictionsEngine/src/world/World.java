package world;

import world.entities.EntitiesDefinition;
import world.environment.Environment;
import world.rules.Rules;
import world.termination.api.Termination;

public class World {
    private Environment environment;
    private EntitiesDefinition entities;
    private Rules rules;
    private Termination termination;

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public EntitiesDefinition getEntities() {
        return entities;
    }

    public void setEntities(EntitiesDefinition entities) {
        this.entities = entities;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    @Override
    public String toString() {
        return "World{" +
                "environment=" + environment +
                ", entities=" + entities +
                '}';
    }
}
