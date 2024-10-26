package application.dto;

import application.game.Player;

public record GameResponseDto(
        String gameResult,
        Player.Move player1Move,
        Player.Move player2Move
) {
}
