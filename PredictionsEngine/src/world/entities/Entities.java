package world.entities;

import world.entities.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Entities {
    private String entityName;
    private Map<Integer, Entity> entities = new TreeMap<>();
    Integer population;

    public Entities(String name, Integer population){
        this.entityName = name;
        this.population = population;
    }

    public void addEntity (Entity entity, int number){
        entities.put(number, entity);
    }


    public Entity getEntity (int serialNumber){
        return entities.get(serialNumber);
    }

    public String getEntityName() {
        return entityName;
    }

    public Integer getPopulation() {
        return population;
    }

    public Map<Integer, Entity> getEntities() {
        return entities;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public void setEntities(Map<Integer, Entity> entities) {
        this.entities = entities;
    }

    public void setPopulation(Integer population) {
        this.population = population;
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
        Entities entities1 = (Entities) o;
        return Objects.equals(entityName, entities1.entityName) && Objects.equals(entities, entities1.entities) && Objects.equals(population, entities1.population);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityName, entities, population);
    }
}
