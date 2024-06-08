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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import reLogic.players.Player;
import reLogic.tiles.Tile;
import reLogic.tilesSets.PlayedTileSet;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class GameRoomController {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    // UI elements defined in FXML
    @FXML
    public Label autoHideLabel; // Auto-hiding label for displaying messages
    public VBox player4LockedTiles; // Containers for locked tiles for players 2 and 4
    public HBox player3LockedTiles; // Containers for locked tiles for players 1 and 3
    public VBox player2LockedTiles; // Lists for managing player, dealer, and turn labels dynamically
    public HBox player1LockedTiles;
    public ArrayList<Label> playerLabelList;
    public ArrayList<Label> dealerLabelList;
    public ArrayList<Label> turnLabelList;
    public Label playerLabel1;
    public Label dealerLabel1;
    public Label turnLabel1;
    public Label playerLabel2;
    public Label dealerLabel2;
    public Label turnLabel2;
    public Label playerLabel3;
    public Label dealerLabel3;
    public Label turnLabel3;
    public Label playerLabel4;
    public Label dealerLabel4;
    public Label turnLabel4;
    @FXML
    private VBox chowOptionsVBox; // Container for displaying chow options to the player
    @FXML
    public TilePane rightTilePane; // Containers for displaying tiles
    @FXML
    public TilePane leftTilePane;
    @FXML
    public TilePane topTilePane;
    @FXML
    private HBox player1Tiles;

    @FXML
    private TilePane bottomTilePane;

    private Socket socket; // Socket for network communication
    private Rectangle selectedTile; // Currently selected tile
    private ArrayList<Tile> handTileList; // List of tiles in the player's hand
    private ArrayList<PlayedTileSet> playedTileSetList; // List of tiles that have been played
    private ArrayList<ArrayList<Tile>> lockedTileSetList; // List of locked tiles

    private ArrayList<TilePane> playedPane; // List of tile panes for played tiles
    private ArrayList<HBox> lockedBox1; // HBoxes for locked tiles for horizontal layout players
    private ArrayList<VBox> lockedBox2; // VBoxes for locked tiles for vertical layout players
    private MediaPlayer mediaPlayer; // Media player for playing background music
    private List<String> musicList = new ArrayList<>(); // List of music file URLs
    private int currentSongIndex = 0; // Index of the current song being played
    private ObjectInputStream input; // Input stream for receiving data from the server
    private ObjectOutputStream output; // Output stream for sending data to the server
    private boolean gameOver = false; // Flag to check if the game is over
    private ArrayList<Player> endPlayerList; // List of players at the end of the game

    @FXML
    public void initialize() {

        // Initial setup for various lists and UI components
        handTileList = new ArrayList<Tile>();
        playedTileSetList = new ArrayList<>();
        lockedTileSetList = new ArrayList<>();
        playerLabelList = new ArrayList<>();
        dealerLabelList = new ArrayList<>();
        turnLabelList = new ArrayList<>();
        chowOptionsVBox.setVisible(false);
        lockedBox1 = new ArrayList<HBox>();
        lockedBox2 = new ArrayList<VBox>();
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

        // Load background music if available
        loadMusic();
    }

    // Handle tile selection with a mouse click event
    @FXML
    public void handleTileClick(MouseEvent event) {
        if (selectedTile == event.getSource() && selectedTile.getStroke() == Color.YELLOW){

            // If the clicked tile is already selected, deselect it
            selectedTile.setStroke(null);
        }else {

            // If there is a previously selected tile, deselect it
            if (selectedTile != null) {
                selectedTile.setStroke(null); // 取消之前选中方块的高亮
            }

            // Set the clicked tile as the selected tile and highlight it
            selectedTile = (Rectangle) event.getSource();
            selectedTile.setStroke(Color.YELLOW); // 设置选中方块的高亮颜色
            selectedTile.setStrokeWidth(3);
        }
    }

    // Handle the discard action
    @FXML
    public void handleDiscard() throws IOException {

        // If a tile is selected and highlighted, proceed to discard it
        if (selectedTile != null && selectedTile.getStroke() == Color.YELLOW && !Objects.equals(selectedTile.getId(), "")) {
            sendOperation("Discard " + selectedTile.getId()); // Send the discard operation to the server
            selectedTile.setStroke(null); // Remove highlighting
            selectedTile = null; // Clear the selection

        }
    }

    // Handle the action to draw a tile
    @FXML
    public void handleGet(ActionEvent event) throws IOException {
        sendOperation("Get"); // Send the 'Get' operation to the server
    }

    // Handle the action to form a sequence (Chow)
    @FXML
    public void handleChow(ActionEvent event) throws IOException {
        sendOperation("Chow"); // Send the 'Chow' operation to the server
    }

    // Handle the action to skip the turn
    @FXML
    public void handleSkip(ActionEvent event) throws IOException {
        sendOperation("Skip"); // Send the 'Skip' operation to the server
    }

    // Handle the action to form a triplet (Pong)
    @FXML
    public void handlePong(ActionEvent event) throws IOException {
        sendOperation("Pong"); // Send the 'Pong' operation to the server
    }

    // Handle the action to form a quartet (Kong)
    @FXML
    public void handleKong(ActionEvent event) throws IOException {
        sendOperation("Kong");
    }

    // Handle the action to declare a win (Hu)
    @FXML
    public void handleHu(ActionEvent event) throws IOException {
        sendOperation("Hu"); // Send the 'Hu' operation to the server
    }

    // Send a specified operation to the server
    private void sendOperation(String operation) throws IOException {
        output.reset();
        output.writeObject(operation);
        output.flush();
    }

    // Establish network communication and set up listening for incoming messages
    public void setNetwork(Socket socket,ObjectInputStream input) throws IOException {
        this.socket = socket; // Store the socket for later use
        this.input = input; // Store the input stream for reading data from the server
        output = new ObjectOutputStream(socket.getOutputStream()); // Initialize the output stream for sending data to the server

        // Start a new thread to handle incoming messages continuously
        new Thread(() -> {
            try {
                while (true) { // Keep listening as long as the connection is active
                    try {
                        logger.info("The client is ready to read data"); // Log that the client is ready to read data
                        Object message = input.readObject(); // Read the next object from the input stream
                        if (message instanceof String){
                            String msg = (String) message;
                            String[] parts = msg.split(" ");
                            if (Objects.equals(parts[0], "chowTypes")){
                                Platform.runLater(() -> showChowChoice(msg)); // Process chow options on the UI thread
                            }else if (Objects.equals(parts[0], "Hu")){
                                Platform.runLater(() -> {
                                    try {
                                        gameOver(msg,endPlayerList); // Process the game over scenario on the UI thread
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                            }else if (Objects.equals(parts[0], "GameOver")){
                                gameOver = true; // Set the game over flag
                            } else {
                                Platform.runLater(() -> showMsg(msg)); // Show other messages on the UI thread
                            }
                        }else {
                            ArrayList<Player> playerList = (ArrayList<Player>) message;
                            if (gameOver){
                               endPlayerList = new ArrayList<>(playerList); // Store the list of players at game end
                            }
                            for (Player player: playerList){
                                logger.info(""+player.getHandTileSet().getTileSets()); // Log the current hand tile sets
                            }
                            Platform.runLater(() -> updateGameBoard(playerList)); // Update the game board on the UI thread
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(socket.getRemoteSocketAddress() + "Self-logged off"); // Log that the socket has disconnected
                        input.close(); // Close the input stream
                        socket.close(); // Close the socket
                        break; // Exit the loop and end the thread
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Conclude the game by processing victory conditions and updating the UI
    private void gameOver(String msg,ArrayList<Player> endPlayerList) throws IOException {
        String[] parts = msg.split(" "); // Split the message to analyze it
        String victoryPlayerName = parts[1]; // Extract the name of the player who won
        ArrayList<ArrayList<Tile>> victoryTiles = null; // Initialize a structure to hold the winning tiles
        ArrayList<ArrayList<Tile>> copyLockedTile; // Temporary storage for locked tiles
        for (Player player: endPlayerList){
            if (Objects.equals(player.getName(), victoryPlayerName)){
                victoryTiles = new ArrayList<>(player.getHandTileSet().getTileSets()); // Copy the hand tiles of the winning player
                copyLockedTile = new ArrayList<>(player.getLockedTileSet().getTileSets()); // Copy the locked tiles of the winning player
                int i = 0;
                while (i<4){
                    if (i == 0){
                        for (ArrayList<Tile> tileArrayList: copyLockedTile){
                            for (Tile tile: tileArrayList){
                                if (Objects.equals(tile.getType(), "Character")){
                                    victoryTiles.get(0).add(tile); // Add character tiles to the victory tiles
                                }
                            }
                        }
                    }else if (i==1){
                        for (ArrayList<Tile> tileArrayList: copyLockedTile){
                            for (Tile tile: tileArrayList){
                                if (Objects.equals(tile.getType(), "Bamboo")){
                                    victoryTiles.get(1).add(tile); // Add bamboo tiles to the victory tiles
                                }
                            }
                        }
                    }else if (i==2){
                        for (ArrayList<Tile> tileArrayList: copyLockedTile){
                            for (Tile tile: tileArrayList){
                                if (Objects.equals(tile.getType(), "Dot")){
                                    victoryTiles.get(2).add(tile); // Add dot tiles to the victory tiles
                                }
                            }
                        }
                    }else if (i==3){
                        for (ArrayList<Tile> tileArrayList: copyLockedTile){
                            for (Tile tile: tileArrayList){
                                if (!Objects.equals(tile.getType(), "Character" )&&!Objects.equals(tile.getType(), "Bamboo" )&&!Objects.equals(tile.getType(), "Dot" )){
                                    victoryTiles.get(3).add(tile); // Add other types of tiles to the victory tiles
                                }
                            }
                        }
                    }
                    i++;
                }
            }
        }

        int index = 0;
        String[] name = {"Hanze Dong","Jingzhe Xu","Tianyi Ma","Jiawei Xing"}; // Localized names for display
        for (Player player: endPlayerList){
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = endPlayerList.indexOf(player); // Find the index of the local player
            }
        }

        parts[1] = name[index]; // Replace the winning player's name with the localized name

        String newMsg = String.join(" ",parts); // Construct the new message for display

        // Transition to the game over scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("overRoom.fxml"));
        Scene overScene = new Scene(loader.load(),1000,800);

        // Get the current window
        Stage stage = (Stage) autoHideLabel.getScene().getWindow();
        stage.setScene(overScene); // Set the new scene for game over

        // Get the controller for the game over room and initialize it
        OverRoomController controller = loader.getController();
        controller.initialize(newMsg,victoryTiles);
    }

    // Display chow options based on the server's message
    private void showChowChoice(String msg) {
        chowOptionsVBox.getChildren().clear(); // Clear previous options
        String[] parts = msg.split(" ");
        if (parts.length <= 2) {
            chowOptionsVBox.setVisible(false); // Hide if no options are available
            return;
        }
        String tile = parts[1]; // The tile involved in the chow
        char firstChar = tile.charAt(0);
        int magnitude = Character.getNumericValue(firstChar); // Numerical value of the tile

        // Iterate over possible chow combinations provided by the server
        for (int i = 2; i < parts.length; i++) {
            ArrayList<String> chowTiles = new ArrayList<>();
            String tile1 = "";
            String tile2 = "";

            // Calculate the specific tiles needed to form a Chow
            if (Objects.equals(parts[i], "1")){
                tile1 = (magnitude - 2) + tile.substring(1);
                tile2 = (magnitude - 1) + tile.substring(1);
            }else if (Objects.equals(parts[i], "2")){
                tile1 = (magnitude - 1) + tile.substring(1);
                tile2 = (magnitude + 1) + tile.substring(1);
            }else if (Objects.equals(parts[i], "3")){
                tile1 = (magnitude + 1) + tile.substring(1);
                tile2 = (magnitude + 2) + tile.substring(1);
            }
            chowTiles.add(tile1);
            chowTiles.add(tile2);

            // Create visual representation for chow options
            VBox optionBox = new VBox();
            optionBox.setSpacing(10);

            HBox tilesBox = new HBox();
            tilesBox.setSpacing(5);

            for (String tiles : chowTiles) {
                Rectangle tileRect = new Rectangle(30, 45);
                tileRect.setArcWidth(10);
                tileRect.setArcHeight(10);
                Image tileImage = new Image(getClass().getResource("/tiles/" + tiles + ".png").toExternalForm());
                tileRect.setFill(new ImagePattern(tileImage));
                tilesBox.getChildren().add(tileRect);
            }

            Button selectButton = new Button("Select");
            int finalI = i;
            selectButton.setOnAction(e -> {
                try {
                    sendOperation("Chow " + parts[finalI]);
                    chowOptionsVBox.setVisible(false); // Collapse options after selection
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            optionBox.getChildren().addAll(tilesBox, selectButton);
            chowOptionsVBox.getChildren().add(optionBox);
        }

        chowOptionsVBox.setVisible(true);
    }

    // Display messages temporarily on the UI
    private void showMsg(String msg) {
        autoHideLabel.setText(msg); // Set text of the auto-hide label
        autoHideLabel.setVisible(true); // Show the label

        // Set up a timeline to hide the label after 1 second
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1),
                event -> autoHideLabel.setVisible(false)
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }

    // Update the game board based on the current game state
    private void updateGameBoard(ArrayList<Player> playerList) {
        updatePlayerName(playerList); // Update the names of the players on the board
        updateDealerState(playerList); // Update the dealer status for each player
        updateTurnState(playerList); // Update the turn status, showing whose turn it is
        for (Player player: playerList){
            System.out.println(player.getHandTileSet().getTileSets()); // Debug print of hand tiles
        }
        updateHandTileDisplay(playerList); // Update the visual display of players' hand tiles
        updatePlayedTileDisplay(playerList); // Update the visual display of tiles that have been played
        updateLockedTile(playerList); // Update the visual display of locked tiles
    }

    // Updates the visual display of played tiles
    private void updatePlayedTileDisplay(ArrayList<Player> playerList) {
        int index = 0; // Index to track the local player's position relative to the board
        playedTileSetList.clear(); // Clear previous list of played tiles

        // Iterate over players to update played tiles list and find the local player's index
        for (Player player: playerList){
            playedTileSetList.add(player.getPlayedTileSet());
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = playerList.indexOf(player);
            }
        }

        // Update the display for each side of the board
        int i = 0;
        while (i != 4){
            playedPane.get(i).getChildren().clear(); // Clear the current pane

            // Add tiles to the respective pane according to the current player's relative position
            for (Tile tile : playedTileSetList.get(index).getTileSets()){
                Rectangle tile1 = new Rectangle();
                tile1.setWidth(30);
                tile1.setHeight(45);
                tile1.setArcWidth(10);
                tile1.setArcHeight(10);
                String tileType = tile.toString();
                Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
                tile1.setFill(new ImagePattern(tileImage));
                Rotate rotate = new Rotate();

                // Rotate the tile based on its pane position for correct orientation
                if (i==1){
                    rotate.setAngle(270);
                    rotate.setPivotX(tile1.getWidth() / 2);
                    rotate.setPivotY(tile1.getHeight() / 2);
                    tile1.getTransforms().add(rotate);
                }else if (i==3){
                    rotate.setAngle(90);
                    rotate.setPivotX(tile1.getWidth() / 2);
                    rotate.setPivotY(tile1.getHeight() / 2);
                    tile1.getTransforms().add(rotate);
                }
                playedPane.get(i).getChildren().add(tile1);
            }
            i++;
            index = (index+1)%playerList.size(); // Circular increment to handle board sides
        }
    }

    // Update the display of the hand tiles for the local player
    private void updateHandTileDisplay(ArrayList<Player> playerList) {
        handTileList.clear(); // Clear previous hand tiles

        // Find the local player and update their hand tiles based on the current game state
        for (Player player : playerList) {
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString()+":"+socket.getLocalPort())) {
                for (ArrayList<Tile> tileList : player.getHandTileSet().getTileSets()) {
                    for (Tile tile:tileList){
                        handTileList.add(tile); // Add each tile to the hand tile list
                    }
                }
            }
        }

        // Update the visual representation of tiles in the player's hand
        player1Tiles.getChildren().clear(); // Clear existing tiles
        for (Tile tile: handTileList){
            Rectangle tile1 = new Rectangle();
            tile1.setId(tile.toString());
            tile1.setWidth(30);
            tile1.setHeight(45);
            tile1.setArcWidth(10);
            tile1.setArcHeight(10);
            tile1.setOnMouseClicked((MouseEvent event) -> {
                handleTileClick(event); // Set a mouse click event handler
            });
            String tileType = tile.toString();
            Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
            tile1.setFill(new ImagePattern(tileImage));
            player1Tiles.getChildren().add(tile1); // Add the tile to the display
        }

    }

    // This method updates the display of locked tiles for all players
    private void updateLockedTile(ArrayList<Player> playerList) {
        int index = 0;
        lockedTileSetList.clear(); // Clear existing locked tiles

        // Populate the lockedTileSetList with locked tiles from all players
        for (Player player: playerList){
            ArrayList<Tile> lockedTileSet = new ArrayList<>();
            for (ArrayList<Tile> tileSet : player.getLockedTileSet().getTileSets()){
                lockedTileSet.addAll(tileSet);
            }
            lockedTileSetList.add(lockedTileSet);
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = playerList.indexOf(player); // Get index for local player
            }
        }

        // Update locked tiles display based on player's relative positions
        int i = 0;
        int m = index;
        while (i != 2){ // Update horizontal locked tiles
            lockedBox1.get(i).getChildren().clear(); // Clear the current tiles
            for (Tile tile : lockedTileSetList.get(m)){
                Rectangle tile1 = new Rectangle();
                tile1.setWidth(30);
                tile1.setHeight(45);
                tile1.setArcWidth(10);
                tile1.setArcHeight(10);
                String tileType = tile.toString();
                Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
                tile1.setFill(new ImagePattern(tileImage));
                lockedBox1.get(i).getChildren().add(tile1);
            }
            i++;
            m = (m+1)%playerList.size();
            m = (m+1)%playerList.size();
        }


        i = 0;
        m = (index+1)%playerList.size();
        while (i != 2){ // Update vertical locked tiles
            lockedBox2.get(i).getChildren().clear(); // Clear the current tiles
            for (Tile tile : lockedTileSetList.get(m)){
                Rectangle tile1 = new Rectangle();
                tile1.setWidth(30);
                tile1.setHeight(45);
                tile1.setArcWidth(10);
                tile1.setArcHeight(10);
                String tileType = tile.toString();
                Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
                tile1.setFill(new ImagePattern(tileImage));
                if (i == 0){
                    Rotate rotate = new Rotate();
                    rotate.setAngle(270); // Rotate the tile for vertical display
                    rotate.setPivotX(tile1.getWidth() / 2);
                    rotate.setPivotY(tile1.getHeight() / 2);
                    tile1.getTransforms().add(rotate);
                }else {
                    Rotate rotate = new Rotate();
                    rotate.setAngle(90);
                    rotate.setPivotX(tile1.getWidth() / 2);
                    rotate.setPivotY(tile1.getHeight() / 2);
                    tile1.getTransforms().add(rotate);
                }
                lockedBox2.get(i).getChildren().add(tile1);
            }
            i++;
            m = (m+1)%playerList.size();
            m = (m+1)%playerList.size();
        }
    }

    // Update the state indicating whether it's the player's turn
    private void updateTurnState(ArrayList<Player> playerList) {
        int index = 0;

        // Determine the index of the local player
        for (Player player: playerList){
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = playerList.indexOf(player);
            }
        }

        int i = 0;

        // Update the turn status labels for each player in the game
        while (i != 4){
            if (playerList.get(index).getTurnState()){
                turnLabelList.get(i).setText("Acting"); // Player is currently acting
            }else {
                turnLabelList.get(i).setText("Waiting"); // Player is waiting
            }
            i++;
            index = (index+1)%playerList.size(); // Move to the next player
        }

    }

    // This method updates each player's dealer status on the UI
    private void updateDealerState(ArrayList<Player> playerList) {
        int index = 0;

        // Find the local player's index
        for (Player player: playerList){
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = playerList.indexOf(player);
            }
        }

        int i = 0;

        // Update the dealer status labels for each player
        while (i != 4){
            if (playerList.get(index).getDealerState()){
                dealerLabelList.get(i).setText("Dealer"); // Player is the dealer
            }
            i++;
            index = (index+1)%playerList.size();
        }
    }

    // This method updates the displayed names of the players
    private void updatePlayerName(ArrayList<Player> playerList) {
        int index = 0;

        String[] name = {"Hanze Dong","Jingzhe Xu","Tianyi Ma","Jiawei Xing"}; // Localized player names
        for (Player player: playerList){
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = playerList.indexOf(player);
            }
        }

        int i = 0;

        // Update the player name labels with localized names
        while (i != 4){
            playerLabelList.get(i).setText(name[index]);
            i++;
            index = (index+1)%playerList.size();
        }

    }

    // Handles actions triggered by music control buttons
    @FXML
    private void handleMusic(ActionEvent event) {
        if (mediaPlayer == null) {
            if (!musicList.isEmpty()) {
                playSong(currentSongIndex); // Play the first song if none is currently playing
            }
        } else {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause(); // Pause the music if it is playing
            } else {
                mediaPlayer.play(); // Play the music if it is paused
            }
        }
    }

    // Skip to the next song
    @FXML
    private void handleNext(ActionEvent event) {
        playNextSong();
    } // Play the next song in the playlist

    // Go back to the previous song
    @FXML
    private void handlePrevious(ActionEvent event) {
        playPreviousSong();
    } // Play the previous song in the playlist

    // Load music files from the predefined music folder
    private void loadMusic() {
        File musicFolder = new File(getClass().getResource("/music/").getPath()); // Access the music directory
        if (musicFolder.isDirectory()) {
            for (File file : musicFolder.listFiles()) {
                if (file.isFile() && (file.getName().endsWith(".mp3"))){
                    musicList.add(file.toURI().toString()); // Add music file URIs to the list
                }
            }
        }
        if (!musicList.isEmpty()) {
            playSong(currentSongIndex); // Start playing the first song
        }
    }

    // Play a specific song from the playlist based on index
    private void playSong(int index) {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop any currently playing media
        }
        Media media = new Media(musicList.get(index));
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(this::playNextSong); // Set up to play the next song when this one ends
        mediaPlayer.play(); // Play the media
    }

    // Play the next song in the playlist
    private void playNextSong() {
        currentSongIndex = (currentSongIndex + 1) % musicList.size();
        playSong(currentSongIndex);
    }

    // Play the previous song in the playlist
    private void playPreviousSong() {
        currentSongIndex = (currentSongIndex - 1 + musicList.size()) % musicList.size();
        playSong(currentSongIndex);
    }

}
