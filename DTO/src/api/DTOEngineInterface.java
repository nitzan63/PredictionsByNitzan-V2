package api;

import dto.*;

import java.security.PublicKey;
import java.util.List;
import java.util.Map;

public interface DTOEngineInterface {
    void loadXmlFile (String Path) throws Exception;
    Map<String, EntitiesDefinitionDTO> getEntitiesDefinition();
    List<RuleDTO> getRules();
    TerminationDTO getTermination();
    EnvironmentDTO getEnvironmentProperties();
    // void setEnvironmentProperties(UserInputDTO input);
    void RunSimulation(UserInputDTO userInputDTO);
    SimulationExecutionDetailsDTO getSimulationResults(String runIdentifier);
    Map<String, SimulationExecutionDetailsDTO> getAllSimulationResults();
    List<ErrorDTO> getErrors();
    ThreadInfoDTO getThreadInfo();
    GridDTO getGridDTO();
    SimulationExecutionDetailsDTO getLiveSimulationExecutionDetails(String runID);
    void resumeSimulation(String runId);
    void pauseSimulation(String runID);
    void stopSimulation(String runID);
    void exit();
    void progressOneTick(String runID);
}
