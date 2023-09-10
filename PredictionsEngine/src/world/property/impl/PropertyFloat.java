package world.property.impl;

import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.properties.property.api.EnvProperty;
import world.utils.range.Range;

import java.util.Objects;
import java.util.Random;

public class PropertyFloat implements EntityProperty, EnvProperty {
    private String name;
    private Float value;
    private Boolean isRandom;
    private Range<Float> range;
    private int lastChangedTick;

    // ctor that gets all the data members, checks if random, and assigning accordingly the data members
    public PropertyFloat(String name, Boolean isRandom, Float init, Range<Float> range) {
        this.name = name;
        this.range = range;
        if (isRandom) {
            Random random = new Random();
            this.value = range.getFrom() + random.nextFloat() * (range.getTo() - range.getFrom());
        } else this.value = init;
        this.lastChangedTick = 0;
    }

    // ctor that gets name, init and range, and sets the data members.
    public PropertyFloat(String name, Float init, Range<Float> range) {
        this.name = name;
        this.range = range;
        this.isRandom = false;
        this.value = init;
    }

    // ctor that gets name and range, and generates the data members.
    public PropertyFloat(String name, Range<Float> range) {
        this.isRandom = true;
        this.name = name;
        this.range = range;
        Random random = new Random();
        this.value = range.getFrom() + random.nextFloat() * (range.getTo() - range.getFrom());
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "float";
    }

    @Override
    public Range getRange() {
        return range;
    }

    @Override
    public boolean isRandomInitialize() {
        return isRandom;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setRange(Number from, Number to) {
        range.setRange((Float) from, (Float) to);
    }

    @Override
    public void setName(String newName) {
        this.name = name;
    }

    @Override
    public void setValue(Object newValue, int currentTick) {
        Float newFloatValue = (Float) newValue;

        if (!this.value.equals(newFloatValue)) {
            this.value = newFloatValue;
            this.lastChangedTick = currentTick;
        }
    }

    @Override
    public int getLastChangedTick() {
        return lastChangedTick;
    }

    @Override
    public void setValue(Object newValue) {
        this.value = (Float) newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyFloat that = (PropertyFloat) o;
        return Objects.equals(name, that.name) && Objects.equals(value, that.value) && Objects.equals(isRandom, that.isRandom) && Objects.equals(range, that.range);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, isRandom, range);
    }

    @Override
    public String toString() {
        return "PropertyFloat{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", isRandom=" + isRandom +
                ", range=" + range +
                '}';
    }
}
