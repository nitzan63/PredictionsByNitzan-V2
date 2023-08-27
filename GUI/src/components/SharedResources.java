package components;

import api.DTOUIInterface;

public class SharedResources {
    private static final SharedResources instance = new SharedResources();

    private DTOUIInterface simulationInterface;

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
}
