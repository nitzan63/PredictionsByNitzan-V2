package world.property.impl;

import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.properties.property.api.EnvProperty;
import world.utils.range.Range;

import java.util.Objects;
import java.util.Random;

public class PropertyBool implements EntityProperty, EnvProperty {


    private String name;
    private boolean isRandom;
    private Boolean value;


    public PropertyBool(String name, Boolean isRandom, Boolean init) {
        this.name = name;
        this.isRandom = isRandom;
        if (isRandom) {
            Random random = new Random();
            this.value = random.nextBoolean();
        } else this.value = init;
    }

    public PropertyBool(String name) {
        this.name = name;
        this.isRandom = true;
        Random random = new Random();
        this.value = random.nextBoolean();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return boolean.class;
    }

    @Override
    public Range<?> getRange() {
        return null;
    }

    @Override
    public boolean isRandomInitialize() {
        return isRandom;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setRange(Number a, Number b) {
        return;
    }

    @Override
    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    public void setValue(Object newValue) {
        this.value = (boolean) newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyBool that = (PropertyBool) o;
        return isRandom == that.isRandom && Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isRandom, value);
    }

    @Override
    public String toString() {
        return "PropertyBool{" +
                "name='" + name + '\'' +
                ", isRandom=" + isRandom +
                ", value=" + value +
                '}';
    }
}

