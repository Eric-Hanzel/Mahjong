package com.mahjong.client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class GameRulesController extends Client{

    @FXML
    private Button returnButton; // Button to handle returning to the previous screen

    public GameRulesController() throws IOException {
        // Constructor for the controller
    }


    @FXML
    private void handleReturn() throws IOException {
        // Method to handle the action when the 'Return' button is pressed
        // Load the main menu view
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("menu.fxml"));
        Scene menuScene = new Scene(fxmlLoader.load(), 1000, 800); // Create a scene for the main menu with specified dimensions

        // Get the current stage (window) using the button's scene and set the new scene
        Stage stage= (Stage) returnButton.getScene().getWindow();
        stage.setScene(menuScene);
    }

}
