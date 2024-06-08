package com.mahjong.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class WaitRoomController {

    @FXML
    private Button returnButton; // Button to return to the main menu

    private Socket socket; // Socket for network communication

    // Constructor that initializes the socket connection
    public WaitRoomController() throws IOException {

        // Establish a socket connection to the server at localhost on port 8888
         socket = new Socket("localhost", 8888);
    }

    // Initializes network communication and sets up the waiting room functionality
    public void initializeNet(Stage stage){
        new Thread(() -> {
            try {
                // Continuously read messages from the server
                while (true) {
                    ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                    String msg = input.readUTF(); // Read messages sent from the server
                    if (msg.equals("GameStart")) {
                        // If the message indicates game start, load the game room
                        Platform.runLater(() -> {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("gameRoom.fxml"));
                                Scene gameScene = new Scene(loader.load(),1000,800);
                                stage.setScene(gameScene);// Change the scene to the game room

                                // Pass the socket and input stream to the game room controller
                                GameRoomController controller = loader.getController();
                                controller.setNetwork(socket,input);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break; // Break the loop once the game starts
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Handle action to return to the main menu
    @FXML
    private void handleReturn() throws IOException {

        socket.close(); // Close the socket connection when returning to the main menu

        // Load and display the main menu scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Scene menuScene = new Scene(loader.load(),600,400);

        Stage stage= (Stage) returnButton.getScene().getWindow();
        stage.setScene(menuScene);
    }

}
