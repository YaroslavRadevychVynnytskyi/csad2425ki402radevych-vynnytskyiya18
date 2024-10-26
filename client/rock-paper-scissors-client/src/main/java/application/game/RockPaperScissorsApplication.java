package application.game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import application.controller.GameController;
import application.dto.GameResponseDto;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.ini4j.Ini;
import org.ini4j.Profile;

public class RockPaperScissorsApplication extends Application {
    private Player player1;
    private Player player2;

    private GameController gameController;

    public RockPaperScissorsApplication() {
        try {
            gameController = new GameController(0);
        } catch (Exception ex) {
            System.err.println("Connection with server failed!");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Rock Paper Scissors Menu");

        Image logoImage = new Image(Objects.requireNonNull(RockPaperScissorsApplication.class.getResourceAsStream("/images/logo.png")));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(300);
        logoImageView.setPreserveRatio(true);
        logoImageView.setSmooth(true);

        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");
        MenuItem newGameItem = new MenuItem("New Game");
        MenuItem loadGameItem = new MenuItem("Load Game");

        gameMenu.getItems().addAll(newGameItem, loadGameItem);
        menuBar.getMenus().add(gameMenu);

        newGameItem.setOnAction(e -> showGameModeSelection());

        loadGameItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Game");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("INI Files", "*.ini"));
            File file = fileChooser.showOpenDialog(primaryStage);
            try {
                loadGame(file);
                startManVsManGame(primaryStage, true);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Load Failed");
                alert.setHeaderText(null);
                alert.setContentText("Game is already played");
                alert.showAndWait();
            }
        });

        VBox vbox = new VBox(10, logoImageView, menuBar);
        Scene scene = new Scene(vbox, 300, 350);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void loadGame(File file) throws IOException {
        Ini ini = new Ini(file);
        Profile.Section gameStatus = ini.get("Game Status");

        if (gameStatus.containsKey("Winner")) {
            throw new IllegalStateException("Game is already finished");
        }

        player1 = new Player();
        player2 = new Player();

        player1.setName(gameStatus.get("Player1"));
        player2.setName(gameStatus.get("Player2"));

        String player1Move = gameStatus.get("Player1Move");
        String player2Move = gameStatus.get("Player2Move");

        if (player1Move != null && !player1Move.isEmpty()) {
            player1.setMove(Player.Move.valueOf(player1Move));
        }

        if (player2Move != null && !player2Move.isEmpty()) {
            player2.setMove(Player.Move.valueOf(player2Move));
        }
    }

    private void showGameModeSelection() {
        Stage modeStage = new Stage();
        modeStage.setTitle("Select Game Mode");

        Button manVsManButton = new Button(GameMode.MAN_VS_MAN.name().replace("_", " "));
        Button manVsAIButton = new Button(GameMode.MAN_VS_AI.name().replace("_", " "));
        Button aiVsAIButton = new Button(GameMode.AI_VS_AI.name().replace("_", " "));

        manVsManButton.setOnAction(e -> {
            player1 = new Player();
            player2 = new Player();
            setPlayerNicknames(GameMode.MAN_VS_MAN);
        });

        manVsAIButton.setOnAction(e -> {
            player1 = new Player();
            player2 = new Player();
            setPlayerNicknames(GameMode.MAN_VS_AI);
        });

        aiVsAIButton.setOnAction(e -> {
            player1 = new Player();
            player2 = new Player();
            startAiVsAiGame(modeStage);
        });

        VBox modeLayout = new VBox(10, manVsManButton, manVsAIButton, aiVsAIButton);
        Scene modeScene = new Scene(modeLayout, 300, 200);
        modeStage.setScene(modeScene);
        modeStage.show();
    }

    private void startAiVsAiGame(Stage modeStage) {
        modeStage.close();

        Stage gameStage = new Stage();
        gameStage.setTitle("AI vs AI Game");

        GridPane grid = new GridPane();
        HBox moveImages = new HBox(10);
        grid.add(moveImages, 0, 5, 3, 1);

        Label player1Label = new Label("AI 1's Move:");
        Label player1MoveLabel = new Label();
        grid.add(player1Label, 0, 0);
        grid.add(player1MoveLabel, 1, 0);

        Label player2Label = new Label("AI 2's Move:");
        Label player2MoveLabel = new Label();
        grid.add(player2Label, 0, 1);
        grid.add(player2MoveLabel, 1, 1);

        Button playButton = new Button("Play");
        grid.add(playButton, 0, 2, 3, 1);
        Label resultLabel = new Label();
        grid.add(resultLabel, 0, 3, 3, 1);

        playButton.setOnAction(e -> {
            try {
                gameController.sendModeAndMoves(GameMode.AI_VS_AI.name(), Player.Move.ROCK, Player.Move.ROCK);
                GameResponseDto gameResponseDto = gameController.receiveResult();
                String resultText;
                if (gameResponseDto.gameResult().equals("DRAW")) {
                    resultText = "Draw";
                } else {
                    resultText = gameResponseDto.gameResult().equals("Player 1") ? "AI1" : "AI2";
                }

                String movesHistory = "AI 1 put " + gameResponseDto.player1Move().name() + ". "
                        + "AI 2 put " + gameResponseDto.player2Move().name();
                resultLabel.setText("Result: " + resultText + "\nMoves: " + movesHistory);

                moveImages.getChildren().clear();
                moveImages.getChildren().add(createMoveImage(gameResponseDto.player1Move()));
                moveImages.getChildren().add(createMoveImage(gameResponseDto.player2Move()));
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Error");
                alert.setHeaderText(null);
                alert.setContentText("Server connection failed");
                alert.showAndWait();
            }
        });

        Scene gameScene = new Scene(grid, 500, 300);
        gameStage.setScene(gameScene);
        gameStage.show();

    }

