package com.mmahjong.server;

import logic.games.Game;
import logic.players.Player;
import logic.rules.PlayerOperateCheckRule;
import logic.rules.ScoringRule;
import logic.rules.VictoryCheckRule;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Logger;

public class Server {
    public static final Logger logger = Logger.getLogger(Server.class.getName());
    private static ServerSocket serverSocket;
    private static final ArrayList<Socket> onLineSocket = new ArrayList<>();
    private static final ArrayList<ObjectOutputStream> outputs = new ArrayList<>();

    private static logic.games.Game game;

    public static int skipNumber = 0;
    public static volatile boolean gameStart = false;

    public static void main(String[] args) throws IOException {
        // Initialize the server socket to listen on port 8888
        serverSocket = new ServerSocket(8888);
        logger.info("-------Server started, waiting for clients to connect...------");

        // accept client connections
        acceptClients();
    }
    private static void acceptClients(){
        logger.info("------Server connection thread started------");
        while (true) {
            try {
                // Accept a new client connection
                Socket socket = serverSocket.accept();
                synchronized (onLineSocket) {
                    // If the number of connected clients exceeds 4, reject the connection
                    if (onLineSocket.size() >= 4) {
                        logger.info("------Maximum number of client connections reached, rejecting connection------");
                        socket.close();
                        continue;
                    }
                    // Add the new client socket to the list of online sockets
                    onLineSocket.add(socket);
                }
                // Create an ObjectOutputStream for the new client
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                synchronized (outputs) {
                    // Add the ObjectOutputStream to the list of output streams
                    outputs.add(oos);
                }

                logger.info("------Client connected------" + socket.getRemoteSocketAddress());
                logger.info("------Current online clients------" + onLineSocket.size());
                logger.info("------Current online client output streams------" + outputs.size());

                // Start a new thread to handle communication with the new client
                new HandleClient(socket, socket.getRemoteSocketAddress().toString()).start();

                // If 4 clients are connected, start the game
                if (onLineSocket.size() == 4) {
                    synchronized (outputs) {
                        for (ObjectOutputStream oots : outputs) {
                            oots.writeUTF("GameStart");
                            oots.flush();
                        }
                    }
                    game = new Game();
                    game.gameInit(onLineSocket);
                    gameStart = true;
                    logger.info("------Game started------");

                    // Send the initialized player list to all clients
                    synchronized (outputs) {
                        for (ObjectOutputStream ooss : outputs) {
                            ArrayList<logic.players.Player> players = new ArrayList<>(game.getPlayerList());
                            ooss.reset();
                            ooss.writeObject(players);
                            ooss.flush();
                        }
                    }
                    logger.info("------Player list initialized and sent to all clients------");
                    logger.info("------Player list------" + game.getPlayerList());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // Thread class to handle communication with a specific client
    private static class HandleClient extends Thread {
        private volatile boolean running = true;
        private Socket client;
        private String clientId;
        private ObjectInputStream input;
        private VictoryCheckRule victoryCheckRule;
        private ScoringRule scoringRule;

        public HandleClient(Socket client, String clientId) {
            logger.info(client + "------Client started successfully------");
            victoryCheckRule = VictoryCheckRule.getInstance();
            scoringRule = logic.rules.ScoringRule.getInstance();

            this.client = client;
            this.clientId = clientId;
            try {
                // Create an ObjectInputStream for the client
                this.input = new ObjectInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (running) {
                    System.out.println("------Current player information------");
                    if (game != null) {
                        game.printAllPlayersDetails();
                    }
                    logger.info(client + "------Waiting for client message------");

                    // Read the message from the client
                    Object message = input.readObject();

                    if (message instanceof String) {
                        String operate = (String) message;
                        logger.info(clientId + "------Received operation----" + message);

                        // Handle client disconnect
                        if (operate.equals("Disconnect")) {
                            handleDisconnect();
                            break; // Exit loop, end thread
                        }

                        // Handle game operations
                        if (game != null) {
                            if (game.getCheckState()) {
                                logger.info("------Checking phase for Kong/Chow------");
                                checkStateCommunication(operate);
                            } else {
                                logger.info("------Player operation phase------");
                                nonCheckStateCommunication(operate);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Stop the thread
        private void stopThread() {
            running = false;
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Handle client disconnect
        private void handleDisconnect() throws IOException {
            game = null;
            gameStart = false;
            logger.info(clientId + "------Client disconnected------");
            synchronized (onLineSocket) {
                int index = onLineSocket.indexOf(client);
                outputs.get(index).close();
                outputs.remove(index);
                onLineSocket.remove(index);
            }
            logger.info("------Current online clients------" + onLineSocket.size());
            input.close();

            logger.info("------Current online client output streams------" + outputs.size());
            client.close();
            stopThread();
        }

        // Handle communication during the checking phase for Kong/Chow
        private void checkStateCommunication(String operate) {
            if (PlayerOperateCheckRule.checkOperateLegal(clientId, operate, game)) {
                logger.info("------Valid operation------" + operate);
                checkStateLegalResponse(operate);
            } else {
                logger.info("------Invalid operation------" + operate);
                illegalResponse();
            }
        }

        // Handle communication during the normal player operation phase
        private void nonCheckStateCommunication(String operate) {
            if (PlayerOperateCheckRule.checkOperateLegal(clientId, operate, game)) {
                logger.info("------Valid operation------" + operate);
                nonCheckStateLegalResponse(operate);
            } else {
                logger.info("------Invalid operation------" + operate);
                illegalResponse();
            }
        }

        // Handle valid operations during the checking phase
        private void checkStateLegalResponse(String operate) {
            Player player = findOperaterPlayer();
            // Increase skip count if the player skips
            if (Objects.equals(operate, "Skip") && !player.getSkip()) {
                skipNumber++;
                logger.info("------Skip count increased------" + skipNumber);
            }

            String operateResult = game.logicOperate(clientId, operate);
            logger.info("------Check phase operation result------" + operateResult);
            skipResponse(player, operateResult);
            gameOverResponse(player);
            ArrayList<Player> players = new ArrayList<>(game.getPlayerList());
            sendToAllClient(players);
        }

        // Handle valid operations during the normal player operation phase
        private void nonCheckStateLegalResponse(String operate) {
            Player player = findOperaterPlayer();
            String operateResult = game.logicOperate(clientId, operate);
            logger.info("------Player operation phase result------" + operateResult);
            chowResponse(player, operate, operateResult);
            gameOverResponse(player);
            ArrayList<Player> players = new ArrayList<>(game.getPlayerList());
            sendToAllClient(players);
        }

        // Send an illegal operation response to the client
        private void illegalResponse() {
            sendToOneClient(client, "illegal");
        }

        // Find the player object associated with the client
        private Player findOperaterPlayer() {
            logger.info("------Finding player------");
            Player player = null;
            for (Player p : game.getPlayerList()) {
                if (Objects.equals(p.getName(), clientId)) {
                    player = p;
                    return player;
                }
            }
            return null;
        }

        // Send a game over response to all clients
        private void gameOverResponse(Player player) {
            if (!game.getGameState()) {
                logger.info("------Game over, updating status to all clients------");
                ArrayList<Player> players = new ArrayList<>(game.getPlayerList());
                scoringRule.getScoring(PlayerOperateCheckRule.checkAllVictoryConditions(game, player, victoryCheckRule, ""), game, players);
                sendToAllClient("GameOver");
                sendToAllClient(players);
                sendToAllClient("Hu " + player.getName() + " " + PlayerOperateCheckRule.checkAllVictoryConditions(game, player, victoryCheckRule, ""));
            }
        }

        // Handle skip responses
        private void skipResponse(Player player, String operateResult) {
            // Reply to the client
            if (Objects.equals(operateResult, "Skip")) {
                sendToOneClient(client, "skipAccept");
                // Reset if three players have skipped
                if (skipNumber == 3) {
                    logger.info("------Check phase ended------Three players skipped");
                    game.resetAllSkip();
                    skipNumber = 0;
                    game.setCheckState(false);
                    game.changePlayer();
                    sendToAllClient("checkStateOver");
                }
            }
        }

        // Handle chow responses
        private void chowResponse(Player player, String operate, String operateResult) {
            if (Objects.equals(operateResult, "Chow")) {
                String chowTypes = PlayerOperateCheckRule.getCanChowTypes(player, game.getEndPlayerDiscardTile());
                logger.info("------Player chow types------" + chowTypes);
                sendToOneClient(client, chowTypes);
            }
        }

        // Send a message to a specific client
        private void sendToOneClient(Socket socket, String string) {
            try {
                logger.info("------Replying to single client------" + socket.getRemoteSocketAddress() + "------" + string);
                String[] parts = string.split(" ");
                for (Socket socket1 : onLineSocket) {
                    if (socket == socket1) {
                        ObjectOutputStream oos = outputs.get(onLineSocket.indexOf(socket));
                        if (string.equals("illegal")) {
                            oos.reset();
                            oos.writeObject("!!!!!!Illegal operation!!!!!!Prohibited!!!!!!");
                            oos.flush();
                            logger.info("Player performed an illegal operation");
                        } else if (string.equals("skipAccept")) {
                            oos.reset();
                            oos.writeObject("skipAccept");
                            oos.flush();
                            logger.info("skipAccept");
                        } else if (string.equals("checkStateOver")) {
                            oos.reset();
                            oos.writeObject("checkStateOver");
                            oos.flush();
                            logger.info("checkStateOver");
                        } else if (Objects.equals(parts[0], "chowTypes")) {
                            oos.reset();
                            oos.writeObject(string);
                            oos.flush();
                            logger.info("Chow types sent to client" + string);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Send a player list to all clients
        private void sendToAllClient(ArrayList<Player> playerList) {
            try {
                synchronized (outputs) {
                    for (ObjectOutputStream oos : outputs) {
                        oos.reset();
                        oos.writeObject(playerList);
                        oos.flush();
                    }
                }
                logger.info("------Updating all clients with player list------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Send a string message to all clients
        private void sendToAllClient(String s) {
            try {
                synchronized (outputs) {
                    for (ObjectOutputStream oos : outputs) {
                        oos.reset();
                        oos.writeObject(s);
                        oos.flush();
                    }
                }
                logger.info("------Updating all clients with game status------" + s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
