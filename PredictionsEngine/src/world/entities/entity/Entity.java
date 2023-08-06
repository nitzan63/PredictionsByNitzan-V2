package world.entities.entity;

import world.entities.entity.properties.EntityProperties;

public class Entity {
    private final Integer serialNumber;
    private final EntityProperties properties;

    public Entity (Integer serialNumber, EntityProperties properties){
        this.serialNumber = serialNumber;
        this.properties = properties;
    }

    public EntityProperties getProperties (){
        return properties;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "serialNumber=" + serialNumber +
                ", properties=" + properties +
                '}';
    }

}
