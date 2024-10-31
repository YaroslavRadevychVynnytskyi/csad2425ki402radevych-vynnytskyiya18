package application.controller;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import application.communication.Communication;
import application.communication.SerialCommunication;
import application.dto.GameResponseDto;
import application.game.Player;

public class GameController {
    private final Communication communication;

    public GameController(int portNumber) {
        communication = new SerialCommunication(SerialPort.getCommPorts()[portNumber]);
    }

    public void sendModeAndMoves(String mode, Player.Move move1, Player.Move move2) throws IOException {
        communication.sendMessage(mode + "," + move1.name() + "," + move2.name() + "\n");
    }

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

    public void close() {
        communication.close();
    }
}
