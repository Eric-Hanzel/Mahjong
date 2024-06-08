package reLogic.games;

import com.mmahjong.server.Server;
import reLogic.factories.TilesSetsFactory;
import reLogic.factories.TilesSetsFactoryProducer;
import reLogic.players.Player;
import reLogic.rules.PlayerOperateCheckRule;
import reLogic.rules.VictoryCheckRule;
import reLogic.tiles.Tile;
import reLogic.tilesSets.LibraryTileSet;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

// Represents the Mahjong game, handling game logic, player management, and tile operations
public class Game {
    private ArrayList<Player> playerList;
    private LibraryTileSet libraryTileSet;
    private boolean checkState;
    private boolean gameState;
    private Tile endPlayerDiscardTile;

    private Tile endPlayerGetTile;
    private int indexOfDealer;
    private Player lasrPlayer;
    private Player victoryPlayer;

    // Constructor initializes the game by setting up the tile set and default game state
    public Game(){
        playerList = new ArrayList<>();

        // Instantiate the factory for single array list and get the library tile set
        TilesSetsFactory singleArrayListFactory = TilesSetsFactoryProducer.getTilesSetFactory(true);
        libraryTileSet = (LibraryTileSet) singleArrayListFactory.getTileSet("Library");

        checkState = false;
        gameState = true;

        endPlayerDiscardTile = null;


        endPlayerGetTile = null;
        indexOfDealer = -1;
        lasrPlayer = null;
        victoryPlayer = null;

    }

    // Initializes the game with players connected over network sockets
    public void gameInit(ArrayList<Socket> onLineSocket){
        for (Socket socket: onLineSocket){
            playerList.add(new Player(socket.getRemoteSocketAddress().toString()));
        }

        libraryTileSet.sort(); // Sort the tiles before the game starts

        chooseDealer(playerList); // Randomly choose a dealer to start the game
        dealTiles(playerList); // Deal tiles to all players
    }

    // Randomly selects a dealer from the player list
    private void chooseDealer(ArrayList<Player> playerList) {
        Random random = new Random();
        int index = random.nextInt(playerList.size());
        indexOfDealer = index;
        playerList.get(indexOfDealer).setDealerState(true);
        playerList.get(indexOfDealer).setTurnState(true);

        System.out.println(playerList.get(index).getCanDarkKong());

    }

    // Deals tiles to each player from the library tile set
    private void dealTiles(ArrayList<Player> playerList) {
        Player dealerPlayer = playerList.get(indexOfDealer);
        for (Player player : playerList){
            for (int i = 0; i < 13; i++){
                player.getHandTileSet().addTile(libraryTileSet.discardTile("Tile"));
                player.getHandTileSet().sort();
            }
        }

        dealerPlayer.getHandTileSet().addTile(libraryTileSet.discardTile("Tile"));
        dealerPlayer.getHandTileSet().sort();

        // Check if dealer can perform a Kong immediately after dealing
        if (PlayerOperateCheckRule.checkCanKong(dealerPlayer)){
            dealerPlayer.setCanDarkKong(true);
        }else {
            dealerPlayer.setCanDiscard(true);
        }

    }

    // Rotates the turn to the next player in the game
    public void changePlayer(){
        int index = 0;
        int nextIndex = 0;
        for (Player player : playerList){
            if (player.getTurnState()){
                index = playerList.indexOf(player);
                nextIndex = (index+1)%playerList.size();
            }
        }
        Server.count = 0; // Reset server count
        playerList.get(index).setTurnState(false);
        playerList.get(index).setCanDiscard(false);
        playerList.get(nextIndex).setTurnState(true);

        // Check if next player can Chow the last discarded tile
        if (PlayerOperateCheckRule.checkCanChow(playerList.get(nextIndex),endPlayerDiscardTile)){
            playerList.get(nextIndex).setCanChow(true);
        }
        playerList.get(nextIndex).setCanGet(true);
        playerList.get(nextIndex).setCanDarkKong(false);
    }

