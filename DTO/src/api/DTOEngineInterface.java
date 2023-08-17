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
    void setEnvironmentProperties(UserEnvironmentInputDTO input);
    SimulationRunMetadataDTO RunSimulation();
    SimulationRunResultsDTO getSimulationResults(String runIdentifier);
    Map<String, SimulationRunResultsDTO> getAllSimulationResults();

    void exit();
}
