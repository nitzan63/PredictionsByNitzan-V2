package commands.impl;

import api.DTOSimulationInterface;
import commands.api.Command;
import dto.EnvironmentDTO;
import dto.PropertyDTO;
import dto.SimulationRunMetadataDTO;
import dto.UserEnvironmentInputDTO;
import input.UserInputHandler;

import java.util.*;

public class RunSimulationCommand implements Command {
    private final DTOSimulationInterface simulationInterface;
    private final UserInputHandler inputHandler;
    private final Map<String, String > userInputProperties;

    public RunSimulationCommand(DTOSimulationInterface simulationInterface, UserInputHandler inputHandler) {
        this.simulationInterface = simulationInterface;
        this.inputHandler = inputHandler;
        this.userInputProperties = new LinkedHashMap<>();
    }

    public void execute() {
        //Handle Environment properties from the user:
        handleEnvironmentProperties();
        displayFinalEnvironmentProperties();

        SimulationRunMetadataDTO result = simulationInterface.runSimulation();

        System.out.println("Simulation run successfully!");
        System.out.println("Simulation ID: " + result.getRunIdentifier());
        System.out.println("DateTime: " + result.getDateTime());
        System.out.println("Reason for termination: " + result.getTerminationReason());
    }

    private void handleEnvironmentProperties() {
        displayEnvironmentProperties();
        System.out.println("You can manually set the value of an environment property.");
        while (true) {
            int choice = getValidChoice();
            if (choice == 0) break;
            System.out.println("Please enter the value for the selected property: ");
            String newValue = inputHandler.getUserCommand();
            updateEnvironmentProperty(choice, newValue);
            displayEnvironmentProperties();
            displayUpdatedProperties();
        }
    }

    private int getValidChoice() {
        int choice;
        int totalProperties = simulationInterface.getEnvironmentProperties().getEnvironmentProperties().size();
        while (true) {
            System.out.println("Please choose a property from the list (by writing its number), or 0 to continue and run the simulation:");
            choice = inputHandler.getUserInt();
            if (choice >= 0 && choice <= totalProperties) break;
            System.out.println("Invalid choice. Please enter a valid number.");
        }
        return choice;
    }

    private void displayEnvironmentProperties() {
        EnvironmentDTO environmentDTO = simulationInterface.getEnvironmentProperties();
        Map<String, Object> environmentProperties = environmentDTO.getEnvironmentProperties();
        System.out.println("Environment properties:");
        int counter = 1;
        for (Map.Entry<String, Object> entry : environmentProperties.entrySet()) {
            PropertyDTO property = (PropertyDTO) entry.getValue();
            System.out.println(counter + ". Name: " + property.getName() + ", Type: " + property.getType() +
                    ", Range: [" + property.getRangeFrom() + ", " + property.getRangeTo() + "]");
            counter++;
        }
    }

    private void updateEnvironmentProperty(int choice, String newValue) {
        EnvironmentDTO environmentDTO = simulationInterface.getEnvironmentProperties();
        Map<String, Object> environmentProperties = environmentDTO.getEnvironmentProperties();

        int counter = 1;

        for (Map.Entry<String, Object> entry : environmentProperties.entrySet()) {
            if (counter == choice) {
                userInputProperties.put(entry.getKey(), newValue);
                break;
            }
            counter++;
        }
    }

    private void displayUpdatedProperties() {
        // This method should display the properties and values that have been updated by the user
        System.out.println("Updated properties:");
        for (Map.Entry<String, String> entry : userInputProperties.entrySet()) {
            System.out.println("Name: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }

    private void displayFinalEnvironmentProperties() {
        EnvironmentDTO environmentDTO = simulationInterface.getEnvironmentProperties();
        Map<String, Object> environmentProperties = environmentDTO.getEnvironmentProperties();
        System.out.println("Final environment properties:");
        for (Map.Entry<String, Object> entry : environmentProperties.entrySet()) {
            PropertyDTO property = (PropertyDTO) entry.getValue();
            String propertyName = property.getName();
            // Check if the user has updated the property
            if (userInputProperties.containsKey(propertyName)) {
                // Display the updated value
                System.out.println("Name: " + propertyName + ", Value: " + userInputProperties.get(propertyName));
            } else {
                // Display the original value
                System.out.println("Name: " + propertyName + ", Value: " + property.getValue());
            }
        }
    }
}
