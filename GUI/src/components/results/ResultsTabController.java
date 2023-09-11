package components.results;

import api.DTOUIInterface;
import components.SharedResources;
import components.main.MainController;
import dto.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ResultsTabController {

    @FXML
    private ChoiceBox<String> populationStatsEntityChooser;
    @FXML
    private Button reRunButton;
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

        // Initialize Buttons:
        initButtons();

        // Initialize the listeners for the simulation list and property chooser
        initSimulationListListeners();

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

    private void initButtons(){
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        resumeButton.setDisable(true);
        reRunButton.setDisable(true);
    }

    // Initialize listeners for the simulation list
    private void initSimulationListListeners() {
        simulationList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleSimulationSelection(newValue);
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
            // Populate Environment Properties Table
            populateEnvironmentPropertiesTable(newValue);
            // Initialize population statistics and property histogram tabs here.
            initPopulationStatisticsTab(newValue);
            initPropertyHistogramTab(newValue);
            if (isSimulationLive(newValue)) {
                // The simulation is currently live (running)
                disablePropertyAndPopulationTabs();
                pauseButton.setDisable(false);
                resumeButton.setDisable(true);
                stopButton.setDisable(false);
                reRunButton.setDisable(true);
                // Start updating the UI to reflect the live simulation data
                startPolling(newValue);
            } else if (isSimulationPaused(newValue)) {
                // The simulation is paused
                enableAllTabs();
                pauseButton.setDisable(true);
                resumeButton.setDisable(false);
                stopButton.setDisable(false);
                reRunButton.setDisable(true);
                // Update UI with current data but don't continue polling
                updateEntitiesPopulationTable(newValue);
            } else if (isSimulationQueued(newValue)){
                // Simulation is in queue but yet to start
                pauseButton.setDisable(true);
                resumeButton.setDisable(true);
                stopButton.setDisable(true);
                reRunButton.setDisable(true);
                disablePropertyAndPopulationTabs();
                updateEntitiesPopulationTable(newValue);
            }

            else {
                // The simulation is completed
                enableAllTabs();
                pauseButton.setDisable(true);
                resumeButton.setDisable(true);
                stopButton.setDisable(true);
                reRunButton.setDisable(false);
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
        // fetch simulation results:
        allResults = simulationInterface.getAllSimulationResults();
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
            enableTabs();
            updateProgressSection(simulationInterface.getLiveSimulationExecutionDetails(selectedSimulation));
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
            updateProgressSection(simulationInterface.getLiveSimulationExecutionDetails(selectedSimulation));
            disableTabs();
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
            reRunButton.setDisable(false);
            updateProgressSection(allResults.get(selectedSimulation));
            enableTabs();
        }
    }
    @FXML
    public void onReRunButtonAction() {
        String selectedSimulation = simulationList.getSelectionModel().getSelectedItem();
        SimulationExecutionDetailsDTO simulationDetails = allResults.get(selectedSimulation);
        UserInputDTO userInputDTO = simulationDetails.getUserInputDTO();

        SharedResources.getInstance().setLastSelectedSimulation(userInputDTO);
        MainController mainController = SharedResources.getInstance().getMainController();
        if (mainController != null) {
            mainController.switchToExecutionTab();
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

    private void initPopulationStatisticsTab(String runID) {
        if (runID == null || allResults.get(runID) == null) {
            // Data is not ready; return or disable the tab.
            populationStatisticsTab.setDisable(true);
            return;
        }
        // Populate ChoiceBox with Entity Names (This assumes you can get entity names from simulationInterface)
        List<String> entityNames = new ArrayList<>(simulationInterface.getEntitiesDefinition().keySet());
        populationStatsEntityChooser.getItems().setAll(entityNames);

        // Set a Listener to update the chart when an entity is chosen
        populationStatsEntityChooser.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String runIdentifier = simulationList.getSelectionModel().getSelectedItem();
                updatePopulationLineChart(runIdentifier, newVal.toString());  // Update the chart
            }
        });
    }

    private void updatePopulationLineChart(String runIdentifier, String selectedEntity) {
        // Get the SimulationExecutionDetailsDTO based on the selected simulation
        SimulationExecutionDetailsDTO details = allResults.get(runIdentifier);

        // Fetch the PopulationStatisticsDTO based on the selected entity
        PopulationStatisticsDTO populationStats = details.getPopulationStatisticsDTOMap().get(selectedEntity);

        // Clear the old data
        populationLineChart.getData().clear();

        // Initialize new data series
        XYChart.Series<Integer, Integer> series = new XYChart.Series<>();

        // Populate new data series
        for (Map.Entry<Integer, Integer> entry : populationStats.getTicksToPopulationMap().entrySet()) {
            int tick = entry.getKey();
            int population = entry.getValue();
            series.getData().add(new XYChart.Data<>(tick, population));
        }

        // Set the name for this series
        series.setName(selectedEntity);

        // Update the chart
        populationLineChart.getData().add(series);
    }

    private void initPropertyHistogramTab(String runID) {
        if (runID == null || allResults.get(runID) == null) {
            // Data is not ready; return or disable the tab.
            propertyHistogramTab.setDisable(true);
            return;
        }

        // Populate ChoiceBox with Entity Names
        List<String> entityNames = new ArrayList<>(allResults.get(runID).getEntityPropertiesHistogramDTOMap().keySet());
        propertyEntitiesChooser.getItems().setAll(entityNames);

        // Set a Listener to update the Property ChoiceBox when an entity is chosen
        propertyEntitiesChooser.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                String runIdentifier = simulationList.getSelectionModel().getSelectedItem();
                updatePropertyChoiceBox(runIdentifier, newVal.toString());
            }
        });
    }

    private void updatePropertyChoiceBox(String runIdentifier, String selectedEntity) {
        SimulationExecutionDetailsDTO details = allResults.get(runIdentifier);
        EntityPropertiesHistogramDTO entityHistogram = details.getEntityPropertiesHistogramDTOMap().get(selectedEntity);

        // Clear the old data
        propertyChooser.getItems().clear();

        // Populate new data
        List<String> propertyNames = new ArrayList<>(entityHistogram.getPropertyHistograms().keySet());
        propertyChooser.getItems().setAll(propertyNames);

        // Set Listener to update Histogram when a property is chosen
        propertyChooser.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updatePropertyHistogramBarChart(runIdentifier, selectedEntity, newVal.toString());
            }
        });
    }

    private void updatePropertyHistogramBarChart(String runIdentifier, String entity, String property) {
        // Get the SimulationExecutionDetailsDTO based on the selected simulation
        SimulationExecutionDetailsDTO details = allResults.get(runIdentifier);

        // Fetch the PropertyHistogramDTO based on the selected entity and property
        PropertyHistogramDTO propertyHistogram = details.getEntityPropertiesHistogramDTOMap().get(entity).getPropertyHistograms().get(property);

        // Clear the old data
        propertyHistogramBarChart.getData().clear();

        // Initialize new data series
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Populate new data series
        for (Map.Entry<String, Integer> entry : propertyHistogram.getHistogram().entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Update the chart
        propertyHistogramBarChart.getData().add(series);
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




}
