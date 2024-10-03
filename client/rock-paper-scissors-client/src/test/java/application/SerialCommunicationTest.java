package application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import application.communication.SerialCommunication;
import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SerialCommunicationTest {
    private SerialPort serialPort;
    private SerialCommunication serialCommunication;

    @BeforeEach
    void setUp() {
        serialPort = mock(SerialPort.class);
        serialCommunication = new SerialCommunication(serialPort);
    }

    @Test
    void open_AllOk_ShouldOpenPort() {
        //Act
        serialCommunication.open();

        //Assert
        verify(serialPort).openPort();
    }

    @Test
    void sendMessage_AllOk_ShouldSendMessage() throws IOException {
        // Arrange
        OutputStream mockOutputStream = mock(OutputStream.class);
        when(serialPort.getOutputStream()).thenReturn(mockOutputStream);

        String mockMessage = "Test message";

        // Act
        serialCommunication.sendMessage(mockMessage);

        // Assert
        verify(mockOutputStream, times(1)).write(mockMessage.getBytes());
        verify(mockOutputStream, times(1)).flush();
    }

    @Test
    void receiveMessage_AllOk_ShouldReturnReceivedMessage() throws IOException {
        // Arrange
        String expected = "Arduino response\n";

        byte[] testData = expected.getBytes();
        InputStream mockInputStream = mock(InputStream.class);

        when(serialPort.getInputStream()).thenReturn(mockInputStream);
        when(mockInputStream.read(any(byte[].class))).thenAnswer(i -> {
            byte[] buffer = i.getArgument(0);
            System.arraycopy(testData, 0, buffer, 0, testData.length);
            return testData.length;
        });

        // Act
        String actual = serialCommunication.receiveMessage();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void receiveMessage_NoData_ShouldThrowIOException() throws IOException {
        // Arrange
        String expectedExceptionMessage = "No data received from server";
        InputStream mockInputStream = mock(InputStream.class);

        when(serialPort.getInputStream()).thenReturn(mockInputStream);
        when(mockInputStream.read(any(byte[].class))).thenReturn(-1);

        // Act & Assert
        IOException exception = assertThrows(IOException.class, () -> serialCommunication.receiveMessage());

        assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    @Test
    void close_AllOk_ShouldClosePort() {
        // Act
        serialCommunication.close();

        // Assert
        verify(serialPort).closePort();
    }
}
