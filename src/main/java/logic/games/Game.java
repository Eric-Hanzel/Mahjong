package logic.games;

import com.mmahjong.server.Server;
import logic.factories.TilesSetsFactory;
import logic.factories.TilesSetsFactoryProducer;
import logic.players.Player;
import logic.rules.PlayerOperateCheckRule;
import logic.rules.VictoryCheckRule;
import logic.tiles.*;
import logic.tilesSets.LibraryTileSet;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.mmahjong.server.Server.logger;

public class Game {
    private ArrayList<Player> playerList;
    private LibraryTileSet libraryTileSet;
    private boolean checkState;
    private boolean gameState;
    private Tile endPlayerDiscardTile;
    private Tile endPlayerGetTile;
    private int indexOfDealer;
    private int kongNumber;
    private Player lastPlayer;
    private Player victoryPlayer;
    private boolean selfMo;

    public Game() {
        playerList = new ArrayList<>();

        TilesSetsFactory singleArrayListFactory = TilesSetsFactoryProducer.getTilesSetFactory(true);
        libraryTileSet = (LibraryTileSet) singleArrayListFactory.getTileSet("Library");

        checkState = false;
        gameState = true;
        selfMo = false;

        endPlayerDiscardTile = null;
        endPlayerGetTile = null;
        indexOfDealer = -1;
        kongNumber = 0;
        lastPlayer = null;
        victoryPlayer = null;
    }

    // Initialize the game with a list of online sockets
    public void gameInit(ArrayList<Socket> onLineSocket) {
        for (Socket socket : onLineSocket) {
            playerList.add(new Player(socket.getRemoteSocketAddress().toString()));
        }

        libraryTileSet.sort();

        chooseDealer(playerList);
        dealTiles(playerList);
    }

    // Choose a random dealer from the player list
    private void chooseDealer(ArrayList<Player> playerList) {
        Random random = new Random();
        int index = random.nextInt(playerList.size());
        indexOfDealer = index;
        playerList.get(indexOfDealer).setDealerState(true);
        playerList.get(indexOfDealer).setTurnState(true);

        System.out.println(playerList.get(index).getCanDarkKong());
    }

    // Deal tiles to all players, with special tiles for the dealer
    private void dealTiles(ArrayList<Player> playerList) {
        Player dealerPlayer = playerList.get(indexOfDealer);
        for (Player player : playerList) {
            for (int i = 0; i < 13; i++) {
                player.getHandTileSet().addTile(libraryTileSet.discardTile("Tile"));
                player.getHandTileSet().sort();
            }
        }

        dealerPlayer.getHandTileSet().addTile(libraryTileSet.discardTile("Tile"));
        dealerPlayer.getHandTileSet().sort();

        if (PlayerOperateCheckRule.checkCanKong(dealerPlayer)) {
            dealerPlayer.setCanDarkKong(true);
        } else {
            dealerPlayer.setCanDiscard(true);
        }
    }

    // Change the turn to the next player
    public void changePlayer() {
        int index = 0;
        int nextIndex = 0;
        for (Player player : playerList) {
            if (player.getTurnState()) {
                index = playerList.indexOf(player);
                nextIndex = (index + 1) % playerList.size();
            }
        }
        playerList.get(index).setTurnState(false);
        playerList.get(index).setCanDiscard(false);
        playerList.get(nextIndex).setTurnState(true);

        if (PlayerOperateCheckRule.checkCanChow(playerList.get(nextIndex), endPlayerDiscardTile)) {
            playerList.get(nextIndex).setCanChow(true);
        }
        playerList.get(nextIndex).setCanGet(true);
        playerList.get(nextIndex).setCanDarkKong(false);
    }

