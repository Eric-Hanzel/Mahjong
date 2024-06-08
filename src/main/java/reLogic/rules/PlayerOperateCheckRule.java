package logic.rules;

import logic.games.Game;
import logic.players.Player;
import logic.tiles.Tile;
import logic.tilesSets.HandTileSet;
import logic.tilesSets.LockedTileSet;

import java.util.ArrayList;
import java.util.Objects;

import static com.mmahjong.server.Server.logger;

public class PlayerOperateCheckRule {
    private static PlayerOperateCheckRule uniqueInstance = null;

    // Singleton pattern to ensure only one instance of PlayerOperateCheckRule exists
    public static PlayerOperateCheckRule getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new PlayerOperateCheckRule();
        }
        return uniqueInstance;
    }

    // Private constructor for Singleton pattern
    private PlayerOperateCheckRule() {
    }

    // Check if the player's operation is legal
    public static boolean checkOperateLegal(String remoteSocketAddress, String operate, Game game) {
        logger.info("------Check Player Operation Legality------" + remoteSocketAddress + "------" + operate);
        ArrayList<Player> playerList = game.getPlayerList();
        String[] parts = operate.split(" ");
        Player operatePlayer = null;

        for (Player player : playerList) {
            // Find the player
            if (Objects.equals(player.getName(), remoteSocketAddress)) {
                operatePlayer = player;
            }
        }
        logger.info("------Found the Player------" + remoteSocketAddress);
        if (operatePlayer == null) {
            System.out.println("Player does not exist");
            return false;
        }

        if (game.getCheckState()) {
            logger.info("------Game--Pong/Kong Check Phase--Operation Legality Check------");
            if (operatePlayer.getTurnState()) {
                logger.info("------Game--Pong/Kong Check Phase--Player is the discarder, illegal operation------");
                return false;
            } else {
                if (Objects.equals(parts[0], "Pong")) {
                    logger.info("------Game--Pong/Kong Check Phase--Pong Operation Check------");
                    return checkCanPong(operatePlayer, game.getEndPlayerDiscardTile());

                } else if (Objects.equals(parts[0], "Kong")) {
                    logger.info("------Game--Pong/Kong Check Phase--Bright Kong Operation Check------");
                    return checkCanBrightKong(operatePlayer, game.getEndPlayerDiscardTile());

                } else if (Objects.equals(parts[0], "Hu")) {
                    logger.info("------Game--Pong/Kong Check Phase--Hu Operation Check------");
                    return checkCanHu(operatePlayer, game, game.getEndPlayerDiscardTile());

                } else if (Objects.equals(parts[0], "Skip")) {
                    logger.info("------Game--Pong/Kong Check Phase--Skip Operation Check------");
                    return true;
                }
            }
        } else {
            logger.info("------Game--Player Operation Phase--Operation Legality Check------");
            if (operatePlayer.getTurnState()) {
                logger.info("------Game--Player Operation Phase--Player Turn Operation------");
                if (Objects.equals(parts[0], "Discard")) {
                    logger.info("------Game--Player Operation Phase--Discard Operation Check------");
                    return checkCanDiscard(operatePlayer);

                } else if (Objects.equals(parts[0], "Get")) {
                    logger.info("------Game--Player Operation Phase--Get Operation Check------");
                    return checkCanGet(operatePlayer);

                } else if (Objects.equals(parts[0], "Chow") && !Objects.equals(operate, "Chow")) {
                    logger.info("------Game--Player Operation Phase--Discard Operation Check------");
                    return checkCanChow(operatePlayer, game.getEndPlayerDiscardTile()) && operatePlayer.getCanChow();

                } else if (Objects.equals(operate, "Chow")) {
                    if (game.getEndPlayerDiscardTile() != null) {
                        logger.info("------Game--Player Operation Phase--Chow Operation Check------");
                        return checkCanChow(operatePlayer, game.getEndPlayerDiscardTile()) && operatePlayer.getCanChow();
                    }
                    return false;

                } else if (Objects.equals(parts[0], "Kong")) {
                    logger.info("------Game--Player Operation Phase--Pong Operation Check------");
                    return checkCanKong(operatePlayer);

                } else if (Objects.equals(parts[0], "Hu")) {
                    logger.info("------Game--Player Operation Phase--Hu Operation Check------" + checkCanHu(operatePlayer, game));
                    return checkCanHu(operatePlayer, game);
                }
            }
            logger.info("------Game--Player Operation Phase--Illegal Operation Outside Player Turn------");
        }
        return false;
    }

    // Check if the player can perform Pong
    public static boolean checkCanPong(Player operatePlayer, Tile endPlayerDiscardTile) {
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        ArrayList<ArrayList<Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileSet.getTileSets());
        if (endPlayerDiscardTile != null) {
            int count = 0;
            for (ArrayList<Tile> tileSet : copyHandTileArrayList) {
                count = 0;
                for (Tile tile : tileSet) {
                    if (Objects.equals(tile.getType(), endPlayerDiscardTile.getType()) && tile.getMagnitude() == endPlayerDiscardTile.getMagnitude()) {
                        count++;
                        if (count == 2) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    // Check if the player can perform Bright Kong
    public static boolean checkCanBrightKong(Player operatePlayer, Tile endPlayerDiscardTile) {
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        ArrayList<ArrayList<Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileSet.getTileSets());
        if (endPlayerDiscardTile != null) {
            int count = 0;
            for (ArrayList<Tile> tileSet : copyHandTileArrayList) {
                count = 0;
                for (Tile tile : tileSet) {
                    if (Objects.equals(tile.getType(), endPlayerDiscardTile.getType()) && tile.getMagnitude() == endPlayerDiscardTile.getMagnitude()) {
                        count++;
                        if (count == 3) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    // Check if the player can perform Dark Kong or Kong after self-draw
    public static boolean checkCanKong(Player operatePlayer) {
        Rule rule = Rule.getInstance();
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        LockedTileSet lockedTileSet = operatePlayer.getLockedTileSet();

        ArrayList<ArrayList<Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileSet.getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTileArrayList = VictoryCheckRule.deepCopyTileSet(lockedTileSet.getTileSets());

        ArrayList<Tile> kongSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : copyHandTileArrayList) {
            kongSet.addAll(rule.checkKong(tileSet));
        }

        System.out.println(kongSet);
        return kongSet.size() == 4;
    }

    // Check if the player can Hu (self-draw)
    public static boolean checkCanHu(Player operatePlayer, Game game) {

        VictoryCheckRule victoryCheckRule = VictoryCheckRule.getInstance();
        String victoryMsg = "";

        victoryMsg = checkAllVictoryConditions(game, operatePlayer, victoryCheckRule, victoryMsg);

        if (victoryMsg != null) {
            game.setSelfMo(true);
        }
        return !victoryMsg.isEmpty();
    }

    // Check if the player can Hu (after discard)
    public static boolean checkCanHu(Player operatePlayer, Game game, Tile endPlayerDiscardTile) {
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        VictoryCheckRule victoryCheckRule = VictoryCheckRule.getInstance();
        String victoryMsg = "";
        handTileSet.addTile(endPlayerDiscardTile);
        handTileSet.sort();

        victoryMsg = checkAllVictoryConditions(game, operatePlayer, victoryCheckRule, victoryMsg);

        if (victoryMsg.isEmpty()) {
            handTileSet.discardTile(endPlayerDiscardTile.toString());
        }

        return !victoryMsg.isEmpty();
    }

    // Check all victory conditions for a player
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
        if (victoryCheckRule.haiDiLaoYue(player, game.getLibraryTileSet(), game.getKongNumber())) {
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

    // Check if the player can discard
    private static boolean checkCanDiscard(Player operatePlayer) {
        return operatePlayer.getCanDiscard();
    }

    // Check if the player can draw a tile
    private static boolean checkCanGet(Player operatePlayer) {
        return operatePlayer.getCanGet();
    }

    // Check if the player can Chow
    public static boolean checkCanChow(Player operatePlayer, Tile endPlayerDiscardTile) {
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        ArrayList<ArrayList<Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileSet.getTileSets());

        if (Objects.equals(endPlayerDiscardTile.getType(), "Character")) {
            return checkCanChowByType("Character", copyHandTileArrayList, endPlayerDiscardTile);

        } else if (Objects.equals(endPlayerDiscardTile.getType(), "Bamboo")) {
            return checkCanChowByType("Bamboo", copyHandTileArrayList, endPlayerDiscardTile);

        } else if (Objects.equals(endPlayerDiscardTile.getType(), "Dot")) {
            return checkCanChowByType("Dot", copyHandTileArrayList, endPlayerDiscardTile);

        } else {
            return false;
        }
    }

    // Check if the player can Chow based on tile type
    private static boolean checkCanChowByType(String type, ArrayList<ArrayList<Tile>> copyHandTileArrayList, Tile endPlayerDiscardTile) {
        int index = -1;
        if (Objects.equals(type, "Character")) index = 0;
        if (Objects.equals(type, "Bamboo")) index = 1;
        if (Objects.equals(type, "Dot")) index = 2;

        if (endPlayerDiscardTile.getMagnitude() == 1) {
            return tileInList(copyHandTileArrayList.get(index), 2, 3);

        } else if (endPlayerDiscardTile.getMagnitude() == 2) {
            return tileInList(copyHandTileArrayList.get(index), 1, 3) || tileInList(copyHandTileArrayList.get(index), 3, 4);

        } else if (endPlayerDiscardTile.getMagnitude() == 8) {
            return tileInList(copyHandTileArrayList.get(index), 6, 7) || tileInList(copyHandTileArrayList.get(index), 7, 9);

        } else if (endPlayerDiscardTile.getMagnitude() == 9) {
            return tileInList(copyHandTileArrayList.get(index), 7, 8);

        } else {
            int magnitude = endPlayerDiscardTile.getMagnitude();
            return tileInList(copyHandTileArrayList.get(index), magnitude - 1, magnitude - 2) || tileInList(copyHandTileArrayList.get(index), magnitude - 1, magnitude + 1) || tileInList(copyHandTileArrayList.get(index), magnitude + 1, magnitude + 2);
        }
    }

    // Get types of Chow possible for the player
    public static String getCanChowTypes(Player operatePlayer, Tile endPlayerDiscardTile) {
        HandTileSet handTileSet = operatePlayer.getHandTileSet();
        ArrayList<ArrayList<Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileSet.getTileSets());
        int index = -1;
        if (Objects.equals(endPlayerDiscardTile.getType(), "Character")) index = 0;
        if (Objects.equals(endPlayerDiscardTile.getType(), "Bamboo")) index = 1;
        if (Objects.equals(endPlayerDiscardTile.getType(), "Dot")) index = 2;

        String types = "chowTypes " + endPlayerDiscardTile.toString() + " ";
        if (tileInList(copyHandTileArrayList.get(index), endPlayerDiscardTile.getMagnitude() - 1, endPlayerDiscardTile.getMagnitude() - 2)) {
            types = types + "1 ";
        }
        if (tileInList(copyHandTileArrayList.get(index), endPlayerDiscardTile.getMagnitude() - 1, endPlayerDiscardTile.getMagnitude() + 1)) {
            types = types + "2 ";
        }
        if (tileInList(copyHandTileArrayList.get(index), endPlayerDiscardTile.getMagnitude() + 1, endPlayerDiscardTile.getMagnitude() + 2)) {
            types = types + "3 ";
        }
        return types;
    }

    // Check if specific tiles are in the list
    private static boolean tileInList(ArrayList<Tile> tileArrayList, int i, int j) {
        boolean checkI = false;
        boolean checkJ = false;
        for (Tile tile : tileArrayList) {
            if (tile.getMagnitude() == i) {
                checkI = true;
            } else if (tile.getMagnitude() == j) {
                checkJ = true;
            }
        }
        return checkI && checkJ;
    }
}
