package components.execution;

import api.DTOUIInterface;
import components.SharedResources;
import components.main.MainController;
import dto.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionTabController {
    @FXML
    private TableColumn<EntitiesDefinitionDTO, String> entityNameColumn;
    @FXML
    private TableView<EntitiesDefinitionDTO> entitiesTable;
    @FXML
    private TableColumn<EntitiesDefinitionDTO, Integer> populationColumn;
    @FXML
    private TableView<PropertyDTO> envPropertiesTable;
    @FXML
    private TableColumn<PropertyDTO, String> envPropertyName;
    @FXML
    private TableColumn<PropertyDTO, String> envPropertyRangeFrom;
    @FXML
    private TableColumn<PropertyDTO, String> envPropertyType;
    @FXML
    private TableColumn<PropertyDTO, String> envPropertyValue;
    @FXML
    private TableColumn<PropertyDTO, String> envPropertyRangeTo;
    @FXML
    private Button startButton;
    @FXML
    private Button clearButton;

    private DTOUIInterface simulationInterface;
    private static ExecutionTabController instance;
    private Map<String, String> environmentPropertiesInput = new HashMap<>();
    private Map<String, Integer> entityPopulationInput = new HashMap<>();


    public static ExecutionTabController getInstance() {
        return instance;
    }

    private int gridSize;


    public void initialize() {
        instance = this;
        this.simulationInterface = SharedResources.getInstance().getDTOUIInterface();

        // Listen to changes on isFileSelected property
        SharedResources.getInstance().getIsFileSelected().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Only initialize these methods if a file has been selected.
                initializeEntitiesTable();
                initializeEnvironmentTable();
                setGridSize();
                // Optionally, you can also update other UI elements based on the loaded XML here.
                //updateOnXMLLoad();
            }
        });

        // Check if isFileSelected is already true at the time of this tab's initialization,
        // and if so, run the initialization methods.
        if (SharedResources.getInstance().getIsFileSelected().get()) {
            initializeEntitiesTable();
            initializeEnvironmentTable();
            setGridSize();
        }
    }

    private void setGridSize(){
        gridSize = simulationInterface.getGridDTO().getGridSize();
    }

    private void initializeEntitiesTable() {
        entityNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        populationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        populationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        populationColumn.setOnEditCommit(
                t -> {
                    EntitiesDefinitionDTO entity = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    int newPopulation = t.getNewValue();
                    entityPopulationInput.put(entity.getName(), newPopulation);
                    // Temporarily update the map to check if the new total is valid
                    entityPopulationInput.put(entity.getName(), newPopulation);

                    if (!validateTotalPopulation()) {
                        showAlert("Population Error", "Total population exceeds grid size.");
                        // Revert the value to the old one if validation fails (optional)
                        entityPopulationInput.put(entity.getName(), t.getOldValue());
                        t.getTableView().refresh(); // This should revert the UI to the old value
                    }
                }
        );
    }


    private void initializeEnvironmentTable() {
        envPropertyName.setCellValueFactory(new PropertyValueFactory<>("name"));
        envPropertyType.setCellValueFactory(new PropertyValueFactory<>("type"));
        envPropertyRangeFrom.setCellValueFactory(new PropertyValueFactory<>("rangeFrom"));
        envPropertyRangeTo.setCellValueFactory(new PropertyValueFactory<>("rangeTo"));
        envPropertyValue.setCellFactory(TextFieldTableCell.forTableColumn());
        envPropertyValue.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<PropertyDTO, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<PropertyDTO, String> t) {
                        PropertyDTO property = t.getTableView().getItems().get(
                                t.getTablePosition().getRow()
                        );
                        String newValue = t.getNewValue();

                        if (isValidNewValue(newValue, property)) {
                            environmentPropertiesInput.put(property.getName(), newValue);
                            property.setValue(newValue);
                        } else {
                            int row = t.getTablePosition().getRow();
                            property.setValue("");
                            t.getTableView().getItems().set(row, property);
                        }
                    }
                }
        );
    }

    private boolean validateTotalPopulation() {
        int totalPopulation = entityPopulationInput.values().stream().mapToInt(Integer::intValue).sum();
        return totalPopulation <= gridSize; // GRID_SIZE should be the maximum allowed total population
    }

    private boolean isValidNewValue(String newValue, PropertyDTO property) {
        String type = property.getType();

        if (type.equals("decimal") || type.equals("float")) {
            try {
                double value = Double.parseDouble(newValue);
                if (value >= property.getRangeFrom() && value <= property.getRangeTo()) {
                    return true;
                } else {
                    showAlert("Validation Error", "Property value should be in range from " + property.getRangeFrom() + " to " + property.getRangeTo());
                    return false;
                }
            } catch (NumberFormatException e) {
                showAlert("Validation Error", "Invalid number format");
                return false;
            }
        } else if (type.equals("boolean")) {
            if (newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("false")) {
                return true;
            } else {
                showAlert("Validation Error", "Property value needs to be boolean! (true or false)");
                return false;
            }
        } else return type.equals("string");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void updateOnXMLLoad() {
        populateEntitiesTable();
        populateEnvPropertiesTable();
    }

    private void populateEntitiesTable() {
        Map<String, EntitiesDefinitionDTO> entitiesMap = simulationInterface.getEntitiesDefinition();
        entitiesTable.getItems().clear();
        entitiesTable.getItems().addAll(entitiesMap.values());
    }

    private List<PropertyDTO> convertMapToList() {
        return new ArrayList<>(simulationInterface.getEnvironmentProperties().getEnvironmentProperties().values());
    }

    private void populateEnvPropertiesTable() {
        List<PropertyDTO> properties = convertMapToList();
        envPropertiesTable.getItems().clear();
        envPropertiesTable.getItems().addAll(properties);
    }

    @FXML
    private void onStartButtonClick(ActionEvent event) {
        UserInputDTO inputDTO = new UserInputDTO(environmentPropertiesInput, entityPopulationInput);
        simulationInterface.runSimulation(inputDTO);

        // switch to Results tab
        MainController mainController = SharedResources.getInstance().getMainController();
        if (mainController != null) {
            mainController.switchToResultsTab();
        }
    }

    private void successfulRunMessage(SimulationRunMetadataDTO metadata){

        String message = "Run Identifier: " + metadata.getRunIdentifier() + "\n" +
                "Run Date-Time: " + metadata.getDateTime() + "\n" +
                "Termination Reason: " + metadata.getTerminationReason() + "\n";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulation Run Successful");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onClearButtonClick(ActionEvent event) {
        // Clear the userInputProperties map
        environmentPropertiesInput.clear();
        entityPopulationInput.clear();

        // Reset table cells to their default values
        for (PropertyDTO property : envPropertiesTable.getItems()) {
            property.setValue("");
            environmentPropertiesInput.remove(property.getName());
        }

        // Reset entities population to their default values
        for (EntitiesDefinitionDTO entity : entitiesTable.getItems()) {
            entity.setPopulation(0);  // Assuming 0 is the default population
        }

        // Refresh the table to reflect changes
        envPropertiesTable.refresh();
        entitiesTable.refresh();
    }






}
