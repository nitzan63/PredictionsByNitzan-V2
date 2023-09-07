package components.details;

import api.DTOUIInterface;
import components.SharedResources;
import dto.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DetailsTabController {
    @FXML
    private FlowPane detailsFlowPane;
    @FXML
    private TreeView<String> factorsTreeView;
    @FXML
    private Label chosenFactorLabel;

    //
    private DTOUIInterface simulationInterface;

    public enum SimulationFactor {
        ENVIRONMENT, RULES, ENTITIES, TERMINATION, GRID
    }

    public void initialize() {
        this.simulationInterface = SharedResources.getInstance().getDTOUIInterface();
        initializeFactorsTreeView();
        addTreeSelectionListener();
    }

    private void initializeFactorsTreeView(){
        // Initialize TreeView
        TreeItem<String> rootItem = new TreeItem<>("Simulation Factors");
        rootItem.setExpanded(true);

        // For Environment Properties
        TreeItem<String> envPropertiesItem = new TreeItem<>("Environment");
        Map<String, PropertyDTO> envProperties = simulationInterface.getEnvironmentProperties().getEnvironmentProperties();
        for (String propName : envProperties.keySet()) {
            envPropertiesItem.getChildren().add(new TreeItem<>(propName));
        }
        rootItem.getChildren().add(envPropertiesItem);

        // For Rules
        TreeItem<String> rulesItem = new TreeItem<>("Rules");
        List<RuleDTO> rules = simulationInterface.getRules();
        for (RuleDTO rule : rules) {
            TreeItem<String> ruleItem = new TreeItem<>(rule.getName());
            TreeItem<String> actionsItem = new TreeItem<>("Actions");
            for (String actionName : rule.getActionNames()) {
                actionsItem.getChildren().add(new TreeItem<>(actionName));
            }
            ruleItem.getChildren().add(actionsItem);
            ruleItem.getChildren().add(new TreeItem<>("Activation"));
            rulesItem.getChildren().add(ruleItem);
        }
        rootItem.getChildren().add(rulesItem);

        // For Entities
        TreeItem<String> entitiesItem = new TreeItem<>("Entities");
        Map<String, EntitiesDefinitionDTO> entities = simulationInterface.getEntitiesDefinition();
        for (String entityName : entities.keySet()) {
            TreeItem<String> entityItem = new TreeItem<>(entityName);
            EntitiesDefinitionDTO entity = entities.get(entityName);
            for (PropertyDTO prop : entity.getProperties()) {
                entityItem.getChildren().add(new TreeItem<>(prop.getName()));
            }
            entitiesItem.getChildren().add(entityItem);
        }
        rootItem.getChildren().add(entitiesItem);

        // For Grid and Termination
        rootItem.getChildren().add(new TreeItem<>("Grid"));
        rootItem.getChildren().add(new TreeItem<>("Termination"));

        factorsTreeView.setRoot(rootItem);
    }

    private void addTreeSelectionListener(){
        // Listen for changes in selection and update the display accordingly
        factorsTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                showDetails(newValue.getValue());
            }
        });
    }

    public void showDetails(String factor) {
        detailsFlowPane.getChildren().clear();
        if (factor.startsWith("Environment")) {
            displayEnvironmentProperties();
        } else if (factor.startsWith("Rules")) {
            displayRules();
        } else if (factor.startsWith("Entities")) {
            // The factor string could contain the name of the specific entity
            // e.g., "Entities: Human"
            String[] parts = factor.split(": ");
            if (parts.length > 1) {
                displaySpecificEntity(parts[1]);
            } else {
                displayEntities();
            }
        } else if (factor.startsWith("Termination")) {
            displayTerminationConditions();
        } else if (factor.startsWith("Grid")) {
            displayGrid();
        }
    }

    private void displaySpecificEntity(String entityName) {
        detailsFlowPane.getChildren().clear();
        Map<String, EntitiesDefinitionDTO> entities = simulationInterface.getEntitiesDefinition();
        EntitiesDefinitionDTO specificEntity = entities.get(entityName);

        if (specificEntity == null) {
            return;
        }

        for (PropertyDTO property : specificEntity.getProperties()) {
            addPropertyDetailToFlowPane(property);
        }
    }


    private void displayRules(){
        detailsFlowPane.getChildren().clear();
        List<RuleDTO> rules = simulationInterface.getRules();
        for (RuleDTO rule : rules){
            addRuleToFlowPane(rule);
        }
    }

    private void displayGrid(){
        detailsFlowPane.getChildren().clear();
        GridDTO gridDTO = simulationInterface.getGridDTO();
        addGridToFlowPane(gridDTO);
    }

    private void addGridToFlowPane(GridDTO gridDTO){
        // Initialize the controller and FXML loader here
        Pair<SingleDetailController, Parent> loadedComponent = loadFXMLComponent("SingleDetailComponent.fxml");
        if (loadedComponent == null) {
            return;
        }

        SingleDetailController singleDetailController = loadedComponent.getKey();
        Parent root = loadedComponent.getValue();

        // Populate the component's fields
        StringBuilder gridDetails = new StringBuilder();
        gridDetails.append("Grid Rows: ").append(gridDTO.getRows()).append("\n");
        gridDetails.append("Grid Cols: ").append(gridDTO.getCols());

        singleDetailController.getDataLabel().setText(gridDetails.toString());

        // Add the pane to the FlowPane
        detailsFlowPane.getChildren().add(root);
    }


    private void addRuleToFlowPane(RuleDTO rule) {
        // Initialize the controller and FXML loader here
        Pair<SingleDetailController, Parent> loadedComponent = loadFXMLComponent("SingleDetailComponent.fxml");
        if (loadedComponent == null) {
            return;
        }

        SingleDetailController singleDetailController = loadedComponent.getKey();
        Parent root = loadedComponent.getValue();


        // Populate the component's fields
        StringBuilder ruleDetails = new StringBuilder();
        ruleDetails.append("Rule Name: ").append(rule.getName()).append("\n");
        ruleDetails.append("Activation Ticks: ").append(rule.getActivationTicks());
        ruleDetails.append(" | Activation Probability: ").append(rule.getActivationProbability());
        ruleDetails.append("\nNumber of Actions: ").append(rule.getNumberOfActions());

        if (rule.getActionNames() != null && !rule.getActionNames().isEmpty()) {
            ruleDetails.append("\nActions: ").append(String.join(", ", rule.getActionNames()));
        }

        singleDetailController.getDataLabel().setText(ruleDetails.toString());

        // Add the pane to the FlowPane
        detailsFlowPane.getChildren().add(root);
    }


    public void displayTerminationConditions(){
        detailsFlowPane.getChildren().clear();
        TerminationDTO termination = simulationInterface.getTermination();
        addTerminationConditionToFlowPane(termination);
    }

    public void addTerminationConditionToFlowPane(TerminationDTO termination){
        // Initialize the controller and FXML loader here
        Pair<SingleDetailController, Parent> loadedComponent = loadFXMLComponent("SingleDetailComponent.fxml");
        if (loadedComponent == null) {
            return;
        }

        SingleDetailController singleDetailController = loadedComponent.getKey();
        Parent root = loadedComponent.getValue();


        StringBuilder terminationDetails = new StringBuilder();

        if (termination.getMaxTicks() != null && termination.getMaxTicks() > 0) {
            terminationDetails.append("Termination By Ticks: ").append(termination.getMaxTicks()).append(" Ticks\n");
        }

        if (termination.getMaxTime() != null && termination.getMaxTime() > 0) {
            terminationDetails.append("Termination By Time: ").append(termination.getMaxTime()).append(" Seconds\n");
        }

        // No termination conditions
        if (terminationDetails.length() == 0) {
            terminationDetails.append("No termination conditions specified.");
        }

        singleDetailController.getDataLabel().setText(terminationDetails.toString());

        // Add the pane to the FlowPane
        detailsFlowPane.getChildren().add(root);
    }

    private void displayEnvironmentProperties() {
        detailsFlowPane.getChildren().clear();
        EnvironmentDTO environmentDTO = simulationInterface.getEnvironmentProperties();
        Map<String, PropertyDTO> properties = environmentDTO.getEnvironmentProperties();

        for (PropertyDTO property : properties.values()){
            addPropertyDetailToFlowPane(property);
        }
    }

    private void displayEntities() {
        detailsFlowPane.getChildren().clear();
        Map<String, EntitiesDefinitionDTO> entities = simulationInterface.getEntitiesDefinition();

        for (EntitiesDefinitionDTO entity : entities.values()) {
            for (PropertyDTO property : entity.getProperties()) {
                addPropertyDetailToFlowPane(property);
            }
        }
    }

    private void addPropertyDetailToFlowPane(PropertyDTO property) {
        // Initialize the controller and FXML loader here
        Pair<SingleDetailController, Parent> loadedComponent = loadFXMLComponent("SingleDetailComponent.fxml");
        if (loadedComponent == null) {
            return;
        }

        SingleDetailController singleDetailController = loadedComponent.getKey();
        Parent root = loadedComponent.getValue();


        // Build the custom string
        StringBuilder sb = new StringBuilder();
        sb.append("Property Name: ").append(property.getName());
        sb.append(" | Type: ").append(property.getType()).append("\n");

        if (property.getType().equalsIgnoreCase("decimal") || property.getType().equalsIgnoreCase("float")) {
            sb.append("Range: ").append(property.getRangeFrom()).append(" -> ").append(property.getRangeTo()).append("\n");
        }

        sb.append("Random Initialized: ").append(property.isRandomlyInitialized() ? "Yes" : "No");

        // Populate the label
        singleDetailController.getDataLabel().setText(sb.toString());

        // Add the pane to the FlowPane
        detailsFlowPane.getChildren().add(root);
    }

    private <T> Pair<T, Parent> loadFXMLComponent(String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            return null;
        }
        return new Pair<>(loader.getController(), root);
    }
}
