package com.mahjong.client;

import com.mmahjong.server.Server;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import logic.players.Player;
import logic.rules.VictoryCheckRule;
import logic.tiles.Tile;
import logic.tilesSets.PlayedTileSet;

import java.io.*;
import java.net.Socket;
import java.util.*;

import java.util.logging.Logger;

public class GameRoomController {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    @FXML
    public Label autoHideLabel;

    @FXML
    public HBox player1LockedTiles;
    @FXML
    public VBox player2LockedTiles;
    @FXML
    public HBox player3LockedTiles;
    @FXML
    public VBox player4LockedTiles;

    @FXML
    public ArrayList<Label> playerLabelList;
    @FXML
    public ArrayList<Label> dealerLabelList;
    @FXML
    public ArrayList<Label> turnLabelList;
    @FXML
    public Label playerLabel1;
    @FXML
    public Label dealerLabel1;
    @FXML
    public Label turnLabel1;
    @FXML
    public Label playerLabel2;
    @FXML
    public Label dealerLabel2;
    @FXML
    public Label turnLabel2;
    @FXML
    public Label playerLabel3;
    @FXML
    public Label dealerLabel3;
    @FXML
    public Label turnLabel3;
    @FXML
    public Label playerLabel4;
    @FXML
    public Label dealerLabel4;
    @FXML
    public Label turnLabel4;
    @FXML
    public BorderPane rootPane;
    public Button Hint;
    public BorderPane gameBoard;

    @FXML
    private HBox player1Tiles;
    @FXML
    public VBox player2HandTiles;
    @FXML
    public HBox player3HandTiles;
    @FXML
    public VBox player4HandTiles;

    @FXML
    private VBox chowOptionsVBox;

    @FXML
    public TilePane rightTilePane;
    @FXML
    public TilePane leftTilePane;
    @FXML
    public TilePane topTilePane;
    @FXML
    private TilePane bottomTilePane;

    private Socket socket;
    private Rectangle selectedTile;
    private ArrayList<Tile> handTileList;
    private ArrayList<VBox> otherHandTileBoxList;

    private ArrayList<TilePane> playedPane;
    private ArrayList<PlayedTileSet> playedTileSetList;

    private ArrayList<ArrayList<Tile>> lockedTileSetList;
    private ArrayList<HBox> lockedBox1;
    private ArrayList<VBox> lockedBox2;

    private MediaPlayer mediaPlayer;
    private List<String> musicList = new ArrayList<>();
    private int currentSongIndex = 0;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean gameOver = false;
    private ArrayList<Player> endPlayerList;
    private String name;

