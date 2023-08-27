package commands.impl;

import api.DTOUIInterface;
import commands.api.Command;

public class ExitCommand implements Command {
    DTOUIInterface simulationInterface;

    public ExitCommand(DTOUIInterface simulationInterface) {
        this.simulationInterface = simulationInterface;
    }

    @Override
    public void execute() {
        System.out.println("Exiting...");
        simulationInterface.exit();
    }
}
