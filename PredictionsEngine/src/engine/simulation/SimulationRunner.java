package engine.simulation;

import dto.*;
import world.World;
import world.termination.api.Termination;
import java.time.LocalDateTime;
import java.util.*;

public class SimulationRunner implements Runnable {
    private World world;
    private final Termination termination;
    private int tickNumber;
    private long startTime;
    private final List<ErrorDTO> errorList = new ArrayList<>();


    public SimulationRunner(World world) {
        this.world = world;
        this.termination = world.getTermination();
    }

    @Override
    public void run() {
        prepareRun();
        executeRun();
    }

    private void prepareRun() {
        tickNumber = 0;
        startTime = System.currentTimeMillis();
    }

    private void executeRun() {
        while (shouldContinue()) {
            tickNumber++;
            int elapsedSeconds = getElapsedSeconds();
            if (!termination.isNotTerminated(tickNumber, elapsedSeconds)) {
                break;
            }
            try {
                world.simulateThisTick(tickNumber);
            } catch (Exception e) {
                logError(e);
            }
        }
    }

    private void logError(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage(), e.getClass().getName(), LocalDateTime.now());
        errorList.add(errorDTO);
    }

    private boolean shouldContinue() {
        // Add thread pause / resume logic
        return true;
    }

    private int getElapsedSeconds() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return (int) (elapsedTime / 1000);
    }


//    public SimulationRunMetadataDTO runSimulation() {
//        runIdentifier = generateRunIdentifier();
//        SimulationRunResultsDTO resultsDTO = new SimulationRunResultsDTO(runIdentifier , LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss")));
//        PopulationStatisticsDTO populationStatistics = new PopulationStatisticsDTO(world.getEntitiesMap().getEntityName(), world.getEntitiesMap().getPopulation(), 0);
//        simulationResults.put(runIdentifier, resultsDTO);
//        tickNumber = 0;
//        startTime = System.currentTimeMillis();
//        while (shouldContinue()) {
//            tickNumber++;
//            int elapsedSeconds = getElapsedSeconds();
//            if (!termination.isNotTerminated(tickNumber, elapsedSeconds)) {
//                break;
//            }
//            try {
//                world.simulateThisTick(tickNumber);
//            } catch (Exception e) {
//                ErrorDTO errorDTO = new ErrorDTO(e.getMessage(), e.getClass().getName() , LocalDateTime.now());
//                errorList.add(errorDTO);
//            }
//        }
//        populationStatistics.setFinalPopulation(world.getEntitiesMap().getPopulation());
//        resultsDTO.setPopulationStatistics(populationStatistics);
//        for (EntityInstance entityInstance : world.getEntitiesMap().getEntities().values()){
//            EntityProperties entityProperties= entityInstance.getProperties();
//            for (EntityProperty property:entityProperties.getProperties()){
//                String propName = property.getName();
//                String value = String.valueOf(property.getValue());
//
//                Map<String, PropertyHistogramDTO> propertyHistogram = resultsDTO.getPropertyHistograms();
//                PropertyHistogramDTO histogram = propertyHistogram.get(propName);
//                if (histogram == null){
//                    histogram = new PropertyHistogramDTO(propName);
//                    propertyHistogram.put(propName, histogram);
//                }
//                histogram.addValue(value);
//            }
//        }
//
//        SimulationRunMetadataDTO metadataDTO = new SimulationRunMetadataDTO(runIdentifier, LocalDateTime.now().toString(), termination.getTerminationMessage());
//        simulationData.put(runIdentifier, metadataDTO);
//        // set the env properties to random after simulation run!
//        world.getEnvironment().getProperties().generateRandomEnvPropertiesValues();
//        return metadataDTO;
//    }

}
