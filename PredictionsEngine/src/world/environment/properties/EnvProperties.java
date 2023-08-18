package world.environment.properties;

import world.environment.properties.property.api.EnvProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnvProperties {
    List<EnvProperty> properties;

    public EnvProperties() {
        properties = new ArrayList<>();
    }

    public void addProperty(EnvProperty property) {
        properties.add(property);
    }

    public List<EnvProperty> getProperties() {
        return properties;
    }

    public EnvProperty getProperty(String name) {
        for (EnvProperty property : properties)
            if (property.getName().equals(name))
                return property;
        return null;
    }

    public void generateRandomEnvPropertiesValues (){
        Random random = new Random();
        for (EnvProperty property : properties){
            if (property.getType().equals("decimal")){
                int from = property.getRange().getFromDouble().intValue();
                int to = property.getRange().getToDouble().intValue();
                int randomIntValue = from + random.nextInt(to - from + 1);
                property.setValue(randomIntValue);
            } else if (property.getType().equals("float")){
                float from = property.getRange().getFromDouble().floatValue();
                float to = property.getRange().getToDouble().floatValue();
                float randomFloatValue = from + random.nextFloat() * (to - from);
                property.setValue(randomFloatValue);
            } else if (property.getType().equals("boolean")){
                boolean randomBoolean = random.nextBoolean();
                property.setValue(randomBoolean);
            }
        }
    }
    @Override
    public String toString() {
        return "EnvProperties{" +
                "properties=" + properties +
                '}';
    }
}