    // Handle game logic operations
    public String logicOperate(String remoteSocketAddress, String operate) {
        logger.info("------Game Logic Operation Phase------" + operate);

        Player player = null;
        for (Player p : playerList) {
            if (Objects.equals(p.getName(), remoteSocketAddress)) {
                player = p;
                break;
            }
        }

        logger.info("------Found Player------" + player);

        String[] parts = operate.split(" ");

        if (Objects.equals(operate, "Hu")) {
            logger.info("------Player Wins------" + player);

            player.getHandTileSet().sort();
            gameState = false;
            victoryPlayer = player;
            return "Hu";
        }

        if (Objects.equals(parts[0], "Discard")) {
            lastPlayer = player;
            endPlayerDiscardTile = player.discard(parts[1]);
            player.getPlayedTileSet().addTile(endPlayerDiscardTile);
            player.getHandTileSet().sort();
            logger.info("------Player Discards------" + endPlayerDiscardTile);

            setPlayersPongBrightKongState(endPlayerDiscardTile);

            checkState = true;

            logger.info("------Game Enters Pong/Kong Check State------" + endPlayerDiscardTile);

            return "Discard";
        }

        if (Objects.equals(parts[0], "Get")) {
            player.setCanChow(false);
            player.setCanGet(false);

            endPlayerGetTile = libraryTileSet.discardTile("Tile");
            player.getTile(endPlayerGetTile);
            player.getHandTileSet().sort();
            logger.info("------Player Draws Tile------" + endPlayerDiscardTile);

            setPlayersDarkKongState(player);
            setHuState(player);

            return "Get";
        }

        if (Objects.equals(parts[0], "Skip")) {
            logger.info("------Player Skips Pong/Kong Phase------");

            player.getHandTileSet().sort();
            player.setSkip(true);
            player.setCanPong(false);
            player.setCanBrightKong(false);
            return "Skip";
        }

        if (!operate.equals("Chow") && Objects.equals(parts[0], "Chow")) {
            lastPlayer.getPlayedTileSet().discardTile(endPlayerDiscardTile.toString());
            player.chow(endPlayerDiscardTile, parts[1]);
            logger.info("------Player Chows------" + endPlayerDiscardTile);

            player.setCanChow(false);
            player.setCanGet(false);
            player.setCanDiscard(true);

            player.getHandTileSet().sort();

            setHuState(player);
            return "ChowTile";
        }

        if (Objects.equals(operate, "Chow")) {
            logger.info("------Player Requests to Chow------" + endPlayerDiscardTile);
            player.getHandTileSet().sort();
            return "Chow";
        }

        if (Objects.equals(parts[0], "Pong")) {
            lastPlayer.getPlayedTileSet().discardTile(endPlayerDiscardTile.toString());
            player.pong(endPlayerDiscardTile);
            logger.info("------Player Pongs------" + endPlayerDiscardTile);
            Server.skipNumber = 0;
            resetAllSkip();

            int indexOfPlayerDiscard = 0;
            for (Player playerDiscard : playerList) {
                if (playerDiscard.getTurnState()) {
                    indexOfPlayerDiscard = playerList.indexOf(playerDiscard);
                }
            }

            playerList.get(indexOfPlayerDiscard).setTurnState(false);
            playerList.get(indexOfPlayerDiscard).setCanDiscard(false);
            player.setTurnState(true);
            player.setCanChow(false);
            player.setCanGet(false);
            player.setCanDiscard(true);
            player.getHandTileSet().sort();
            checkState = false;
            logger.info("------Pong/Kong Check Phase Ends------Player Pongs------" + endPlayerDiscardTile);
            setHuState(player);
            return "Pong";
        }

        if (Objects.equals(parts[0], "Kong")) {
            if (!player.getTurnState()) {
                lastPlayer.getPlayedTileSet().discardTile(endPlayerDiscardTile.toString());
                player.brightKong(endPlayerDiscardTile);
                kongNumber++;
                logger.info("------Player Bright Kongs------" + endPlayerDiscardTile);
                Server.skipNumber = 0;
                resetAllSkip();

                int indexOfPlayerDiscard = 0;
                for (Player playerDiscard : playerList) {
                    if (playerDiscard.getTurnState()) {
                        indexOfPlayerDiscard = playerList.indexOf(playerDiscard);
                    }
                }

                playerList.get(indexOfPlayerDiscard).setTurnState(false);
                playerList.get(indexOfPlayerDiscard).setCanDiscard(false);
                player.setTurnState(true);
                player.setCanChow(false);
                player.setCanGet(true);
                player.setCanDiscard(false);
                player.getHandTileSet().sort();
                checkState = false;
                logger.info("------Pong/Kong Check Phase Ends------Player Kongs------" + endPlayerDiscardTile);
                return "Kong";
            } else {
                logger.info("------Player Dark Kongs------" + endPlayerDiscardTile);

                player.Kong();
                kongNumber++;
                player.setTurnState(true);
                player.setCanChow(false);
                player.setCanGet(true);
                player.setCanDiscard(false);
                player.setCanDarkKong(false);
                player.getHandTileSet().sort();

                return "DarkKong";
            }
        }
        return null;
    }

