package reLogic.rules;

import reLogic.games.Game;
import reLogic.players.Player;
import reLogic.tiles.Tile;
import reLogic.tilesSets.HandTileSet;
import reLogic.tilesSets.LockedTileSet;

import java.util.ArrayList;
import java.util.Objects;

// Class that handles checking various operations a player can perform in the Mahjong game
public class PlayerOperateCheckRule {

    // Returns the single instance of PlayerOperateCheckRule
    private static PlayerOperateCheckRule uniqueInstance = null;

    public static PlayerOperateCheckRule getInstance(){
        if (uniqueInstance == null){
            uniqueInstance = new PlayerOperateCheckRule();
        }
        return uniqueInstance;
    }

    // Private constructor to prevent instantiation
    private PlayerOperateCheckRule(){

    }

    // Checks if a player's operation is legal in the current game state
    public static boolean checkOperateLegal(String remoteSocketAddress, String operate, Game game){
        ArrayList<Player> playerList = game.getPlayerList();
        String[] parts = operate.split(" ");
        Player operatePlayer = null;

        for (Player player : playerList) {
            // Find the player by their remote socket address
            if (Objects.equals(player.getName(), remoteSocketAddress)) {
                operatePlayer = player;
            }
        }

        if (operatePlayer == null){
            System.out.println("玩家不存在");
            return false;
        }

        if (game.getCheckState()){
            if (operatePlayer.getTurnState()){
                return false;
            }else {
                if (Objects.equals(parts[0], "Pong")){
                    return checkCanPong(operatePlayer,game.getEndPlayerDiscardTile());

                }else if (Objects.equals(parts[0], "Kong")){
                    return checkCanBrightKong(operatePlayer,game.getEndPlayerDiscardTile());

                }else if (Objects.equals(parts[0], "Hu")) {
                    return checkCanHu(operatePlayer, game , game.getEndPlayerDiscardTile());
                }else if (Objects.equals(parts[0], "Skip")) {
                    return true;
                }
            }
        }else {
            if (operatePlayer.getTurnState()){
                if (Objects.equals(parts[0], "Discard")) {
                    return checkCanDiscard(operatePlayer);

                } else if (Objects.equals(parts[0], "Get")) {
                    return checkCanGet(operatePlayer);

                } else if (Objects.equals(parts[0], "Chow") && !Objects.equals(operate, "Chow")) {
                    return checkCanChow(operatePlayer,game.getEndPlayerDiscardTile()) && operatePlayer.getCanChow();

                } else if (Objects.equals(operate, "Chow")){
                    if (game.getEndPlayerDiscardTile()!=null){
                        return checkCanChow(operatePlayer,game.getEndPlayerDiscardTile())&& operatePlayer.getCanChow();
                    }
                    return false;

                } else if (Objects.equals(parts[0], "Kong")){
                    return checkCanKong(operatePlayer);

                }else if (Objects.equals(parts[0], "Hu")) {
                    return checkCanHu(operatePlayer,game);
                }
            }
        }
        return false;
    }

