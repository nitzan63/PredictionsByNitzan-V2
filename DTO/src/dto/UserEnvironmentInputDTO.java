package dto;

import java.util.Map;

public class UserEnvironmentInputDTO {
    private final Map<String, Object> userInputProperties;

    public UserEnvironmentInputDTO(Map<String, Object> userInputProperties) {
        this.userInputProperties = userInputProperties;
    }

    public Map<String, Object> getUserInputProperties() {
        return userInputProperties;
    }
}
