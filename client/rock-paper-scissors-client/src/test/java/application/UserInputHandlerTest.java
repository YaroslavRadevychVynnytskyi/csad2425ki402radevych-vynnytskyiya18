package application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import application.input.UserInputHandler;
import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;


public class UserInputHandlerTest {
    @Test
    void getInputMessage_AllOk_ShouldReturnMessage() {
        String expected = "Hello, world!";

        System.setIn(new ByteArrayInputStream(expected.getBytes()));

        UserInputHandler inputHandler = new UserInputHandler();
        String actual = inputHandler.getInputMessage();

        assertEquals(expected + "\n", actual);
    }
}
