package world;

import world.entities.EntitiesDefinition;
import world.environment.Environment;
import world.rules.Rules;
import world.termination.api.Termination;

import java.util.HashMap;
import java.util.Map;

public class World {
    private Environment environment;
    private Map<String, EntitiesDefinition> entitiesMap = new HashMap<>();
    private Rules rules;
    private Termination termination;

    public void simulateThisTick (int tickNumber) throws Exception{
        try {
            rules.simulateRules(entitiesMap, tickNumber , environment);
        } catch (Exception e){
            throw new Exception("In Tick Number: " + tickNumber +" Error: " + e.getMessage(), e);
        }
    }

    public void addEntitiesDefinition(String entityName, EntitiesDefinition entitiesDefinition) {
        entitiesMap.put(entityName, entitiesDefinition);
    }

    public EntitiesDefinition getEntitiesDefinition(String entityName) {
        return entitiesMap.get(entityName);
    }
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Map<String, EntitiesDefinition> getEntitiesMap() {
        return entitiesMap;
    }

    public void setEntitiesMap(Map<String,EntitiesDefinition> entitiesMap) {
        this.entitiesMap = entitiesMap;
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
                ", entities=" + entitiesMap +
                ", rules=" + rules +
                ", termination=" + termination +
                '}';
    }
}
