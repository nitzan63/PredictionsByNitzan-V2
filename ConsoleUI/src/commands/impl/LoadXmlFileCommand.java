package commands.impl;

import api.DTOUIInterface;
import commands.api.Command;
import input.UserInputHandler;

public class LoadXmlFileCommand implements Command {
    private final DTOUIInterface simulationInterface;
    private final UserInputHandler inputHandler;

    public LoadXmlFileCommand(DTOUIInterface simulationInterface, UserInputHandler inputHandler){
        this.inputHandler =inputHandler;
        this.simulationInterface = simulationInterface;
    }

    public void execute(){
        System.out.println("Please enter full path to the XML file: ");
        String filePath = inputHandler.getUserCommand();
        try {
            simulationInterface.loadXmlFile(filePath);
            System.out.println("XML file loaded successfully!");
            simulationInterface.setWorldLoaded(true);
        } catch (Exception e){
            System.out.println("Error loading XML file: " + e.getMessage());
        }
    }
}
