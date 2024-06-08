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
    private Button gameStartButton; // Button to start the game
    @FXML
    private Button gameRulesButton; // Button to display the game rules
    @FXML
    private Button exitGameButton; // Button to exit the game

    // Handles the action to start the game
    @FXML
    private void handleGameStart() throws IOException {

        // Load the waiting room scene before the game starts
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameRoomWait.fxml"));
        Scene waitingScene = new Scene(loader.load(),1000,800);

        // Get the current stage (window) using the scene of the start button
        Stage stage = (Stage) gameStartButton.getScene().getWindow();
        stage.setScene(waitingScene); // Set the new scene to the stage

        // Get the controller for the waiting room and initialize network connections
        WaitRoomController controller = loader.getController();
        controller.initializeNet(stage);

    }

    // Handles the action to display the game rules
    @FXML
    private void handleGameRules() throws IOException {

        // Load the scene for displaying game rules
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gameRule.fxml"));
        Scene gameRuleScene = new Scene(loader.load(),1000,800);

        // Get the current stage (window) using the scene of the rules button
        Stage stage = (Stage) gameRulesButton.getScene().getWindow();
        stage.setScene(gameRuleScene); // Set the new scene to the stage

    }

    // Handles the action to exit the game
    @FXML
    private void handleExitGame() {
        // Get the current stage (window) using the scene of the exit button
        Stage stage = (Stage) exitGameButton.getScene().getWindow();
        stage.close(); // Close the window, effectively exiting the application
    }

}
