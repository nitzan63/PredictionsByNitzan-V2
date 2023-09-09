package engine;

import api.DTOEngineInterface;
import dto.*;
import engine.file.XMLProcessor;
import engine.file.exceptions.XMLProcessingException;
import engine.input.validator.EnvironmentInputValidator;
import engine.simulation.SimulationManager;
import engine.simulation.SimulationRunner;
import world.World;
import world.entities.EntitiesDefinition;
import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.properties.EnvProperties;
import world.environment.properties.property.api.EnvProperty;
import world.rules.rule.action.api.Action;
import world.rules.rule.api.Rule;
import world.termination.api.Termination;
import world.termination.impl.TerminationByTicks;
import world.termination.impl.TerminationByTime;
import world.termination.impl.TerminationCombined;
import world.utils.range.Range;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationEngine implements DTOEngineInterface {
    private World prototypeWorld;
    private final List<ErrorDTO> errorList = new ArrayList<>();
    private String xmlFilePath;
    SimulationManager simulationManager;


    @Override
    public void loadXmlFile(String filePath) throws Exception {
        this.xmlFilePath = filePath;
        try {

            // process and validate the xml file, and create a prototype world:
            XMLProcessor processor = new XMLProcessor();
            this.prototypeWorld = processor.processXML(filePath);

            // create new simulation manager
            simulationManager = new SimulationManager(prototypeWorld, processor.getNumberOfThreads());

        } catch (XMLProcessingException e) {
            ErrorDTO errorDTO = new ErrorDTO(e.getMessage(), e.getClass().getName(), LocalDateTime.now(), "while XML Loading");
            errorList.add(errorDTO);
            throw new Exception("Error processing XML file: " + e.getMessage(), e);
        }
    }

    public Map<String, EntitiesDefinitionDTO> getEntitiesDefinition() {
        Map<String, EntitiesDefinition> entities = prototypeWorld.getEntitiesMap();
        Map<String, EntitiesDefinitionDTO> entitiesDefinitionDTOMap = new HashMap<>();
        for (EntitiesDefinition entityDefinition : entities.values()) {
            String name = entityDefinition.getEntityName();
            int population = entityDefinition.getPopulation();
            List<EntityProperty> properties = entityDefinition.getPrototypeEntity().getProperties().getProperties();
            List<PropertyDTO> propertyDTOList = new ArrayList<>();
            for (EntityProperty property : properties) {
                String propertyName = property.getName();
                String propertyType = property.getType();
                boolean isRandom = property.isRandomInitialize();
                Object value = property.getValue();
                if (property.getRange() != null) {
                    Double to = property.getRange().getToDouble();
                    Double from = property.getRange().getFromDouble();
                    PropertyDTO propertyDTO = new PropertyDTO(propertyName, propertyType, to, from, isRandom, value);
                    propertyDTOList.add(propertyDTO);
                } else {
                    PropertyDTO propertyDTO = new PropertyDTO(propertyName, propertyType, 0, 0, isRandom, value);
                    propertyDTOList.add(propertyDTO);
                }


            }
            EntitiesDefinitionDTO entitiesDefinitionDTO = new EntitiesDefinitionDTO(name, population, propertyDTOList);
            entitiesDefinitionDTOMap.put(name, entitiesDefinitionDTO);
        }
        return entitiesDefinitionDTOMap;
    }

    @Override
    public List<RuleDTO> getRules() {
        List<RuleDTO> ruleDTOList = new ArrayList<>();
        for (Rule rule : prototypeWorld.getRules().getRules()) {
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
        Termination termination = prototypeWorld.getTermination();
        if (termination instanceof TerminationCombined) {
            TerminationCombined terminationCombined = (TerminationCombined) termination;
            return new TerminationDTO(terminationCombined.getByTicks().getMaxTicks(), terminationCombined.getByTime().getMaxTime());
        } else if (termination instanceof TerminationByTime) {
            TerminationByTime terminationByTime = (TerminationByTime) termination;
            return new TerminationDTO(null, terminationByTime.getMaxTime());
        } else if (termination instanceof TerminationByTicks) {
            TerminationByTicks terminationByTicks = (TerminationByTicks) termination;
            return new TerminationDTO(terminationByTicks.getMaxTicks(), null);
        } else {
            return new TerminationDTO(0, 0);
        }

        //TODO remember returning null as ticks or time...
    }

    @Override
    public EnvironmentDTO getEnvironmentProperties() {
        EnvironmentDTO environmentDTO = new EnvironmentDTO();
        EnvProperties properties = prototypeWorld.getEnvironment().getProperties();
        for (EnvProperty property : properties.getProperties()) {
            PropertyDTO propertyDTO = new PropertyDTO(property.getName(), property.getType(), property.getRange().getFromDouble(), property.getRange().getToDouble(), false, property.getValue());
            environmentDTO.addEnvironmentProperty(property.getName(), propertyDTO);
        }
        return environmentDTO;
    }

//    @Override
//    public void setEnvironmentProperties(UserInputDTO input) {
//        // Validate the user input
//        EnvProperties properties = prototypeWorld.getEnvironment().getProperties();
//        try {
//            EnvironmentInputValidator.validateInput(input, properties);
//        } catch (IllegalArgumentException e) {
//            ErrorDTO errorDTO = new ErrorDTO(e.getMessage(), e.getClass().getName(), LocalDateTime.now());
//            errorList.add(errorDTO);
//            return;
//        }
//
//        // Apply the changes to the environment properties
//        for (Map.Entry<String, String> entry : input.getEnvironmertPropMap().entrySet()) {
//            String propertyName = entry.getKey();
//            String stringValue = entry.getValue();
//
//            EnvProperty property = properties.getProperty(propertyName);
//            if (property != null) {
//                String expectedType = property.getType();
//                Object value = EnvironmentInputValidator.parseValue(stringValue, expectedType);  // Use the public method
//                property.setValue(value);
//            }
//        }
//    }

    public void RunSimulation(UserInputDTO userInputDTO) {
        if (prototypeWorld != null) {
            simulationManager.runSimulation(userInputDTO);
        } else {
            throw new IllegalStateException("World has not been initialized yet.");
        }
    }

    public SimulationExecutionDetailsDTO getSimulationResults(String runIdentifier) {
        if (simulationManager != null) {
            return simulationManager.getResultsByID(runIdentifier);
        } else {
            throw new IllegalStateException("Simulation has not been run yet.");
        }
    }

    public Map<String, SimulationExecutionDetailsDTO> getAllSimulationResults() {
        if (simulationManager != null) {
            return simulationManager.getAllSimulationResults();
        } else {
            throw new IllegalStateException("Simulation has not been run yet.");
        }
    }

    @Override
    public List<ErrorDTO> getErrors() {
        return errorList;
    }

    @Override
    public ThreadInfoDTO getThreadInfo() {
        return simulationManager.getThreadInfo();
    }

    @Override
    public GridDTO getGridDTO() {
        return new GridDTO(prototypeWorld.getGrid().getRows(), prototypeWorld.getGrid().getCols());
    }

    @Override
    public SimulationExecutionDetailsDTO getLiveSimulationExecutionDetails(String runID) {
        return simulationManager.getLiveSimulationExecutionDetails(runID);
    }


    public void exit() {
    }

    @Override
    public void pauseSimulation(String runID) {
        simulationManager.pauseSimulation(runID);
    }

    @Override
    public void resumeSimulation(String runID) {
        simulationManager.resumeSimulation(runID);
    }

    @Override
    public void stopSimulation(String runID) {
        simulationManager.stopSimulation(runID);
    }


}
