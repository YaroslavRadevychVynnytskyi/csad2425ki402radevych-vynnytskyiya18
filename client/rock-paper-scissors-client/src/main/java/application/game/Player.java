package application.game;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a player in the game.
 * Each player has a name and a move they make during the game.
 */
@Getter
@Setter
public class Player {
    /**
     * The name of the player.
     */
    private String name;

    /**
     * The move made by the player in the game.
     */
    private Move move;

    /**
     * Enum representing the possible moves a player can make.
     * A player can choose one of the following moves: ROCK, PAPER, or SCISSORS.
     */
    public enum Move {
        /**
         * The ROCK move, which beats SCISSORS but is beaten by PAPER.
         */
        ROCK,

        /**
         * The PAPER move, which beats ROCK but is beaten by SCISSORS.
         */
        PAPER,

        /**
         * The SCISSORS move, which beats PAPER but is beaten by ROCK.
         */
        SCISSORS
    }
}
