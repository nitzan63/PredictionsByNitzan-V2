package dto;

import java.util.HashMap;
import java.util.Map;

public class SimulationExecutionDetailsDTO {
    private final String runIdentifier;
    private final String dateTime;
    private Map <String, PopulationStatisticsDTO> populationStatisticsDTOMap;
    private Map<String, EntityPropertiesHistogramDTO> entityPropertiesHistogramDTOMap;

    private int currTick;
    private int elapsedSeconds;

    public SimulationExecutionDetailsDTO(String runIdentifier, String dateTime) {
        this.runIdentifier = runIdentifier;
        this.entityPropertiesHistogramDTOMap = new HashMap<>();
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getRunIdentifier() {
        return runIdentifier;
    }

    public Map<String, PopulationStatisticsDTO> getPopulationStatistics() {
        return populationStatisticsDTOMap;
    }

    public void setPopulationStatistics(Map<String, PopulationStatisticsDTO> populationStatistics) {
        this.populationStatisticsDTOMap = populationStatistics;
    }

    public Map<String, EntityPropertiesHistogramDTO> getEntityPropertiesHistogramDTOMap() {
        return entityPropertiesHistogramDTOMap;
    }

    public void setEntityPropertiesHistogramDTOMap(Map<String, EntityPropertiesHistogramDTO> entityPropertiesHistogramDTOMap) {
        this.entityPropertiesHistogramDTOMap = entityPropertiesHistogramDTOMap;
    }

    public Map<String, PopulationStatisticsDTO> getPopulationStatisticsDTOMap() {
        return populationStatisticsDTOMap;
    }

    public void setPopulationStatisticsDTOMap(Map<String, PopulationStatisticsDTO> populationStatisticsDTOMap) {
        this.populationStatisticsDTOMap = populationStatisticsDTOMap;
    }

    public void addPropertyHistogram(String propertyName, PropertyHistogramDTO propertyHistogram) {
        this.propertyHistograms.put(propertyName, propertyHistogram);
    }

    public int getCurrTick() {
        return currTick;
    }

    public void setCurrTick(int currTick) {
        this.currTick = currTick;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }
}
