#include <Arduino.h>

/**
 * @enum GameMode
 * @brief Defines the possible game modes.
 */
enum GameMode {
  MAN_VS_MAN,
  MAN_VS_AI,
  AI_VS_AI
};

/**
 * @enum Move
 * @brief Defines the possible moves in the game.
 */
enum Move {
  ROCK,
  PAPER,
  SCISSORS
};

/**
 * @class Player
 * @brief Represents a player in the game.
 */
class Player {
  private:
    String name; /**< The name of the player */
    Move move; /**< The move selected by the player */

  public:
    /**
     * @brief Constructs a Player object with a given name and move.
     * @param name The name of the player.
     * @param move The move chosen by the player.
     */
    Player(String name, Move move) : name(name), move(move) {}

    /**
     * @brief Gets the name of the player.
     * @return The name of the player.
     */
    String getName() {
      return name;
    }

    /**
     * @brief Gets the move of the player.
     * @return The move of the player.
     */
    Move getMove() {
      return move;
    }
};

/**
 * @class Game
 * @brief Represents a game between two players.
 */
class Game {
  private:
    Player player1; /**< The first player */
    Player player2; /**< The second player */

  public:
    /**
     * @brief Constructs a Game object with two players.
     * @param p1 The first player.
     * @param p2 The second player.
     */
    Game(Player p1, Player p2) : player1(p1), player2(p2) {}

    /**
     * @brief Plays the game and determines the winner.
     * @return A pointer to the winning player, or nullptr if there is a draw.
     */
    Player* play() {
      Move move1 = player1.getMove();
      Move move2 = player2.getMove();

      if (move1 == move2) {
      return nullptr; /**< Draw condition */
    }

      switch (move1) {
        case ROCK:
          return (move2 == SCISSORS) ? &player1 : &player2;
        case PAPER:
          return (move2 == ROCK) ? &player1 : &player2;
        case SCISSORS:
          return (move2 == PAPER) ? &player1 : &player2;
        default:
          return nullptr; /**< Invalid move */
      }
    }
};

/**
 * @brief Initializes the game setup.
 */
void setup() {
  Serial.begin(9600);
  randomSeed(analogRead(0)); /**< Initialize random number generator */
}

/**
 * @brief Main loop where the game runs continuously.
 */
void loop() {
  if (Serial.available() > 0) {
    String input = Serial.readStringUntil('\n');

    int delimiterIndex1 = input.indexOf(',');
    int delimiterIndex2 = input.indexOf(',', delimiterIndex1 + 1);

    String modeString = input.substring(0, delimiterIndex1);
    String move1String = input.substring(delimiterIndex1 + 1, delimiterIndex2);
    String move2String = input.substring(delimiterIndex2 + 1);

    GameMode mode = stringToGameMode(modeString);
    Move player1Move;
    Move player2Move;

    String result;
    String response;

    if (mode == MAN_VS_MAN) {
      
      player1Move = stringToMove(move1String);
      player2Move = stringToMove(move2String);

      Player player1("Player 1", player1Move);
      Player player2("Player 2", player2Move);

      Game game(player1, player2);
      Player* winner = game.play();

      response = convertResponse(winner, player1Move, player2Move);

    } else if (mode == MAN_VS_AI) {
      
      player1Move = stringToMove(move1String);
      Move aiMove = generateAIMove();

      Player player("Player 1", player1Move);
      Player ai("AI", aiMove);
      
      Game game(player, ai);
      Player* winner = game.play();
      
      response = convertResponse(winner, player1Move, aiMove);

    } else if (mode == AI_VS_AI) {
      
      Move ai1Move = generateAIMove();
      Move ai2Move = generateAIMove();

      Player ai1("Player 1", ai1Move);
      Player ai2("Player 2", ai2Move);

      Game game(ai1, ai2);
      Player* winner = game.play();
      
      response = convertResponse(winner, ai1Move, ai2Move);
    }

    Serial.print(response);
    Serial.print("|");
    delay(2000);
  }
}

/**
 * @brief Converts a string to a Move enum.
 * @param moveString The move as a string ("ROCK", "PAPER", "SCISSORS").
 * @return The corresponding Move enum value.
 */
Move stringToMove(String moveString) {
  if (moveString == "ROCK") return ROCK;
  if (moveString == "PAPER") return PAPER;
  if (moveString == "SCISSORS") return SCISSORS;
}

/**
 * @brief Converts a string to a GameMode enum.
 * @param modeString The game mode as a string ("MAN_VS_MAN", "MAN_VS_AI", "AI_VS_AI").
 * @return The corresponding GameMode enum value.
 */
GameMode stringToGameMode(String modeString) {
  if (modeString == "MAN_VS_MAN") return MAN_VS_MAN;
  if (modeString == "MAN_VS_AI") return MAN_VS_AI;
  if (modeString == "AI_VS_AI") return AI_VS_AI;
}

/**
 * @brief Converts a Move enum to its string representation.
 * @param move The Move enum value.
 * @return The string representation of the move ("ROCK", "PAPER", "SCISSORS").
 */
String moveToString(Move move) {
  switch  (move) {
    case ROCK: return "ROCK";
    case PAPER: return "PAPER";
    case SCISSORS: return "SCISSORS";
    default: return "UNDEFINED";
  }
}

/**
 * @brief Generates a random AI move.
 * @return A random Move (ROCK, PAPER, or SCISSORS).
 */
Move generateAIMove() {
  return static_cast<Move>(random(3));
}

/**
 * @brief Converts the result of the game to a response string.
 * @param winner A pointer to the winning player, or nullptr if there is a draw.
 * @param player1Move The move of the first player.
 * @param player2Move The move of the second player.
 * @return A formatted string indicating the result of the game.
 */
String convertResponse(Player* winner, Move player1Move, Move player2Move) {
  String result;

  if (winner == nullptr) {
        result = "DRAW";
      } else {
        result = winner->getName();
      }

  String player1MoveStr = moveToString(player1Move);
  String player2MoveStr = moveToString(player2Move);

  return result + "," + player1MoveStr + "," + player2MoveStr;
}
