package com.mahjong.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class WaitRoomController {

    public VBox vBox;
    @FXML
    private Button returnButton;

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    @FXML
    public void initialize() {
        Image backgroundImage = new Image(getClass().getResource("/images/backGround.png").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true)); // 调整BackgroundSize的参数以覆盖整个VBox
        vBox.setBackground(new Background(background));
    }

    // Constructor to initialize socket connection to the server
    public WaitRoomController() throws IOException {
        socket = new Socket("localhost", 8888);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        System.out.println("Client connected");
    }

    // Set socket and initialize streams
    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        output = new ObjectOutputStream(this.socket.getOutputStream());
        input = new ObjectInputStream(this.socket.getInputStream());
    }

    // Initialize network communication and handle server messages
    public void initializeNet(Stage stage) {
        new Thread(() -> {
            try {
                while (true) {
                    String msg = input.readUTF();
                    if (msg.equals("GameStart")) {
                        Platform.runLater(() -> {
                            try {
                                // Load the game interface
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("gameRoom.fxml"));
                                Scene gameScene = new Scene(loader.load(), 1000, 1000);
                                stage.setScene(gameScene);
                                // Pass network connection to the game controller
                                GameRoomController controller = loader.getController();
                                controller.setNetwork(socket, input, output);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Handle disconnecting from the server
    @FXML
    private void handleReturn() throws IOException {
        // Send disconnect message to the server
        output.reset();
        output.writeObject("Disconnect");
        output.flush();

        // Close the socket
        output.close();
        socket.close();

        // Load the main menu scene and layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Scene menuScene = new Scene(loader.load(), 1000, 1000);

        // Get the current window and set the new scene
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.setScene(menuScene);
    }
}

