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

    // Main server method to start the server and manage game initialization
    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(8888);
        System.out.println("Server starts, waiting for client connection...");

        // Start a new thread to accept client connections
        new Thread(new AcceptClients()).start();

        // Wait for the game to start
        while (!gameStart) {
            Thread.onSpinWait();
        }
        System.out.println("Game launch");
        logger.info("The player list initialization has been sent to all clients.");

        // Once the game starts, send the player list to all clients
        for (ObjectOutputStream oos : outputs) {
            ArrayList<reLogic.players.Player> players = new ArrayList<>(game.getPlayerList());
            oos.writeObject(players);
            oos.flush();
        }
    }

    // Runnable class to handle client connections
    private static class AcceptClients implements Runnable{
        @Override
        public void run(){
            logger.info("Accept thread start");
            while (true) {
                try {
                    Socket socket = serverSocket.accept(); // Accept a new client connection
                    onLineSocket.add(socket);
                    logger.info(""+socket);
                    outputs.add(new ObjectOutputStream(socket.getOutputStream()));
                    logger.info("The client is connected: " + socket.getRemoteSocketAddress());
                    logger.info("Current online client: " + onLineSocket.size());

                    // Start the game if four clients have connected
                    if (onLineSocket.size() == 4) {
                        for (ObjectOutputStream oos : outputs){
                            oos.writeUTF("GameStart");
                            oos.flush();
                        }
                        game = new reLogic.games.Game();
                        game.gameInit(onLineSocket);
                        gameStart = true;
                        logger.info("Player list: " + game.getPlayerList());
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

    // Thread class to handle individual client connections
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
                    logger.info(client+"Started successfully");
                    Object message = input.readObject(); // Read messages from the client
                    game.printAllPlayersDetails();
                    if (message instanceof String){
                        String operate = (String) message;
                        logger.info(clientId + "Operation: " + message);

                        // Process the operation from the client
                        if (game.getCheckState()){
                            System.out.println("Entering checking phase");

                            // Handle game operations like Pong, Kong, and Skip
                            if (PlayerOperateCheckRule.checkOperateLegal(clientId,operate,game)){

                                Player player = null;
                                for (Player p : game.getPlayerList()){
                                    if (Objects.equals(p.getName(), clientId)){
                                        player = p;
                                        break;
                                    }
                                }

                                // End game and victory conditions check
                                if (!game.getGameState()){
                                    sendToAllClient("GameOver");
                                    ArrayList<Player> players = new ArrayList<>(game.getPlayerList());
                                    sendToAllClient(players);
                                    sendToAllClient("Hu "+player.getName()+" "+PlayerOperateCheckRule.checkAllVictoryConditions(game,player,victoryCheckRule,""));
                                }

                                // Handle skip operation
                                if (operate.equals("Skip") && !player.getSkip()){
                                    skipNumber++;
                                    System.out.println(skipNumber);
                                }

                                // If operation is skip and skips are complete, reset state
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
                                }else {
                                    ArrayList<Player> players = new ArrayList<>(game.getPlayerList());
                                    sendToAllClient(players);
                                }
                            }else {
                                sendToOneClient(client,"illegal");
                            }

                        }else {
                            System.out.println("Entering normal phase");

                            // Normal game phase operations like drawing and discarding tiles
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

        // Method to send a message to a single client
        private void sendToOneClient(Socket socket,String string) {
            try {
                String[] parts = string.split(" ");
                for (Socket socket1:onLineSocket){
                    if (socket == socket1) {
                        ObjectOutputStream oos = outputs.get(onLineSocket.indexOf(socket));
                        if (string.equals("illegal")) {
                            oos.reset();
                            oos.writeObject("!!!!!! Illegal Operation !!!!!!");
                            oos.flush();
                            logger.info("Player illegal operation");
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

        // Method to broadcast a message or data to all connected clients
        private void sendToAllClient(ArrayList<Player> playerList) {
            try {
                for (ObjectOutputStream oos: outputs) {
                    oos.reset();
                    oos.writeObject(playerList);
                    oos.flush();
                }
                logger.info("Player list update has been sent to all clients.");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // Method to broadcast a simple message to all clients
        private void sendToAllClient(String s) {
            try {
                for (ObjectOutputStream oos: outputs) {
                    oos.reset();
                    oos.writeObject(s);
                    oos.flush();
                }
                logger.info("Check state update has been sent to all clients.");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


}
