package commands.impl;

import api.DTOSimulationInterface;
import commands.api.Command;
import input.UserInputHandler;

public class DisplayPastActivationCommand implements Command {
    private final DTOSimulationInterface simulationInterface;
    private final UserInputHandler inputHandler;

    public DisplayPastActivationCommand(DTOSimulationInterface simulationInterface, UserInputHandler inputHandler) {
        this.simulationInterface = simulationInterface;
        this.inputHandler = inputHandler;
    }

    @Override
    public void execute() {

    }
}