    private void setPlayerNicknames(GameMode gameMode) {
        Stage nicknameStage = new Stage();
        nicknameStage.setTitle("Set Player Nicknames");

        TextField player1NameField = new TextField();
        player1NameField.setPromptText("Enter Player 1 Name");

        TextField player2NameField = new TextField();
        player2NameField.setPromptText("Enter Player 2 Name");

        Button confirmButton = new Button("Confirm");
        confirmButton.setOnAction(e -> {
            player1.setName(player1NameField.getText());
            player2.setName(player2NameField.getText());

            if (gameMode.equals(GameMode.MAN_VS_MAN)) {
                startManVsManGame(nicknameStage, false);
            }
            if (gameMode.equals(GameMode.MAN_VS_AI)) {
                startManVsAiGame(nicknameStage);
            }

        });

        VBox nicknameLayout = new VBox(10, player1NameField, player2NameField, confirmButton);
        Scene nicknameScene = new Scene(nicknameLayout, 300, 200);
        nicknameStage.setScene(nicknameScene);
        nicknameStage.show();
    }

    private void startManVsAiGame(Stage nicknameStage) {
        nicknameStage.close();

        Stage gameStage = new Stage();
        gameStage.setTitle("Man vs AI Game");

        GridPane grid = new GridPane();
        HBox moveImages = new HBox(10);
        grid.add(moveImages, 0, 5, 3, 1);

        Label player1Label = new Label(player1.getName() + "'s Move:");
        ChoiceBox<Player.Move> player1Move = new ChoiceBox<>();
        player1Move.getItems().addAll(Player.Move.values());
        Button player1MakeMoveButton = new Button("Make Move");
        grid.add(player1Label, 0, 0);
        grid.add(player1Move, 1, 0);
        grid.add(player1MakeMoveButton, 2, 0);

        Label player2Label = new Label("AI's Move:");
        Label player2MoveLabel = new Label();
        grid.add(player2Label, 0, 1);
        grid.add(player2MoveLabel, 1, 1);

        Button playButton = new Button("Play");
        grid.add(playButton, 0, 2, 3, 1);
        Label resultLabel = new Label();
        grid.add(resultLabel, 0, 3, 3, 1);

        player1MakeMoveButton.setOnAction(e -> {
            if (player1Move.getValue() != null) {
                player1.setMove(player1Move.getValue());
                player1Move.setDisable(true);
                player1Move.getSelectionModel().clearSelection();
                resultLabel.setText(player1.getName() + " has made their move!");
            } else {
                resultLabel.setText("Player 1 must make a move!");
            }
        });

        playButton.setOnAction(e -> {
            if (player1.getMove() != null) {
                try {
                    gameController.sendModeAndMoves(GameMode.MAN_VS_AI.name(), player1.getMove(), Player.Move.ROCK);
                    GameResponseDto gameResponseDto = gameController.receiveResult();

                    String resultText;
                    if (gameResponseDto.gameResult().equals("DRAW")) {
                        resultText = "Draw";
                    } else {
                        resultText = gameResponseDto.gameResult().equals("Player 1") ? player1.getName() : "AI";
                    }

                    String movesHistory = player1.getName() + " put " + gameResponseDto.player1Move().name() + ". "
                            + player2.getName() + " put " + gameResponseDto.player2Move().name();
                    resultLabel.setText("Result: " + resultText + " wins!\n Moves: " + movesHistory);

                    moveImages.getChildren().clear();
                    moveImages.getChildren().add(createMoveImage(gameResponseDto.player1Move()));
                    moveImages.getChildren().add(createMoveImage(gameResponseDto.player2Move()));
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Connection Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Server connection failed");
                    alert.showAndWait();
                }
            } else {
                resultLabel.setText("Player 1 must make a move!");
            }
        });

        Scene gameScene = new Scene(grid, 500, 300);
        gameStage.setScene(gameScene);
        gameStage.show();

    }

