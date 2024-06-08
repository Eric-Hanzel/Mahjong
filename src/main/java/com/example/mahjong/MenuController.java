package com.example.mahjong;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button createRoomButton; // Button to create a new game room
    @FXML
    private Button joinRoomButton; // Button to join an existing game room
    @FXML
    private Button gameRulesButton; // Button to view the game rules
    @FXML
    private Button exitGameButton; // Button to exit the game


    @FXML
    private void handleCreateRoom() throws IOException {

        // Handle the action of creating a new room
        // Load the FXML file for the game room creation page
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("gameRoom.fxml"));
        Scene gameRuleScene = new Scene(fxmlLoader.load(), 600, 400);

        // Get the current stage and set the new scene
        Stage primaryStage = (Stage) createRoomButton.getScene().getWindow();
        primaryStage.setScene(gameRuleScene);
    }

    @FXML
    private void handleJoinRoom() {

        // TODO: Implement the action to transition to the join room page
        // This method will need to load and set the scene for joining a room
    }

    @FXML
    private void handleGameRules() throws IOException {

        // Display the game rules
        // Load the FXML file for the game rules page
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("gameRule.fxml"));
        Scene gameRuleScene = new Scene(fxmlLoader.load(), 600, 400);

        // Get the current window and set the new scene
        Stage primaryStage = (Stage) gameRulesButton.getScene().getWindow();
        primaryStage.setScene(gameRuleScene);
    }

    @FXML
    private void handleExitGame() {

        // Exit the application
        // Get the current stage and close it
        Stage stage = (Stage) exitGameButton.getScene().getWindow();
        stage.close();
    }

}
