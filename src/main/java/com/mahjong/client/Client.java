package com.mahjong.client;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class Client extends Application {

    // When a player clicks to join a game, a communication channel is established with the server.
    // There are two threads: one for receiving messages from the server to update the canvas,
    // and another for sending messages to the server based on player actions.
    // When a player clicks a corresponding action, a message is sent to the server.

    @Override
    public void start(Stage stage) throws IOException {

        // Load the main menu FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("menu.fxml"));

        // Create a scene with the loaded FXML, setting the dimensions to 1000x800 pixels
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);

        // Set the title of the window to "Mahjong"
        stage.setTitle("Mahjong");

        // Set the scene for this stage
        stage.setScene(scene);

        // Display the stage
        stage.show();

    }


    public static void main(String[] args) {
        launch();
    }// Launch the application
}