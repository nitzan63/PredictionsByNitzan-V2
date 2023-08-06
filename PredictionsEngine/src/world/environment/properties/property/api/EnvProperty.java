package world.environment.properties.property.api;

import world.utils.range.Range;

public interface EnvProperty {
    String getName();

    Class<?> getType();

    Range<?> getRange();

    Object getValue();

    void setRange(Number a, Number b);

    void setName(String newName);

    void setValue(Object newValue);

    public String toString();
}
