package application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.dto.GameResponseDto;
import application.game.Player;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

public class GameControllerTest {
    private GameController gameController;

    private static final int ARDUINO_PORT_INDEX = 0;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        gameController = new GameController(ARDUINO_PORT_INDEX);
        Thread.sleep(2000);
    }

    @AfterEach
    void tearDown() {
        gameController.close();
    }

    @Order(1)
    @Test
    @DisabledIfSystemProperty(named = "ci.environment", matches = "true")
    public void testSendModeAndMoves_MAN_VS_MAN() throws Exception {
        String mode = "MAN_VS_MAN";
        Player.Move player1Move = Player.Move.ROCK;
        Player.Move player2Move = Player.Move.SCISSORS;

        gameController.sendModeAndMoves(mode, player1Move, player2Move);
        GameResponseDto response = gameController.receiveResult();

        System.out.println(response.gameResult());

        assertNotNull(response, "Response should not be null");
        assertEquals("Player 1", response.gameResult(), "Expected Player 1 to win with ROCK vs SCISSORS");
        assertEquals(Player.Move.ROCK, response.player1Move(), "Player 1's move should be ROCK");
        assertEquals(Player.Move.SCISSORS, response.player2Move(), "Player 2's move should be SCISSORS");
    }

    @Order(2)
    @Test
    @DisabledIfSystemProperty(named = "ci.environment", matches = "true")
    public void testSendModeAndMoves_DRAW() throws Exception {
        String mode = "MAN_VS_MAN";
        Player.Move player1Move = Player.Move.PAPER;
        Player.Move player2Move = Player.Move.PAPER;

        gameController.sendModeAndMoves(mode, player1Move, player2Move);

        GameResponseDto response = gameController.receiveResult();

        assertNotNull(response, "Response should not be null");
        assertEquals("DRAW", response.gameResult(), "Expected a draw when both moves are PAPER");
        assertEquals(Player.Move.PAPER, response.player1Move(), "Player 1's move should be PAPER");
        assertEquals(Player.Move.PAPER, response.player2Move(), "Player 2's move should be PAPER");
    }
}
