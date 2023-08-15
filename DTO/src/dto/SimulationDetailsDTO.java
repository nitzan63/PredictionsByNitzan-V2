package dto;

import java.util.List;

public class SimulationDetailsDTO {
    private List<EntitiesDefinitionDTO> entityDefinitions;
    private List<RuleDTO> rules;
    private TerminationDTO termination;

    public SimulationDetailsDTO(List<EntitiesDefinitionDTO> entityDefinitions, List<RuleDTO> rules, TerminationDTO termination) {
        this.entityDefinitions = entityDefinitions;
        this.rules = rules;
        this.termination = termination;
    }

    public List<EntitiesDefinitionDTO> getEntityDefinitions() {
        return entityDefinitions;
    }

    public void setEntityDefinitions(List<EntitiesDefinitionDTO> entityDefinitions) {
        this.entityDefinitions = entityDefinitions;
    }

    public List<RuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<RuleDTO> rules) {
        this.rules = rules;
    }

    public TerminationDTO getTermination() {
        return termination;
    }

    public void setTermination(TerminationDTO termination) {
        this.termination = termination;
    }

    @Override
    public String toString() {
        return "SimulationDetailsDTO{" +
                "entityDefinitions=" + entityDefinitions +
                ", rules=" + rules +
                ", termination=" + termination +
                '}';
    }
}
