package components.execution;

import api.DTOUIInterface;
import components.SharedResources;
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
    private Map<String, String> userInputProperties = new HashMap<>();

    public static ExecutionTabController getInstance() {
        return instance;
    }


    public void initialize() {
        instance = this;
        this.simulationInterface = SharedResources.getInstance().getDTOUIInterface();

        // initialize entities table:
        initializeEntitiesTable();

        // initialize environment table:
        initializeEnvironmentTable();

    }

    private void initializeEntitiesTable() {
        entityNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        populationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        populationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
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
                            userInputProperties.put(property.getName(), newValue);
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
        List<EntitiesDefinitionDTO> entities = new ArrayList<>();
        entities.add(simulationInterface.getEntitiesDefinition());
        entitiesTable.getItems().clear();
        entitiesTable.getItems().addAll(entities);
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
        if (!userInputProperties.isEmpty()) {
            UserEnvironmentInputDTO inputDTO = new UserEnvironmentInputDTO(userInputProperties);
            simulationInterface.setEnvironmentProperties(inputDTO);
        }

        SimulationRunMetadataDTO runMetadataDTO = simulationInterface.runSimulation();
        successfulRunMessage(runMetadataDTO);

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
        userInputProperties.clear();

        // Reset table cells to their default values
        for (PropertyDTO property : envPropertiesTable.getItems()) {
            property.setValue("");
            userInputProperties.remove(property.getName());
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
