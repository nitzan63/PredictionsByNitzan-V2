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

public class SimulationManager {

    private final Map<String, SimulationRunMetadataDTO> simulationsMetadataMap = new HashMap<>();
    private final Map<String, SimulationExecutionDetailsDTO> simulationExecutionDetailsMap = new HashMap<>();
    private final Map<String, World> simulationWorldInstancesMap = new HashMap<>();
    private World prototypeWorld;
    private WorldGenerator worldGenerator;
    private int numberOfThreads;
    private final List<ErrorDTO> sharedErrorList = Collections.synchronizedList(new ArrayList<>());

    private ExecutorService executorService;

    public SimulationManager(World prototypeWorld, int numberOfThreads) {
        // set Data members
        this.prototypeWorld = prototypeWorld;
        this.numberOfThreads = numberOfThreads;

        // create world generator
        this.worldGenerator = new WorldGenerator(prototypeWorld);

        // create thread pool
        executorService = Executors.newFixedThreadPool(numberOfThreads);

    }

    public SimulationRunMetadataDTO runSimulation(UserInputDTO userInputDTO) {
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
        // create runner
        SimulationRunner simulationRunner = new SimulationRunner(worldInstance, sharedErrorList);
        // run simulation
        executorService.execute(simulationRunner);
        //gather and store run results
        processSimulationExecutionDetails(worldInstance, runID);
        // return metadata:
        return runMetadataDTO;
    }

    private void prepareSimulationData(World worldInstance, String runID){
        // create the slot for the results:
        SimulationExecutionDetailsDTO resultsDTO = new SimulationExecutionDetailsDTO(runID, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss")));
        // put to the results map
        simulationExecutionDetailsMap.put(runID, resultsDTO);
        // put the world instance in the SED map:
        simulationWorldInstancesMap.put(runID, worldInstance);
    }

    public SimulationExecutionDetailsDTO getLiveSimulationExecutionDetails (String runID){
        // get the world instance:
        World worldInstance = simulationWorldInstancesMap.get(runID);
        // process current world details
        processSimulationExecutionDetails(worldInstance, runID);
        // set curr tick:
        simulationExecutionDetailsMap.get(runID).setCurrTick(worldInstance.getCurrTick());
        // return the processed simulation data:
        return simulationExecutionDetailsMap.get(runID);
    }

    private void processSimulationExecutionDetails(World world, String runID){
        // get the results from the map to update:
        SimulationExecutionDetailsDTO resultsDTO = simulationExecutionDetailsMap.get(runID);
        // create population statistics:
        Map <String, PopulationStatisticsDTO> populationStatisticsDTOMap = createPopulationStatisticsMap(world.getEntitiesMap());
        resultsDTO.setPopulationStatistics(populationStatisticsDTOMap);
        // create property histograms for each entity definition:
        Map <String, EntityPropertiesHistogramDTO> entityPropertiesHistogramDTOMap = createEntitiesPropertiesHistogramMap(world.getEntitiesMap());
        resultsDTO.setEntityPropertiesHistogramDTOMap(entityPropertiesHistogramDTOMap);
        // set termination message if the simulation ended:
        if (world.getTermination().getTerminationMessage() != null)
            simulationsMetadataMap.get(runID).setTerminationReason(world.getTermination().getTerminationMessage());
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
