package com.mmahjong.server;

import logic.gameManagement.Game;
import logic.players.Player;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerReaderThread extends Thread {
//    private static final Logger logger = Logger.getLogger(ServerReaderThread.class.getName());
//
//    private final Socket socket;
//    private final ObjectOutputStream oos;
//    private final ObjectInputStream ois;
//
//    public ServerReaderThread(Socket socket) throws IOException {
//        this.socket = socket;
//        this.oos = new ObjectOutputStream(socket.getOutputStream());
//        this.ois = new ObjectInputStream(socket.getInputStream());
//        logger.info("流已初始化: " + socket.getRemoteSocketAddress());
//    }
//
//    @Override
//    public void run() {
//        try {
//            oos.writeObject(Server.playerList);
//            oos.flush();
//            logger.info("发生信息循环");
//            while (true) {
//                try {
//                    String playerOperate = ois.readUTF();
//                    Game.logicOperate(socket.getRemoteSocketAddress(), playerOperate);
//                    sendPlayerListToAll(Server.playerList);
//                } catch (EOFException e) {
//                    logger.log(Level.WARNING, "客户端流已到达末尾: " + socket.getRemoteSocketAddress(), e);
//                    closeResources();
//                    synchronized (Server.lock) {
//                        Server.onLineSocket.remove(socket);
//                    }
//                    break;
//                } catch (IOException e) {
//                    logger.log(Level.SEVERE, "读取线程发生异常: " + socket.getRemoteSocketAddress(), e);
//                    synchronized (Server.lock) {
//                        Server.onLineSocket.remove(socket);
//                    }
//                    closeResources();
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "读取线程发生异常: " + socket.getRemoteSocketAddress(), e);
//        }
//    }
//
//    private synchronized void closeResources() {
//        try {
//            if (ois != null) {
//                ois.close();
//            }
//            if (oos != null) {
//                oos.close();
//            }
//            if (socket != null && !socket.isClosed()) {
//                socket.close();
//            }
//            logger.info("资源已关闭: " + socket.getRemoteSocketAddress());
//        } catch (IOException e) {
//            logger.log(Level.SEVERE, "关闭资源时发生异常: " + socket.getRemoteSocketAddress(), e);
//        }
//    }
//
//    private void sendPlayerListToAll(ArrayList<Player> playerList) throws IOException {
//        synchronized (Server.lock) {
//            for (Socket s : Server.onLineSocket) {
//                oos.writeObject(playerList);
//                oos.flush();
//            }
//            logger.info("玩家列表已发送给所有客户端。");
//        }
//    }
//
//    private void sendMessageToAll(String msg) throws IOException {
//        synchronized (Server.lock) {
//            for (Socket s : Server.onLineSocket) {
//                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
//                dos.writeUTF(msg);
//                dos.flush();
//            }
//            logger.info("消息已发送给所有客户端: " + msg);
//        }
//    }
}
