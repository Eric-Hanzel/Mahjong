package com.example.mahjong;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
public class JoinGameRoomController {

    @FXML
    private TextField inviteCodeField;  // TextField for entering the room invite code

    @FXML
    private Button joinRoomButton;  // Button to trigger the join room action

    @FXML
    private Button backButton;  // Button to go back to the previous screen

    // Method to handle the join room button click
    @FXML
    private void joinRoom(ActionEvent event) {
        String inviteCode = inviteCodeField.getText();  // Retrieve the invite code from the TextField

        // Add logic here to join the room using the invite code
        System.out.println("加入房间的邀请码: " + inviteCode);
        // Example: roomManager.joinRoom(inviteCode);
    }

    // Method to handle the back button click
    @FXML
    private void goBack(ActionEvent event) {

        // Add logic here to return to the previous screen
        System.out.println("返回上一个界面");
        // Example: mainController.showPreviousView();
    }
}
