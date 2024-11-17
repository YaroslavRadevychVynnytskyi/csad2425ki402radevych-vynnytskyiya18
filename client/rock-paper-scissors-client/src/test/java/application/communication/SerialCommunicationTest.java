package application.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SerialCommunicationTest {
    private SerialPort serialPort;
    private SerialCommunication serialCommunication;
    private InputStream mockInputStream;
    private OutputStream mockOutputStream;

    @BeforeEach
    void setUp() {
        serialPort = mock(SerialPort.class);
        mockInputStream = mock(InputStream.class);
        mockOutputStream = mock(OutputStream.class);

        when(serialPort.getInputStream()).thenReturn(mockInputStream);
        when(serialPort.getOutputStream()).thenReturn(mockOutputStream);

        serialCommunication = new SerialCommunication(serialPort);
    }

    @Test
    void sendMessage_AllOk_ShouldSendMessage() throws IOException {
        // Arrange
        String mockMessage = "Test message";

        // Act
        serialCommunication.sendMessage(mockMessage);

        // Assert
        verify(mockOutputStream, times(1)).write(mockMessage.getBytes());
        verify(mockOutputStream, times(1)).flush();
    }

    @Test
    void receiveMessage_NoData_ShouldThrowIOException() throws IOException {
        // Arrange
        String expectedExceptionMessage = "No data received from server";

        when(mockInputStream.read(any(byte[].class))).thenReturn(-1);

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> serialCommunication.receiveMessage());

        assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    @Test
    void close_AllOk_ShouldClosePort() throws IOException {
        // Act
        serialCommunication.close();

        // Assert
        verify(mockInputStream).close();
        verify(mockOutputStream).close();
        verify(serialPort).closePort();
    }

    @Test
    void receiveMessageUntil_NoData_ShouldThrowIOException() throws IOException {
        // Arrange
        String expectedExceptionMessage = "No data received from server";
        String delimiter = "\n";

        when(mockInputStream.read(any(byte[].class))).thenReturn(-1);

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> serialCommunication.receiveMessageUntil(delimiter));

        assertEquals(expectedExceptionMessage, exception.getMessage());
    }
}