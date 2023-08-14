package world.property.impl;

import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.properties.property.api.EnvProperty;
import world.utils.range.Range;

import java.util.Objects;
import java.util.Random;

public class PropertyDecimal implements EntityProperty, EnvProperty {
    private String name;
    private boolean isRandom;
    private Integer value;
    private Range<Integer> range;

    // ctor that gets all the data members, checks if random, and assigning accordingly the data members
    public PropertyDecimal(String name, Boolean isRandom, Integer init, Range<Integer> range) {
        this.name = name;
        this.range = range;
        if (isRandom) {
            Random random = new Random();
            this.value = random.nextInt((range.getTo() - range.getFrom() + 1) + range.getFrom());
        } else this.value = init;
    }

    // ctor that gets name, init and range, and sets the data members.
    public PropertyDecimal(String name, Integer init, Range<Integer> range) {
        this.name = name;
        this.range = range;
        this.isRandom = false;
        this.value = init;
    }


    // ctor that gets name and range, and generates the data members.
    public PropertyDecimal(String name, Range<Integer> range) {
        this.isRandom = true;
        this.name = name;
        this.range = range;
        Random random = new Random();
        this.value = random.nextInt((range.getTo() - range.getFrom() + 1) + range.getFrom());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "decimal";
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
    public Integer getValue() {
        return value;
    }

    @Override
    public void setRange(Number from, Number to) {
        range.setRange((Integer) from, (Integer) to);
    }

    @Override
    public void setName(String newName) {
        this.name = newName;
    }

    @Override
    public void setValue(Object newValue) {
        if (newValue instanceof Double){
            value = ((Double) newValue).intValue();
        }else
        value = (Integer) newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyDecimal that = (PropertyDecimal) o;
        return isRandom == that.isRandom && Objects.equals(name, that.name) && Objects.equals(value, that.value) && Objects.equals(range, that.range);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isRandom, value, range);
    }

    @Override
    public String toString() {
        return "PropertyDecimal{" +
                "name='" + name + '\'' +
                ", isRandom=" + isRandom +
                ", value=" + value +
                ", range=" + range +
                '}';
    }
}
