package commands.impl;

import api.DTOUIInterface;
import commands.api.Command;
import dto.EnvironmentDTO;
import dto.PropertyDTO;
import dto.SimulationRunMetadataDTO;
import dto.UserEnvironmentInputDTO;
import input.UserInputHandler;

import java.util.*;

public class RunSimulationCommand implements Command {
    private final DTOUIInterface simulationInterface;
    private final UserInputHandler inputHandler;
    private final Map<String, String> userInputProperties;

    public RunSimulationCommand(DTOUIInterface simulationInterface, UserInputHandler inputHandler) {
        this.simulationInterface = simulationInterface;
        this.inputHandler = inputHandler;
        this.userInputProperties = new LinkedHashMap<>();
    }

    public void execute() {
        //Handle Environment properties from the user:
        if (!simulationInterface.isWorldLoaded()){
            System.out.println("To run simulation, make sure you have loaded a valid file (Command 1)");
            return;
        }
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
        Map<String, PropertyDTO> environmentProperties = environmentDTO.getEnvironmentProperties();
        System.out.println("Environment properties:");
        int counter = 1;
        for (Map.Entry<String, PropertyDTO> entry : environmentProperties.entrySet()) {
            PropertyDTO property = entry.getValue();
            System.out.println(counter + ". Name: " + property.getName() + ", Type: " + property.getType() + ", Range: [" + property.getRangeFrom() + ", " + property.getRangeTo() + "]");
            counter++;
        }
    }

    private void updateEnvironmentProperty(int choice, String newValue) {
        EnvironmentDTO environmentDTO = simulationInterface.getEnvironmentProperties();
        Map<String, PropertyDTO> environmentProperties = environmentDTO.getEnvironmentProperties();

        int counter = 1;
        String selectedPropertyKey = null;

        for (Map.Entry<String, PropertyDTO> entry : environmentProperties.entrySet()) {
            if (counter == choice) {
                if (entry.getValue().getType().equals("decimal") || entry.getValue().getType().equals("float")) {
                    double value = Double.parseDouble(newValue);
                    if (value > entry.getValue().getRangeTo() || value < entry.getValue().getRangeFrom()) {
                        System.out.println("Property value should be in range from " + entry.getValue().getRangeFrom() + " to " + entry.getValue().getRangeTo());
                        break;
                    }
                } else if (entry.getValue().getType().equals("boolean")) {
                    if (!(newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("false"))) {
                        System.out.println("Property value needs to be boolean! (true of false");
                        break;
                    }
                }
                selectedPropertyKey = entry.getKey();
                break;
            }
            counter++;
        }

        if (selectedPropertyKey != null) {
            // Update the userInputProperties map
            userInputProperties.put(selectedPropertyKey, newValue);

            // Update the environment properties in the engine
            UserEnvironmentInputDTO inputDTO = new UserEnvironmentInputDTO(userInputProperties);
            simulationInterface.setEnvironmentProperties(inputDTO);
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
        Map<String, PropertyDTO> environmentProperties = environmentDTO.getEnvironmentProperties();
        System.out.println("Final environment properties:");
        for (Map.Entry<String, PropertyDTO> entry : environmentProperties.entrySet()) {
            PropertyDTO property = entry.getValue();
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
