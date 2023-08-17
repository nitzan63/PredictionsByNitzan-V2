package input;

import java.util.Scanner;

public class UserInputHandler {
    private final Scanner scanner;

    // Constructor
    public UserInputHandler() {
        this.scanner = new Scanner(System.in);
    }

    // Get the user's command
    public String getUserCommand() {
        return scanner.nextLine().trim();
    }

    public int getUserInt() {
        while (true) {
            try {
                return Integer.parseInt(getUserCommand());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
