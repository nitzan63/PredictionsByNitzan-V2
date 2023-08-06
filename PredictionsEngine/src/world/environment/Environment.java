package world.environment;

import world.environment.properties.EnvProperties;
import world.environment.properties.property.api.EnvProperty;

import java.util.List;

public class Environment {
    private EnvProperties properties;

    public void setProperties(EnvProperties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Environment{" +
                "properties=" + properties +
                '}';
    }
}
