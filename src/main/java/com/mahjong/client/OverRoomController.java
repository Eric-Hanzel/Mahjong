package com.mahjong.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import reLogic.tiles.Tile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Objects;


public class OverRoomController {
    public HBox showVictoryTiles; // HBox to display the tiles that led to the victory
    @FXML
    private Label playerName; // Label to display the name of the winning player

    @FXML
    private Label victoryType; // Label to display the type of victory achieved

    @FXML
    private Label playerHand; // Label to display the player's hand, not used in this code snippet

    // Initializes the controller with the game over message and victory tiles
    public void initialize(String msg, ArrayList<ArrayList<Tile>> victoryTiles){
        showVictoryMsg(msg); // Display the victory message
        showVictoryTiles(victoryTiles); // Display the tiles that contributed to the victory
    }

    // Displays the victory tiles in the provided HBox
    private void showVictoryTiles(ArrayList<ArrayList<Tile>> victoryTiles) {
        ArrayList<Tile> handTileList = new ArrayList<>();
        handTileList.clear();

        // Flatten the list of lists of tiles into a single list
        for (ArrayList<Tile> tileList : victoryTiles) {
            for (Tile tile:tileList){
                handTileList.add(tile);
            }
        }

        showVictoryTiles.getChildren().clear(); // Clear existing tiles in the HBox

        // Create and display rectangles for each tile
        for (Tile tile: handTileList){
            Rectangle tile1 = new Rectangle();
            tile1.setId(tile.toString());
            tile1.setWidth(30);
            tile1.setHeight(45);
            tile1.setArcWidth(10);
            tile1.setArcHeight(10);
            String tileType = tile.toString();
            Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
            tile1.setFill(new ImagePattern(tileImage));
            showVictoryTiles.getChildren().add(tile1);
        }

    }

    // Parses and displays the victory message
    private void showVictoryMsg(String msg) {
        String[] victoryTypes = {"All Triplets","Big Single Wait", "Zhuo Wu Kui","Hai Di Lao Yue","Yi Tiao Long","Qi Dui Zi","Gang Shang Kai Hua","Qing Yi Se","Super Qi Dui Zi"};
        String[] parts = msg.split(" ");
        String victortMsg = "";
        playerName.setText(parts[1]); // Set the winning player's name

        // Construct the victory message from the indices provided in the message
        int i = 2;
        while (i<parts.length){
            int index = Integer.parseInt(parts[i]);
            victortMsg = victortMsg + victoryTypes[index] + "    ";
        }

        victoryType.setText(victortMsg); // Set the text of the victory type label


    }

    // Placeholder methods for continuing or exiting the game
    public void continueGame(ActionEvent event) {
        // Implementation needed to continue playing
    }

    public void exitGame(ActionEvent event) {
        // Implementation needed to handle game exit
    }
}
