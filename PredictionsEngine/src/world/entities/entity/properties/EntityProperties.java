package world.entities.entity.properties;

import world.entities.entity.properties.property.api.EntityProperty;

import java.util.ArrayList;
import java.util.List;

public class EntityProperties {
    List<EntityProperty> properties;

    public EntityProperties (){
        properties = new ArrayList<>();
    }

    public void addProperty(EntityProperty property) {
        properties.add(property);
    }

    public List<EntityProperty> getProperties() {
        return properties;
    }

    public EntityProperty getProperty (String name){
        for (EntityProperty property : properties)
            if (property.getName().equals(name))
                return property;
        return null;
    }

    @Override
    public String toString() {
        return "EntityProperties{" +
                "properties=" + properties +
                '}';
    }
}
