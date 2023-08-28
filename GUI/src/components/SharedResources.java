package components;

import api.DTOUIInterface;
import javafx.beans.property.SimpleBooleanProperty;

public class SharedResources {
    private static final SharedResources instance = new SharedResources();

    private DTOUIInterface simulationInterface;
    private SimpleBooleanProperty isFileSelected = new SimpleBooleanProperty(false);


    private SharedResources() {
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
}
