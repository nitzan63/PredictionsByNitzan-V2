package world.environment.properties;

import world.environment.properties.property.api.EnvProperty;

import java.util.ArrayList;
import java.util.List;

public class EnvProperties {
    List<EnvProperty> properties;

    public EnvProperties() {
        properties = new ArrayList<>();
    }

    public void addProperty(EnvProperty property) {
        properties.add(property);
    }

    public List<EnvProperty> getProperties() {
        return properties;
    }

    public EnvProperty getProperty(String name) {
        for (EnvProperty property : properties)
            if (property.getName().equals(name))
                return property;
        return null;
    }

    @Override
    public String toString() {
        return "EnvProperties{" +
                "properties=" + properties +
                '}';
    }
}
