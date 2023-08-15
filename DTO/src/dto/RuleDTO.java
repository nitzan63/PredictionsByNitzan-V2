package dto;

import java.util.List;

public class RuleDTO {
    private String name;
    private int activationTicks;
    private double activationProbability;
    private int numberOfActions;
    private List<String> actionNames;

    public RuleDTO(String name, int activationTicks, double activationProbability, int numberOfActions, List<String> actionNames) {
        this.name = name;
        this.activationTicks = activationTicks;
        this.activationProbability = activationProbability;
        this.numberOfActions = numberOfActions;
        this.actionNames = actionNames;
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

    public int getNumberOfActions() {
        return numberOfActions;
    }

    public List<String> getActionNames() {
        return actionNames;
    }
}