    // Image cache for preloading images
    private Map<String, Image> imageCache = new HashMap<>();
    @FXML
    public void initialize() {
        // Initialize lists for hand tiles, played tiles, locked tiles, and other players' hand tiles
        handTileList = new ArrayList<>();
        playedTileSetList = new ArrayList<>();
        lockedTileSetList = new ArrayList<>();
        otherHandTileBoxList = new ArrayList<>();

        otherHandTileBoxList.add(player2HandTiles);
        otherHandTileBoxList.add(player4HandTiles);

        playerLabelList = new ArrayList<>();
        dealerLabelList = new ArrayList<>();
        turnLabelList = new ArrayList<>();
        chowOptionsVBox.setVisible(false);

        lockedBox1 = new ArrayList<>();
        lockedBox2 = new ArrayList<>();
        lockedBox1.add(player1LockedTiles);
        lockedBox1.add(player3LockedTiles);
        lockedBox2.add(player2LockedTiles);
        lockedBox2.add(player4LockedTiles);

        playerLabelList.add(playerLabel1);
        playerLabelList.add(playerLabel2);
        playerLabelList.add(playerLabel3);
        playerLabelList.add(playerLabel4);

        dealerLabelList.add(dealerLabel1);
        dealerLabelList.add(dealerLabel2);
        dealerLabelList.add(dealerLabel3);
        dealerLabelList.add(dealerLabel4);

        turnLabelList.add(turnLabel1);
        turnLabelList.add(turnLabel2);
        turnLabelList.add(turnLabel3);
        turnLabelList.add(turnLabel4);

        playedPane = new ArrayList<>();
        playedPane.add(bottomTilePane);
        playedPane.add(rightTilePane);
        playedPane.add(topTilePane);
        playedPane.add(leftTilePane);

        name = "";

        // Preload all possible images into the cache
        preloadImages();
        setBackground();
//        loadMusic();
    }
    private void setBackground(){
        Image backgroundImage = new Image(getClass().getResource("/images/backGround.png").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, false, true)); // 调整BackgroundSize的参数以覆盖整个VBox
        rootPane.setBackground(new Background(background));
    }

    // Preload all possible images into the cache
    private void preloadImages() {
        String[] tileTypes = {
                "1Bamboo", "2Bamboo", "3Bamboo", "4Bamboo", "5Bamboo", "6Bamboo", "7Bamboo", "8Bamboo", "9Bamboo",
                "1Character", "2Character", "3Character", "4Character", "5Character", "6Character", "7Character", "8Character", "9Character",
                "1Dot", "2Dot", "3Dot", "4Dot", "5Dot", "6Dot", "7Dot", "8Dot", "9Dot",
                "East", "South", "West", "North", "Red", "Green", "White", "tileBack"
        };
        for (String tileType : tileTypes) {
            String imagePath = "/tiles/" + tileType + ".png";
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            imageCache.put(tileType, image);
        }
    }

    // Use cached images to set tile views
    private Rectangle setTileImageView(Rectangle tile1, Tile tile, int width, int height, int direction) {
        tile1.setWidth(width);
        tile1.setHeight(height);
        tile1.setArcWidth(10);
        tile1.setArcHeight(10);

        String tileType = tile.toString();
        Image tileImage = imageCache.get(tileType);
        if (tileImage == null) {
            tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
            imageCache.put(tileType, tileImage);
        }

        ImageView tileImageView = new ImageView(tileImage);
        tileImageView.setFitWidth(width);
        tileImageView.setFitHeight(height);
        if (direction == 1) {
            tileImageView.setRotate(270);
        } else if (direction == 2) {
            tileImageView.setRotate(180);
        } else if (direction == 3) {
            tileImageView.setRotate(90);
        }
        tile1.setFill(new ImagePattern(tileImageView.snapshot(null, null)));
        return tile1;
    }

    // Handle tile click events
    @FXML
    public void handleTileClick(MouseEvent event) {
        if (selectedTile == event.getSource() && selectedTile.getStroke() == Color.YELLOW) {
            selectedTile.setStroke(null);
        } else {
            if (selectedTile != null) {
                selectedTile.setStroke(null); // Remove highlight from previously selected tile
            }

            selectedTile = (Rectangle) event.getSource();
            selectedTile.setStroke(Color.YELLOW); // Highlight the selected tile
            selectedTile.setStrokeWidth(3);
        }
    }

    // Handle the discard action
    @FXML
    public void handleDiscard() throws IOException {
        // Send message to server to update played tile; then receive message to update the board
        if (selectedTile != null && selectedTile.getStroke() == Color.YELLOW && !Objects.equals(selectedTile.getId(), "")) {
            sendOperation("Discard " + selectedTile.getId());
            selectedTile.setStroke(null); // Remove highlight
            selectedTile = null; // Reset selected tile
        }
    }

    // Handle the get action
    @FXML
    public void handleGet(ActionEvent event) throws IOException {
        sendOperation("Get");
    }

    // Handle the chow action
    @FXML
    public void handleChow(ActionEvent event) throws IOException {
        sendOperation("Chow");
    }

    // Handle the skip action
    @FXML
    public void handleSkip(ActionEvent event) throws IOException {
        sendOperation("Skip");
    }

    // Handle the pong action
    @FXML
    public void handlePong(ActionEvent event) throws IOException {
        sendOperation("Pong");
    }

    // Handle the kong action
    @FXML
    public void handleKong(ActionEvent event) throws IOException {
        sendOperation("Kong");
    }

    // Handle the Hu (win) action
    @FXML
    public void handleHu(ActionEvent event) throws IOException {
        sendOperation("Hu");
    }

    // Send operation to the server
    private void sendOperation(String operation) throws IOException {
        output.reset();
        output.writeObject(operation);
        output.flush();
    }

    // Set network connection
    public void setNetwork(Socket socket, ObjectInputStream input, ObjectOutputStream output) throws IOException {
        name = socket.getLocalAddress().toString() + ":" + socket.getLocalPort();
        this.socket = socket;
        this.input = input;
        this.output = output;
        // Receive players and tile sets from the server
        new Thread(() -> {
            try {
                while (true) {
                    try {
                        logger.info("Client ready to read data");
                        Object message = input.readObject();

                        if (message instanceof String) {
                            String msg = (String) message;
                            String[] parts = msg.split(" ");

                            if (Objects.equals(parts[0], "chowTypes")) {
                                Platform.runLater(() -> showChowChoice(msg));
                            } else if (Objects.equals(parts[0], "Hu")) {
                                Platform.runLater(() -> {
                                    try {
                                        gameOver(msg, endPlayerList);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            } else if (Objects.equals(parts[0], "GameOver")) {
                                gameOver = true;
                            } else if (Objects.equals(parts[0], "Disconnect")) {
                                socket.close();
                            } else {
                                Platform.runLater(() -> showMsg(msg));
                            }
                        } else {
                            ArrayList<Player> playerList = (ArrayList<Player>) message;
                            if (gameOver) {
                                endPlayerList = new ArrayList<>(playerList);
                            }
                            for (Player player : playerList) {
                                logger.info("" + player.getHandTileSet().getTileSets());
                            }
                            Platform.runLater(() -> updateGameBoard(playerList));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(socket.getRemoteSocketAddress() + " server disconnected");
                        input.close();
                        output.close();
                        socket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // End the game and handle the end game logic
    private void gameOver(String msg, ArrayList<Player> endPlayerList) throws IOException {
        String[] parts = msg.split(" ");
        String victoryPlayerName = parts[1];
        ArrayList<ArrayList<Tile>> victoryTiles = null;
        for (Player player : endPlayerList) {
            if (Objects.equals(player.getName(), victoryPlayerName)) {
                victoryTiles = VictoryCheckRule.joinHandLockedTile(player.getHandTileSet().getTileSets(), player.getLockedTileSet().getTileSets());
            }
        }

        int index = 0;
        String[] names = {"East", "South", "West", "North"};
        for (Player player : endPlayerList) {
            if (Objects.equals(player.getName(), parts[1])) {
                index = endPlayerList.indexOf(player);
            }
        }

        parts[1] = names[index];

        String newMsg = String.join(" ", parts);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("overRoom.fxml"));
        Scene overScene = new Scene(loader.load(), 1000, 1000);

        // Get the current window
        Stage stage = (Stage) autoHideLabel.getScene().getWindow();
        stage.setScene(overScene);

        OverRoomController controller = loader.getController();
        controller.initialize(newMsg, victoryTiles, endPlayerList, this.name);

        sendOperation("Disconnect");
        // Close the socket
        input.close();
        output.close();
        socket.close();
    }

    // Show chow choice options
    private void showChowChoice(String msg) {
        chowOptionsVBox.getChildren().clear(); // Clear previous options

        String[] parts = msg.split(" ");
        if (parts.length <= 2) {
            chowOptionsVBox.setVisible(false);
            return;
        }

        String tile = parts[1];
        char firstChar = tile.charAt(0);
        int magnitude = Character.getNumericValue(firstChar);

        for (int i = 2; i < parts.length; i++) {
            ArrayList<String> chowTiles = new ArrayList<>();
            String tile1 = "";
            String tile2 = "";
            if (Objects.equals(parts[i], "1")) {
                tile1 = (magnitude - 2) + tile.substring(1);
                tile2 = (magnitude - 1) + tile.substring(1);
            } else if (Objects.equals(parts[i], "2")) {
                tile1 = (magnitude - 1) + tile.substring(1);
                tile2 = (magnitude + 1) + tile.substring(1);
            } else if (Objects.equals(parts[i], "3")) {
                tile1 = (magnitude + 1) + tile.substring(1);
                tile2 = (magnitude + 2) + tile.substring(1);
            }

            chowTiles.add(tile1);
            chowTiles.add(tile2);

            VBox optionBox = new VBox();
            optionBox.setSpacing(10);

            HBox tilesBox = new HBox();
            tilesBox.setSpacing(5);

            for (String tiles : chowTiles) {
                Rectangle tileRect = new Rectangle(30, 45);
                tileRect.setArcWidth(10);
                tileRect.setArcHeight(10);
                Image tileImage = imageCache.get(tiles);
                if (tileImage == null) {
                    tileImage = new Image(getClass().getResource("/tiles/" + tiles + ".png").toExternalForm());
                    imageCache.put(tiles, tileImage);
                }
                tileRect.setFill(new ImagePattern(tileImage));
                tilesBox.getChildren().add(tileRect);
            }

            Button selectButton = new Button("Select");
            int finalI = i;
            selectButton.setOnAction(e -> {
                try {
                    sendOperation("Chow " + parts[finalI]);
                    chowOptionsVBox.setVisible(false); // Hide options after selection
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            optionBox.getChildren().addAll(tilesBox, selectButton);
            chowOptionsVBox.getChildren().add(optionBox);
        }

        chowOptionsVBox.setVisible(true);
    }

    // Show a message on the screen
    private void showMsg(String msg) {
        autoHideLabel.setText(msg);
        autoHideLabel.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(4),
                event -> autoHideLabel.setVisible(false)
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }

    // Update the game board
    private void updateGameBoard(ArrayList<Player> playerList) {
        updatePlayerName(playerList);
        updateDealerState(playerList);
        updateTurnState(playerList);
        updateOthersHandTileDisplay(playerList);
        updatePlayer1HandTileDisplay(playerList);
        updatePlayedTileDisplay(playerList);
        updateLockedTile(playerList);
    }

    // Find the index of the player in the player list
    private int findIndexOfPlayer(String name, ArrayList<Player> playerList) {
        for (Player player : playerList) {
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                return playerList.indexOf(player);
            }
        }
        return -1;
    }

    // Update player names
    private void updatePlayerName(ArrayList<Player> playerList) {
        String playerName = socket.getLocalAddress().toString() + ":" + socket.getLocalPort();
        int index = findIndexOfPlayer(playerName, playerList);

        String[] names = {"East", "South", "West", "North"};

        int i = 0;
        while (i != 4) {
            playerLabelList.get(i).setText(names[index]);
            i++;
            index = (index + 1) % playerList.size();
        }
    }

    // Update dealer state
    private void updateDealerState(ArrayList<Player> playerList) {
        String playerName = socket.getLocalAddress().toString() + ":" + socket.getLocalPort();
        int index = findIndexOfPlayer(playerName, playerList);

        int i = 0;
        while (i != 4) {
            if (playerList.get(index).getDealerState()) {
                dealerLabelList.get(i).setText("Dealer");
            }
            i++;
            index = (index + 1) % playerList.size();
        }
    }

    // Update turn state
    private void updateTurnState(ArrayList<Player> playerList) {
        String playerName = socket.getLocalAddress().toString() + ":" + socket.getLocalPort();
        int index = findIndexOfPlayer(playerName, playerList);

        int i = 0;
        while (i != 4) {
            if (playerList.get(index).getTurnState()) {
                turnLabelList.get(i).setText("Acting");
            } else {
                turnLabelList.get(i).setText("Waiting");
            }
            i++;
            index = (index + 1) % playerList.size();
        }
    }

    // Update other players' hand tiles display
    private void updateOthersHandTileDisplay(ArrayList<Player> playerList) {
        String playerName = socket.getLocalAddress().toString() + ":" + socket.getLocalPort();
        int index = findIndexOfPlayer(playerName, playerList);
        // Horizontal
        int m = (index + 1) % playerList.size();
        m = (m + 1) % playerList.size();

        int handTileNumbers = playerList.get(m).getHandTileSet().getTileNumber();
        player3HandTiles.getChildren().clear();
        for (int j = 0; j < handTileNumbers; j++) {
            Rectangle tile1 = new Rectangle();
            tile1 = setHideTileImage(tile1, 30, 45, 2);
            player3HandTiles.getChildren().add(tile1);
        }

        // Vertical
        int i = 0;
        m = (index + 1) % playerList.size();
        while (i != 2) {
            handTileNumbers = playerList.get(m).getHandTileSet().getTileNumber();
            otherHandTileBoxList.get(i).getChildren().clear();
            for (int j = 0; j < handTileNumbers; j++) {
                Rectangle tile1 = new Rectangle();
                if (i == 0) {
                    tile1 = setHideTileImage(tile1, 45, 30, 1);
                    otherHandTileBoxList.get(i).getChildren().add(tile1);
                } else {
                    tile1 = setHideTileImage(tile1, 45, 30, 3);
                    otherHandTileBoxList.get(i).getChildren().add(tile1);
                }
            }
            i++;
            m = (m + 1) % playerList.size();
            m = (m + 1) % playerList.size();
        }
    }

    // Set hidden tile image
    private Rectangle setHideTileImage(Rectangle tile1, int width, int height, int direction) {
        tile1.setWidth(width);
        tile1.setHeight(height);
        tile1.setArcWidth(10);
        tile1.setArcHeight(10);
        Image tileImage = imageCache.get("tileBack");
        if (tileImage == null) {
            tileImage = new Image(getClass().getResource("/tiles/tileBack.png").toExternalForm());
            imageCache.put("tileBack", tileImage);
        }
        ImageView tileImageView = new ImageView(tileImage);
        tileImageView.setFitWidth(width);
        tileImageView.setFitHeight(height);
        tileImageView.setPreserveRatio(true); // Maintain aspect ratio
        tileImageView.setSmooth(true); // Smooth processing
        if (direction == 1) {
            tileImageView.setRotate(270);
        } else if (direction == 2) {
            tileImageView.setRotate(180);
        } else if (direction == 3) {
            tileImageView.setRotate(90);
        }
        tile1.setFill(new ImagePattern(tileImageView.snapshot(null, null)));
        return tile1;
    }

    // Update player's hand tiles display
    private void updatePlayer1HandTileDisplay(ArrayList<Player> playerList) {
        // Update hand tiles
        handTileList.clear();
        for (Player player : playerList) {
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                for (ArrayList<Tile> tileList : player.getHandTileSet().getTileSets()) {
                    for (Tile tile : tileList) {
                        handTileList.add(tile);
                    }
                }
            }
        }

        player1Tiles.getChildren().clear();
        for (Tile tile : handTileList) {
            Rectangle tile1 = new Rectangle();
            tile1.setId(tile.toString());
            tile1.setWidth(30);
            tile1.setHeight(45);
            tile1.setArcWidth(10);
            tile1.setArcHeight(10);
            tile1.setOnMouseClicked((MouseEvent event) -> {
                handleTileClick(event);
            });
            String tileType = tile.toString();
            Image tileImage = imageCache.get(tileType);
            if (tileImage == null) {
                tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
                imageCache.put(tileType, tileImage);
            }
            tile1.setFill(new ImagePattern(tileImage));
            player1Tiles.getChildren().add(tile1);
        }
    }

    // Update played tiles display
    private void updatePlayedTileDisplay(ArrayList<Player> playerList) {
        // Get played tiles
        String playerName = socket.getLocalAddress().toString() + ":" + socket.getLocalPort();
        int index = findIndexOfPlayer(playerName, playerList);

        playedTileSetList.clear();
        for (Player player : playerList) {
            playedTileSetList.add(player.getPlayedTileSet());
        }
        // Update played tiles
        int i = 0;
        while (i != 4) {
            playedPane.get(i).getChildren().clear();
            for (Tile tile : playedTileSetList.get(index).getTileSets()) {
                Rectangle tile1 = new Rectangle();
                if (i == 0 || i == 2) {
                    tile1 = setTileImageView(tile1, tile, 30, 45, i);
                } else if (i == 1 || i == 3) {
                    tile1 = setTileImageView(tile1, tile, 45, 30, i);
                }
                playedPane.get(i).getChildren().add(tile1);
            }
            i++;
            index = (index + 1) % playerList.size();
        }
    }

    // Update locked tiles display
    private void updateLockedTile(ArrayList<Player> playerList) {
        String playerName = socket.getLocalAddress().toString() + ":" + socket.getLocalPort();
        int index = findIndexOfPlayer(playerName, playerList);

        lockedTileSetList.clear();

        for (Player player : playerList) {
            ArrayList<Tile> lockedTileSet = new ArrayList<>();
            for (ArrayList<Tile> tileSet : player.getLockedTileSet().getTileSets()) {
                lockedTileSet.addAll(tileSet);
            }
            lockedTileSetList.add(lockedTileSet);
        }

        setPlayerLockedTileDisplay(index, playerList);
    }

    // Set player's locked tile display
    private void setPlayerLockedTileDisplay(int index, ArrayList<Player> playerList) {
        // Horizontal
        int i = 0;
        int m = index;
        while (i != 2) {
            lockedBox1.get(i).getChildren().clear();
            for (Tile tile : lockedTileSetList.get(m)) {
                Rectangle tile1 = new Rectangle();
                if (i == 0) {
                    tile1 = setTileImageView(tile1, tile, 30, 45, 0);
                } else {
                    tile1 = setTileImageView(tile1, tile, 30, 45, 2);
                }
                lockedBox1.get(i).getChildren().add(tile1);
            }
            i++;
            m = (m + 1) % playerList.size();
            m = (m + 1) % playerList.size();
        }
        // Vertical
        i = 0;
        m = (index + 1) % playerList.size();
        while (i != 2) {
            lockedBox2.get(i).getChildren().clear();
            for (Tile tile : lockedTileSetList.get(m)) {
                Rectangle tile1 = new Rectangle();
                if (i == 0) {
                    tile1 = setTileImageView(tile1, tile, 45, 30, 1);
                } else {
                    tile1 = setTileImageView(tile1, tile, 45, 30, 3);
                }
                lockedBox2.get(i).getChildren().add(tile1);
            }
            i++;
            m = (m + 1) % playerList.size();
            m = (m + 1) % playerList.size();
        }
    }

    // Handle music play/pause
    @FXML
    private void handleMusic(ActionEvent event) {
        if (mediaPlayer == null) {
            if (!musicList.isEmpty()) {
                playSong(currentSongIndex);
            }
        } else {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        }
    }

    // Handle playing the next song
    @FXML
    private void handleNext(ActionEvent event) {
        playNextSong();
    }

    // Handle playing the previous song
    @FXML
    private void handlePrevious(ActionEvent event) {
        playPreviousSong();
    }

    // Load music from predefined folder
    private void loadMusic() {
        File musicFolder = new File(getClass().getResource("/music/").getPath());
        if (musicFolder.isDirectory()) {
            for (File file : musicFolder.listFiles()) {
                if (file.isFile() && (file.getName().endsWith(".mp3"))) {
                    musicList.add(file.toURI().toString());
                }
            }
        }
        if (!musicList.isEmpty()) {
            playSong(currentSongIndex);
        }
    }

    // Play a song by index
    private void playSong(int index) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Media media = new Media(musicList.get(index));
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(this::playNextSong);
        mediaPlayer.play();
    }

    // Play the next song in the list
    private void playNextSong() {
        currentSongIndex = (currentSongIndex + 1) % musicList.size();
        playSong(currentSongIndex);
    }

    // Play the previous song in the list
    private void playPreviousSong() {
        currentSongIndex = (currentSongIndex - 1 + musicList.size()) % musicList.size();
        playSong(currentSongIndex);
    }

    // Show hint messages
    public void handleHint(ActionEvent event) {
        showMsg("1. One player discards a tile, game come into check state.\n" +
                "other players need to skip or Pong/Kong! to finish check state!\n" +
                "2. You can Chow (if you can) or Get when it's your turn!\n" +
                "3. If you want to discard, you need to finish your necessary operate!\n" +
                "4. After get/chow/pong, you need to discard a tile" +
                "5. After kong, you need to get a tile first then discard!\n" +
                "6. All illegal operations will be rejected!");
    }
}
