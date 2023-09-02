package dto;

import java.util.Map;

public class UserInputDTO {
    private final Map<String, String> environmertPropMap;
    private final Map<String, Integer> entityPopulationMap;

    public UserInputDTO(Map<String, String> userInputProperties , Map <String,Integer> entityPopulationMap) {

        this.environmertPropMap = userInputProperties;
        this.entityPopulationMap = entityPopulationMap;
    }

    public Map<String, String> getEnvironmertPropMap() {
        return environmertPropMap;
    }

    public Map<String, Integer> getEntityPopulationMap() {
        return entityPopulationMap;
    }
}
