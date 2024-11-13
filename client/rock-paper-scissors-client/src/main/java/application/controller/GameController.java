package application.controller;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import application.communication.Communication;
import application.communication.SerialCommunication;
import application.dto.GameResponseDto;
import application.game.Player;

/**
 * Controller for managing the game logic and communication with the server.
 * Handles sending moves, receiving results, and managing the communication lifecycle.
 */
public class GameController {
    private final Communication communication;

    /**
     * Initializes the game controller with a specific serial port.
     *
     * @param portNumber the index of the serial port to be used for communication
     */
    public GameController(int portNumber) {
        communication = new SerialCommunication(SerialPort.getCommPorts()[portNumber]);
    }

    /**
     * Sends the game mode and player moves to the server.
     *
     * @param mode the game mode (e.g., single-player, multi-player)
     * @param move1 the move of the first player
     * @param move2 the move of the second player
     * @throws IOException if an I/O error occurs during message transmission
     */
    public void sendModeAndMoves(String mode, Player.Move move1, Player.Move move2) throws IOException {
        communication.sendMessage(mode + "," + move1.name() + "," + move2.name() + "\n");
    }

    /**
     * Receives the game result from the server.
     *
     * @return the game result as a {@link GameResponseDto} containing the outcome and moves
     * @throws RuntimeException if the message cannot be received or parsed correctly
     */
    public GameResponseDto receiveResult() {
        String rawResponse;
        try {
            rawResponse = communication.receiveMessageUntil("|");
        } catch (IOException e) {
            throw new RuntimeException("Failed to receive a message from server" + e);
        }
        String[] responseData = rawResponse.split(",");

        return new GameResponseDto(responseData[0], Player.Move.valueOf(responseData[1]), Player.Move.valueOf(responseData[2]));
    }

    /**
     * Closes the communication connection.
     */
    public void close() {
        communication.close();
    }
}
