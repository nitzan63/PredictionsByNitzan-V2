package components.details;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;

public class DetailsTabController {
    @FXML
    private FlowPane detailsFlowPane;

    @FXML
    private ListView<String> factorsListView;

    @FXML
    private Button showDetailsButton;

    @FXML
    private Label chosenFactorLabel;

    private String[] factors = {"Environment Properties","Rules", "Entity" , "Termination"};
    public DetailsTabController (){}

    public void initialize (){
        factorsListView.getItems().addAll(factors);
    }

}
