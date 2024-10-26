#include <Arduino.h>

enum GameMode {
  MAN_VS_MAN,
  MAN_VS_AI,
  AI_VS_AI
};

enum Move {
  ROCK,
  PAPER,
  SCISSORS
};

class Player {
  private:
    String name;
    Move move;

  public:
    Player(String name, Move move) : name(name), move(move) {}

    String getName() {
      return name;
    }

    Move getMove() {
      return move;
    }
};

class Game {
  private:
    Player player1;
    Player player2;

  public:
    Game(Player p1, Player p2) : player1(p1), player2(p2) {}

    Player* play() {
      Move move1 = player1.getMove();
      Move move2 = player2.getMove();

      if (move1 == move2) {
      return nullptr;
    }

      switch (move1) {
        case ROCK:
          return (move2 == SCISSORS) ? &player1 : &player2;
        case PAPER:
          return (move2 == ROCK) ? &player1 : &player2;
        case SCISSORS:
          return (move2 == PAPER) ? &player1 : &player2;
        default:
          return nullptr;
      }
    }
};

void setup() {
  Serial.begin(9600);
  randomSeed(analogRead(0));
}

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

Move stringToMove(String moveString) {
  if (moveString == "ROCK") return ROCK;
  if (moveString == "PAPER") return PAPER;
  if (moveString == "SCISSORS") return SCISSORS;
}

GameMode stringToGameMode(String modeString) {
  if (modeString == "MAN_VS_MAN") return MAN_VS_MAN;
  if (modeString == "MAN_VS_AI") return MAN_VS_AI;
  if (modeString == "AI_VS_AI") return AI_VS_AI;
}

String moveToString(Move move) {
  switch  (move) {
    case ROCK: return "ROCK";
    case PAPER: return "PAPER";
    case SCISSORS: return "SCISSORS";
    default: return "UNDEFINED";
  }
}

Move generateAIMove() {
  return static_cast<Move>(random(3));
}

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
