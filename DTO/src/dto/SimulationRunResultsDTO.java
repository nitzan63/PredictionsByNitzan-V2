package dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationRunResultsDTO {
    private final String runIdentifier;
    private PopulationStatisticsDTO populationStatistics;
    private Map<String, PropertyHistogramDTO> propertyHistograms;

    public SimulationRunResultsDTO(String runIdentifier) {
        this.runIdentifier = runIdentifier;
        this.propertyHistograms = new HashMap<>();
    }

    public String getRunIdentifier() {
        return runIdentifier;
    }

    public PopulationStatisticsDTO getPopulationStatistics() {
        return populationStatistics;
    }

    public void setPopulationStatistics(PopulationStatisticsDTO populationStatistics) {
        this.populationStatistics = populationStatistics;
    }

    public Map<String, PropertyHistogramDTO> getPropertyHistograms() {
        return propertyHistograms;
    }

    public void addPropertyHistogram(String propertyName, PropertyHistogramDTO propertyHistogram) {
        this.propertyHistograms.put(propertyName, propertyHistogram);
    }
}
