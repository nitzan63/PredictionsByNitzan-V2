package world.entities.entity.properties.property.api;

import world.utils.range.Range;

public interface EntityProperty {

    String getName();
    Class<?> getType();
    Range<?> getRange();
    boolean isRandomInitialize();
    Object getValue();
    void setRange(Number a, Number b);
    void setName(String newName);
    void setValue(Object newValue);

    public String toString();
}
