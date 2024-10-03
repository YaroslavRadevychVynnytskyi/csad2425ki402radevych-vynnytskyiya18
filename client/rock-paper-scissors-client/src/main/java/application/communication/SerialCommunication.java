package application.communication;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SerialCommunication implements Communication {
    private final SerialPort serialPort;

    @Override
    public void open() {
        serialPort.openPort();
    }

    @Override
    public void sendMessage(String message) throws IOException {
        serialPort.getOutputStream().write(message.getBytes());
        serialPort.getOutputStream().flush();
    }

    @Override
    public String receiveMessage() throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = serialPort.getInputStream().read(buffer);

        if (bytesRead < 0) {
            throw new IOException("No data received from server");
        }

        return new String(buffer, 0, bytesRead);
    }

    @Override
    public void close() {
        serialPort.closePort();
    }
}
