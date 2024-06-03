package com.mmahjong.server;

import logic.gameManagement.Game;
import logic.players.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private static ServerSocket serverSocket;
    private static ArrayList<ObjectOutputStream> outputs = new ArrayList<>();
    public static final ArrayList<Socket> onLineSocket = new ArrayList<>();
    public static int count = 0;
    private static int skipNumber = 0;
    public static volatile boolean gameStart = false;
    public static boolean gameOver = false;

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(8888);
        System.out.println("服务器启动，等待客户端连接...");

        new Thread(new AcceptClients()).start();

        while (!gameStart) {
            Thread.onSpinWait();
        }
        System.out.println("游戏启动");
        logger.info("玩家列表初始化已发送给所有客户端。");
        for (ObjectOutputStream oos : outputs) {
            ArrayList<Player> players = new ArrayList<>(Game.playerList);
            oos.writeObject(players);
            oos.flush();
        }
    }

    private static class AcceptClients implements Runnable{
        @Override
        public void run(){
            logger.info("Accept线程启动");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    onLineSocket.add(socket);
                    logger.info(""+socket);
                    outputs.add(new ObjectOutputStream(socket.getOutputStream()));
                    logger.info("客户端已连接: " + socket.getRemoteSocketAddress());
                    logger.info("当前在线客户端: " + onLineSocket.size());
                    if (onLineSocket.size() == 4) {
                        for (ObjectOutputStream oos : outputs){
                            oos.writeUTF("GameStart");
                            oos.flush();
                        }
                        Game.init(onLineSocket);
                        gameStart = true;
                        logger.info("玩家列表: " + Game.playerList);
                        for (Socket socket1: onLineSocket){
                            new HandleClient(socket1,socket1.getRemoteSocketAddress().toString()).start();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static class HandleClient extends Thread{
        private Socket client;
        private String clientId;
        private ObjectInputStream input;


        public HandleClient(Socket client, String clientId){
            this.client = client;
            this.clientId = clientId;
            try {
                this.input = new ObjectInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            try {
                while (true){
                    //合法非法操作判定，根据turn,非法操作单发。
//                    if (Game.checkState){
//                        Object message = input.readObject();
//
//                    }else {
//                        Object message = input.readObject();
//                        if (message instanceof String){
//                            String operate = (String) message;
//                            logger.info(clientId + " 操作: " + message);
//                            //吃，碰杠阶段
//                            if (Game.checkLegal(clientId,operate)){
//                                Game.logicOperate(clientId, operate);
//                                ArrayList<Player> players = new ArrayList<>(Game.playerList);
//                                sendToAllClient(players);
//                            }else {
//                                sendToOneClient(client);
//                            }
//                        }
//                    }

                    logger.info(client+"启动成功");
                    Object message = input.readObject();
                    if (message instanceof String){
                        String operate = (String) message;
                        logger.info(clientId + "操作: " + message);
                        //吃，碰明杠阶段
                        if (Game.checkState){
                            //收集玩家操作，处理碰杠和跳，约10秒状态自动结束，
                            if (Game.checkLegal(clientId,operate)){
                                //如果是跳，返回接受，并检查人数，满三结束checkstate并告知所有人,顺延玩家
                                Player player = null;
                                for (Player p : Game.playerList){
                                    if (Objects.equals(p.getName(), clientId)){
                                        player = p;
                                        break;
                                    }
                                }
                                if (operate.equals("Skip") && !player.getSkip()){
                                    skipNumber++;
                                    System.out.println(skipNumber);
                                }
                                if (Objects.equals(Game.logicOperate(clientId, operate), "Skip")){
                                    sendToOneClient(client,"skipAccept");
                                    if (skipNumber == 3){
                                        System.out.println("Over");
                                        Game.resetAllSkip();
                                        skipNumber = 0;
                                        Game.checkState = false;
                                        Game.changePlayer();
                                        sendToAllClient("checkStateOver");
                                        ArrayList<Player> players = new ArrayList<>(Game.playerList);
                                        sendToAllClient(players);
                                    }
                                    //有人碰或杠
                                }else {
                                    ArrayList<Player> players = new ArrayList<>(Game.playerList);
                                    sendToAllClient(players);
                                }
                                //退出checkState
                            }else {
                                sendToOneClient(client,"illegal");
                            }

                        }else {
                            //摸牌和出牌阶段，暗杠
                            if (Game.checkLegal(clientId,operate)){
                                if (Objects.equals(Game.logicOperate(clientId, operate), "Chow")){
                                    Player player = null;
                                    for (Player p : Game.playerList){
                                        if (Objects.equals(p.getName(), clientId)){
                                            player = p;
                                            break;
                                        }
                                    }
                                    String chowTypes = player.getHandTile().chowTypes(Game.endDiscardTile);
                                    sendToOneClient(client,chowTypes);
                                }else {
                                    ArrayList<Player> players = new ArrayList<>(Game.playerList);
                                    sendToAllClient(players);
                                }
                            }else {
                                sendToOneClient(client,"illegal");
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //gai'xie
        private void sendToOneClient(Socket socket,String string) {
            try {
                String[] parts = string.split(" ");
                for (Socket socket1:onLineSocket){
                    if (socket == socket1) {
                        ObjectOutputStream oos = outputs.get(onLineSocket.indexOf(socket));
                        if (string.equals("illegal")) {
                            oos.reset();
                            oos.writeObject("!!!!!!非法操作!!!!!!一概禁止!!!!!!");
                            oos.flush();
                            logger.info("玩家非法操作");
                        } else if (string.equals("skipAccept")) {
                            oos.reset();
                            oos.writeObject("skipAccept");
                            oos.flush();
                            logger.info("skipAccept");
                        } else if (string.equals("checkStateOver")){
                            oos.reset();
                            oos.writeObject("checkStateOver");
                            oos.flush();
                            logger.info("checkStateOver");
                        } else if (Objects.equals(parts[0], "chowTypes")){
                            oos.reset();
                            oos.writeObject(string);
                            oos.flush();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void sendToAllClient(ArrayList<Player> playerList) {
            try {
                for (ObjectOutputStream oos: outputs) {
                    oos.reset();
                    oos.writeObject(playerList);
                    oos.flush();
                }
                logger.info("玩家列表更新已发送给所有客户端。");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        private void sendToAllClient(String s) {
            try {
                for (ObjectOutputStream oos: outputs) {
                    oos.reset();
                    oos.writeObject(s);
                    oos.flush();
                }
                logger.info("checkstate更新已发送给所有客户端。");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


}

