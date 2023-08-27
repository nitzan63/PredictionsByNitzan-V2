package commands.impl;

import api.DTOUIInterface;
import commands.api.Command;
import dto.EntitiesDefinitionDTO;
import dto.PropertyDTO;
import dto.RuleDTO;
import dto.TerminationDTO;

public class DisplaySimulationDetailsCommand implements Command {
    private final DTOUIInterface simulationInterface;

    public DisplaySimulationDetailsCommand(DTOUIInterface simulationInterface) {
        this.simulationInterface = simulationInterface;
    }

    public void execute() {
        if (simulationInterface.isWorldLoaded()){
            displayEntities();
            displayRules();
            displayTerminationConditions();
        } else {
            System.out.println("To display simulation details, first load a file (command 1).");
        }
    }

    private void displayEntities() {
        System.out.println("\nEntities:");
        EntitiesDefinitionDTO entity = simulationInterface.getEntitiesDefinition();
        System.out.println(String.format("  %-15s: %s", "Name", entity.getName()));
        System.out.println(String.format("  %-15s: %d", "Population", entity.getPopulation()));
        System.out.println("  Properties:");
        for (PropertyDTO property : entity.getProperties()) {
            System.out.println(String.format("    %-20s: %s", "Name", property.getName()));
            System.out.println(String.format("    %-20s: %s", "Type", property.getType()));
            System.out.println(String.format("    %-20s: %.2f - %.2f", "Range", property.getRangeFrom(), property.getRangeTo()));
            System.out.println(String.format("    %-20s: %b", "Random Init", property.isRandomlyInitialized()));
        }
        System.out.println();

    }

    private void displayRules() {
        System.out.println("\nRules:");
        for (RuleDTO rule : simulationInterface.getRules()) {
            System.out.println(String.format("  %-20s: %s", "Name", rule.getName()));
            System.out.println(String.format("  %-20s: %d", "Activation ticks", rule.getActivationTicks()));
            System.out.println(String.format("  %-20s: %.2f", "Activation prob", rule.getActivationProbability()));
            System.out.println(String.format("  %-20s: %d", "Number of actions", rule.getNumberOfActions()));
            System.out.println("  Actions:");
            for (String action : rule.getActionNames()) {
                System.out.println(String.format("    %s", action));
            }
            System.out.println();
        }
    }

    private void displayTerminationConditions() {
        System.out.println("\nTermination conditions:");
        TerminationDTO terminationCondition = simulationInterface.getTermination();
        if (terminationCondition.getMaxTicks() != null && terminationCondition.getMaxTicks() > 0)
            System.out.println("Termination By Ticks - " + terminationCondition.getMaxTicks() + " Ticks.");
        if (terminationCondition.getMaxTime() != null && terminationCondition.getMaxTime() > 0)
            System.out.println("Termination By Time - " + terminationCondition.getMaxTime() + " Seconds.");
    }

}
