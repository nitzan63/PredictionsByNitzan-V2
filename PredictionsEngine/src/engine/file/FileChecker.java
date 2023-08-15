package engine.file;

import java.io.File;

public class FileChecker {
    public static void validateFile(String filePath) throws FileValidationException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileValidationException("File does not exist: " + filePath);
        }
        if (!file.canRead()) {
            throw new FileValidationException("File cannot be read: " + filePath);
        }
    }
}

class FileValidationException extends Exception {
    public FileValidationException(String message) {
        super(message);
    }
}