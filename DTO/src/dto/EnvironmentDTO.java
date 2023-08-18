package dto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnvironmentDTO {
    private Map<String, PropertyDTO> environmentProperties;

    public EnvironmentDTO() {
        this.environmentProperties = new LinkedHashMap<>();
    }

    public Map<String, PropertyDTO> getEnvironmentProperties() {
        return environmentProperties;
    }

    public void addEnvironmentProperty(String propertyName, PropertyDTO property) {
        this.environmentProperties.put(propertyName, property);
    }
}
