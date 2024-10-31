package application.communication;

import java.io.IOException;

public interface Communication {
    void sendMessage(String message) throws IOException;
    String receiveMessage() throws IOException;
    String receiveMessageUntil(String delimiter) throws IOException;
    void close();
}
