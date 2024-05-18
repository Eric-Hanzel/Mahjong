package com.mahjong.client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class GameRulesController extends Client{

    @FXML
    private Button returnButton;

    public GameRulesController() throws IOException {
    }


    @FXML
    private void handleReturn() throws IOException {
        // TODO: 实现返回上一个页面的逻辑
        // 假设你有一个方法来加载并显示上一个页面
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("menu.fxml"));
        Scene menuScene = new Scene(fxmlLoader.load(), 600, 400);
        // 创建游戏主页面的场景和布局

        // 获取当前窗口并设置新场景
        Stage stage= (Stage) returnButton.getScene().getWindow();
        stage.setScene(menuScene);
    }

}