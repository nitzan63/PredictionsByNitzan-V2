package dto;

import java.util.Map;

public class UserEnvironmentInputDTO {
    private final Map<String, String> userInputProperties;

    public UserEnvironmentInputDTO(Map<String, String> userInputProperties) {
        this.userInputProperties = userInputProperties;
    }

    public Map<String, String> getUserInputProperties() {
        return userInputProperties;
    }
}
