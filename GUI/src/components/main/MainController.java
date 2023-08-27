package components.main;

import api.DTOUIInterface;
import engine.SimulationEngine;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


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
    private TextField filePathTextField;
    @FXML
    private Button loadFileButton;

    // DTO Interface:
    DTOUIInterface simulationInterface;

    // Properties:
    private SimpleBooleanProperty isFileSelected;
    private SimpleStringProperty selectedFileProperty;

    // Stage:
    private Stage primaryStage;



    // Constructor:
    public MainController() {
        this.simulationInterface = new DTOUIInterface(new SimulationEngine());
        isFileSelected = new SimpleBooleanProperty(false);
        selectedFileProperty = new SimpleStringProperty();
    }

    public void initialize() {
        filePathTextField.textProperty().bind(selectedFileProperty);
        executionTabTitle.disableProperty().bind(isFileSelected.not());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void loadFileButtonListener() {
        try {
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

            isFileSelected.set(true);
        } catch (Exception e) {
            // Fail Alert:
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("File Error");
            alert.setHeaderText("An error occurred while loading the file.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}
