package dto;

import java.util.ArrayList;
import java.util.List;

public class RuleDTO {
    private String name;
    private int activationTicks;
    private double activationProbability;
    private List<ActionDTO> actionsList;

    private List<String> actionsNames;

    public RuleDTO(String name, int activationTicks, double activationProbability, List<ActionDTO> actionsList) {
        this.name = name;
        this.activationTicks = activationTicks;
        this.activationProbability = activationProbability;
        this.actionsList = actionsList;

        this.actionsNames = new ArrayList<>();

        for (ActionDTO actionDTO : actionsList){
            actionsNames.add(actionDTO.getType());
        }
    }

    public String getName() {
        return name;
    }

    public int getActivationTicks() {
        return activationTicks;
    }

    public double getActivationProbability() {
        return activationProbability;
    }

    public List<ActionDTO> getActionsList() {
        return actionsList;
    }

    public List<String> getActionNames() {
        return actionsNames;
    }
}
