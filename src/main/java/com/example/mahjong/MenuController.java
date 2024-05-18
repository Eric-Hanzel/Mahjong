package com.example.mahjong;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button createRoomButton;
    @FXML
    private Button joinRoomButton;
    @FXML
    private Button gameRulesButton;
    @FXML
    private Button exitGameButton;


    @FXML
    private void handleCreateRoom() throws IOException {
        // TODO: 跳转到创建房间的页面
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("gameRoom.fxml"));
        Scene gameRuleScene = new Scene(fxmlLoader.load(), 600, 400);
        // 获取当前窗口并设置新场景
        Stage primaryStage = (Stage) createRoomButton.getScene().getWindow();
        primaryStage.setScene(gameRuleScene);
    }

    @FXML
    private void handleJoinRoom() {
        // TODO: 跳转到加入房间的页面
    }

    @FXML
    private void handleGameRules() throws IOException {
        // TODO: 显示游戏规则
        // 创建游戏主页面的场景和布局
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("gameRule.fxml"));
        Scene gameRuleScene = new Scene(fxmlLoader.load(), 600, 400);
        // 获取当前窗口并设置新场景
        Stage primaryStage = (Stage) gameRulesButton.getScene().getWindow();
        primaryStage.setScene(gameRuleScene);
    }

    @FXML
    private void handleExitGame() {
        // 退出程序
        Stage stage = (Stage) exitGameButton.getScene().getWindow();
        stage.close();
    }

}
