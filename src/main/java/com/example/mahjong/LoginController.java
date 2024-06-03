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
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidCredentials(username, password)) {
            // 凭据有效，进入游戏主页面
            goToGameMenuPage();
        } else {
            // 凭据无效，弹出警告并清空输入字段
            showLoginFailedAlert();
            usernameField.clear();
            passwordField.clear();
        }
    }

    private boolean isValidCredentials(String username, String password) {
        // 这里应该有一个更安全的验证机制
        return "user".equals(username) && "pass".equals(password);
    }

    private void goToGameMenuPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("menu.fxml"));
        Scene menuScene = new Scene(fxmlLoader.load(), 600, 400);
        // 创建游戏主页面的场景和布局

        // 获取当前窗口并设置新场景
        Stage primaryStage = (Stage) usernameField.getScene().getWindow();
        primaryStage.setScene(menuScene);
    }

    private void showLoginFailedAlert() {
        // 创建并显示登录失败的警告
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("登录失败");
        alert.setHeaderText(null);
        alert.setContentText("用户名或密码错误，请重新输入！");
        alert.showAndWait();
    }
}
