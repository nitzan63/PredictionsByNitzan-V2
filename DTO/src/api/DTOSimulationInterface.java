package api;

import dto.*;

import java.util.List;
import java.util.Map;

public class DTOSimulationInterface {
    private final DTOEngineInterface engineInterface;
    private  boolean worldLoaded = false;

    public DTOSimulationInterface(DTOEngineInterface engineInterface) {
        this.engineInterface = engineInterface;
    }

    public void setWorldLoaded(boolean worldLoaded) {
        this.worldLoaded = worldLoaded;
    }

    public boolean isWorldLoaded() {
        return worldLoaded;
    }

    public void loadXmlFile(String path) throws Exception {
        engineInterface.loadXmlFile(path);
    }

    public EntitiesDefinitionDTO getEntitiesDefinition() {
        return engineInterface.getEntitiesDefinition();
    }

    public List<RuleDTO> getRules() {
        return engineInterface.getRules();
    }

    public TerminationDTO getTermination() {
        return engineInterface.getTermination();
    }

    public EnvironmentDTO getEnvironmentProperties() {
        return engineInterface.getEnvironmentProperties();
    }

    public void setEnvironmentProperties(UserEnvironmentInputDTO input) {
        engineInterface.setEnvironmentProperties(input);
    }

    public SimulationRunMetadataDTO runSimulation() {
        return engineInterface.RunSimulation();
    }

    public SimulationRunResultsDTO getSimulationResults(String runIdentifier) {
        return engineInterface.getSimulationResults(runIdentifier);
    }

    public Map<String, SimulationRunResultsDTO> getAllSimulationResults() {
        return engineInterface.getAllSimulationResults();
    }

    public void exit() {
        engineInterface.exit();
    }
}
