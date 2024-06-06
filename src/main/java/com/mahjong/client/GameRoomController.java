package com.mahjong.client;

<<<<<<< HEAD
import com.mmahjong.server.Server;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import javafx.util.Duration;
import logic.players.Player;
import logic.tiles.PlayedTile;
import logic.tiles.Tile;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class GameRoomController {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    @FXML
    public Label autoHideLabel;
    public VBox player4LockedTiles;
    public HBox player3LockedTiles;
    public VBox player2LockedTiles;
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
    private VBox chowOptionsVBox;
    @FXML
    public TilePane rightTilePane;
    @FXML
    public TilePane leftTilePane;
    @FXML
    public TilePane topTilePane;
    @FXML
    private HBox player1Tiles;

    @FXML
    private TilePane bottomTilePane;

    private Socket socket;
    private Rectangle selectedTile;
    private ArrayList<Tile> handTileList;
    private ArrayList<PlayedTile> playedTileSetList;
    private ArrayList<ArrayList<Tile>> lockedTileSetList;

    private ArrayList<TilePane> playedPane;
    private ArrayList<HBox> lockedBox1;
    private ArrayList<VBox> lockedBox2;
    private MediaPlayer mediaPlayer;
    private List<String> musicList = new ArrayList<>();
    private int currentSongIndex = 0;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    @FXML
    public void initialize() {
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

//        loadMusic();
    }
    @FXML
    public void handleTileClick(MouseEvent event) {
        if (selectedTile == event.getSource() && selectedTile.getStroke() == Color.YELLOW){
            selectedTile.setStroke(null);
        }else {
            if (selectedTile != null) {
                selectedTile.setStroke(null); // 取消之前选中方块的高亮
            }

            selectedTile = (Rectangle) event.getSource();
            selectedTile.setStroke(Color.YELLOW); // 设置选中方块的高亮颜色
            selectedTile.setStrokeWidth(3);
        }
    }

    @FXML
    public void handleDiscard() throws IOException {
        // 发信息给服务器，更新playedtile；然后再收消息，更新画板
        if (selectedTile != null && selectedTile.getStroke() == Color.YELLOW && !Objects.equals(selectedTile.getId(), "")) {
            sendOperation("Discard " + selectedTile.getId());
            selectedTile.setStroke(null); // 移除高亮效果
            selectedTile = null; // 重置选中方块

        }
    }
    @FXML
    public void handleGet(ActionEvent event) throws IOException {
        sendOperation("Get");
    }

    @FXML
    public void handleChow(ActionEvent event) throws IOException {
        sendOperation("Chow");
    }
    @FXML
    public void handleSkip(ActionEvent event) throws IOException {
        sendOperation("Skip");
    }
    @FXML
    public void handlePong(ActionEvent event) throws IOException {
        sendOperation("Pong");
    }
    @FXML
    public void handleKong(ActionEvent event) throws IOException {
        sendOperation("Kong");
    }



    private void sendOperation(String operation) throws IOException {
        output.reset();
        output.writeObject(operation);
        output.flush();
    }

    public void setNetwork(Socket socket,ObjectInputStream input) throws IOException {
        this.socket = socket;
        this.input = input;
        output = new ObjectOutputStream(socket.getOutputStream());
        //收玩家和牌库
        new Thread(() -> {
            try {
                while (true) {
                    try {
                        logger.info("客户端准备读取数据");
                        Object message = input.readObject();
                        if (message instanceof String){
                            String msg = (String) message;
                            String[] parts = msg.split(" ");
                            if (Objects.equals(parts[0], "chowTypes")){
                                Platform.runLater(() -> showChowChoice(msg));
                            }else {
                                Platform.runLater(() -> showMsg(msg));
                            }
                        }else {
                            ArrayList<Player> playerList = (ArrayList<Player>) message;
                            checkVictory(playerList);
                            for (Player player: playerList){
                                logger.info(""+player.getHandTile().getTileSet());
                            }
                            Platform.runLater(() -> updateGameBoard(playerList));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(socket.getRemoteSocketAddress() + "自己下线了");
                        input.close();
=======
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
>>>>>>> origin/master
                        socket.close();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
<<<<<<< HEAD
    }

    private void checkVictory(ArrayList<Player> playerList) {
        int index = 0;
        for (Player player: playerList){
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = playerList.indexOf(player);
            }
        }

    }

    private void showChowChoice(String msg) {
        chowOptionsVBox.getChildren().clear(); // 清空之前的选项
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

            Button selectButton = new Button("选择");
            int finalI = i;
            selectButton.setOnAction(e -> {
                try {
                    sendOperation("Chow " + parts[finalI]);
                    chowOptionsVBox.setVisible(false); // 选择后收起选项
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            optionBox.getChildren().addAll(tilesBox, selectButton);
            chowOptionsVBox.getChildren().add(optionBox);
        }

        chowOptionsVBox.setVisible(true);
    }

    private void showMsg(String msg) {
        autoHideLabel.setText(msg);
        autoHideLabel.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1),
                event -> autoHideLabel.setVisible(false)
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }

    //更新自己的手牌，所有人的弃牌堆，所有人锁定的牌
//    //玩家
    private void updateGameBoard(ArrayList<Player> playerList) {
        updatePlayerName(playerList);
        updateDealerState(playerList);
        updateTurnState(playerList);
        for (Player player: playerList){
            System.out.println(player.getHandTile().getTileSet());
        }
        updateHandTileDisplay(playerList);
        updatePlayedTileDisplay(playerList);
        updateLockedTile(playerList);
        // 根据当前游戏状态更新游戏板
        // 例如：显示牌、玩家动作等
    }

    private void updatePlayedTileDisplay(ArrayList<Player> playerList) {
        // 获取打出的牌
        int index = 0;
        playedTileSetList.clear();
        for (Player player: playerList){
            playedTileSetList.add(player.getPlayedTile());
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = playerList.indexOf(player);
            }
        }
        // 打出的牌更新
        int i = 0;
        while (i != 4){
            playedPane.get(i).getChildren().clear();
            for (Tile tile : playedTileSetList.get(index).getTileSet()){
                Rectangle tile1 = new Rectangle();
                tile1.setWidth(30);
                tile1.setHeight(45);
                tile1.setArcWidth(10);
                tile1.setArcHeight(10);
                String tileType = tile.toString();
                Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
//                Image tileImage = new Image(getClass().getResource("/tiles/1Bamboo.png").toExternalForm());
                tile1.setFill(new ImagePattern(tileImage));
                Rotate rotate = new Rotate();
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
            index = (index+1)%playerList.size();
        }
    }

    private void updateHandTileDisplay(ArrayList<Player> playerList) {
        //更新手牌内容
        handTileList.clear();
        for (Player player : playerList) {
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString()+":"+socket.getLocalPort())) {
                for (ArrayList<Tile> tileList : player.getHandTile().getTileSet()) {
                    for (Tile tile:tileList){
                        if (!tile.getLock()){
                            handTileList.add(tile);
                        }
                    }
                }
            }
        }
        //给手牌上图像
//        for (int i = 0; i < player1Tiles.getChildren().size(); i++) {
//            if (i < handTileList.size()) {
//                //
//                String tileType = handTileList.get(i).toString();
//                Rectangle tile = (Rectangle) player1Tiles.getChildren().get(i);
//                tile.setId(tileType);
//                Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
//                tile.setFill(new ImagePattern(tileImage));
//            }else {
//                Rectangle tile = (Rectangle) player1Tiles.getChildren().get(i);
//                tile.setId("");
//                tile.setFill(Color.WHITE);
//            }
//        }

        player1Tiles.getChildren().clear();
        for (Tile tile: handTileList){
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
            Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
//                Image tileImage = new Image(getClass().getResource("/tiles/1Bamboo.png").toExternalForm());
            tile1.setFill(new ImagePattern(tileImage));
            player1Tiles.getChildren().add(tile1);
        }

    }

    private void updateLockedTile(ArrayList<Player> playerList) {
        int index = 0;
        lockedTileSetList.clear();
        for (Player player: playerList){
            ArrayList<Tile> lockedTileSet = new ArrayList<>();
            for (ArrayList<Tile> tileSet : player.getHandTile().getLockedTile()){
                lockedTileSet.addAll(tileSet);
            }
            lockedTileSetList.add(lockedTileSet);
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = playerList.indexOf(player);
            }
        }

        int i = 0;
        int m = index;
        while (i != 2){
            lockedBox1.get(i).getChildren().clear();
            for (Tile tile : lockedTileSetList.get(m)){
                Rectangle tile1 = new Rectangle();
                tile1.setWidth(30);
                tile1.setHeight(45);
                tile1.setArcWidth(10);
                tile1.setArcHeight(10);
                String tileType = tile.toString();
                Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
//                Image tileImage = new Image(getClass().getResource("/tiles/1Bamboo.png").toExternalForm());
                tile1.setFill(new ImagePattern(tileImage));
                lockedBox1.get(i).getChildren().add(tile1);
            }
            i++;
            m = (m+1)%playerList.size();
            m = (m+1)%playerList.size();
        }


        i = 0;
        m = (index+1)%playerList.size();
        while (i != 2){
            lockedBox2.get(i).getChildren().clear();
            for (Tile tile : lockedTileSetList.get(m)){
                Rectangle tile1 = new Rectangle();
                tile1.setWidth(30);
                tile1.setHeight(45);
                tile1.setArcWidth(10);
                tile1.setArcHeight(10);
                String tileType = tile.toString();
                Image tileImage = new Image(getClass().getResource("/tiles/" + tileType + ".png").toExternalForm());
//                Image tileImage = new Image(getClass().getResource("/tiles/1Bamboo.png").toExternalForm());
                tile1.setFill(new ImagePattern(tileImage));
                if (i == 0){
                    Rotate rotate = new Rotate();
                    rotate.setAngle(270);
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

    private void updateTurnState(ArrayList<Player> playerList) {
        int index = 0;
        for (Player player: playerList){
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = playerList.indexOf(player);
            }
        }

        int i = 0;
        while (i != 4){
            if (playerList.get(index).getTurnState()){
                turnLabelList.get(i).setText("Acting");
            }else {
                turnLabelList.get(i).setText("Waiting");
            }
            i++;
            index = (index+1)%playerList.size();
        }

    }

    private void updateDealerState(ArrayList<Player> playerList) {
        int index = 0;
        for (Player player: playerList){
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = playerList.indexOf(player);
            }
        }

        int i = 0;
        while (i != 4){
            if (playerList.get(index).getDealerState()){
                dealerLabelList.get(i).setText("Dealer");
            }
            i++;
            index = (index+1)%playerList.size();
        }
    }
    private void updatePlayerName(ArrayList<Player> playerList) {
        int index = 0;

        String[] name = {"董瀚泽","许敬哲","孙凌睿","邢家维"};
        for (Player player: playerList){
            if (Objects.equals(player.getName(), socket.getLocalAddress().toString() + ":" + socket.getLocalPort())) {
                index = playerList.indexOf(player);
            }
        }

        int i = 0;
        while (i != 4){
            playerLabelList.get(i).setText(name[index]);
            i++;
            index = (index+1)%playerList.size();
        }

    }








































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

    @FXML
    private void handleNext(ActionEvent event) {
        playNextSong();
    }

    @FXML
    private void handlePrevious(ActionEvent event) {
        playPreviousSong();
    }

    private void loadMusic() {
        // 预定义的音乐文件夹路径
        File musicFolder = new File(getClass().getResource("/music/").getPath());
        if (musicFolder.isDirectory()) {
            for (File file : musicFolder.listFiles()) {
                if (file.isFile() && (file.getName().endsWith(".mp3"))){
                    musicList.add(file.toURI().toString());
                }
            }
        }
        if (!musicList.isEmpty()) {
            playSong(currentSongIndex);
        }
    }

    private void playSong(int index) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Media media = new Media(musicList.get(index));
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(this::playNextSong);
        mediaPlayer.play();
    }

    private void playNextSong() {
        currentSongIndex = (currentSongIndex + 1) % musicList.size();
        playSong(currentSongIndex);
    }

    private void playPreviousSong() {
        currentSongIndex = (currentSongIndex - 1 + musicList.size()) % musicList.size();
        playSong(currentSongIndex);
    }



}
=======
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
>>>>>>> origin/master
