package dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionDTO {
    private String type;
    private String mainEntity;
    private String secondaryEntity; // This can be null if no secondary entity exists
    private Map<String, String> additionalDetails = new HashMap<>(); // For holding type-specific information
    private int numberOfThenActions;
    private int numberOfElseActions;

    public ActionDTO(String type, String mainEntity) {
        this.type = type;
        this.mainEntity = mainEntity;
    }

    public void addAdditionalDetails (String key, String value){
        this.additionalDetails.put(key,value);
    }

    public String getAdditionalDetails(String key){
        return this.additionalDetails.get(key);
    }

    public String getType() {
        return type;
    }

    public String getMainEntity() {
        return mainEntity;
    }

    public String getSecondaryEntity() {
        return secondaryEntity;
    }

    public Map<String, String> getAdditionalDetails() {
        return additionalDetails;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMainEntity(String mainEntity) {
        this.mainEntity = mainEntity;
    }

    public void setSecondaryEntity(String secondaryEntity) {
        this.secondaryEntity = secondaryEntity;
    }

    public void setAdditionalDetails(Map<String, String> additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    public int getNumberOfThenActions() {
        return numberOfThenActions;
    }

    public void setNumberOfThenActions(int numberOfThenActions) {
        this.numberOfThenActions = numberOfThenActions;
    }

    public int getNumberOfElseActions() {
        return numberOfElseActions;
    }

    public void setNumberOfElseActions(int numberOfElseActions) {
        this.numberOfElseActions = numberOfElseActions;
    }
}