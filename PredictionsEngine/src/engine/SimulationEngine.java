package engine;

import api.DTOEngineInterface;
import dto.*;
import engine.file.XMLProcessor;
import engine.file.exceptions.XMLProcessingException;
import world.World;
import world.entities.EntitiesDefinition;
import world.entities.entity.EntityInstance;
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

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements DTOEngineInterface {
    private World world;

    @Override
    public void loadXmlFile(String filePath) throws Exception {
        try {
            XMLProcessor processor = new XMLProcessor();
            this.world = processor.processXML(filePath);
        } catch (XMLProcessingException e) {
            throw new Exception("Error processing XML file: " + e.getMessage(), e);
        }
    }

    public EntitiesDefinitionDTO getEntitiesDefinition() {
        EntitiesDefinition entities = world.getEntities();
        String name = entities.getEntityName();
        int population = entities.getPopulation();
        List<EntityProperty> properties = entities.getEntity(0).getProperties().getProperties();
        List<EntityPropertyDTO> propertyDTOList = new ArrayList<>();
        for (EntityProperty property : properties) {
            propertyDTOList.add(new EntityPropertyDTO(property.getName(), property.getType(), property.getRange().getFromDouble(), property.getRange().getToDouble(), property.isRandomInitialize()));
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
        EnvProperties properties = world.getEnvironment()
    }


}
