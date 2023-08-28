package components.results;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;

public class ResultsTabController {

    @FXML
    private ChoiceBox<?> entityChooser;

    @FXML
    private ListView<?> pastSimulationList;

    @FXML
    private Button pauseButton;

    @FXML
    private LineChart<?, ?> populationLineChart;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressPrecentLabel;

    @FXML
    private ChoiceBox<?> propertyChooser;

    @FXML
    private BarChart<?, ?> propertyHistogramBarChart;

    @FXML
    private Button resumeButton;

    @FXML
    private Label secondsCounterLabel;

    @FXML
    private Button stopButton;

    @FXML
    private Label ticksCounterLabel;

}
