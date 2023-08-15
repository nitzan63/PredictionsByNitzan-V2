package dto;

import java.util.HashMap;
import java.util.Map;

public class PropertyHistogramDTO {
    private String propertyName;
    private Map<String, Integer> histogram;

    public PropertyHistogramDTO(String propertyName) {
        this.propertyName = propertyName;
        this.histogram = new HashMap<>();
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Map<String, Integer> getHistogram() {
        return histogram;
    }

    public void setHistogram(Map<String, Integer> histogram) {
        this.histogram = histogram;
    }

    public void addValue(String value) {
        histogram.put(value, histogram.getOrDefault(value, 0) + 1);
    }

    @Override
    public String toString() {
        return "PropertyHistogramDTO{" +
                "propertyName='" + propertyName + '\'' +
                ", histogram=" + histogram +
                '}';
    }
}
