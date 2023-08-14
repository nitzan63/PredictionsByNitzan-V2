package world.utils.expression;

import world.environment.Environment;

import java.util.Random;

public class AuxiliaryMethods {
    public static Double environmentAuxMethod(String propertyName){
        Object value = Environment.getEnvironmentPropValue(propertyName);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else {
            throw new IllegalArgumentException(propertyName + " is not a number in the environment.");
        }
    }

    public static Double randomAuxMethod (String args){
        int number = Integer.parseInt(args);
        return new Random().nextDouble()*(number);
    }
}
