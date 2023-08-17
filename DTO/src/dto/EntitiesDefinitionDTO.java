package dto;

import java.util.List;

public class EntitiesDefinitionDTO {
    private String name;
    private int population;
    private List<PropertyDTO> properties;

    public EntitiesDefinitionDTO(String name, int population, List<PropertyDTO> properties) {
        this.name = name;
        this.population = population;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public List<PropertyDTO> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyDTO> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "EntitiesDefinitionDTO{" +
                "name='" + name + '\'' +
                ", population=" + population +
                ", properties=" + properties +
                '}';
    }
}
