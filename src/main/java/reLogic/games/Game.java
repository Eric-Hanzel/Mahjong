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

    public Game(){
        playerList = new ArrayList<>();

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

    public void gameInit(ArrayList<Socket> onLineSocket){
        for (Socket socket: onLineSocket){
            playerList.add(new Player(socket.getRemoteSocketAddress().toString()));
        }

        libraryTileSet.sort();

        chooseDealer(playerList);
        dealTiles(playerList);
    }

    private void chooseDealer(ArrayList<Player> playerList) {
        Random random = new Random();
        int index = random.nextInt(playerList.size());
        indexOfDealer = index;
        playerList.get(indexOfDealer).setDealerState(true);
        playerList.get(indexOfDealer).setTurnState(true);

        System.out.println(playerList.get(index).getCanDarkKong());

    }

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

        if (PlayerOperateCheckRule.checkCanKong(dealerPlayer)){
            dealerPlayer.setCanDarkKong(true);
        }else {
            dealerPlayer.setCanDiscard(true);
        }

    }

    public void changePlayer(){
        int index = 0;
        int nextIndex = 0;
        for (Player player : playerList){
            if (player.getTurnState()){
                index = playerList.indexOf(player);
                nextIndex = (index+1)%playerList.size();
            }
        }
        Server.count = 0;
        playerList.get(index).setTurnState(false);
        playerList.get(index).setCanDiscard(false);
        playerList.get(nextIndex).setTurnState(true);

        if (PlayerOperateCheckRule.checkCanChow(playerList.get(nextIndex),endPlayerDiscardTile)){
            playerList.get(nextIndex).setCanChow(true);
        }
        playerList.get(nextIndex).setCanGet(true);
        playerList.get(nextIndex).setCanDarkKong(false);
    }

    public String logicOperate(String remoteSocketAddress, String operate){
        Player player = null;
        for (Player p : playerList){
            if (Objects.equals(p.getName(), remoteSocketAddress)){
                player = p;
                break;
            }
        }

        String[] parts = operate.split(" ");
        if (Objects.equals(operate, "Hu")){
            System.out.println("LHu");
            player.getHandTileSet().sort();
            gameState = false;
            victoryPlayer = player;
            return "Hu";
        }

        if (Objects.equals(parts[0], "Discard")){
            System.out.println("LDiscard");

            lasrPlayer = player;
            endPlayerDiscardTile = player.discard(parts[1]);
            player.getPlayedTileSet().addTile(endPlayerDiscardTile);
            player.getHandTileSet().sort();

            setPlayersPongBrightKongState(endPlayerDiscardTile);
            checkState = true;
            return "Discard";
        }

        if (Objects.equals(parts[0], "Get")){
            System.out.println("LGet");

            player.setCanChow(false);
            player.setCanGet(false);
            endPlayerGetTile = libraryTileSet.discardTile("Tile");
            player.getTile(endPlayerGetTile);
            player.getHandTileSet().sort();
            setPlayersDarkKongState(player);
            setHuState(player);
            return "Get";
        }

        if (Objects.equals(parts[0], "Skip")){
            System.out.println("LSkip");
            player.getHandTileSet().sort();
            player.setSkip(true);
            player.setCanPong(false);
            player.setCanBrightKong(false);
            return "Skip";
        }

        if (!operate.equals("Chow") && Objects.equals(parts[0], "Chow")){
            System.out.println("chowoperate");

            lasrPlayer.getPlayedTileSet().discardTile(endPlayerDiscardTile.toString());
            player.chow(endPlayerDiscardTile,parts[1]);
            player.setCanChow(false);
            player.setCanGet(false);
            player.setCanDiscard(true);
            player.getHandTileSet().sort();
            setHuState(player);
            return "ChowTile";
        }

        if (Objects.equals(operate, "Chow")){
            System.out.println("LChow");
            player.getHandTileSet().sort();
            return "Chow";
        }


        if (Objects.equals(parts[0], "Pong")){
            System.out.println("LPong");
            lasrPlayer.getPlayedTileSet().discardTile(endPlayerDiscardTile.toString());
            player.pong(endPlayerDiscardTile);
            Server.skipNumber=0;
            resetAllSkip();

            int indexOfPlayerDiscard = 0;
            for (Player playerDiscard : playerList){
                if (playerDiscard.getTurnState()){
                    indexOfPlayerDiscard = playerList.indexOf(playerDiscard);
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
            checkState = false;
            setHuState(player);
            return "Pong";
        }

        if (Objects.equals(parts[0], "Kong")){
            System.out.println("LKong");

            if (!player.getTurnState()){
                System.out.println("LBright");
                lasrPlayer.getPlayedTileSet().discardTile(endPlayerDiscardTile.toString());
                player.brightKong(endPlayerDiscardTile);
                Server.skipNumber=0;
                resetAllSkip();

                int indexOfPlayerDiscard = 0;
                for (Player playerDiscard : playerList){
                    if (playerDiscard.getTurnState()){
                        indexOfPlayerDiscard = playerList.indexOf(playerDiscard);
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
                checkState = false;
                return "Kong";
            }else {

                System.out.println("LDark");

                player.Kong();
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

    private void setHuState(Player player){
        if (PlayerOperateCheckRule.checkCanHu(player,this)){
            player.setCanHu(true);
        }else {
            player.setCanHu(false);
        }
    }

    private void setPlayersPongBrightKongState(Tile endDiscardTile) {
        for (Player player: playerList){
            if (PlayerOperateCheckRule.checkCanPong(player,endDiscardTile)){
                player.setCanPong(true);
            }else {
                player.setCanPong(false);
            }

            if (PlayerOperateCheckRule.checkCanBrightKong(player,endDiscardTile)){
                player.setCanBrightKong(true);
            }else {
                player.setCanBrightKong(false);
            }
        }
    }

    private static void setPlayersDarkKongState(Player player) {
        if (PlayerOperateCheckRule.checkCanKong(player)){
            player.setCanDarkKong(true);
            player.setCanDiscard(false);
        }else {
            player.setCanDarkKong(false);
            player.setCanDiscard(true);
        }
    }
    public void resetAllSkip() {
        for (Player player:playerList){
            player.setSkip(false);
        }
    }


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

    public void setCheckState(boolean b){
        checkState = b;
    }

    public void printAllPlayersDetails(){
        System.out.println(endPlayerDiscardTile);
        System.out.println(endPlayerGetTile);
        VictoryCheckRule victoryCheckRule = VictoryCheckRule.getInstance();
        for (Player player:playerList){
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
