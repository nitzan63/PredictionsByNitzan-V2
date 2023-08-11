package world.entities.entity;

import world.entities.entity.properties.EntityProperties;
import world.entities.entity.properties.property.api.EntityProperty;

public class EntityInstance {
    private final Integer serialNumber;
    private final EntityProperties properties;

    public EntityInstance(Integer serialNumber, EntityProperties properties){
        this.serialNumber = serialNumber;
        this.properties = properties;
    }

    public EntityProperties getProperties (){
        return properties;
    }

    public EntityProperty getProperty (String name){
        return properties.getProperty(name);
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "serialNumber=" + serialNumber +
                ", properties=" + properties +
                "}\n";
    }


}
