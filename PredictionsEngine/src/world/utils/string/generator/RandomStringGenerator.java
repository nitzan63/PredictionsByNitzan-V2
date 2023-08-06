package world.utils.string.generator;

import java.security.SecureRandom;

public class RandomStringGenerator {
    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!?,_-(). 0123456789";
    private static final int MAX_LENGTH = 50;

    public String generateRandomString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        int logicLength = random.nextInt(MAX_LENGTH) + 1;
        for (int i = 0; i < logicLength; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
