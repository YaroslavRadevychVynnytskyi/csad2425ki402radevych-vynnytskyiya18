package application.communication;

import java.io.IOException;


/**
 * Interface for communication operations.
 * Provides methods to send and receive messages.
 */
public interface Communication {
    /**
     * Sends a message.
     *
     * @param message the message to be sent
     * @throws IOException if an I/O error occurs during message sending
     */
    void sendMessage(String message) throws IOException;

    /**
     * Receives a message.
     *
     * @return the received message as a string
     * @throws IOException if an I/O error occurs during message reception
     */
    String receiveMessage() throws IOException;

    /**
     * Receives a message until a specified delimiter is encountered.
     *
     * @param delimiter the delimiter to stop receiving the message
     * @return the received message as a string
     * @throws IOException if an I/O error occurs during message reception
     */
    String receiveMessageUntil(String delimiter) throws IOException;

    /**
     * Closes the communication channel.
     */
    void close();
}
