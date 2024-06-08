package com.example.mahjong;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // Load the FXML file for the login view
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("login.fxml"));

        // Create a new scene with the loaded FXML, setting its width to 600 and height to 400
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        // Set the title of the stage (window) to "Mahjong"
        stage.setTitle("Mahjong");

        // Assign the scene to the stage
        stage.setScene(scene);

        // Display the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }// Launch the JavaFX application
}
