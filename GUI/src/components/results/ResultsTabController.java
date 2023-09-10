package components.results;

import api.DTOUIInterface;
import components.SharedResources;
import dto.PropertyDTO;
import dto.PropertyHistogramDTO;
import dto.SimulationExecutionDetailsDTO;
import dto.TerminationDTO;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
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
    private Label simulationStatusLabel;
    @FXML
    private TableView<Map.Entry<String, String>> environmentPropertiesTable;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> propertyColumn;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> valueColumn;
    @FXML
    private TableView<Map.Entry<String, Integer>> entitiesPopulationTable;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> entityColumn;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> populationColumn;
    // DTO - UI interface:
    private DTOUIInterface simulationInterface;
    private Map<String, SimulationExecutionDetailsDTO> allResults;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public void initialize() {
        // Initialize shared resources and DTO interface
        initResources();

        // Initialize the listeners for the simulation list and property chooser
        initSimulationListListeners();
        initPropertyChooserListener();

        // Initialize tabs
        initSimulationDetailsTab();
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
            // Fetch details about the selected simulation
            SimulationExecutionDetailsDTO details = allResults.get(newValue); // Assuming allResults is a Map keyed by simulation IDs
            // Update the progress section based on the selected simulation
            updateProgressSection(details);
            // Load properties related to the selected simulation
            loadPropertyChoices(newValue);
            // Populate Environment Properties Table
            populateEnvironmentPropertiesTable(newValue);

            if (isSimulationLive(newValue)) {
                // The simulation is currently live (running)
                disablePropertyAndPopulationTabs();
                pauseButton.setDisable(false);
                resumeButton.setDisable(true);
                stopButton.setDisable(false);
                // Start updating the UI to reflect the live simulation data
                startPolling(newValue);
            } else if (isSimulationPaused(newValue)) {
                // The simulation is paused
                enableAllTabs();
                pauseButton.setDisable(true);
                resumeButton.setDisable(false);
                stopButton.setDisable(false);
                // Update UI with current data but don't continue polling
                updateEntitiesPopulationTable(newValue);
            } else if (isSimulationQueued(newValue)){
                // Simulation is in queue but yet to start
                pauseButton.setDisable(true);
                resumeButton.setDisable(true);
                stopButton.setDisable(true);
                disablePropertyAndPopulationTabs();
                updateEntitiesPopulationTable(newValue);
            }

            else {
                // The simulation is completed
                enableAllTabs();
                pauseButton.setDisable(true);
                resumeButton.setDisable(true);
                stopButton.setDisable(true);
                // Stop updating the UI and populate entities table with final data
                stopPolling();
                updateEntitiesPopulationTable(newValue);
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
    private boolean isSimulationLive(String runId) {
        SimulationExecutionDetailsDTO details = simulationInterface.getLiveSimulationExecutionDetails(runId);
        return details.isSimulationLive();
    }

    private boolean isSimulationPaused(String runID){
        SimulationExecutionDetailsDTO details = simulationInterface.getLiveSimulationExecutionDetails(runID);
        return details.isSimulationPaused();
    }

    private boolean isSimulationCompleted (String runID){
        SimulationExecutionDetailsDTO detailsDTO = simulationInterface.getLiveSimulationExecutionDetails(runID);
        return detailsDTO.isSimulationCompleted();
    }

    private boolean isSimulationQueued(String runID){
        SimulationExecutionDetailsDTO detailsDTO = simulationInterface.getLiveSimulationExecutionDetails(runID);
        return detailsDTO.isSimulationQueued();
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


    private void initializeSimulationList() {
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
            updateEntitiesPopulationTable(details.getEntitiesPopulationMap());
        });
    }

    private void updateProgressSection(SimulationExecutionDetailsDTO details) {
        if (details != null) {
            // get ticks and seconds
            int currTick = details.getCurrTick();
            int elapsedTime = details.getElapsedSeconds();
            double progress = 0;

            // set the progress:
            TerminationDTO terminationDTO = simulationInterface.getTermination();
            Integer maxTicks = terminationDTO.getMaxTicks();
            if (terminationDTO.getMaxTicks() != null) {
                progress = (double) currTick / maxTicks;
            } else if (terminationDTO.getMaxTime() != null) {
                int maxTime = terminationDTO.getMaxTime();
                progress = (double) elapsedTime / maxTime;
            } else {
                progressBar.setDisable(true);
            }


            // Update Status Label and ProgressBar:
            if (details.isSimulationCompleted()) {
                simulationStatusLabel.setText("Simulation " + details.getRunIdentifier() + " Completed!");
                simulationStatusLabel.setStyle("-fx-text-fill: green;");
                progressBar.setProgress(1);
                progressPercentLabel.setText("100%");
            } else if (details.isSimulationLive()) {
                simulationStatusLabel.setText("Simulation " + details.getRunIdentifier() + " Ongoing...");
                simulationStatusLabel.setStyle("-fx-text-fill: #4a63e8");
                progressBar.setProgress(progress);
                progressPercentLabel.setText((progress * 100) + "%");
            } else if (details.isSimulationPaused()){
                simulationStatusLabel.setText("Simulation " + details.getRunIdentifier() + " Paused!");
                simulationStatusLabel.setStyle("-fx-text-fill: #e79f20");
                progressBar.setProgress(progress);
                progressPercentLabel.setText((progress * 100) + "%");
            } else if (details.isSimulationQueued()){
                simulationStatusLabel.setText("Simulation " + details.getRunIdentifier() + " Queued");
                simulationStatusLabel.setStyle("-fx-text-fill: #c914bf");
                progressBar.setProgress(0);
                progressPercentLabel.setText((0 * 100) + "%");
            }

            // update ticks count:
            if (terminationDTO.getMaxTicks() != null) {
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
        String selectedSimulation = simulationList.getSelectionModel().getSelectedItem();
        if (selectedSimulation != null) {
            simulationInterface.pauseSimulation(selectedSimulation);
            pauseButton.setDisable(true);
            resumeButton.setDisable(false);
            stopButton.setDisable(false);
            // Stop updating the UI for the paused simulation but update UI with current data
            stopPolling();
            updateEntitiesPopulationTable(selectedSimulation);
        }
    }


    @FXML
    private void onResumeButtonClicked() {
        String selectedSimulation = simulationList.getSelectionModel().getSelectedItem();
        if (selectedSimulation != null) {
            simulationInterface.resumeSimulation(selectedSimulation);
            pauseButton.setDisable(false);
            resumeButton.setDisable(true);
            stopButton.setDisable(false);
            // Restart updating the UI to reflect the live simulation data
            startPolling(selectedSimulation);
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

    private void initSimulationDetailsTab(){
        // for the entities table:
        TableColumn<Map.Entry<String, Integer>, String> entityColumn = new TableColumn<>("Entity");
        entityColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKey()));

        TableColumn<Map.Entry<String, Integer>, Integer> populationColumn = new TableColumn<>("Population");
        populationColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getValue()));

        entitiesPopulationTable.getColumns().setAll(entityColumn, populationColumn);

        // for the environment table:
        TableColumn<Map.Entry<String, String>, String> propertyNameColumn = new TableColumn<>("Property Name");
        propertyNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));

        TableColumn<Map.Entry<String, String>, String> propertyValueColumn = new TableColumn<>("Property Value");
        propertyValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));

        environmentPropertiesTable.getColumns().setAll(propertyNameColumn, propertyValueColumn);

    }

    private void updateEntitiesPopulationTable(Map<String, Integer> entitiesPopulationMap) {
        ObservableList<Map.Entry<String, Integer>> items = FXCollections.observableArrayList(entitiesPopulationMap.entrySet());
        entitiesPopulationTable.setItems(items);
    }

    private void updateEntitiesPopulationTable(String simulationId) {
        SimulationExecutionDetailsDTO details = simulationInterface.getSimulationResults(simulationId);
        if (details != null) {
            ObservableList<Map.Entry<String, Integer>> data = FXCollections.observableArrayList(details.getEntitiesPopulationMap().entrySet());
            entitiesPopulationTable.setItems(data);
        }
    }

    private void populateEnvironmentPropertiesTable(String simulationId) {
        SimulationExecutionDetailsDTO details = simulationInterface.getSimulationResults(simulationId);
        if (details != null) {
            ObservableList<Map.Entry<String, String>> data = FXCollections.observableArrayList(details.getEnvironmentPropertiesValues().entrySet());
            environmentPropertiesTable.setItems(data);
        }
    }

    private void enableAllTabs() {
        propertyHistogramTab.setDisable(false);
        populationStatisticsTab.setDisable(false);
        simulationDetailsTab.setDisable(false);
    }

    private void disablePropertyAndPopulationTabs() {
        propertyHistogramTab.setDisable(true);
        populationStatisticsTab.setDisable(true);
        simulationDetailsTab.setDisable(false);
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
}
