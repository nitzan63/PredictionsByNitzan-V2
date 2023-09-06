package commands.impl;

import api.DTOUIInterface;
import commands.api.Command;
import dto.PopulationStatisticsDTO;
import dto.PropertyHistogramDTO;
import dto.SimulationExecutionDetailsDTO;
import input.UserInputHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.*;

public class DisplayPastActivationCommand implements Command {
    private final DTOUIInterface simulationInterface;
    private final UserInputHandler inputHandler;
    private List<SimulationExecutionDetailsDTO> sortedSimulations;



    public DisplayPastActivationCommand(DTOUIInterface simulationInterface, UserInputHandler inputHandler) {
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
        Map<String, SimulationExecutionDetailsDTO> allSimulations = simulationInterface.getAllSimulationResults();
        sortedSimulations = new ArrayList<>(allSimulations.values());
        sortedSimulations.sort(Comparator.comparing(SimulationExecutionDetailsDTO::getDateTime));
        for (int i = 0; i < sortedSimulations.size(); i++) {
            System.out.println((i + 1) + ". Date and Time: " + sortedSimulations.get(i).getDateTime() + " | Simulation ID: " + sortedSimulations.get(i).getRunIdentifier());
        }
    }


    private void displaySimulationResults(String runIdentifier) {
        SimulationExecutionDetailsDTO results = simulationInterface.getSimulationResults(runIdentifier);
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
        System.out.println("Entity: " + populationStatistics.getEntityName() + ", Initial Population: " + (populationStatistics.getInitialPopulation()) + ", Final Population: " + populationStatistics.getFinalPopulation());

    }

    private void displayPropertyHistograms(Map<String, PropertyHistogramDTO> propertyHistograms) {
        System.out.println("Property Histograms:");
        for (String propertyName : propertyHistograms.keySet()) {
            PropertyHistogramDTO histogram = propertyHistograms.get(propertyName);
            System.out.println("\nProperty: " + propertyName);
            System.out.println("---------------------------");

            // Sort the histogram keys based on their natural order or numerical value
            Map<String, Integer> sortedHistogram = new TreeMap<>((a, b) -> {
                try {
                    double valA = Double.parseDouble(a);
                    double valB = Double.parseDouble(b);
                    return Double.compare(valA, valB);
                } catch (NumberFormatException e) {
                    return a.compareTo(b);
                }
            });

            sortedHistogram.putAll(histogram.getHistogram());

            for (Map.Entry<String, Integer> entry : sortedHistogram.entrySet()) {
                System.out.println("Value: " + entry.getKey() + ", Count: " + entry.getValue());
            }
        }
    }

}
