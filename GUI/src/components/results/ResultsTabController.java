package components.results;

import api.DTOUIInterface;
import components.SharedResources;
import dto.PropertyHistogramDTO;
import dto.SimulationExecutionDetailsDTO;
import dto.TerminationDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ResultsTabController {

    @FXML
    private ChoiceBox<String> populationStatsEntityChooser;
    @FXML
    private ChoiceBox<String> propertyEntitiesChooser;
    @FXML
    private ListView<String> simulationList;
    @FXML
    private Button pauseButton;
    @FXML
    private LineChart<Integer, Integer> populationLineChart;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressPercentLabel;
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
    @FXML
    private Tab simulationDetailsTab;
    @FXML
    private TableView<String> entitiesPopulationTable;
    @FXML
    private TableView<String> environmentPropertiesTable;
    @FXML
    private Label simulationStatusLabel;
    // DTO - UI interface:
    private DTOUIInterface simulationInterface;
    private Map<String, SimulationExecutionDetailsDTO> allResults;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public void initialize() {
        this.simulationInterface = SharedResources.getInstance().getDTOUIInterface();
        simulationInterface.addSimulationRunListener(this::initializeSimulationList);
        SharedResources.getInstance().getIsFileSelected().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                initializeSimulationList();
            }
        });

        // Add a listener to enable/disable tabs based on list selection
        simulationList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
        simulationList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                propertyHistogramTab.setDisable(false);  // Enable the tab
                populationStatisticsTab.setDisable(false);  // Enable the other tab as well
                loadPropertyChoices(newVal);  // Load the properties for the selected simulation
            }
        });

        // Add a listener to propertyChooser to update the propertyHistogramBarChart when a property is selected.
        propertyChooser.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String runIdentifier = simulationList.getSelectionModel().getSelectedItem();
                updateHistogramChart(runIdentifier, newVal.toString());  // Update the chart
            }
        });
    }

    // Initialize shared resources and DTO interface
    private void initResources() {
        this.simulationInterface = SharedResources.getInstance().getDTOUIInterface();
        simulationInterface.addSimulationRunListener(this::initializeSimulationList);
        SharedResources.getInstance().getIsFileSelected().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                initializeSimulationList();
            }
        });
    }

    // Initialize listeners for the simulation list
    private void initSimulationListListeners() {
        simulationList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleSimulationSelection(newValue);
        });
    }

    // Initialize listeners for the property chooser
    private void initPropertyChooserListener() {
        propertyChooser.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String runIdentifier = simulationList.getSelectionModel().getSelectedItem();
                updateHistogramChart(runIdentifier, newVal.toString());  // Update the chart
            }
        });
    }

    // Handle simulation selection logic
    private void handleSimulationSelection(String newValue) {
        // Check if a simulation is actually selected
        if (newValue != null) {
            // Enable all related tabs
            enableTabs();
            // Load properties related to the selected simulation
            loadPropertyChoices(newValue);

            // If the simulation is currently live (running)
            if (isSimulationLive(newValue)) {
                // Enable the Pause and Stop buttons
                pauseButton.setDisable(false);
                // Disable the Resume button
                resumeButton.setDisable(true);
                // Enable the Stop button
                stopButton.setDisable(false);
                // Start updating the UI to reflect the live simulation data
                startPolling(newValue);
            } else {
                // Disable all buttons since the simulation is not live
                pauseButton.setDisable(true);
                resumeButton.setDisable(true);
                stopButton.setDisable(true);
                // Stop updating the UI
                stopPolling();
            }
        } else {
            // Disable all related tabs and buttons since nothing is selected
            disableTabs();
            pauseButton.setDisable(true);
            resumeButton.setDisable(true);
            stopButton.setDisable(true);
        }
    }

    // Enable the tabs
    private void enableTabs() {
        propertyHistogramTab.setDisable(false);
        populationStatisticsTab.setDisable(false);
        simulationDetailsTab.setDisable(false);
    }

    // Disable the tabs
    private void disableTabs() {
        propertyHistogramTab.setDisable(true);
        populationStatisticsTab.setDisable(true);
        simulationDetailsTab.setDisable(true);
    }

    // Check if the simulation is live (you'll need to implement this based on your data)
    private boolean isSimulationLive(String simulationId) {
        SimulationExecutionDetailsDTO details = simulationInterface.getLiveSimulationExecutionDetails(simulationId);
        return !details.isSimulationComplete();
    }

    public void startPolling(String simulationId) {
        final Runnable updater = () -> updateSimulationDetails(simulationId);
        scheduler.scheduleAtFixedRate(updater, 0, 200, TimeUnit.MILLISECONDS);
    }

    // Stop polling
    public void stopPolling() {
        scheduler.shutdownNow();  // This will cancel currently executing tasks
        scheduler = Executors.newScheduledThreadPool(1);  // Create a new scheduler for future tasks
    }





    private void updateHistogramChart(String runIdentifier, String propertyName) {
/*        SimulationExecutionDetailsDTO selectedRun = simulationInterface.getSimulationResults(runIdentifier);
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
        }*/
    }

    private void loadPropertyChoices(String runIdentifier) {
        SimulationExecutionDetailsDTO selectedRun = simulationInterface.getSimulationResults(runIdentifier);
/*        if (selectedRun != null) {
            ObservableList<String> propertyNames = FXCollections.observableArrayList(selectedRun.getPropertyHistograms().keySet());
            propertyChooser.setItems(propertyNames);
        }*/
    }


    private void initializeSimulationList(){
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
        simulationList.setItems(pastSimulationsObservable);
    }



    private void updateSimulationDetails(String simulationId) {
        // Call engine to get SED
        SimulationExecutionDetailsDTO details = simulationInterface.getLiveSimulationExecutionDetails(simulationId);
        // Update UI (on the JavaFX thread)
        Platform.runLater(() -> {
            updateProgressSection(details);
        });
    }

    private void updateProgressSection(SimulationExecutionDetailsDTO details) {
        if(details != null) {
            // get ticks and seconds
            int currTick = details.getCurrTick();
            int elapsedTime = details.getElapsedSeconds();
            double progress = 0;

            // set the progress:
            TerminationDTO terminationDTO = simulationInterface.getTermination();
            int maxTicks = terminationDTO.getMaxTicks();
            if (terminationDTO.getMaxTicks() != null) {
                progress = (double) currTick /maxTicks;
            } else if (terminationDTO.getMaxTime() != null){
                int maxTime = terminationDTO.getMaxTime();
                progress = (double) elapsedTime / maxTime;
            } else {
                progressBar.setDisable(true);
            }


            // Update Status Label and ProgressBar:
            if (details.isSimulationComplete()) {
                simulationStatusLabel.setText("Simulation " + details.getRunIdentifier() +" Completed!");
                simulationStatusLabel.setStyle("-fx-text-fill: green;");
                progressBar.setProgress(1);
                progressPercentLabel.setText("100%");
            } else {
                simulationStatusLabel.setText("Simulation " + details.getRunIdentifier() +" Ongoing...");
                simulationStatusLabel.setStyle("-fx-text-fill: #4a63e8");
                progressBar.setProgress(progress);
                progressPercentLabel.setText((progress*100) + "%");
            }

            // update ticks count:
            if (terminationDTO.getMaxTicks() != null){
                ticksCounterLabel.setText(currTick + "/" + maxTicks);
            } else {
                ticksCounterLabel.setText(String.valueOf(currTick));
            }

            // update seconds count:
            secondsCounterLabel.setText(elapsedTime + " Seconds");
        }
    }

    @FXML
    private void onPauseButtonClicked() {
        // Get the currently selected simulation from the simulation list
        String selectedSimulation = simulationList.getSelectionModel().getSelectedItem();
        // Check if a simulation is actually selected
        if (selectedSimulation != null) {
            // Use the DTOUIInterface to pause the selected simulation
            simulationInterface.pauseSimulation(selectedSimulation);
            // Disable the Pause button since the simulation is now paused
            pauseButton.setDisable(true);
            // Enable the Resume and Stop buttons
            resumeButton.setDisable(false);
            stopButton.setDisable(false);
        }
    }

    @FXML
    private void onResumeButtonClicked() {
        // Get the currently selected simulation from the simulation list
        String selectedSimulation = simulationList.getSelectionModel().getSelectedItem();
        // Check if a simulation is actually selected
        if (selectedSimulation != null) {
            // Use the DTOUIInterface to resume the selected simulation
            simulationInterface.resumeSimulation(selectedSimulation);
            // Enable the Pause button since the simulation is now running
            pauseButton.setDisable(false);
            // Disable the Resume button
            resumeButton.setDisable(true);
            // Enable the Stop button
            stopButton.setDisable(false);
        }
    }

    @FXML
    private void onStopButtonClicked() {
        // Get the currently selected simulation from the simulation list
        String selectedSimulation = simulationList.getSelectionModel().getSelectedItem();
        // Check if a simulation is actually selected
        if (selectedSimulation != null) {
            // Use the DTOUIInterface to stop the selected simulation
            simulationInterface.stopSimulation(selectedSimulation);
            // Disable all buttons since the simulation is now stopped
            pauseButton.setDisable(true);
            resumeButton.setDisable(true);
            stopButton.setDisable(true);
        }
    }


}
