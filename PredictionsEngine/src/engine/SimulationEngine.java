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
import world.rules.rule.action.api.ActionType;
import world.rules.rule.action.api.CalculationAction;
import world.rules.rule.action.impl.*;
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
        // Initialize the list to hold RuleDTO objects
        List<RuleDTO> ruleDTOList = new ArrayList<>();

        // Check if the prototypeWorld or its rules are null
        if (prototypeWorld == null || prototypeWorld.getRules() == null) {
            return ruleDTOList;
        }

        // Loop through each Rule object in the prototype world
        for (Rule rule : prototypeWorld.getRules().getRules()) {
            // Null check for each rule
            if (rule == null) continue;

            // Retrieve rule details
            String name = rule.getName();
            int ticks = rule.getActivation().getTicksToActivate();
            double probability = rule.getActivation().getProbability();

            // Initialize the list to hold ActionDTO objects
            List<ActionDTO> actionDTOList = new ArrayList<>();

            // Loop through each Action object for this rule
            for (Action action : rule.getActionsToPerform()) {
                // Null check for each action
                if (action == null) continue;

                // Create an ActionDTO and add it to the list
                ActionDTO actionDTO = createActionDTO(action);
                actionDTOList.add(actionDTO);
            }

            // Create a RuleDTO and add it to the list
            ruleDTOList.add(new RuleDTO(name, ticks, probability, actionDTOList));
        }

        return ruleDTOList;
    }

    private ActionDTO createActionDTO (Action action){
        ActionDTO actionDTO = new ActionDTO(action.getActionType().name(), action.getEntityName());
        if (action.getSecondaryEntityName() != null)
            actionDTO.addAdditionalDetails("Secondary Entity ", action.getSecondaryEntityName());
        ActionType actionType = action.getActionType();
        switch (actionType){
            case CONDITION:
                ConditionAction conditionAction = (ConditionAction) action;
                actionDTO.addAdditionalDetails("Number of Then actions " , String.valueOf(conditionAction.getNumberOfThenActions()));
                if (conditionAction.getElseActions() != null)
                    actionDTO.addAdditionalDetails("Number of Else actions " , String.valueOf(conditionAction.getNumberOfElseActions()));
                actionDTO.addAdditionalDetails("Conditions \n ----------- \n ", conditionAction.getMainConditionData());
                break;
            case PROXIMITY:
                ProximityAction proximityAction = (ProximityAction) action;
                actionDTO.addAdditionalDetails("Target Entity ", proximityAction.getTargetEntityName());
                actionDTO.addAdditionalDetails("Proximity ", proximityAction.getByExpression());
                actionDTO.addAdditionalDetails("Number of Actions ", String.valueOf(proximityAction.getProximityNumberOfActions()));
                break;
            case DECREASE:
            case INCREASE:
            case SET:
                actionDTO.addAdditionalDetails("Property ", action.getPropertyName());
                actionDTO.addAdditionalDetails("By ", action.getByExpression());
                break;
            case CALCULATION:
                CalculationAction calculationAction = (CalculationAction) action;
                if (calculationAction instanceof MultiplyAction)
                    actionDTO.addAdditionalDetails("Calculation type ", "Multiply");
                else actionDTO.addAdditionalDetails("Calculation type ", "Divide");
                actionDTO.addAdditionalDetails("Argument 1 ", calculationAction.getArgs1());
                actionDTO.addAdditionalDetails("Argument 2 ", calculationAction.getArgs2());
                actionDTO.addAdditionalDetails("Result Property ", calculationAction.getPropertyName());
                break;
            case REPLACE:
                assert action instanceof ReplaceAction;
                ReplaceAction replaceAction = (ReplaceAction) action;
                actionDTO.addAdditionalDetails("Entity to Kill ", replaceAction.getKillEntityName());
                actionDTO.addAdditionalDetails("Entity to Create ", replaceAction.getCreateEntityName());
                actionDTO.addAdditionalDetails("Mode ", replaceAction.getMode());
                break;
        }
        return actionDTO;
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
            PropertyDTO propertyDTO;
            if (property.getType().equals("float") || property.getType().equals("decimal")) {
                propertyDTO = new PropertyDTO(property.getName(), property.getType(), property.getRange().getFromDouble(), property.getRange().getToDouble(), false, property.getValue());
            } else {
                propertyDTO = new PropertyDTO(property.getName(), property.getValue());
            }
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

    public void progressOneTick(String runID){
        simulationManager.progressOneTick(runID);
    }


}
