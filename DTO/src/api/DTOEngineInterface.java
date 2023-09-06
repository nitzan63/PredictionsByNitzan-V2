package api;

import dto.*;

import java.util.List;
import java.util.Map;

public interface DTOEngineInterface {
    void loadXmlFile (String Path) throws Exception;
    EntitiesDefinitionDTO getEntitiesDefinition();
    List<RuleDTO> getRules();
    TerminationDTO getTermination();
    EnvironmentDTO getEnvironmentProperties();
    void setEnvironmentProperties(UserInputDTO input);
    SimulationRunMetadataDTO RunSimulation();
    SimulationExecutionDetailsDTO getSimulationResults(String runIdentifier);
    Map<String, SimulationExecutionDetailsDTO> getAllSimulationResults();

    void exit();
}
