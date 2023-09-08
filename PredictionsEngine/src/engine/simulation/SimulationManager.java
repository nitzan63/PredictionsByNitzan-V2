package engine.simulation;

import dto.*;
import world.World;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
import world.entities.entity.properties.EntityProperties;
import world.entities.entity.properties.property.api.EntityProperty;
import world.generator.WorldGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SimulationManager {

    private final Map<String, SimulationRunMetadataDTO> simulationsMetadataMap = new HashMap<>();
    private final Map<String, SimulationExecutionDetailsDTO> simulationExecutionDetailsMap = new HashMap<>();
    private final Map<String, SimulationRunner> simulationRunnerMap = new HashMap<>();
    private final Map<String, World> simulationWorldInstancesMap = new HashMap<>();
    private World prototypeWorld;
    private WorldGenerator worldGenerator;
    private int numberOfThreads;
    private final List<ErrorDTO> sharedErrorList = Collections.synchronizedList(new ArrayList<>());

    private ExecutorService executorService;
    private final ThreadCountManager threadCountManager;

    public SimulationManager(World prototypeWorld, int numberOfThreads) {
        // set Data members
        this.prototypeWorld = prototypeWorld;
        this.numberOfThreads = numberOfThreads;

        // create world generator
        this.worldGenerator = new WorldGenerator(prototypeWorld);

        // create thread pool
        executorService = Executors.newFixedThreadPool(numberOfThreads);

        this.threadCountManager = new ThreadCountManager((ThreadPoolExecutor) executorService);

    }

    public void runSimulation(UserInputDTO userInputDTO) {
        // Increment queued simulations count
        threadCountManager.incrementQueuedSimulations();
        // generate world instance
        World worldInstance = worldGenerator.generateWorld(userInputDTO);
        // create run id's and allocate to world instance:
        String runID = generateRunIdentifier();
        worldInstance.setRunID(runID);
        // generate run metadata:
        SimulationRunMetadataDTO runMetadataDTO = new SimulationRunMetadataDTO(runID, LocalDateTime.now().toString());
        simulationsMetadataMap.put(runID, runMetadataDTO);
        // prepare data to be stored: (create new population statistics and save initials
        prepareSimulationData(worldInstance, runID);
        // create runner and add to map
        SimulationRunner simulationRunner = new SimulationRunner(worldInstance, sharedErrorList);
        simulationRunnerMap.put(runID, simulationRunner);
        // run simulation
        executorService.execute(() -> {
            threadCountManager.decrementQueuedSimulations();
            simulationRunner.run();
            //gather and store run results
            processSimulationExecutionDetails(worldInstance, runID);
            // set the simulation status to "completed":
            simulationExecutionDetailsMap.get(runID).setSimulationComplete(true);
            // update thread count manager
            threadCountManager.incrementTotalSimulations();
        });

    }

    private void prepareSimulationData(World worldInstance, String runID){
        // create the slot for the results:
        SimulationExecutionDetailsDTO sedDTO = new SimulationExecutionDetailsDTO(runID, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss")));
        // create population data map for the SED:
        sedDTO.setEntitiesPopulationMap(createEntitesPopulationMap(worldInstance));
        // put to the results map
        simulationExecutionDetailsMap.put(runID, sedDTO);
        // put the world instance in the SED map:
        simulationWorldInstancesMap.put(runID, worldInstance);
    }

    private Map<String, Integer> createEntitesPopulationMap(World worldInstance){
        Map<String, EntitiesDefinition> entitiesDefinitionMap = worldInstance.getEntitiesMap();
        Map<String, Integer> entitiesPopulationMap = new HashMap<>();
        for (EntitiesDefinition entitiesDefinition : entitiesDefinitionMap.values()){
            entitiesPopulationMap.put(entitiesDefinition.getEntityName(), entitiesDefinition.getPopulation());
        }
        return entitiesPopulationMap;
    }

    public SimulationExecutionDetailsDTO getLiveSimulationExecutionDetails (String runID){
        // get the world instance:
        World worldInstance = simulationWorldInstancesMap.get(runID);
        // process current world details
        processSimulationExecutionDetails(worldInstance, runID);
        // set curr tick and seconds elapsed:
        simulationExecutionDetailsMap.get(runID).setCurrTick(worldInstance.getCurrTick());
        simulationExecutionDetailsMap.get(runID).setElapsedSeconds(worldInstance.getSecondsElapsed());
        // return the processed simulation data:
        return simulationExecutionDetailsMap.get(runID);
    }

    private void processSimulationExecutionDetails(World world, String runID){
        // get the results from the map to update:
        SimulationExecutionDetailsDTO sedDTO = simulationExecutionDetailsMap.get(runID);
        // create population statistics:
        Map <String, PopulationStatisticsDTO> populationStatisticsDTOMap = createPopulationStatisticsMap(world.getEntitiesMap());
        sedDTO.setPopulationStatistics(populationStatisticsDTOMap);
        // create property histograms for each entity definition:
        Map <String, EntityPropertiesHistogramDTO> entityPropertiesHistogramDTOMap = createEntitiesPropertiesHistogramMap(world.getEntitiesMap());
        sedDTO.setEntityPropertiesHistogramDTOMap(entityPropertiesHistogramDTOMap);
        // update entities population map:
        sedDTO.setEntitiesPopulationMap(createEntitesPopulationMap(world));
        // set termination message if the simulation ended:
        if (world.getTermination().getTerminationMessage() != null)
            simulationsMetadataMap.get(runID).setTerminationReason(world.getTermination().getTerminationMessage());
    }

    public ThreadInfoDTO getThreadInfo() {
        return threadCountManager.getThreadInfoDTO();
    }

    private Map<String, EntityPropertiesHistogramDTO> createEntitiesPropertiesHistogramMap(Map<String, EntitiesDefinition> entitiesDefinitionMap ){
        Map<String, EntityPropertiesHistogramDTO> entitiesPropertiesHistogramMap = new HashMap<>();
        // for each entity definition, creat properties histogram:
        for (EntitiesDefinition entitiesDefinition : entitiesDefinitionMap.values()){
            String entityName = entitiesDefinition.getEntityName();
            EntityPropertiesHistogramDTO entityPropertiesHistogramDTO = createPropertiesHistogramForEntity(entitiesDefinition.getEntities(), entityName);
            entitiesPropertiesHistogramMap.put(entityName, entityPropertiesHistogramDTO);
        }
        return entitiesPropertiesHistogramMap;
    }

    private EntityPropertiesHistogramDTO createPropertiesHistogramForEntity(Map<Integer, EntityInstance> entitiesInstances, String entityName){
        // create Map <propertyName, propertyHistogram> to return
        Map <String, PropertyHistogramDTO> propertyHistogramDTOMap = new HashMap<>();
        // iterate over all the definition instances:
        for (EntityInstance entityInstance : entitiesInstances.values()){
            // get the properties of the entity instance:
            EntityProperties entityProperties = entityInstance.getProperties();
            // iterate the properties:
            for (EntityProperty property : entityProperties.getProperties()){
                String propName = property.getName();
                String value = String.valueOf(property.getValue());
                // Get from the map if exists, otherwise create and add to map
                PropertyHistogramDTO histogram = propertyHistogramDTOMap.computeIfAbsent(
                        propName, k -> new PropertyHistogramDTO(propName)
                );
                // add the value.
                histogram.addValue(value);
            }
        }
        return new EntityPropertiesHistogramDTO(propertyHistogramDTOMap, entityName);
    }



    private Map<String , PopulationStatisticsDTO> createPopulationStatisticsMap (Map<String, EntitiesDefinition> entitiesDefinitionMap){
        Map<String, PopulationStatisticsDTO> entitiesPopulationStatisticsMap = new HashMap<>();
        for (EntitiesDefinition entitiesDefinition : entitiesDefinitionMap.values()){
            PopulationStatisticsDTO populationStatisticsDTO = new PopulationStatisticsDTO(entitiesDefinition.getEntityName() , entitiesDefinition.getTicksToPopulationMap());
            entitiesPopulationStatisticsMap.put(entitiesDefinition.getEntityName(), populationStatisticsDTO);
        }
        return  entitiesPopulationStatisticsMap;
    }

    private String generateRunIdentifier() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String datePart = now.format(formatter);
        return datePart + "-" + (simulationExecutionDetailsMap.size() + 1);
    }

    public void pauseSimulation(String runID) {
        SimulationRunner runner = simulationRunnerMap.get(runID);
        if (runner != null) {
            runner.pause();
        }
    }

    public void resumeSimulation(String runID) {
        SimulationRunner runner = simulationRunnerMap.get(runID);
        if (runner != null) {
            runner.resume();
        }
    }

    public void stopSimulation(String runID) {
        SimulationRunner runner = simulationRunnerMap.get(runID);
        if (runner != null) {
            runner.stop();
        }
    }



    public SimulationExecutionDetailsDTO getResultsByID(String runIdentifier) {
        return simulationExecutionDetailsMap.get(runIdentifier);
    }

    public Map<String, SimulationExecutionDetailsDTO> getAllSimulationResults() {
        return new HashMap<>(simulationExecutionDetailsMap);
    }


    public SimulationRunMetadataDTO getSimulationMetadata(String runIdentifier) {
        return simulationsMetadataMap.get(runIdentifier);
    }

    public Map<String, SimulationRunMetadataDTO> getAllSimulationMetadata() {
        return new HashMap<>(simulationsMetadataMap);
    }

    public List<ErrorDTO> getSharedErrorList() {
        return sharedErrorList;
    }
}
