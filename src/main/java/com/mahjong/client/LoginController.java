package com.mahjong.client;

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

    // Method to handle the login button click event
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText(); // Retrieve the username from the input field
        String password = passwordField.getText(); // Retrieve the password from the input field

        if (isValidCredentials(username, password)) {

            // If the credentials are valid, transition to the game main menu page
            goToGameMenuPage();
        } else {

            // If credentials are invalid, show an alert and clear the input fields
            showLoginFailedAlert();
            usernameField.clear();
            passwordField.clear();
        }
    }

    // Method to check if the provided username and password are valid
    private boolean isValidCredentials(String username, String password) {
        // Placeholder for a more secure authentication mechanism
        return "user".equals(username) && "pass".equals(password);
    }

    // Method to transition to the game main menu
    private void goToGameMenuPage() throws IOException {

        // Load the FXML file for the game menu and create a new scene
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("menu.fxml"));
        Scene menuScene = new Scene(fxmlLoader.load(), 600, 400);

        // Get the current stage (window) and set the new scene
        Stage primaryStage = (Stage) usernameField.getScene().getWindow();
        primaryStage.setScene(menuScene);
    }

    // Method to display an alert for failed login attempts
    private void showLoginFailedAlert() {

        // Create and configure an error alert for incorrect username or password
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Failed");
        alert.setHeaderText(null); // No header text
        alert.setContentText("Incorrect username or password, please try again!");
        alert.showAndWait(); // Show the alert and wait for user response
    }
}
