package com.example.mahjong;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField; // TextField for user to input their username

    @FXML
    private PasswordField passwordField; // PasswordField for user to input their password

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText(); // Get text from username input field
        String password = passwordField.getText(); // Get text from password input field

        if (isValidCredentials(username, password)) {

            // If the credentials are valid, proceed to the game main menu page
            goToGameMenuPage();
        } else {

            // If credentials are invalid, show an alert and clear input fields
            showLoginFailedAlert();
            usernameField.clear();
            passwordField.clear();
        }
    }

    private boolean isValidCredentials(String username, String password) {

        // Placeholder for a more secure authentication mechanism
        return "user".equals(username) && "pass".equals(password);
    }

    private void goToGameMenuPage() throws IOException {

        // Load the FXML for the game menu page and create a new scene
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("menu.fxml"));
        Scene menuScene = new Scene(fxmlLoader.load(), 600, 400);

        // Get the current stage and set the new scene for the game menu
        Stage primaryStage = (Stage) usernameField.getScene().getWindow();
        primaryStage.setScene(menuScene);
    }

    private void showLoginFailedAlert() {

        // Create and display an alert for a failed login attempt
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("login failure");
        alert.setHeaderText(null);
        alert.setContentText("User name or password is incorrect, please enter again!");
        alert.showAndWait();
    }
}
