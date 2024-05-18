package com.mahjong.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GameRoomController {

    public void setNetwork(Socket socket) {
        //收
        new Thread(() -> {
            try {
                InputStream is = socket.getInputStream();
//            ObjectInputStream oos = new ObjectInputStream(is);
                DataInputStream dis = new DataInputStream(is);
                while (true){
                    try {
                        String serverInstruction = dis.readUTF();
                        System.out.println(serverInstruction);

//                      updateScene(serverInstruction);

                    } catch (Exception e) {
                        System.out.println(socket.getRemoteSocketAddress() + "自己下线了");
                        dis.close();
                        socket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        //发
        new Thread(() -> {
            try {
                OutputStream os = socket.getOutputStream();
                // 包装输出流，更高级更好用，玩家操作信息，要发给服务器端
                DataOutputStream operate = new DataOutputStream(os);

                // 后端测试用，后应接入前端接口接收玩家具体操作信息
                Scanner sc = new Scanner(System.in);
                while (true) {
                    System.out.println(socket.getLocalPort() + " 玩家操作： ");
                    String playerOperate = sc.nextLine();
                    if ("exit".equals(playerOperate)){
                        System.out.println("退出游戏");
                        operate.close();
                        socket.close();
                        break;
                    }
                    operate.writeUTF(playerOperate);
                    operate.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void startGame() {
        // 初始化游戏
        // 例如：初始化玩家分数、牌局等
        updateGameBoard();
    }

//    private void processServerMessage(String message) {
//        Platform.runLater(() -> {
//            // 根据服务器消息更新UI
//            if (message.startsWith("UPDATE_SCORE")) {
//                String[] parts = message.split(" ");
//                int player1 = Integer.parseInt(parts[1]);
//                int player2 = Integer.parseInt(parts[2]);
//                player1Score.setText(String.valueOf(player1));
//                player2Score.setText(String.valueOf(player2));
//            } else if (message.startsWith("UPDATE_BOARD")) {
//                // 更新游戏板
//                updateGameBoard();
//            }
//        });
//    }

    private void updateGameBoard() {
        // 根据当前游戏状态更新游戏板
        // 例如：显示牌、玩家动作等
//        gameBoard.getChildren().clear();
//        ImageView tile = new ImageView("path/to/tile/image.png");
//        gameBoard.getChildren().add(tile);
    }
}