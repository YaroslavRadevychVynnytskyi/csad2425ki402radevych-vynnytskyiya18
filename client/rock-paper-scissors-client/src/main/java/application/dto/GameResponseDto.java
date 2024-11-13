package application.dto;

import application.game.Player;

/**
 * Data Transfer Object (DTO) for the game response.
 * Contains the result of the game and the moves of both players.
 */
public record GameResponseDto(
        /**
         * The result of the game (e.g., "Player 1 wins", "Draw").
         */
        String gameResult,

        /**
         * The move made by the first player.
         */
        Player.Move player1Move,

        /**
         * The move made by the second player.
         */
        Player.Move player2Move
) {
}
