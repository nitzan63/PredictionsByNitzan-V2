package engine.simulation;

import dto.*;
import world.World;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.EntityProperties;
import world.entities.entity.properties.property.api.EntityProperty;
import world.termination.api.Termination;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SimulationRunner {
    private final World world;
    private final Termination termination;
    private int tickNumber;
    private final long startTime;
    private final String runIdentifier;
    private final Map<String, SimulationRunMetadataDTO> simulationData = new HashMap<>();
    private final Map<String, SimulationRunResultsDTO> simulationResults = new HashMap<>();
    private final List<ErrorDTO> errorList = new ArrayList<>();



    public SimulationRunner(World world) {
        this.world = world;
        this.termination = world.getTermination();
        this.tickNumber = 0;
        this.startTime = System.currentTimeMillis();
        this.runIdentifier = generateRunIdentifier();
    }

    public SimulationRunMetadataDTO runSimulation() {

        SimulationRunResultsDTO resultsDTO = new SimulationRunResultsDTO(runIdentifier);
        PopulationStatisticsDTO populationStatistics = new PopulationStatisticsDTO(world.getEntities().getPopulation(), 0);
        simulationResults.put(runIdentifier, resultsDTO);

        while (shouldContinue()) {
            tickNumber++;
            int elapsedSeconds = getElapsedSeconds();
            if (!termination.isNotTerminated(tickNumber, elapsedSeconds)) {
                break;
            }
            try {
                world.simulateThisTick(tickNumber);
            } catch (Exception e) {
                ErrorDTO errorDTO = new ErrorDTO(e.getMessage(), e.getClass().getName() , LocalDateTime.now());
                errorList.add(errorDTO);
            }
        }

        populationStatistics.setFinalPopulation(world.getEntities().getPopulation());
        resultsDTO.setPopulationStatistics(populationStatistics);
        for (EntityInstance entityInstance : world.getEntities().getEntities().values()){
            EntityProperties entityProperties= entityInstance.getProperties();
            for (EntityProperty property:entityProperties.getProperties()){
                String propName = property.getName();
                String value = String.valueOf(property.getValue());

                Map<String, PropertyHistogramDTO> propertyHistogram = resultsDTO.getPropertyHistograms();
                PropertyHistogramDTO histogram = propertyHistogram.get(propName);
                if (histogram == null){
                    histogram = new PropertyHistogramDTO(propName);
                    propertyHistogram.put(propName, histogram);
                }
                histogram.addValue(value);
            }
        }

        SimulationRunMetadataDTO metadataDTO = new SimulationRunMetadataDTO(runIdentifier, LocalDateTime.now().toString(), termination.getTerminationMessage());
        simulationData.put(runIdentifier, metadataDTO);
        return metadataDTO;
    }

    private boolean shouldContinue() {
        // Add any other conditions to check before each tick
        return true;
    }

    private int getElapsedSeconds() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return (int) (elapsedTime / 1000);
    }

    private String generateRunIdentifier() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String datePart = now.format(formatter);
        return datePart + "-" + (simulationData.size() + 1);
    }

    public String getRunIdentifier() {
        return runIdentifier;
    }

    public SimulationRunResultsDTO getResultsByID(String runIdentifier){
        return  simulationResults.get(runIdentifier);
    }

    public Map<String, SimulationRunResultsDTO> getAllSimulationResults(){
        return new HashMap<>(simulationResults);
    }

    public SimulationRunMetadataDTO getSimulationMetadata(String runIdentifier) {
        return simulationData.get(runIdentifier);
    }

    public Map<String, SimulationRunMetadataDTO> getAllSimulationMetadata() {
        return new HashMap<>(simulationData);
    }

    public List<ErrorDTO> getErrorList() {
        return errorList;
    }

}
