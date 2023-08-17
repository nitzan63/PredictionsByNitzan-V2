package dto;

import java.util.ArrayList;
import java.util.List;

public class PopulationStatisticsDTO {
    private int initialPopulation;
    private int finalPopulation;
    private String EntityName;

    public PopulationStatisticsDTO(String entityName , int initialPopulation, int finalPopulation) {
        this.initialPopulation = initialPopulation;
        this.finalPopulation = finalPopulation;
        this.EntityName = entityName;
    }

    public int getInitialPopulation() {
        return initialPopulation;
    }

    public String getEntityName() {
        return EntityName;
    }

    public void setInitialPopulation(int initialPopulation) {
        this.initialPopulation = initialPopulation;
    }

    public int getFinalPopulation() {
        return finalPopulation;
    }

    public void setFinalPopulation(int finalPopulation) {
        this.finalPopulation = finalPopulation;
    }

    @Override
    public String toString() {
        return "PopulationStatisticsDTO{" +
                "initialPopulation=" + initialPopulation +
                ", finalPopulation=" + finalPopulation +
                '}';
    }
}
