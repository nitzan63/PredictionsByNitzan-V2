package components;

import api.DTOUIInterface;
import components.main.MainController;
import dto.UserInputDTO;
import javafx.beans.property.SimpleBooleanProperty;

public class SharedResources {
    private static final SharedResources instance = new SharedResources();

    private DTOUIInterface simulationInterface;
    private SimpleBooleanProperty isFileSelected = new SimpleBooleanProperty(false);
    private MainController mainController;
    private UserInputDTO lastSelectedSimulation;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return mainController;
    }

    public static SharedResources getInstance() {
        return instance;
    }

    public void setDTOUIInterface(DTOUIInterface simulationInterface) {
        this.simulationInterface = simulationInterface;
    }

    public DTOUIInterface getDTOUIInterface() {
        return simulationInterface;
    }

    public SimpleBooleanProperty getIsFileSelected() {
        return isFileSelected;
    }

    public void setIsFileSelected(boolean value) {
        isFileSelected.set(value);
    }

    public void setLastSelectedSimulation(UserInputDTO userInputDTO) {
        this.lastSelectedSimulation = userInputDTO;
    }

    public UserInputDTO getLastSelectedSimulation() {
        return lastSelectedSimulation;
    }

}
