package application.input;

import java.util.Scanner;

public class UserInputHandler {
    private final Scanner scanner = new Scanner(System.in);

    public String getInputMessage() {
        System.out.println("Please, enter a message to send to server: ");
        String message = scanner.nextLine();

        if (message.equalsIgnoreCase("exit")) {
            System.exit(0);
        }

        return message + "\n";
    }
}
