package logic.gameManagement;

import com.mmahjong.server.Server;
import logic.players.Player;
import logic.tiles.LibraryTile;
import logic.tiles.Tile;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Game {
    
    public static ArrayList<Player> playerList;
    public static LibraryTile libraryTile;
    public static boolean checkState;
    public static Tile endDiscardTile;
    public static Tile endGetTile;
    //牌库，玩家，规则检查器
    //牌库：初始化打乱
    //玩家：庄家选取，发手牌，初始化弃牌堆，初始化锁定的牌堆，
    //OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK
    public static void init(ArrayList<Socket> onLineSocket) {
        playerList = new ArrayList<>();
        checkState = false;
        endDiscardTile = null;
        endGetTile = null;
        libraryTile = new LibraryTile();
        libraryTile.sort();
        for (Socket socket : onLineSocket){
            playerList.add(new Player(socket.getRemoteSocketAddress().toString(),libraryTile));
        }
        chooseDealer(playerList);
        dealTile(playerList);

    }
    //OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK
    private static void dealTile(ArrayList<Player> playerList) {
        for (Player player : playerList){
            for (int i = 0; i < 13; i++){
                player.getHandTile().addTile(libraryTile.discard());
            }
            if (player.getDealerState()){
                player.getHandTile().addTile(libraryTile.discard());
            }
            player.getHandTile().sort();
        }
    }
    //OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK

    private static void chooseDealer(ArrayList<Player> playerList) {
        Random random = new Random();
        int index = random.nextInt(playerList.size());
        playerList.get(index).setDealerState(true);
        playerList.get(index).setTurnState(true);
        if (playerList.get(index).checkCanDarkKong()){
            playerList.get(index).setCanDiscard(false);
            playerList.get(index).setCanDarkKong(true);
        }else {
            playerList.get(index).setCanDiscard(true);
            playerList.get(index).setCanDarkKong(false);
        }

    }
    //OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK
    public static void changePlayer() {
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
        if (playerList.get(nextIndex).checkCanChow(endDiscardTile)){
            playerList.get(nextIndex).setCanChow(true);
        }
        playerList.get(nextIndex).setCanGet(true);
        playerList.get(nextIndex).setCanDarkKong(false);
    }

    public static boolean checkLegal(String remoteSocketAddress,String operate) {
        String[] parts = operate.split(" ");
        for (Player player : playerList){
            //找到该玩家
            if (Objects.equals(player.getName(), remoteSocketAddress)){
                //在检查阶段
                if (Game.checkState){
                    //该玩家是出牌人
                    if (player.getTurnState()){
                        return false;
                    //不是出派人
                    }else {
                        if (Objects.equals(parts[0], "Pong")){
                            if (player.getCanPong()){
                                return true;
                            }else {
                                return false;
                            }
                        }else if (Objects.equals(parts[0], "Kong")){
                            if (player.getCanBrightKong()){
                                return true;
                            }else {
                                return false;
                            }
                        } else if (Objects.equals(parts[0], "Skip")) {
                            return true;
                        }
                    }
                // 不在检查阶段
                }else {
                    //该玩家的回合
                    if (player.getTurnState()) {
                        //玩家出牌
                        if (Objects.equals(parts[0], "Discard")) {
                            if (player.getCanDiscard()) {
                                return true;
                            } else {
                                return false;
                            }
                            //玩家摸牌
                        } else if (Objects.equals(parts[0], "Get")) {
                            if (player.getCanGet()) {
                                return true;
                            } else {
                                return false;
                            }
                        } else if (Objects.equals(parts[0], "Chow") && !Objects.equals(operate, "Chow")) {
                            if (player.getCanChow()) {
                                return true;
                            } else {
                                return false;
                            }
                        } else if (Objects.equals(operate, "Chow")){
                            if (player.getCanChow()) {
                                return true;
                            } else {
                                return false;
                            }
                        } else if (Objects.equals(parts[0], "Kong")){
                            if (player.getCanDarkKong()) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                        //不是该玩家的回合
                    }
                }
            }
        }
        return false;
    }
    //OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK
    // 改变状态
    public static String logicOperate(String remoteSocketAddress, String operate) {
        // 找到该玩家
        Player player = null;
        for (Player p : playerList){
            if (Objects.equals(p.getName(), remoteSocketAddress)){
                player = p;
                break;
            }
        }
        //分类讨论
        String[] parts = operate.split(" ");
        if (Objects.equals(parts[0], "Discard")){
            endDiscardTile = player.discard(parts[1]);
            player.getPlayedTile().addTile(endDiscardTile);
            player.getHandTile().sort();
            setPongBrightKongState(endDiscardTile);
            checkState = true;
            return "Discard";
        }
        if (Objects.equals(parts[0], "Get")){
            player.setCanChow(false);
            player.setCanGet(false);
            endGetTile = libraryTile.discard();
            player.getHandTile().addTile(endGetTile);
            setDarkKongState(player);
            player.getHandTile().sort();
            return "Get";
        }
        if (Objects.equals(parts[0], "Skip")){
            player.setSkip(true);
            player.setCanPong(false);
            player.setCanBrightKong(false);
            return "Skip";
        }
        if (!operate.equals("Chow") && Objects.equals(parts[0], "Chow")){
            player.getHandTile().chow(endDiscardTile,parts[1]);
            player.setCanChow(false);
            player.setCanGet(false);
            player.setCanDiscard(true);
            player.getHandTile().sort();
            return "ChowTile";
        }
        if (Objects.equals(operate, "Chow")){
            return "Chow";
        }
        if (Objects.equals(parts[0], "Pong")){
            player.getHandTile().pong(endDiscardTile);
            resetAllSkip();
            int index = 0;
            for (Player player1 : playerList){
                if (player1.getTurnState()){
                    index = playerList.indexOf(player1);
                }
            }
            Server.count = 0;
            playerList.get(index).setTurnState(false);
            playerList.get(index).setCanDiscard(false);
            player.setTurnState(true);
            player.setCanChow(false);
            player.setCanGet(false);
            player.setCanDiscard(true);
            player.getHandTile().sort();
            checkState = false;
            return "Pong";
        }
        if (Objects.equals(parts[0], "Kong")){
            if (!player.getTurnState()){
                player.getHandTile().brightKong(endDiscardTile);
                resetAllSkip();
                int index = 0;
                for (Player player1 : playerList){
                    if (player1.getTurnState()){
                        index = playerList.indexOf(player1);
                    }
                }
                Server.count = 0;
                playerList.get(index).setTurnState(false);
                playerList.get(index).setCanDiscard(false);
                player.setTurnState(true);
                player.setCanChow(false);
                player.setCanGet(true);
                player.setCanDiscard(false);
                player.getHandTile().sort();
                checkState = false;
                return "Kong";
            }else {
                player.getHandTile().darkKong();
                player.setTurnState(true);
                player.setCanChow(false);
                player.setCanGet(true);
                player.setCanDiscard(false);
                player.setCanDarkKong(false);
                player.getHandTile().sort();
                return "DarkKong";
            }
        }
        return null;
    }

    private static void setDarkKongState(Player player) {
        if (player.checkCanDarkKong()){
            player.setCanDarkKong(true);
            player.setCanDiscard(false);
        }else {
            player.setCanDarkKong(false);
            player.setCanDiscard(true);
        }
    }

    private static void setPongBrightKongState(Tile endDiscardTile) {
        for (Player player: playerList){
            if (player.checkCanPong(endDiscardTile)){
                player.setCanPong(true);
            }else {
                player.setCanPong(false);
            }
            if (player.checkCanBrightKong(endDiscardTile)){
                player.setCanBrightKong(true);
            }else {
                player.setCanBrightKong(false);
            }
        }
    }

    public static void resetAllSkip() {
        for (Player player:playerList){
            player.setSkip(false);
        }
    }
}