    // Checks if a player can perform a Pong action
    public static boolean checkCanPong(Player operatePlayer, Tile endPlayerDiscardTile) {
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        ArrayList<ArrayList<Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileSet.getTileSets());
        if (endPlayerDiscardTile!=null){
            int count = 0;
            for (ArrayList<Tile> tileSet : copyHandTileArrayList){
                count = 0;
                for (Tile tile: tileSet){
                    if (Objects.equals(tile.getType(), endPlayerDiscardTile.getType()) && tile.getMagnitude() == endPlayerDiscardTile.getMagnitude()){
                        count++;
                        if (count==2){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    // Checks if a player can perform a Bright Kong action
    public static boolean checkCanBrightKong(Player operatePlayer, Tile endPlayerDiscardTile) {
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        ArrayList<ArrayList<Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileSet.getTileSets());
        if (endPlayerDiscardTile != null){
            int count = 0;
            for (ArrayList<Tile> tileSet : copyHandTileArrayList){
                count = 0;
                for (Tile tile: tileSet){
                    if (Objects.equals(tile.getType(), endPlayerDiscardTile.getType()) && tile.getMagnitude() == endPlayerDiscardTile.getMagnitude()){
                        count++;
                        if (count==3){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    //Checks if a player can perform a Kong action (Dark Kong or a Kong after Hu)
    public static boolean checkCanKong(Player operatePlayer) {
        reLogic.rules.Rule rule = Rule.getInstance();
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        LockedTileSet lockedTileSet = operatePlayer.getLockedTileSet();

        ArrayList<ArrayList<Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileSet.getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTileArrayList = VictoryCheckRule.deepCopyTileSet(lockedTileSet.getTileSets());

        ArrayList<Tile> kongSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : copyHandTileArrayList){
            kongSet.addAll(rule.checkKong(tileSet));
        }

        System.out.println(kongSet);
        return kongSet.size() == 4;
    }

    // Checks if a player can declare Hu (win) based on their current hand and the game state
    public static boolean checkCanHu(Player operatePlayer, Game game) {
        HandTileSet handTileSet = operatePlayer.getHandTileSet();

        VictoryCheckRule victoryCheckRule = VictoryCheckRule.getInstance();
        String victoryMsg = "";

        checkAllVictoryConditions(game,operatePlayer,victoryCheckRule,victoryMsg);

        return !victoryMsg.isEmpty();
    }

    // Checks if a player can declare Hu (win) with the last discarded tile
    public static boolean checkCanHu(Player operatePlayer, Game game,Tile endPlayerDiscardTile) {
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        VictoryCheckRule victoryCheckRule = VictoryCheckRule.getInstance();
        String victoryMsg = "";
        handTileSet.addTile(endPlayerDiscardTile);
        handTileSet.sort();

        victoryMsg = checkAllVictoryConditions(game,operatePlayer,victoryCheckRule,victoryMsg);

        if (victoryMsg.isEmpty()){
            handTileSet.discardTile(endPlayerDiscardTile.toString());
        }

        return !victoryMsg.isEmpty();
    }

    // Checks all victory conditions for a player and returns the result as a message
    public static String checkAllVictoryConditions(Game game, Player player, VictoryCheckRule victoryCheckRule, String victoryMsg) {
        if (victoryCheckRule.allTriplets(player)) {
            victoryMsg += "0 ";
        }
        if (victoryCheckRule.bigSingleWait(player)) {
            victoryMsg += "1 ";
        }
        if (victoryCheckRule.zhuoWuKui(player)) {
            victoryMsg += "2 ";
        }
        if (victoryCheckRule.haiDiLaoYue(player, game.getLibraryTileSet())) {
            victoryMsg += "3 ";
        }
        if (victoryCheckRule.yiTiaoLong(player)) {
            victoryMsg += "4 ";
        }
        if (victoryCheckRule.qiDuiZi(player)) {
            victoryMsg += "5 ";
        }
        if (victoryCheckRule.gangShangKaiHua(player)) {
            victoryMsg += "6 ";
        }
        if (victoryCheckRule.qingYiSe(player)) {
            victoryMsg += "7 ";
        }
        if (victoryCheckRule.superQiDuiZi(player)) {
            victoryMsg += "8 ";
        }
        if (victoryCheckRule.basicHu(player)) {
            victoryMsg += "9 ";
        }
        System.out.println(victoryMsg);
        return victoryMsg;
    }

    // Additional helper methods for checking player actions
    private static boolean checkCanDiscard(Player operatePlayer) {
        return operatePlayer.getCanDiscard();
    }
    private static boolean checkCanGet(Player operatePlayer) {
        return operatePlayer.getCanGet();
    }
    public static boolean checkCanChow(Player operatePlayer, Tile endPlayerDiscardTile) {
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        ArrayList<ArrayList<Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileSet.getTileSets());

        if (Objects.equals(endPlayerDiscardTile.getType(), "Character")){
            return checkCanChowByType("Character",copyHandTileArrayList,endPlayerDiscardTile);

        } else if (Objects.equals(endPlayerDiscardTile.getType(), "Bamboo")){
            return checkCanChowByType("Bamboo",copyHandTileArrayList,endPlayerDiscardTile);

        } else if (Objects.equals(endPlayerDiscardTile.getType(), "Dot")){
            return checkCanChowByType("Dot",copyHandTileArrayList,endPlayerDiscardTile);

        } else {
            return false;
        }
    }

    private static boolean checkCanChowByType(String type, ArrayList<ArrayList<Tile>> copyHandTileArrayList,Tile endPlayerDiscardTile){
        int index = -1;
        if (Objects.equals(type, "Character")) index = 0;
        if (Objects.equals(type, "Bamboo")) index = 1;
        if (Objects.equals(type, "Dot")) index = 2;

        if (endPlayerDiscardTile.getMagnitude() == 1){
            return tileInList(copyHandTileArrayList.get(index),2,3);

        }else if (endPlayerDiscardTile.getMagnitude() == 2){
            return tileInList(copyHandTileArrayList.get(index),1,3) || tileInList(copyHandTileArrayList.get(index),3,4);

        }else if (endPlayerDiscardTile.getMagnitude() == 8){
            return tileInList(copyHandTileArrayList.get(index),6,7) || tileInList(copyHandTileArrayList.get(index),7,9);

        }else if (endPlayerDiscardTile.getMagnitude() == 9){
            return tileInList(copyHandTileArrayList.get(index),7,8);

        }else {
            int magnitude = endPlayerDiscardTile.getMagnitude();
            return tileInList(copyHandTileArrayList.get(index),magnitude-1,magnitude-2) || tileInList(copyHandTileArrayList.get(index),magnitude-1,magnitude+1) ||tileInList(copyHandTileArrayList.get(index),magnitude+1,magnitude+2);

        }
    }
    public static String getCanChowTypes(Player operatePlayer,Tile endPlayerDiscardTile){
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        ArrayList<ArrayList<Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileSet.getTileSets());
        int index = -1;
        if (Objects.equals(endPlayerDiscardTile.getType(), "Character")) index = 0;
        if (Objects.equals(endPlayerDiscardTile.getType(), "Bamboo")) index = 1;
        if (Objects.equals(endPlayerDiscardTile.getType(), "Dot")) index = 2;

        String types = "chowTypes " + endPlayerDiscardTile.toString() + " ";
        if (tileInList(copyHandTileArrayList.get(index),endPlayerDiscardTile.getMagnitude()-1,endPlayerDiscardTile.getMagnitude()-2)){
            types = types + "1 ";
        }
        if (tileInList(copyHandTileArrayList.get(index),endPlayerDiscardTile.getMagnitude()-1,endPlayerDiscardTile.getMagnitude()+1)){
            types = types + "2 ";
        }
        if (tileInList(copyHandTileArrayList.get(index),endPlayerDiscardTile.getMagnitude()+1,endPlayerDiscardTile.getMagnitude()+2)){
            types = types + "3 ";
        }
        return types;
    }

    private static boolean tileInList(ArrayList<Tile> tileArrayList, int i, int j) {
        boolean checkI = false;
        boolean checkJ = false;
        for (Tile tile: tileArrayList){
            if (tile.getMagnitude() == i){
                checkI = true;
            }else if (tile.getMagnitude() == j){
                checkJ = true;
            }
        }
        if (checkI && checkJ){
            return true;
        }else {
            return false;
        }
    }
}
