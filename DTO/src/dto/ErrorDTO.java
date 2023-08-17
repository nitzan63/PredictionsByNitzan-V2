package dto;

import java.time.LocalDateTime;

public class ErrorDTO {
    private final String errorMessage;
    private final String errorType;
    private final LocalDateTime timestamp;

    public ErrorDTO(String errorMessage, String errorType, LocalDateTime timestamp) {
        this.errorMessage = errorMessage;
        this.errorType = errorType;
        this.timestamp = timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorType() {
        return errorType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
