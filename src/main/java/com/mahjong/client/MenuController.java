package com.mahjong.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private VBox vbox;
    @FXML
    private Button gameStartButton;
    @FXML
    private Button gameRulesButton;
    @FXML
    private Button exitGameButton;

    // Handle the event of starting the game
    @FXML
    public void initialize() {
        // 设置背景图片
        Image backgroundImage = new Image(getClass().getResource("/images/backGround.png").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true)); // 调整BackgroundSize的参数以覆盖整个VBox
        vbox.setBackground(new Background(background));
    }


    @FXML
    private void handleGameStart() throws IOException {
        // Load the game waiting room interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameRoomWait.fxml"));
        Scene waitingScene = new Scene(loader.load(), 1000, 1000);

        // Get the current window
        Stage stage = (Stage) gameStartButton.getScene().getWindow();
        stage.setScene(waitingScene);

        // Initialize network communication
        WaitRoomController controller = loader.getController();
        controller.initializeNet(stage);
    }

    // Handle the event of showing game rules
    @FXML
    private void handleGameRules() throws IOException {
        // TODO: Display game rules
        // Load the game rules interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameRule.fxml"));
        Scene gameRuleScene = new Scene(loader.load(), 1000, 1000);

        // Get the current window
        Stage stage = (Stage) gameRulesButton.getScene().getWindow();
        stage.setScene(gameRuleScene);
    }

    // Handle the event of exiting the game
    @FXML
    private void handleExitGame() {
        // Close the application
        Stage stage = (Stage) exitGameButton.getScene().getWindow();
        stage.close();
    }
}

