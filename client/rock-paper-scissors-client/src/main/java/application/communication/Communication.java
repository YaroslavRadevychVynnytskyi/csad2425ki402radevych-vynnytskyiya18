package application.communication;

import java.io.IOException;

public interface Communication {
    void open();
    void sendMessage(String message) throws IOException;
    String receiveMessage() throws IOException;
    void close();
}
