package commands.impl;

import api.DTOSimulationInterface;
import commands.api.Command;
import dto.PopulationStatisticsDTO;
import dto.PropertyHistogramDTO;
import dto.SimulationRunResultsDTO;
import input.UserInputHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DisplayPastActivationCommand implements Command {
    private final DTOSimulationInterface simulationInterface;
    private final UserInputHandler inputHandler;
    private List<SimulationRunResultsDTO> sortedSimulations;



    public DisplayPastActivationCommand(DTOSimulationInterface simulationInterface, UserInputHandler inputHandler) {
        this.simulationInterface = simulationInterface;
        this.inputHandler = inputHandler;
    }

    @Override
    public void execute() {
        displayListOfSimulations();
        while (true) {
            System.out.println("Please select a simulation by its number:");
            int selection = inputHandler.getUserInt();
            if (selection >= 1 && selection <= sortedSimulations.size()) {
                String runIdentifier = sortedSimulations.get(selection - 1).getRunIdentifier();
                displaySimulationResults(runIdentifier);
                break;
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    private void displayListOfSimulations() {
        Map<String, SimulationRunResultsDTO> allSimulations = simulationInterface.getAllSimulationResults();
        sortedSimulations = new ArrayList<>(allSimulations.values());
        sortedSimulations.sort(Comparator.comparing(SimulationRunResultsDTO::getDateTime));
        for (int i = 0; i < sortedSimulations.size(); i++) {
            System.out.println((i + 1) + ". Date and Time: " + sortedSimulations.get(i).getDateTime() + " | Simulation ID: " + sortedSimulations.get(i).getRunIdentifier());
        }
    }


    private void displaySimulationResults(String runIdentifier) {
        SimulationRunResultsDTO results = simulationInterface.getSimulationResults(runIdentifier);
        if (results == null) {
            System.out.println("Invalid simulation ID. Please try again.");
            return;
        }

        while (true) {
            System.out.println("How would you want to see the results?");
            System.out.println("1. By Entity Population");
            System.out.println("2. By Property Statistics?");
            System.out.println("0. Return");
            int choice = inputHandler.getUserInt();
            switch (choice) {
                case 1:
                    displayPopulationStatistics(results.getPopulationStatistics());
                    break;
                case 2:
                    displayPropertyHistograms(results.getPropertyHistograms());
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Please choose from options 1 or 2.");
                    break;
            }

        }

    }

    private void displayPopulationStatistics(PopulationStatisticsDTO populationStatistics) {
        System.out.println("Population Statistics:");
        System.out.println("Entity: " + populationStatistics.getEntityName() + ", Initial Quantity: " + populationStatistics.getInitialPopulation() + ", Final Quantity: " + populationStatistics.getFinalPopulation());

    }

    private void displayPropertyHistograms(Map<String, PropertyHistogramDTO> propertyHistograms) {
        System.out.println("Property Histograms:");
        for (String propertyName : propertyHistograms.keySet()) {
            PropertyHistogramDTO histogram = propertyHistograms.get(propertyName);
            System.out.println("Property: " + propertyName);
            for (Map.Entry<String, Integer> entry : histogram.getHistogram().entrySet()) {
                System.out.println("Value: " + entry.getKey() + ", Count: " + entry.getValue());
            }
        }
    }
}
