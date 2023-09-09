package components.main;

import api.DTOUIInterface;
import components.SharedResources;
import components.execution.ExecutionTabController;
import dto.ThreadInfoDTO;
import engine.SimulationEngine;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;

public class MainController {
    // FXML elements:
    @FXML
    private Text activeThreadsView;
    @FXML
    private Tab detailsTabTitle;
    @FXML
    private Tab executionTabTitle;
    @FXML
    private Tab resultsTabTitle;
    @FXML
    private TextField filePathTextField;
    @FXML
    private Button loadFileButton;
    @FXML
    private Label activeThreadsLabel;
    @FXML
    private Label simulationsEndedLabel;
    @FXML
    private TabPane mainTabPane;


    // DTO Interface:
    DTOUIInterface simulationInterface;

    // Properties:
    private SimpleBooleanProperty isFileSelected;
    private SimpleStringProperty selectedFileProperty;
    private SimpleStringProperty activeThreadCount;
    private SimpleStringProperty simulationsEndedCount;

    // Stage:
    private Stage primaryStage;


    // Constructor:
    public MainController() {

        // Initialize DTO interface and shared resources
        this.simulationInterface = new DTOUIInterface(new SimulationEngine());
        SharedResources.getInstance().setDTOUIInterface(this.simulationInterface);

        // Initialize properties
        isFileSelected = new SimpleBooleanProperty(false);
        selectedFileProperty = new SimpleStringProperty();
        activeThreadCount = new SimpleStringProperty("0/0");
        simulationsEndedCount = new SimpleStringProperty("0");
    }

    public void initialize() {
        // Bind properties to UI elements:
        filePathTextField.textProperty().bind(selectedFileProperty);
        executionTabTitle.disableProperty().bind(isFileSelected.not());
        detailsTabTitle.disableProperty().bind(isFileSelected.not());
        resultsTabTitle.disableProperty().bind(isFileSelected.not());
        activeThreadsLabel.textProperty().bind(activeThreadCount);
        simulationsEndedLabel.textProperty().bind(simulationsEndedCount);

        // Start updating thread information when a file is selected
        isFileSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Setup a timeline to call updateThreadInfo() every second
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(200),
                        ae -> updateThreadInfo()));
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }
        });

        SharedResources.getInstance().setMainController(this);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void loadFileButtonListener() {
        try {
            // Open file dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select XML file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile == null)
                return;

            String filePath = selectedFile.getAbsolutePath();
            simulationInterface.loadXmlFile(filePath);
            selectedFileProperty.set(filePath);

            // Success alert:
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("File Loaded Successfully");
            successAlert.setContentText("The XML file was loaded without any issues.");
            successAlert.showAndWait();

            // update execution tab:
            ExecutionTabController executionController = ExecutionTabController.getInstance();
            if (executionController != null) {
                executionController.updateOnXMLLoad();
            }


            isFileSelected.set(true);
            SharedResources.getInstance().setIsFileSelected(true);
            updateThreadInfo();

        } catch (Exception e) {
            System.out.println("Execption " + e.getMessage());
            e.printStackTrace();
            // Fail Alert:
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File Error");
            alert.setHeaderText("An error occurred while loading the file.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateThreadInfo() {
        ThreadInfoDTO threadInfo = simulationInterface.getThreadInfo();
        activeThreadCount.set(threadInfo.getActiveThreads() + "/" + threadInfo.getTotalThreads());
        simulationsEndedCount.set(String.valueOf(threadInfo.getTotalSimulations()));
    }

    public void switchToResultsTab() {
        mainTabPane.getSelectionModel().select(resultsTabTitle); // use the ID of the Results tab
    }

}
