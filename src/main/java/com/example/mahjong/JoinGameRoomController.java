package com.example.mahjong;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
public class JoinGameRoomController {

    @FXML
    private TextField inviteCodeField;

    @FXML
    private Button joinRoomButton;

    @FXML
    private Button backButton;

    // 当用户点击加入房间按钮时调用此方法
    @FXML
    private void joinRoom(ActionEvent event) {
        String inviteCode = inviteCodeField.getText();
        // 在这里添加加入房间的逻辑
        System.out.println("加入房间的邀请码: " + inviteCode);
        // 例如：roomManager.joinRoom(inviteCode);
    }

    // 当用户点击返回按钮时调用此方法
    @FXML
    private void goBack(ActionEvent event) {
        // 在这里添加返回上一个界面的逻辑
        System.out.println("返回上一个界面");
        // 例如：mainController.showPreviousView();
    }
}
