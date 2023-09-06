package components.results;

import api.DTOUIInterface;
import components.SharedResources;
import dto.PropertyHistogramDTO;
import dto.SimulationExecutionDetailsDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.util.Map;

public class ResultsTabController {

    @FXML
    private ChoiceBox<?> entityChooser;
    @FXML
    private ListView<String> pastSimulationList;
    @FXML
    private Button pauseButton;
    @FXML
    private LineChart<?, ?> populationLineChart;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressPrecentLabel;
    @FXML
    private ChoiceBox<String> propertyChooser;
    @FXML
    private BarChart<String, Number> propertyHistogramBarChart;
    @FXML
    private Button resumeButton;
    @FXML
    private Label secondsCounterLabel;
    @FXML
    private Button stopButton;
    @FXML
    private Label ticksCounterLabel;
    @FXML
    private Tab propertyHistogramTab;
    @FXML
    private Tab populationStatisticsTab;
    // DTO - UI interface:
    private DTOUIInterface simulationInterface;
    private Map<String, SimulationExecutionDetailsDTO> allResults;
    //private ObservableList<String> pastSimulationsObservable;



    public void initialize() {
        this.simulationInterface = SharedResources.getInstance().getDTOUIInterface();
        simulationInterface.addSimulationRunListener(this::initializePastSimulationList);
        SharedResources.getInstance().getIsFileSelected().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                initializePastSimulationList();
            }
        });

        // Add a listener to enable/disable tabs based on list selection
        pastSimulationList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Enable the tabs
                propertyHistogramTab.setDisable(false);
                populationStatisticsTab.setDisable(false);
            } else {
                // Disable the tabs
                propertyHistogramTab.setDisable(true);
                populationStatisticsTab.setDisable(true);
            }
        });

        // Add a listener to pastSimulationList to handle when a simulation is selected
        pastSimulationList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                propertyHistogramTab.setDisable(false);  // Enable the tab
                populationStatisticsTab.setDisable(false);  // Enable the other tab as well
                loadPropertyChoices(newVal);  // Load the properties for the selected simulation
            }
        });

        // Add a listener to propertyChooser to update the propertyHistogramBarChart when a property is selected.
        propertyChooser.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String runIdentifier = pastSimulationList.getSelectionModel().getSelectedItem();
                updateHistogramChart(runIdentifier, newVal.toString());  // Update the chart
            }
        });
    }

    private void updateHistogramChart(String runIdentifier, String propertyName) {
        SimulationExecutionDetailsDTO selectedRun = simulationInterface.getSimulationResults(runIdentifier);
        if (selectedRun != null) {
            PropertyHistogramDTO histogram = selectedRun.getPropertyHistograms().get(propertyName);
            if (histogram != null) {
                // Clear old data
                propertyHistogramBarChart.getData().clear();

                // Create new series
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                for (Map.Entry<String, Integer> entry : histogram.getHistogram().entrySet()) {
                    series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                }

                // Add series to chart
                propertyHistogramBarChart.getData().add(series);
            }
        }
    }

    private void loadPropertyChoices(String runIdentifier) {
        SimulationExecutionDetailsDTO selectedRun = simulationInterface.getSimulationResults(runIdentifier);
        if (selectedRun != null) {
            ObservableList<String> propertyNames = FXCollections.observableArrayList(selectedRun.getPropertyHistograms().keySet());
            propertyChooser.setItems(propertyNames);
        }
    }


    private void initializePastSimulationList(){
        System.out.println("Initializing past simulations list...");
        // fetch simulation results:
        allResults = simulationInterface.getAllSimulationResults();
        System.out.println("All Results: " + allResults);
        // Create an ObservableList for the ListView
        ObservableList<String> pastSimulationsObservable = FXCollections.observableArrayList();
        // Populate the ObservableList with simulation IDs
        for (SimulationExecutionDetailsDTO dto : allResults.values()) {
            pastSimulationsObservable.add(dto.getRunIdentifier());
        }
        // Assign the ObservableList to the ListView
        pastSimulationList.setItems(pastSimulationsObservable);
    }


}
