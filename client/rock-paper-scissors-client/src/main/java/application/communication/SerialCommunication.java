package application.communication;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Implements the Communication interface using serial port communication.
 * Provides methods to send and receive messages via a serial port.
 */
public class SerialCommunication implements Communication {
    private final SerialPort serialPort;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    /**
     * Initializes the serial communication with the given serial port.
     *
     * @param serialPort the serial port to use for communication
     */
    public SerialCommunication(SerialPort serialPort) {
        this.serialPort = serialPort;
        this.serialPort.openPort();
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
    }

    /**
     * Sends a message over the serial connection.
     *
     * @param message the message to be sent
     * @throws IOException if an I/O error occurs during message transmission
     */
    @Override
    public void sendMessage(String message) throws IOException {
        outputStream.write(message.getBytes());
        outputStream.flush();
    }

    /**
     * Receives a message from the serial connection.
     *
     * @return the received message as a string
     * @throws IOException if an I/O error occurs or if no data is received
     */
    @Override
    public String receiveMessage() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer);

        if (bytesRead < 0) {
            throw new IOException("No data received from server");
        }

        return new String(buffer, 0, bytesRead).trim();
    }

    /**
     * Receives a message until a specified delimiter is encountered.
     *
     * @param delimiter the delimiter to stop receiving the message
     * @return the received message as a string, excluding the delimiter
     * @throws IOException if an I/O error occurs or if no data is received
     */
    @Override
    public String receiveMessageUntil(String delimiter) throws IOException {
        StringBuilder messageBuffer = new StringBuilder();
        byte[] buffer = new byte[1];
        int bytesRead;

        while (true) {
            bytesRead = inputStream.read(buffer);

            if (bytesRead < 0) {
                throw new IOException("No data received from server");
            }

            messageBuffer.append((char) buffer[0]);

            if (messageBuffer.toString().endsWith(delimiter)) {
                break;
            }
        }

        return messageBuffer.substring(0, messageBuffer.length() - delimiter.length()).trim();
    }

    /**
     * Closes the serial communication by closing the I/O streams and the serial port.
     */
    @Override
    public void close() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't close I/O streams");
        }

        serialPort.closePort();
    }
}
