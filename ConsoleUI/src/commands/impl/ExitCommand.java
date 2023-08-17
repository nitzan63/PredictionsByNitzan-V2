package commands.impl;

import api.DTOSimulationInterface;
import commands.api.Command;

public class ExitCommand implements Command {
    DTOSimulationInterface simulationInterface;

    public ExitCommand(DTOSimulationInterface simulationInterface) {
        this.simulationInterface = simulationInterface;
    }

    @Override
    public void execute() {
        System.out.println("Exiting...");
        simulationInterface.exit();
    }
}
