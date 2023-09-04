package world.entities.entity.properties.property.api;

import world.utils.range.Range;

public interface EntityProperty {

    String getName();
    String getType();
    Range<?> getRange();
    boolean isRandomInitialize();
    Object getValue();
    void setRange(Number a, Number b);
    void setName(String newName);
    void setValue(Object newValue, int currentTick);
    int getLastChangedTick();

    public String toString();
}
