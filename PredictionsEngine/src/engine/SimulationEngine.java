package engine;

import api.DTOEngineInterface;
import dto.*;
import engine.file.XMLProcessor;
import engine.file.exceptions.XMLProcessingException;
import engine.input.validator.EnvironmentInputValidator;
import engine.simulation.SimulationRunner;
import scheme.generated.PRDWorld;
import world.World;
import world.entities.EntitiesDefinition;
import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.Environment;
import world.environment.properties.EnvProperties;
import world.environment.properties.property.api.EnvProperty;
import world.rules.rule.action.api.Action;
import world.rules.rule.api.Rule;
import world.termination.api.Termination;
import world.termination.impl.TerminationByTicks;
import world.termination.impl.TerminationByTime;
import world.termination.impl.TerminationCombined;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimulationEngine implements DTOEngineInterface {
    private World world;
    private final List<ErrorDTO> errorList = new ArrayList<>();
    private SimulationRunner simulationRunner;
    private String xmlFilePath;

    @Override
    public void loadXmlFile(String filePath) throws Exception {
        this.xmlFilePath = filePath;
        try {
            XMLProcessor processor = new XMLProcessor();
            this.world = processor.processXML(filePath);
            this.simulationRunner = new SimulationRunner(world);
        } catch (XMLProcessingException e) {
            ErrorDTO errorDTO = new ErrorDTO(e.getMessage(), e.getClass().getName(), LocalDateTime.now());
            errorList.add(errorDTO);
            throw new Exception("Error processing XML file: " + e.getMessage(), e);
        }
    }

    public EntitiesDefinitionDTO getEntitiesDefinition() {
        EntitiesDefinition entities = world.getEntities();
        String name = entities.getEntityName();
        int population = entities.getPopulation();
        List<EntityProperty> properties = entities.getEntity(1).getProperties().getProperties();
        List<PropertyDTO> propertyDTOList = new ArrayList<>();
        for (EntityProperty property : properties) {
            propertyDTOList.add(new PropertyDTO(property.getName(), property.getType(), property.getRange().getFromDouble(), property.getRange().getToDouble(), property.isRandomInitialize(), property.getValue()));
        }
        return new EntitiesDefinitionDTO(name, population, propertyDTOList);
    }

    @Override
    public List<RuleDTO> getRules() {
        List<RuleDTO> ruleDTOList = new ArrayList<>();
        for (Rule rule : world.getRules().getRules()) {
            String name = rule.getName();
            int ticks = rule.getActivation().getTicksToActivate();
            double probability = rule.getActivation().getProbability();

            List<String> actionNames = new ArrayList<>();
            for (Action action : rule.getActionsToPerform()) {
                actionNames.add(action.getActionType().name());
            }

            int numberOfActions = actionNames.size();
            ruleDTOList.add(new RuleDTO(name, ticks, probability, numberOfActions, actionNames));
        }
        return ruleDTOList;
    }

    @Override
    public TerminationDTO getTermination() {
        Termination termination = world.getTermination();
        if (termination instanceof TerminationCombined){
            TerminationCombined terminationCombined = (TerminationCombined) termination;
            return new TerminationDTO(terminationCombined.getByTicks().getMaxTicks(), terminationCombined.getByTime().getMaxTime());
        } else if (termination instanceof TerminationByTime){
            TerminationByTime terminationByTime = (TerminationByTime) termination;
            return new TerminationDTO(null, terminationByTime.getMaxTime());
        } else if (termination instanceof TerminationByTicks){
            TerminationByTicks terminationByTicks = (TerminationByTicks) termination;
            return new TerminationDTO(terminationByTicks.getMaxTicks(), null);
        } else{
            return new TerminationDTO(0,0);
        }

        //TODO remember returning null as ticks or time...
    }

    @Override
    public EnvironmentDTO getEnvironmentProperties() {
        EnvironmentDTO environmentDTO = new EnvironmentDTO();
        EnvProperties properties = Environment.getProperties();
        for (EnvProperty property : properties.getProperties()){
            PropertyDTO propertyDTO = new PropertyDTO(property.getName(), property.getType(), property.getRange().getFromDouble(), property.getRange().getToDouble(), false, property.getValue());
            environmentDTO.addEnvironmentProperty(property.getName(), propertyDTO);
        }
        return environmentDTO;
    }

    @Override
    public void setEnvironmentProperties(UserEnvironmentInputDTO input) {
        // Validate the user input
        EnvProperties properties = Environment.getProperties();
        try {
            EnvironmentInputValidator.validateInput(input, properties);
        } catch (IllegalArgumentException e){
            ErrorDTO errorDTO = new ErrorDTO(e.getMessage(), e.getClass().getName(), LocalDateTime.now());
            errorList.add(errorDTO);
            return;
        }

        // Apply the changes to the environment properties
        for (Map.Entry<String, String> entry : input.getUserInputProperties().entrySet()) {
            String propertyName = entry.getKey();
            String stringValue = entry.getValue();

            EnvProperty property = properties.getProperty(propertyName);
            if (property != null) {
                String expectedType = property.getType();
                Object value = EnvironmentInputValidator.parseValue(stringValue, expectedType);  // Use the public method
                property.setValue(value);
            }
        }
    }

    public SimulationRunMetadataDTO RunSimulation() {
        if (world != null) {
            simulationRunner.setWorld(world);
            SimulationRunMetadataDTO resultMetaData = simulationRunner.runSimulation();
            resetEntities();
            return resultMetaData;
        } else {
            throw new IllegalStateException("World has not been initialized yet.");
        }
    }
    public SimulationRunResultsDTO getSimulationResults(String runIdentifier) {
        if (simulationRunner != null) {
            return simulationRunner.getResultsByID(runIdentifier);
        } else {
            throw new IllegalStateException("Simulation has not been run yet.");
        }
    }

    public Map<String, SimulationRunResultsDTO> getAllSimulationResults() {
        if (simulationRunner != null) {
            return simulationRunner.getAllSimulationResults();
        } else {
            throw new IllegalStateException("Simulation has not been run yet.");
        }
    }

    public void exit(){
        System.exit(0);
    }

    private void resetEntities() {
        try {
            // Process the XML file to get the initial entities
            XMLProcessor processor = new XMLProcessor();
            World resetWorld = processor.processXML(xmlFilePath);

            // Reset the entities in the world object
            //world.setEntities(resetWorld.getEntities());
            world = resetWorld;
        } catch (XMLProcessingException ignore) {

        }
    }
}