    // Processes player operations such as Hu, Discard, Get, Skip, Chow, Pong, and Kong
    public String logicOperate(String remoteSocketAddress, String operate){
        Player player = null;

        // Find the player based on their remote socket address
        for (Player p : playerList){
            if (Objects.equals(p.getName(), remoteSocketAddress)){
                player = p;
                break;
            }
        }

        // Split the operation command to interpret parameters
        String[] parts = operate.split(" ");
        // Check for Hu operation
        if (Objects.equals(operate, "Hu")){
            System.out.println("LHu"); // Log Hu operation
            player.getHandTileSet().sort();
            gameState = false; // Set game state to over
            victoryPlayer = player; // Assign the victory to the current player
            return "Hu";
        }

        // Check for Discard operation
        if (Objects.equals(parts[0], "Discard")){
            System.out.println("LDiscard"); // Log Discard operation

            lasrPlayer = player;
            endPlayerDiscardTile = player.discard(parts[1]); // Discard the specified tile
            player.getPlayedTileSet().addTile(endPlayerDiscardTile); // Add the discarded tile to the played tile set
            player.getHandTileSet().sort();

            setPlayersPongBrightKongState(endPlayerDiscardTile); // Set state for possible Pong or Bright Kong
            checkState = true; // Enable checking state
            return "Discard";
        }

        // Check for Get operation
        if (Objects.equals(parts[0], "Get")){
            System.out.println("LGet"); // Log Get operation

            player.setCanChow(false);
            player.setCanGet(false);
            endPlayerGetTile = libraryTileSet.discardTile("Tile"); // Get a tile from the library
            player.getTile(endPlayerGetTile); // Add the tile to the player's hand
            player.getHandTileSet().sort();
            setPlayersDarkKongState(player); // Set state for possible Dark Kong
            setHuState(player); // Check for possible Hu
            return "Get";
        }

        // Check for Skip operation
        if (Objects.equals(parts[0], "Skip")){
            System.out.println("LSkip"); // Log Skip operation
            player.getHandTileSet().sort();
            player.setSkip(true); // Set player to skip their turn
            player.setCanPong(false);
            player.setCanBrightKong(false);
            return "Skip";
        }

        // Check for Chow operation
        if (!operate.equals("Chow") && Objects.equals(parts[0], "Chow")){
            System.out.println("chowoperate"); // Log Chow operation detail

            lasrPlayer.getPlayedTileSet().discardTile(endPlayerDiscardTile.toString()); // Remove the chow tile from last player's played set
            player.chow(endPlayerDiscardTile,parts[1]); // Perform the Chow operation
            player.setCanChow(false);
            player.setCanGet(false);
            player.setCanDiscard(true);
            player.getHandTileSet().sort();
            setHuState(player); // Check for possible Hu
            return "ChowTile";
        }

        // Generic check for Chow without specifics
        if (Objects.equals(operate, "Chow")){
            System.out.println("LChow"); // Log Chow operation
            player.getHandTileSet().sort();
            return "Chow";
        }

        // Check for Pong operation
        if (Objects.equals(parts[0], "Pong")){
            System.out.println("LPong"); // Log Pong operation
            lasrPlayer.getPlayedTileSet().discardTile(endPlayerDiscardTile.toString()); // Remove the pong tile from last player's played set
            player.pong(endPlayerDiscardTile); // Perform the Pong
            Server.skipNumber=0;
            resetAllSkip(); // Reset all players' skip state

            int indexOfPlayerDiscard = 0;
            for (Player playerDiscard : playerList){
                if (playerDiscard.getTurnState()){
                    indexOfPlayerDiscard = playerList.indexOf(playerDiscard); // Identify the discarding player's index
                }
            }

            Server.count = 0;
            playerList.get(indexOfPlayerDiscard).setTurnState(false);
            playerList.get(indexOfPlayerDiscard).setCanDiscard(false);
            player.setTurnState(true);
            player.setCanChow(false);
            player.setCanGet(false);
            player.setCanDiscard(true);
            player.getHandTileSet().sort();
            checkState = false; // Disable checking state
            setHuState(player); // Check for possible Hu
            return "Pong";
        }

        // Check for Kong operation
        if (Objects.equals(parts[0], "Kong")){
            System.out.println("LKong"); // Log Kong operation

            // Check for Bright Kong
            if (!player.getTurnState()){
                System.out.println("LBright"); // Log Bright Kong specific
                lasrPlayer.getPlayedTileSet().discardTile(endPlayerDiscardTile.toString()); // Remove the kong tile from last player's played set
                player.brightKong(endPlayerDiscardTile); // Perform the Bright Kong
                Server.skipNumber=0;
                resetAllSkip(); // Reset all skips

                int indexOfPlayerDiscard = 0;
                for (Player playerDiscard : playerList){
                    if (playerDiscard.getTurnState()){
                        indexOfPlayerDiscard = playerList.indexOf(playerDiscard); // Identify the discarding player's index
                    }
                }

                Server.count = 0;
                playerList.get(indexOfPlayerDiscard).setTurnState(false);
                playerList.get(indexOfPlayerDiscard).setCanDiscard(false);
                player.setTurnState(true);
                player.setCanChow(false);
                player.setCanGet(true);
                player.setCanDiscard(false);
                player.getHandTileSet().sort();
                checkState = false; // Disable checking state
                return "Kong";
            }else {

                System.out.println("LDark"); // Log Dark Kong specific

                player.Kong(); // Perform the Dark Kong
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

    // Sets the Hu state for a player based on game rules
    private void setHuState(Player player){
        if (PlayerOperateCheckRule.checkCanHu(player,this)){
            player.setCanHu(true); // Set Hu possibility to true if conditions are met
        }else {
            player.setCanHu(false); // Set Hu possibility to false if conditions are not met
        }
    }

    // Sets the possible actions Pong and Bright Kong for all players based on the last discarded tile
    private void setPlayersPongBrightKongState(Tile endDiscardTile) {
        for (Player player: playerList){
            if (PlayerOperateCheckRule.checkCanPong(player,endDiscardTile)){
                player.setCanPong(true); // Allow Pong if the player can match the discarded tile
            }else {
                player.setCanPong(false); // Disallow Pong if the player cannot match the discarded tile
            }

            if (PlayerOperateCheckRule.checkCanBrightKong(player,endDiscardTile)){
                player.setCanBrightKong(true); // Allow Bright Kong if the player can extend a Pung to a Kong
            }else {
                player.setCanBrightKong(false); // Disallow Bright Kong if the player cannot extend a Pung to a Kong
            }
        }
    }

    // Sets the Dark Kong state for a player if they are eligible to declare a Kong with a concealed hand
    private static void setPlayersDarkKongState(Player player) {
        if (PlayerOperateCheckRule.checkCanKong(player)){
            player.setCanDarkKong(true); // Allow Dark Kong if the player can declare it
            player.setCanDiscard(false); // Player cannot discard if they are declaring Dark Kong
        }else {
            player.setCanDarkKong(false); // Disallow Dark Kong if not possible
            player.setCanDiscard(true); // Player can discard normally
        }
    }

    // Resets the skip state for all players in the game
    public void resetAllSkip() {
        for (Player player:playerList){
            player.setSkip(false); // Reset the skip flag for each player
        }
    }

    // Getter methods for game state information and tile details
    public ArrayList<Player> getPlayerList() {
        return playerList;
    }
    public LibraryTileSet getLibraryTileSet(){
        return libraryTileSet;
    }
    public boolean getCheckState() {
        return checkState;
    }
    public boolean getGameState(){
        return gameState;
    }
    public Tile getEndPlayerDiscardTile(){
        return endPlayerDiscardTile;
    }
    public Tile getEndPlayerGetTile(){
        return endPlayerGetTile;
    }

    // Sets the game's check state, which can be used to trigger or halt certain checks within the game logic
    public void setCheckState(boolean b){
        checkState = b;
    }

    // Prints detailed information about all players' current game status
    public void printAllPlayersDetails(){
        System.out.println(endPlayerDiscardTile);
        System.out.println(endPlayerGetTile);

        // Log and display details about each player, including their hand tiles and possible actions
        VictoryCheckRule victoryCheckRule = VictoryCheckRule.getInstance();
        for (Player player:playerList){

            // Output player details to the console
            System.out.println("");
            System.out.println(player.getName());
            System.out.println("HandTile: "+player.getHandTileSet().getTileSets());
            System.out.println("LockedTile: "+player.getLockedTileSet().getTileSets());
            System.out.println("PlayedTile: "+player.getPlayedTileSet().getTileSets());

            System.out.println("EndTile: "+player.getHandTileSet().getEndGetTile());
            System.out.println("EndKong: "+player.getHandTileSet().getEndKongTile());
            System.out.println("EndSecond: "+player.getHandTileSet().getEndSecondGetTile());
            System.out.println("Turn: "+player.getTurnState());
            System.out.println("VictoryMsg: "+PlayerOperateCheckRule.checkAllVictoryConditions(this,player,victoryCheckRule,""));

            System.out.println("MHu: "+PlayerOperateCheckRule.checkCanHu(player,this));
            if (endPlayerDiscardTile!=null){
                System.out.println("Hu: "+PlayerOperateCheckRule.checkCanHu(player,this,endPlayerDiscardTile));
            }

            System.out.println("Get: "+player.getCanGet());
            System.out.println("Discard: "+player.getCanDiscard());
            System.out.println("Chow: " + player.getCanChow());
            System.out.println("Pong: "+player.getCanPong());
            System.out.println("BKong: "+player.getCanBrightKong());
            System.out.println("DKong: "+player.getCanDarkKong());
            System.out.println("Skip: "+player.getSkip());
        }
    }
}
