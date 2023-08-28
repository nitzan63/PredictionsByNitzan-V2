package components.results;

import api.DTOUIInterface;
import components.SharedResources;
import dto.SimulationRunResultsDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
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
    private ChoiceBox<?> propertyChooser;
    @FXML
    private BarChart<?, ?> propertyHistogramBarChart;
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
    private Map<String, SimulationRunResultsDTO> allResults;
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
    }


    private void initializePastSimulationList(){
        System.out.println("Initializing past simulations list...");
        // fetch simulation results:
        allResults = simulationInterface.getAllSimulationResults();
        System.out.println("All Results: " + allResults);
        // Create an ObservableList for the ListView
        ObservableList<String> pastSimulationsObservable = FXCollections.observableArrayList();
        // Populate the ObservableList with simulation IDs
        for (SimulationRunResultsDTO dto : allResults.values()) {
            pastSimulationsObservable.add(dto.getRunIdentifier());
        }
        // Assign the ObservableList to the ListView
        pastSimulationList.setItems(pastSimulationsObservable);
    }


}
