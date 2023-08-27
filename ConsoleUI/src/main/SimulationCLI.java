package main;

import api.DTOUIInterface;
import commands.api.Command;
import commands.impl.*;
import engine.SimulationEngine;
import input.UserInputHandler;
import output.MenuDisplay;

public class SimulationCLI {
    private final MenuDisplay menuDisplay;
    private final UserInputHandler userInputHandler;
    private final DTOUIInterface simulationInterface;


    public SimulationCLI() {
        this.menuDisplay = new MenuDisplay();
        this.userInputHandler = new UserInputHandler();
        this.simulationInterface = new DTOUIInterface(new SimulationEngine());
    }

    public static void main(String[] args) {
        SimulationCLI simulationCLI = new SimulationCLI();
        simulationCLI.runCLI();
    }

    public void runCLI() {
        // Display the welcome message
        System.out.println("Welcome to the Predictions Simulation by Nitzan!");

        // Start the command loop
        while (true) {
            menuDisplay.displayMenu();
            String userCommand = userInputHandler.getUserCommand();

            try {
                handleCommand(userCommand);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void handleCommand(String command) {
        switch (command) {
            case "1":
                Command loadXmlCommand = new LoadXmlFileCommand(simulationInterface, userInputHandler);
                loadXmlCommand.execute();
                break;
            case "2":
                Command displayDetailsCommand = new DisplaySimulationDetailsCommand(simulationInterface);
                displayDetailsCommand.execute();
                break;
            case "3":
                Command runSimulation = new RunSimulationCommand(simulationInterface, userInputHandler);
                runSimulation.execute();
                break;
            case "4":
                Command displayPastResults = new DisplayPastActivationCommand(simulationInterface, userInputHandler);
                displayPastResults.execute();
                break;
            case "5":
                Command exitCommand = new ExitCommand(simulationInterface);
                exitCommand.execute();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid command. Please try again.");
        }
    }
}

