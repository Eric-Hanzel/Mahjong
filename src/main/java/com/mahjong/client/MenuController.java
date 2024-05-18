package com.mahjong.client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Button gameStartButton;
    @FXML
    private Button gameRulesButton;
    @FXML
    private Button exitGameButton;


    @FXML
    private void handleGameStart() throws IOException {
//        clientCommunicateThread.start();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameRoomWait.fxml"));
        Scene waitingScene = new Scene(loader.load(),600,400);

        // 获取当前窗口
        Stage stage = (Stage) gameStartButton.getScene().getWindow();
        stage.setScene(waitingScene);

        WaitRoomController controller = loader.getController();
        controller.initializeNet(stage);

    }

    @FXML
    private void handleGameRules() throws IOException {
        // TODO: 显示游戏规则
        // 创建游戏主页面的场景和布局
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameRule.fxml"));
        Scene gameRuleScene = new Scene(loader.load(),600,400);

        // 获取当前窗口
        Stage stage = (Stage) gameRulesButton.getScene().getWindow();
        stage.setScene(gameRuleScene);

    }

    @FXML
    private void handleExitGame() {
        // 退出程序
        Stage stage = (Stage) exitGameButton.getScene().getWindow();
        stage.close();
    }

}

