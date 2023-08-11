package world.environment;

import world.environment.properties.EnvProperties;
import world.environment.properties.property.api.EnvProperty;

import java.util.List;

public class Environment {
    private static EnvProperties properties;

    public void setProperties(EnvProperties properties) {
        Environment.properties = properties;
    }

    public static Double getEnvironmentPropValue(String propertyName){
        return (Double) properties.getProperty(propertyName).getValue();
    }

    @Override
    public String toString() {
        return "Environment{" +
                "properties=" + properties +
                '}';
    }
}
