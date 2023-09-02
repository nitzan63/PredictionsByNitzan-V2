package api;

import dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DTOUIInterface {
    private final DTOEngineInterface engineInterface;
    private  boolean worldLoaded = false;
    private final List<Runnable> simulationRunListeners = new ArrayList<>();



    public DTOUIInterface(DTOEngineInterface engineInterface) {
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

    public void setEnvironmentProperties(UserInputDTO input) {
        engineInterface.setEnvironmentProperties(input);
    }

    public SimulationRunMetadataDTO runSimulation() {
        SimulationRunMetadataDTO result = engineInterface.RunSimulation();
        // Notify listeners
        for (Runnable listener : simulationRunListeners) {
            listener.run();
        }
        return result;
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

    public void addSimulationRunListener(Runnable listener) {
        simulationRunListeners.add(listener);
    }

    public void removeSimulationRunListener(Runnable listener) {
        simulationRunListeners.remove(listener);
    }



}
