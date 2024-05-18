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
    public HBox showVictoryTiles;
    @FXML
    private Label playerName;

    @FXML
    private Label victoryType;

    @FXML
    private Label playerHand;

    public void initialize(String msg, ArrayList<ArrayList<Tile>> victoryTiles){
        showVictoryMsg(msg);
        showVictoryTiles(victoryTiles);
    }

    private void showVictoryTiles(ArrayList<ArrayList<Tile>> victoryTiles) {
        //更新手牌内容
        ArrayList<Tile> handTileList = new ArrayList<>();
        handTileList.clear();
        for (ArrayList<Tile> tileList : victoryTiles) {
            for (Tile tile:tileList){
                handTileList.add(tile);
            }
        }
        //给手牌上图像
//        for (int i = 0; i < player1Tiles.getChildren().size(); i++) {
//            if (i < handTileList.size()) {
//                //
//                String tileType = handTileList.get(i).toString();
//                Rectangle tile = (Rectangle) player1Tiles.getChildren().get(i);
//                tile.setId(tileType);
//                Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
//                tile.setFill(new ImagePattern(tileImage));
//            }else {
//                Rectangle tile = (Rectangle) player1Tiles.getChildren().get(i);
//                tile.setId("");
//                tile.setFill(Color.WHITE);
//            }
//        }

        showVictoryTiles.getChildren().clear();
        for (Tile tile: handTileList){
            Rectangle tile1 = new Rectangle();
            tile1.setId(tile.toString());
            tile1.setWidth(30);
            tile1.setHeight(45);
            tile1.setArcWidth(10);
            tile1.setArcHeight(10);
            String tileType = tile.toString();
            Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
//                Image tileImage = new Image(getClass().getResource("/tiles/1Bamboo.png").toExternalForm());
            tile1.setFill(new ImagePattern(tileImage));
            showVictoryTiles.getChildren().add(tile1);
        }

    }

    private void showVictoryMsg(String msg) {
        String[] victoryTypes = {"All Triplets","Big Single Wait", "Zhuo Wu Kui","Hai Di Lao Yue","Yi Tiao Long","Qi Dui Zi","Gang Shang Kai Hua","Qing Yi Se","Super Qi Dui Zi"};
        String[] parts = msg.split(" ");
        String victortMsg = "";
        playerName.setText(parts[1]);

        int i = 2;
        while (i<parts.length){
            int index = Integer.parseInt(parts[i]);
            victortMsg = victortMsg + victoryTypes[index] + "    ";
        }

        victoryType.setText(victortMsg);


    }

    public void continueGame(ActionEvent event) {
    }

    public void exitGame(ActionEvent event) {
    }
}
