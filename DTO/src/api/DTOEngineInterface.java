package api;

import dto.EntitiesDefinitionDTO;
import dto.EnvironmentDTO;
import dto.RuleDTO;
import dto.TerminationDTO;

import java.util.List;

public interface DTOEngineInterface {
    void loadXmlFile (String Path) throws Exception;
    EntitiesDefinitionDTO getEntitiesDefinition();
    List<RuleDTO> getRules();
    TerminationDTO getTermination();
    EnvironmentDTO getEnvironmentProperties();
}
