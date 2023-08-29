package components;

import components.main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class SimulatorGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Load Main FXML:
        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/components/main/PredictionsGUI.fxml");
        loader.setLocation(mainFXML);
        Parent root = loader.load();

        // Wire Up Controller:
        MainController mainController = loader.getController();
        mainController.setPrimaryStage(primaryStage);

        // Set Stage:
        primaryStage.setTitle("Predictions By Nitzan");
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

