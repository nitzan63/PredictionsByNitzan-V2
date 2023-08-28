package components.execution;

import api.DTOUIInterface;
import components.SharedResources;
import dto.EntitiesDefinitionDTO;
import dto.EnvironmentDTO;
import dto.PropertyDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
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
    private TableColumn<EnvironmentDTO, String> envPropertyName;
    @FXML
    private TableColumn<EnvironmentDTO, String> envPropertyRangeFrom;
    @FXML
    private TableColumn<EnvironmentDTO, String> envPropertyType;
    @FXML
    private TableColumn<EnvironmentDTO, String> envPropertyValue;
    @FXML
    private TableColumn<EnvironmentDTO, String> envPropertyRangeTo;



    private DTOUIInterface simulationInterface;
    private static ExecutionTabController instance;
    public static ExecutionTabController getInstance(){return instance;}



    public void initialize() {
        instance = this;
        this.simulationInterface = SharedResources.getInstance().getDTOUIInterface();

        // initialize entities table:
        entityNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        populationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));
        populationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // initialize environment table:
        envPropertyName.setCellValueFactory(new PropertyValueFactory<>("name"));
        envPropertyType.setCellValueFactory(new PropertyValueFactory<>("type"));
        envPropertyRangeFrom.setCellValueFactory(new PropertyValueFactory<>("rangeFrom"));
        envPropertyRangeTo.setCellValueFactory(new PropertyValueFactory<>("rangeTo"));
        envPropertyValue.setCellFactory(TextFieldTableCell.forTableColumn());

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


}
