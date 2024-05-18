package com.mahjong.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class WaitRoomController {

    @FXML
    private Button returnButton;


//    public boolean gameStart = false;


    public void initializeNet(Stage stage) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 8888);
//                out = new PrintWriter(socket.getOutputStream(), true);
//                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            ObjectInputStream oos = new ObjectInputStream(is);

                while (true) {
                    InputStream is = socket.getInputStream();
                    DataInputStream dis = new DataInputStream(is);
                    String msg = dis.readUTF();
                    if (msg.equals("GameStart")) {
                        Platform.runLater(() -> {
                            try {
                                // 加载游戏界面
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("gameRoom.fxml"));
                                Scene gameScene = new Scene(loader.load());
                                stage.setScene(gameScene);

                                // 传递网络连接给游戏控制器
                                GameRoomController controller = loader.getController();
                                controller.setNetwork(socket);
                                controller.startGame();
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
    @FXML
    private void handleReturn() throws IOException {

        // 创建游戏主页面的场景和布局
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Scene menuScene = new Scene(loader.load(),600,400);

        // 获取当前窗口并设置新场景
        Stage stage= (Stage) returnButton.getScene().getWindow();
        stage.setScene(menuScene);
    }

}