    // Set Hu state for a player
    private void setHuState(Player player) {
        if (PlayerOperateCheckRule.checkCanHu(player, this)) {
            player.setCanHu(true);
        } else {
            player.setCanHu(false);
        }
        logger.info("------Set Player Hu State------" + player.getCanHu());
    }

    // Set Pong/Bright Kong state for all players
    private void setPlayersPongBrightKongState(Tile endDiscardTile) {
        logger.info("------Set All Players Pong/Kong State------" + endPlayerDiscardTile);
        for (Player player : playerList) {
            if (PlayerOperateCheckRule.checkCanPong(player, endDiscardTile)) {
                player.setCanPong(true);
            } else {
                player.setCanPong(false);
            }

            if (PlayerOperateCheckRule.checkCanBrightKong(player, endDiscardTile)) {
                player.setCanBrightKong(true);
            } else {
                player.setCanBrightKong(false);
            }
        }
    }

    // Set Dark Kong state for a player
    private static void setPlayersDarkKongState(Player player) {
        if (PlayerOperateCheckRule.checkCanKong(player)) {
            player.setCanDarkKong(true);
            player.setCanDiscard(false);
        } else {
            player.setCanDarkKong(false);
            player.setCanDiscard(true);
        }
        logger.info("------Set Player Dark Kong and Discard State------" + player.getCanDarkKong() + "------" + player.getCanDiscard());
    }

    // Reset skip state for all players
    public void resetAllSkip() {
        logger.info("------Reset All Players Skip State------");
        for (Player player : playerList) {
            player.setSkip(false);
        }
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public LibraryTileSet getLibraryTileSet() {
        return libraryTileSet;
    }

    public boolean getCheckState() {
        return checkState;
    }

    public boolean getSelfMo() {
        return selfMo;
    }

    public boolean getGameState() {
        return gameState;
    }

    public Player getVictoryPlayer() {
        return victoryPlayer;
    }

    public Player getLastPlayer() {
        return lastPlayer;
    }

    public Tile getEndPlayerDiscardTile() {
        return endPlayerDiscardTile;
    }

    public Tile getEndPlayerGetTile() {
        return endPlayerGetTile;
    }

    public int getKongNumber() {
        return kongNumber;
    }

    public void setCheckState(boolean b) {
        checkState = b;
    }

    public void setSelfMo(boolean b) {
        selfMo = b;
    }

    // Print detailed information for all players
    public void printAllPlayersDetails() {
        synchronized (playerList) {
            System.out.println("Last discarded tile: " + endPlayerDiscardTile);
            System.out.println("Last drawn tile: " + endPlayerGetTile);
            System.out.println("Skip count: " + Server.skipNumber);
            VictoryCheckRule victoryCheckRule = VictoryCheckRule.getInstance();
            for (Player player : playerList) {
                System.out.println("Player details: ");
                System.out.println(player.getName());
                System.out.println("Hand Tiles----" + player.getHandTileSet().getTileSets());
                System.out.println("Locked Tiles----" + player.getLockedTileSet().getTileSets());
                System.out.println("Played Tiles----" + player.getPlayedTileSet().getTileSets());

                System.out.println("End Get Tile----" + player.getHandTileSet().getEndGetTile());
                System.out.println("End Kong----" + player.getHandTileSet().getEndKongTile());
                System.out.println("End Second Get----" + player.getHandTileSet().getEndSecondGetTile());
                System.out.println("Turn----" + player.getTurnState());
                System.out.println("Victory Msg----" + player.getName() + "------" + PlayerOperateCheckRule.checkAllVictoryConditions(this, player, victoryCheckRule, ""));

                System.out.println("Get----" + player.getCanGet());
                System.out.println("Discard----" + player.getCanDiscard());
                System.out.println("Chow----" + player.getCanChow());
                System.out.println("Pong----" + player.getCanPong());
                System.out.println("BKong----" + player.getCanBrightKong());
                System.out.println("DKong----" + player.getCanDarkKong());
                System.out.println("Skip----" + player.getSkip());
            }
        }
    }
}