    private void startManVsManGame(Stage nicknameStage, boolean isLoaded) {
        nicknameStage.close();

        Stage gameStage = new Stage();
        gameStage.setTitle("Man vs Man Game");

        GridPane grid = new GridPane();
        HBox moveImages = new HBox(10);
        grid.add(moveImages, 0, 5, 3, 1);

        Label player1Label = new Label(player1.getName() + "'s Move:");
        ChoiceBox<Player.Move> player1Move = new ChoiceBox<>();
        player1Move.getItems().addAll(Player.Move.values());
        Button player1MakeMoveButton = new Button("Make Move");
        grid.add(player1Label, 0, 0);
        grid.add(player1Move, 1, 0);
        grid.add(player1MakeMoveButton, 2, 0);

        Label player2Label = new Label(player2.getName() + "'s Move:");
        ChoiceBox<Player.Move> player2Move = new ChoiceBox<>();
        player2Move.getItems().addAll(Player.Move.values());
        Button player2MakeMoveButton = new Button("Make Move");
        grid.add(player2Label, 0, 1);
        grid.add(player2Move, 1, 1);
        grid.add(player2MakeMoveButton, 2, 1);

        Button playButton = new Button("Play");
        grid.add(playButton, 0, 2, 3, 1);
        Label resultLabel = new Label();
        grid.add(resultLabel, 0, 3, 3, 1);

        Button saveButton = new Button("Save");
        grid.add(saveButton, 0, 4, 3, 1);

        if (isLoaded) {
            if (player1.getMove() != null) {
                player1Move.setDisable(true);
                player1MakeMoveButton.setDisable(true);
            }

            if (player2.getMove() != null) {
                player2Move.setDisable(true);
                player2MakeMoveButton.setDisable(true);
            }
        }

        player1MakeMoveButton.setOnAction(e -> {
            if (player1Move.getValue() != null) {
                player1.setMove(player1Move.getValue());
                player1Move.setDisable(true);
                player1Move.getSelectionModel().clearSelection();
                resultLabel.setText(player1.getName() + " has made their move!");
            } else {
                resultLabel.setText("Player 1 must make a move!");
            }
        });

        player2MakeMoveButton.setOnAction(e -> {
            if (player2Move.getValue() != null) {
                player2.setMove(player2Move.getValue());
                player2Move.setDisable(true);
                player2Move.getSelectionModel().clearSelection();
                resultLabel.setText(player2.getName() + " has made their move!");
            } else {
                resultLabel.setText("Player 2 must make a move!");
            }
        });

        String[] gameResultHolder = new String[1];
        playButton.setOnAction(e -> {
            try {
                gameController.sendModeAndMoves(GameMode.MAN_VS_MAN.name(), player1.getMove(), player2.getMove());
                GameResponseDto gameResponseDto = gameController.receiveResult();

                if (gameResponseDto.gameResult().equals("DRAW")) {
                    gameResultHolder[0] = "Draw";
                } else {
                    gameResultHolder[0] = gameResponseDto.gameResult().equals("Player 1") ? player1.getName() : player2.getName();
                }

                String movesHistory = player1.getName() + " put " + gameResponseDto.player1Move().name() + ". "
                        + player2.getName() + " put " + gameResponseDto.player2Move().name();
                resultLabel.setText("Result: " + gameResultHolder[0] + " wins!\n Moves: " + movesHistory);

                moveImages.getChildren().clear();
                moveImages.getChildren().add(createMoveImage(gameResponseDto.player1Move()));
                moveImages.getChildren().add(createMoveImage(gameResponseDto.player2Move()));
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Error");
                alert.setHeaderText(null);
                alert.setContentText("Server connection failed");
                alert.showAndWait();
            }
        });

        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Game Result");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("INI Files", "*.ini"));
            File file = fileChooser.showSaveDialog(gameStage);

            if (file != null) {
                try {
                    saveGameResult(file, String.valueOf(gameResultHolder[0]), player1, player2);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Save Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Game result saved successfully!");
                    alert.showAndWait();
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Save Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to save game result: " + ex.getMessage());
                    alert.showAndWait();
                }
            }
        });

        Scene gameScene = new Scene(grid, 500, 300);
        gameStage.setScene(gameScene);
        gameStage.show();
    }

    private void saveGameResult(File file, String result, Player player1, Player player2) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("[Game Status]");
            writer.newLine();
            writer.write("Player1 = " + player1.getName());
            writer.newLine();
            writer.write("Player2 = " + player2.getName());
            writer.newLine();

            if (player1.getMove() == null || player2.getMove() == null) {
                writer.write("Player1Move = " + (player1.getMove() == null ? "" : player1.getMove().name()));
                writer.newLine();
                writer.write("Player2Move = " + (player2.getMove() == null ? "" : player2.getMove().name()));
                writer.newLine();

                return;
            } else {
                writer.write("Player1Move = " + player1.getMove().name());
                writer.newLine();
                writer.write("Player2Move = " + player2.getMove().name());

                writer.newLine();
            }

            if (!result.isBlank()) {
                writer.write("Winner = " + result);
            }

            writer.newLine();
        }
    }

    private ImageView createMoveImage(Player.Move move) {
        String imagePath = "";

        switch (move) {
            case ROCK:
                imagePath = "/images/rock.png";
                break;
            case PAPER:
                imagePath = "/images/paper.png";
                break;
            case SCISSORS:
                imagePath = "/images/scissors.png";
                break;
        }
        Image image = new Image(RockPaperScissorsApplication.class.getResourceAsStream(imagePath));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
