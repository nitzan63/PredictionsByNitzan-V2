package world.property.impl;

import world.entities.entity.properties.property.api.EntityProperty;
import world.environment.properties.property.api.EnvProperty;
import world.utils.string.generator.RandomStringGenerator;
import world.utils.range.Range;

import java.util.Objects;

public class PropertyString implements EntityProperty, EnvProperty {

    private String name;
    private String value;
    private Boolean isRandom;


    public PropertyString(String name, Boolean isRandom, String value) {
        this.name = name;
        this.isRandom = isRandom;
        if (isRandom) {
            RandomStringGenerator random = new RandomStringGenerator();
            this.value = random.generateRandomString();
        } else this.value = value;
    }

    public PropertyString(String name) {
        this.name = name;
        this.isRandom = true;
        RandomStringGenerator r = new RandomStringGenerator();
        this.value = r.generateRandomString();
    }

    public PropertyString(String name, String init) {
        this.name = name;
        this.isRandom = false;
        this.value = init;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return String.class;
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
    public String getValue() {
        return value;
    }

    @Override
    public void setRange(Number a, Number b) {
        return;
    }

    @Override
    public void setName(String newName) {
        this.name = name;
    }

    @Override
    public void setValue(Object newValue) {
        this.value = (String) newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyString that = (PropertyString) o;
        return Objects.equals(name, that.name) && Objects.equals(value, that.value) && Objects.equals(isRandom, that.isRandom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, isRandom);
    }

    @Override
    public String toString() {
        return "PropertyString{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", isRandom=" + isRandom +
                '}';
    }
}
