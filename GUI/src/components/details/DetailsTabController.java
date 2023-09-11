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

    public void initialize() {
        this.simulationInterface = SharedResources.getInstance().getDTOUIInterface();
        // Listen to changes on isFileSelected property
        SharedResources.getInstance().getIsFileSelected().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Only run these methods if a file has been selected.
                initializeFactorsTreeView();
                addTreeSelectionListener();
            }
        });

        // If isFileSelected is already true (e.g. during a re-initialization)
        if (SharedResources.getInstance().getIsFileSelected().get()) {
            initializeFactorsTreeView();
            addTreeSelectionListener();
        }
    }

    private void initializeFactorsTreeView() {
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

    private void addTreeSelectionListener() {
        factorsTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                showDetails(newValue);
            }
        });
    }

    public void showDetails(TreeItem<String> selectedNode) {
        detailsFlowPane.getChildren().clear();
        String factor = selectedNode.getValue();
        TreeItem<String> parent = selectedNode.getParent();
        TreeItem<String> grandParent = parent != null ? parent.getParent() : null;

        if (parent == null) {
            return;
        }

        String parentValue = parent.getValue();

        if (parentValue.equals("Environment")) {
            displayEnvironmentProperty(factor);
        } else if (parentValue.equals("Actions")) {
            if (grandParent != null) {
                displayActionDetail(grandParent.getValue(), parentValue, factor);
            }
        } else if (factor.equals("Activation")) {
            displayActivationDetail(parentValue);
        } else if (grandParent != null && grandParent.getValue().equals("Entities")) {
            displayEntityProperty(parent.getValue(), factor);
        } else if (factor.equals("Grid")) {
            System.out.println("showing grid");
            displayGrid();
        } else if (factor.equals("Termination")) {
            System.out.println("Showing termination");
            displayTerminationConditions();
        }
    }


    private void displayEnvironmentProperty(String propertyName) {
        PropertyDTO property = simulationInterface.getEnvironmentProperties().getEnvironmentProperties().get(propertyName);
        if (property != null) {
            addPropertyDetailToFlowPane(property);
        }
    }

    private void displayActionDetail(String ruleName, String type, String factor) {
        // Fetch the rules from the interface
        List<RuleDTO> rules = simulationInterface.getRules();

        for (RuleDTO ruleDTO : rules) {
            if (ruleDTO.getName().equals(ruleName)) {
                if ("Actions".equals(type)) {
                    addActionToFlowPane(ruleDTO);
                } else if ("Activation".equals(type)) {
                    addActivationToFlowPane(ruleDTO);
                }
            }
        }
    }

    private void displayActivationDetail(String ruleName) {
        List<RuleDTO> rules = simulationInterface.getRules();
        for (RuleDTO ruleDTO : rules) {
            if (ruleDTO.getName().equals(ruleName)) addActivationToFlowPane(ruleDTO);
        }
    }

    private void displayEntityProperty(String entityName, String propertyName) {
        // logic to find the PropertyDTO object based on propertyName and display its details
        List<PropertyDTO> properties = simulationInterface.getEntitiesDefinition().get(entityName).getProperties();
        for (PropertyDTO property : properties) {
            if (property.getName().equalsIgnoreCase(propertyName)) {
                addPropertyDetailToFlowPane(property);
            }
        }
    }

    private void displayGrid() {
        detailsFlowPane.getChildren().clear();
        GridDTO gridDTO = simulationInterface.getGridDTO();
        addGridToFlowPane(gridDTO);
    }

    private void addGridToFlowPane(GridDTO gridDTO) {
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


    private void addActivationToFlowPane(RuleDTO ruleDTO) {
        // Initialize the controller and FXML loader here
        Pair<SingleDetailController, Parent> loadedComponent = loadFXMLComponent("SingleDetailComponent.fxml");
        if (loadedComponent == null) {
            return;
        }

        SingleDetailController singleDetailController = loadedComponent.getKey();
        Parent root = loadedComponent.getValue();
        String activationDetails = "Activation Ticks: " + ruleDTO.getActivationTicks() + " | Activation Probability: " + ruleDTO.getActivationProbability();

        singleDetailController.getDataLabel().setText(activationDetails);

        detailsFlowPane.getChildren().add(root);
    }

    private void addActionToFlowPane(RuleDTO ruleDTO) {
        for (ActionDTO actionDTO : ruleDTO.getActionsList()) {
            // Initialize the controller and FXML loader here
            Pair<SingleDetailController, Parent> loadedComponent = loadFXMLComponent("SingleDetailComponent.fxml");

            // If the loaded component is null, continue to the next iteration
            if (loadedComponent == null) {
                continue;
            }

            SingleDetailController singleDetailController = loadedComponent.getKey();
            Parent root = loadedComponent.getValue();

            // Build the details string using the ActionDTO fields
            StringBuilder sb = new StringBuilder();
            sb.append("Type: ").append(actionDTO.getType()).append("\n");
            sb.append("Entity: ").append(actionDTO.getMainEntity()).append("\n");

            // Loop through the additional details map and add them to the string
            for (Map.Entry<String, String> entry : actionDTO.getAdditionalDetails().entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }

            singleDetailController.getDataLabel().setText(sb.toString());

            detailsFlowPane.getChildren().add(root);
        }
    }

    public void displayTerminationConditions() {
        detailsFlowPane.getChildren().clear();
        TerminationDTO termination = simulationInterface.getTermination();
        addTerminationConditionToFlowPane(termination);
    }

    public void addTerminationConditionToFlowPane(TerminationDTO termination) {
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
            terminationDetails.append("Termination By User.");
        }

        singleDetailController.getDataLabel().setText(terminationDetails.toString());

        // Add the pane to the FlowPane
        detailsFlowPane.getChildren().add(root);
    }

    private void addPropertyDetailToFlowPane(PropertyDTO property) {
        if (property == null) {
            System.err.println("Property is null!");
            return;
        }

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
        sb.append(" | Type: ").append(property.getType() != null ? property.getType() : "Unknown").append("\n");

        String type = property.getType();
        if (type != null && (type.equalsIgnoreCase("decimal") || type.equalsIgnoreCase("float"))) {
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
