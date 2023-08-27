package components.details;

import api.DTOEngineInterface;
import api.DTOUIInterface;
import components.SharedResources;
import dto.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DetailsTabController {
    @FXML
    private FlowPane detailsFlowPane;
    @FXML
    private ListView<String> factorsListView;
    @FXML
    private Label chosenFactorLabel;

    //
    private DTOUIInterface simulationInterface;
    private String[] factors = {"Environment Properties", "Rules", "Entity", "Termination"};

    public DetailsTabController() {

    }

    public void initialize() {
        this.simulationInterface = SharedResources.getInstance().getDTOUIInterface();
        factorsListView.getItems().addAll(factors);
        factorsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> {
            showDetails(newValue);
            System.out.println(newValue);
        });

    }

    public void showDetails(String factor) {
        if ("Environment Properties".equals(factor)) {
            displayEnvironmentProperties();
        } else if ("Rules".equals(factor)) {
            displayRules();
        } else if ("Entity".equals(factor)) {
            displayEntities();
        } else if ("Termination".equals(factor)) {
            displayTerminationConditions();
        }
    }

    public void displayRules(){
        detailsFlowPane.getChildren().clear();
        List<RuleDTO> rules = simulationInterface.getRules();
        for (RuleDTO rule : rules){
            addRuleToFlowPane(rule);
        }
    }

    private void addRuleToFlowPane(RuleDTO rule) {
        // Initialize the controller and FXML loader here
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SingleDetailComponent.fxml"));
        try {
            loader.load(); // Load the component
        } catch (IOException e) {
            e.printStackTrace();
        }

        SingleDetailController singleDetailController = loader.getController();

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
        detailsFlowPane.getChildren().add(loader.getRoot());
    }


    public void displayTerminationConditions(){
        detailsFlowPane.getChildren().clear();
        TerminationDTO termination = simulationInterface.getTermination();
        addTerminationConditionToFlowPane(termination);
    }

    public void addTerminationConditionToFlowPane(TerminationDTO termination){
        // Initialize the controller and FXML loader here
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SingleDetailComponent.fxml"));
        try {
            loader.load(); // Load the component
        } catch (IOException e) {
            e.printStackTrace();
        }

        SingleDetailController singleDetailController = loader.getController();
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
        detailsFlowPane.getChildren().add(loader.getRoot());
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
        EntitiesDefinitionDTO entity = simulationInterface.getEntitiesDefinition();

        for (PropertyDTO property : entity.getProperties()) {
            addPropertyDetailToFlowPane(property);
        }
    }

    private void addPropertyDetailToFlowPane(PropertyDTO property) {
        // Initialize the controller and FXML loader here
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SingleDetailComponent.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SingleDetailController singleDetailController = loader.getController();

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
        detailsFlowPane.getChildren().add(loader.getRoot());
    }

    public SingleDetailController loadSingleDetailComponent() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SingleDetailComponent.fxml"));
        try {
            loader.load(); // Load the component
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return loader.getController();
    }

}
