package com.mahjong.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import logic.players.Player;
import logic.tiles.Tile;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class OverRoomController {
    public VBox vBox;
    @FXML
    private Label scoringLabel;
    @FXML
    private HBox showVictoryTiles;
    @FXML
    private Label playerNameLabel;
    @FXML
    private Label victoryTypeLabel;
    @FXML
    private Label playerHandLabel;

    // Initialize the controller with the victory message, victory tiles, player list, and player's name
    @FXML
    public void initialize(String msg, ArrayList<ArrayList<Tile>> victoryTiles, ArrayList<Player> players, String name) {
        showVictoryMsg(msg, players, name);
        showVictoryTiles(victoryTiles);

        Image backgroundImage = new Image(getClass().getResource("/images/backGround.png").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true)); // 调整BackgroundSize的参数以覆盖整个VBox
        vBox.setBackground(new Background(background));
    }

    // Display the victory tiles
    private void showVictoryTiles(ArrayList<ArrayList<Tile>> victoryTiles) {
        ArrayList<Tile> handTileList = new ArrayList<>();
        for (ArrayList<Tile> tileList : victoryTiles) {
            handTileList.addAll(tileList);
        }

        showVictoryTiles.getChildren().clear();
        for (Tile tile : handTileList) {
            Rectangle tile1 = new Rectangle(30, 45);
            tile1.setArcWidth(10);
            tile1.setArcHeight(10);
            String tileType = tile.toString();
            Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
            tile1.setFill(new ImagePattern(tileImage));
            showVictoryTiles.getChildren().add(tile1);
        }
    }

    // Display the victory message
    private void showVictoryMsg(String msg, ArrayList<Player> players, String name) {
        for (Player player : players) {
            if (Objects.equals(name, player.getName())) {
                scoringLabel.setText("Your Scoring: " + player.getScoring());
            }
        }

        String[] victoryTypes = {
                "All Triplets", "Big Single Wait", "Zhuo Wu Kui", "Hai Di Lao Yue", "Yi Tiao Long",
                "Qi Dui Zi", "Gang Shang Kai Hua", "Qing Yi Se", "Super Qi Dui Zi", "Pi Hu"
        };
        String[] parts = msg.split(" ");
        StringBuilder victoryMsgBuilder = new StringBuilder();
        playerNameLabel.setText(parts[1]);

        for (int i = 2; i < parts.length; i++) {
            int index = Integer.parseInt(parts[i]);
            victoryMsgBuilder.append(victoryTypes[index]).append("    ");
        }

        victoryTypeLabel.setText(victoryMsgBuilder.toString());
        playerHandLabel.setText("Victory Player's HandTiles!!!");
    }

    // Continue the game and return to the menu
    public void continueGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Scene gameRoomWaitScene = new Scene(loader.load(), 1000, 1000);

        Stage stage = (Stage) playerNameLabel.getScene().getWindow();
        stage.setScene(gameRoomWaitScene);
    }

    // Exit the game
    public void exitGame(ActionEvent event) {
        Stage stage = (Stage) playerNameLabel.getScene().getWindow();
        stage.close();
    }
}
