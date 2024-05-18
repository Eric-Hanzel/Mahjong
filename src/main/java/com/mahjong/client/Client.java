package com.mahjong.client;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application {
    //当玩家点击加入游戏与服务端建立通道 ok
    //两个线程：一个接受服务端的消息，更新画布 ok
    //另一个向服务端发消息，玩家操作消息 ok
    //玩家点击相应操作时，就向服务端发消息

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Mahjong");
        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        launch();
    }
}