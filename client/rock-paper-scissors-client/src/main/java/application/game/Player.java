package application.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {
    private String name;
    private Move move;

    public enum Move {
        ROCK,
        PAPER,
        SCISSORS
    }
}