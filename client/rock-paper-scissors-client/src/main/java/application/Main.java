package application;

import application.communication.Communication;
import application.communication.SerialCommunication;
import application.input.UserInputHandler;
import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final UserInputHandler inputHandler = new UserInputHandler();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Enter number of serial port attached to Arduino: ");
        int portNumber = scanner.nextInt();

        Communication communication = new SerialCommunication(SerialPort.getCommPorts()[portNumber]);

        while (true) {
            communication.open();

            String messageToSend = inputHandler.getInputMessage();
            communication.sendMessage(messageToSend);

            Thread.sleep(90);

            String receivedMessage = communication.receiveMessage();
            System.out.println("Received from server: " + receivedMessage);

            communication.close();
        }
    }
}
