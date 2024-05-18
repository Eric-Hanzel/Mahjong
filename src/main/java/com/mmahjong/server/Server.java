package com.mmahjong.server;

import reLogic.games.Game;
import reLogic.players.Player;
import reLogic.players.Player;
import reLogic.rules.PlayerOperateCheckRule;
import reLogic.rules.VictoryCheckRule;

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
    private static reLogic.games.Game game;
    public static int count = 0;
    public static int skipNumber = 0;
    public static volatile boolean gameStart = false;

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
            ArrayList<reLogic.players.Player> players = new ArrayList<>(game.getPlayerList());
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
                        game = new reLogic.games.Game();
                        game.gameInit(onLineSocket);
                        gameStart = true;
                        logger.info("玩家列表: " + game.getPlayerList());
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
        private VictoryCheckRule victoryCheckRule;


        public HandleClient(Socket client, String clientId){
            victoryCheckRule = VictoryCheckRule.getInstance();
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
                    logger.info(client+"启动成功");
                    Object message = input.readObject();
                    game.printAllPlayersDetails();
                    if (message instanceof String){
                        String operate = (String) message;
                        logger.info(clientId + "操作: " + message);
                        //吃，碰明杠阶段
                        if (game.getCheckState()){
                            System.out.println("进入检查阶段");
                            //收集玩家操作，处理碰杠和跳，约10秒状态自动结束，
                            if (PlayerOperateCheckRule.checkOperateLegal(clientId,operate,game)){
                                //如果是跳，返回接受，并检查人数，满三结束checkstate并告知所有人,顺延玩家

                                Player player = null;
                                for (Player p : game.getPlayerList()){
                                    if (Objects.equals(p.getName(), clientId)){
                                        player = p;
                                        break;
                                    }
                                }
                                if (!game.getGameState()){
                                    sendToAllClient("GameOver");
                                    ArrayList<Player> players = new ArrayList<>(game.getPlayerList());
                                    sendToAllClient(players);
                                    sendToAllClient("Hu "+player.getName()+" "+PlayerOperateCheckRule.checkAllVictoryConditions(game,player,victoryCheckRule,""));
                                }

                                if (operate.equals("Skip") && !player.getSkip()){
                                    skipNumber++;
                                    System.out.println(skipNumber);
                                }
                                if (Objects.equals(game.logicOperate(clientId, operate), "Skip")){
                                    sendToOneClient(client,"skipAccept");
                                    if (skipNumber == 3){
                                        System.out.println("Over");
                                        game.resetAllSkip();
                                        skipNumber = 0;
                                        game.setCheckState(false);
                                        game.changePlayer();
                                        sendToAllClient("checkStateOver");
                                        ArrayList<Player> players = new ArrayList<>(game.getPlayerList());
                                        sendToAllClient(players);
                                    }
                                //有人碰或杠
                                }else {
                                    ArrayList<Player> players = new ArrayList<>(game.getPlayerList());
                                    sendToAllClient(players);
                                }
                                //退出checkState
                            }else {
                                sendToOneClient(client,"illegal");
                            }

                        }else {
                            System.out.println("进入普通阶段");
                            //摸牌和出牌阶段，暗杠
                            if (PlayerOperateCheckRule.checkOperateLegal(clientId,operate,game)){
                                Player player = null;
                                for (Player p : game.getPlayerList()){
                                    if (Objects.equals(p.getName(), clientId)){
                                        player = p;
                                        break;
                                    }
                                }
                                if (!game.getGameState()){

                                    sendToAllClient("GameOver");
                                    ArrayList<Player> players = new ArrayList<>(game.getPlayerList());
                                    sendToAllClient(players);
                                    sendToAllClient("Hu "+player.getName()+" "+PlayerOperateCheckRule.checkAllVictoryConditions(game,player,victoryCheckRule,""));
                                }
                                if (Objects.equals(game.logicOperate(clientId, operate), "Chow")){

                                    String chowTypes = PlayerOperateCheckRule.getCanChowTypes(player,game.getEndPlayerDiscardTile());
                                    sendToOneClient(client,chowTypes);
                                }else {
                                    ArrayList<Player> players = new ArrayList<>(game.getPlayerList());
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
