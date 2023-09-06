package dto;

import java.util.Map;

public class EntityPropertiesHistogramDTO {
    private Map<String, PropertyHistogramDTO> propertyHistograms;
    private String entityName;

    public EntityPropertiesHistogramDTO(Map<String, PropertyHistogramDTO> propertyHistograms, String entityName) {
        this.propertyHistograms = propertyHistograms;
        this.entityName = entityName;
    }

    public Map<String, PropertyHistogramDTO> getPropertyHistograms() {
        return propertyHistograms;
    }

    public void setPropertyHistograms(Map<String, PropertyHistogramDTO> propertyHistograms) {
        this.propertyHistograms = propertyHistograms;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
