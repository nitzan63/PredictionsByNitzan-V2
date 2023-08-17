package dto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnvironmentDTO {
    private Map<String, Object> environmentProperties;

    public EnvironmentDTO() {
        this.environmentProperties = new LinkedHashMap<>();
    }

    public Map<String, Object> getEnvironmentProperties() {
        return environmentProperties;
    }

    public void addEnvironmentProperty(String propertyName, Object value) {
        this.environmentProperties.put(propertyName, value);
    }
}
