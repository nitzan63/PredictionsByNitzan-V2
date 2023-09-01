package world.entities.entity;

import world.entities.entity.properties.EntityProperties;
import world.entities.entity.properties.property.api.EntityProperty;

public class EntityInstance {
    private final Integer serialNumber;
    private final EntityProperties properties;
    private int row;
    private int col;


    public EntityInstance(Integer serialNumber, EntityProperties properties, int row, int col){
        this.serialNumber = serialNumber;
        this.properties = properties;
        this.row = row;
        this.col = col;
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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    // Method to update position
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "serialNumber=" + serialNumber +
                ", properties=" + properties +
                "}\n";
    }


}
