package world.environment;

import world.environment.properties.EnvProperties;
import world.environment.properties.property.api.EnvProperty;

import java.util.List;

public class Environment {
    private EnvProperties properties;

    public void setProperties(EnvProperties properties) {
        this.properties = properties;
    }

    public void randomEnvProperties(){
        properties.generateRandomEnvPropertiesValues();
    }
    public EnvProperties getProperties() {
        return properties;
    }

    public Double getEnvironmentPropValue(String propertyName){
        Object value = properties.getProperty(propertyName).getValue();
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new ClassCastException("The property value is neither an Integer nor a Double.");
        }
    }

    @Override
    public String toString() {
        return "Environment{" +
                "properties=" + properties +
                '}';
    }
}
