package dto;

import java.util.HashMap;
import java.util.Map;

public class SimulationRunResultsDTO {
    private final String runIdentifier;
    private int initialPopulation;
    private int finalPopulation;
    private Map<String, Map<String, Integer>> propertyHistograms;

    public SimulationRunResultsDTO(String runIdentifier) {
        this.runIdentifier = runIdentifier;
        this.propertyHistograms = new HashMap<>();
    }

    public String getRunIdentifier() {
        return runIdentifier;
    }

    public int getInitialPopulation() {
        return initialPopulation;
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

    public Map<String, Map<String, Integer>> getPropertyHistograms() {
        return propertyHistograms;
    }

    public void addPropertyHistogram(String propertyName, Map<String, Integer> histogram) {
        this.propertyHistograms.put(propertyName, histogram);
    }
}
