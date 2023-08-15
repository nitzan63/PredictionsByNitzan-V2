package engine.file.exceptions;

public class XMLProcessingException extends Exception {
    public XMLProcessingException(String message) {
        super(message);
    }

    public XMLProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
