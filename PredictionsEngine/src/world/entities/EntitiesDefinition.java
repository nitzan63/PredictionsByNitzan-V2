package world.entities;

import world.entities.entity.EntityInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class EntitiesDefinition {
    private String entityName;
    private Map<Integer, EntityInstance> entities = new TreeMap<>();
    Integer population;
    private EntityInstance prototypeEntity;
    private Map <Integer, Integer> ticksToPopulationMap = new HashMap<>();

    public EntitiesDefinition(String name, Integer population){
        this.entityName = name;
        this.population = population;
        this.ticksToPopulationMap.put(0, population);
    }

    public Map<Integer, Integer> getTicksToPopulationMap() {
        return ticksToPopulationMap;
    }

    public void addEntity (EntityInstance entity, int number){
        entities.put(number, entity);
    }

    public EntityInstance getEntity (int serialNumber){
        return entities.get(serialNumber);
    }

    public String getEntityName() {
        return entityName;
    }

    public Integer getPopulation() {
        return population;
    }

    public Map<Integer, EntityInstance> getEntities() {
        return entities;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setEntities(Map<Integer, EntityInstance> entities) {
        this.entities = entities;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public void removeEntityInstance (int serialNumber, int tick){
        // remove from entities instance map:
        entities.remove(serialNumber);
        // decrease population:
        population --;
        // update ticks and population map:
        if (ticksToPopulationMap.get(tick) == null) {
            ticksToPopulationMap.put(tick, population);
        } else {
            ticksToPopulationMap.remove(tick);
            ticksToPopulationMap.put(tick, population);
        }
    }

    public EntityInstance getPrototypeEntity() {
        return prototypeEntity;
    }

    public void setPrototypeEntity(EntityInstance prototypeEntity) {
        this.prototypeEntity = prototypeEntity;
    }

    @Override
    public String toString() {
        return "Entities{" +
                "entityName='" + entityName + '\'' +
                ", entities=" + entities +
                ", population=" + population +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntitiesDefinition entities1 = (EntitiesDefinition) o;
        return Objects.equals(entityName, entities1.entityName) && Objects.equals(entities, entities1.entities) && Objects.equals(population, entities1.population);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityName, entities, population);
    }
}
