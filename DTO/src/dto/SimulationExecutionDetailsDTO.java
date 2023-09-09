package dto;

import java.util.HashMap;
import java.util.Map;

public class SimulationExecutionDetailsDTO {
    public enum SimulationState {
        LIVE,
        PAUSED,
        COMPLETED,
        QUEUED
    }
    private final String runIdentifier;
    private final String dateTime;
    private Map <String, PopulationStatisticsDTO> populationStatisticsDTOMap;
    private Map<String, EntityPropertiesHistogramDTO> entityPropertiesHistogramDTOMap;
    private Map<String, Integer> entitiesPopulationMap;
    private Map<String, String> environmentPropertiesValues;
    private SimulationState simulationState;

    private int currTick;
    private int elapsedSeconds;

    public SimulationExecutionDetailsDTO(String runIdentifier, String dateTime) {
        this.runIdentifier = runIdentifier;
        this.entityPropertiesHistogramDTOMap = new HashMap<>();
        this.dateTime = dateTime;
        this.simulationState = SimulationState.LIVE;
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

    public Map<String, Integer> getEntitiesPopulationMap() {
        return entitiesPopulationMap;
    }

    public void setEntitiesPopulationMap(Map<String, Integer> entitiesPopulationMap) {
        this.entitiesPopulationMap = entitiesPopulationMap;
    }

    public Map<String, String> getEnvironmentPropertiesValues() {
        return environmentPropertiesValues;
    }

    public void setEnvironmentPropertiesValues(Map<String, String> environmentPropertiesValues) {
        this.environmentPropertiesValues = environmentPropertiesValues;
    }

    public SimulationState getSimulationState() {
        return simulationState;
    }

    public void setSimulationState(SimulationState simulationState) {
        this.simulationState = simulationState;
    }

    public boolean isSimulationCompleted (){
        return (simulationState == SimulationState.COMPLETED);
    }

    public boolean isSimulationLive (){
        return (simulationState == SimulationState.LIVE);
    }

    public boolean isSimulationPaused (){
        return (simulationState == SimulationState.PAUSED);
    }

    public boolean isSimulationQueued (){
        return (simulationState == SimulationState.QUEUED);
    }
}
