package world.utils.expression;

import world.environment.Environment;

import java.util.Random;

public class AuxiliaryMethods {
    public static Object environmentAuxMethod(String propertyName){
        return Environment.getEnvironmentPropValue(propertyName);
    }

    public static Double randomAuxMethod (String args){
        int number = Integer.parseInt(args);
        return new Random().nextDouble()*(number);
    }
}
