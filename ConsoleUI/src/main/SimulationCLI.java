package main;

import input.UserInputHandler;
import output.MenuDisplay;

public class SimulationCLI {
    private final MenuDisplay menuDisplay;
    private final UserInputHandler userInputHandler;

    public SimulationCLI() {
        this.menuDisplay = new MenuDisplay();
        this.userInputHandler = new UserInputHandler();
    }

    public static void main(String[] args) {
        SimulationCLI simulationCLI = new SimulationCLI();
        simulationCLI.runCLI();
    }

    public void runCLI() {
        // Display the welcome message
        System.out.println("Welcome to the Simulation CLI!");

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
                // Handle the "Load XML File" command
                // TODO: Implement this
                break;
            case "2":
                // Handle the "Display Simulation Details" command
                // TODO: Implement this
                break;
            // Add other cases for other commands
            default:
                System.out.println("Invalid command. Please try again.");
        }
    }
}
