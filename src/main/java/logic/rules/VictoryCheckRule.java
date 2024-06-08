package logic.rules;

import logic.players.Player;
import logic.tiles.Tile;
import logic.tilesSets.LibraryTileSet;

import java.util.ArrayList;
import java.util.Objects;

public class VictoryCheckRule {

    private static VictoryCheckRule uniqueInstance = null;
    private Rule rule;

    // Singleton pattern to ensure only one instance of VictoryCheckRule exists
    public static VictoryCheckRule getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new VictoryCheckRule();
        }
        return uniqueInstance;
    }

    // Private constructor for Singleton pattern
    private VictoryCheckRule() {
        rule = Rule.getInstance();
    }

    // Return a sorted final hand copy including locked tiles
    public static ArrayList<ArrayList<Tile>> joinHandLockedTile(ArrayList<ArrayList<Tile>> handTileArrayList, ArrayList<ArrayList<Tile>> lockedTileArrayList) {
        if (handTileArrayList == null || lockedTileArrayList == null) {
            throw new IllegalArgumentException("Input arrays cannot be null");
        }

        ArrayList<ArrayList<Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileArrayList);

        for (ArrayList<Tile> tileArrayList : lockedTileArrayList) {
            for (Tile tile : tileArrayList) {
                if (tile == null) continue;
                switch (tile.getType()) {
                    case "Character":
                        copyHandTileArrayList.get(0).add(tile);
                        break;
                    case "Bamboo":
                        copyHandTileArrayList.get(1).add(tile);
                        break;
                    case "Dot":
                        copyHandTileArrayList.get(2).add(tile);
                        break;
                    default:
                        copyHandTileArrayList.get(3).add(tile);
                        break;
                }
            }
        }

        for (ArrayList<Tile> tileArrayList : copyHandTileArrayList) {
            tileArrayList.sort(null);
        }

        return copyHandTileArrayList;
    }

    // Return a deep copy of a nested list
    public static ArrayList<ArrayList<Tile>> deepCopyTileSet(ArrayList<ArrayList<Tile>> original) {
        if (original == null) {
            return null;
        }

        ArrayList<ArrayList<Tile>> copy = new ArrayList<>();

        for (ArrayList<Tile> tileSet : original) {
            if (tileSet == null) {
                copy.add(null);
                continue;
            }

            ArrayList<Tile> newTileSet = new ArrayList<>();
            for (Tile tile : tileSet) {
                if (tile != null) {
                    newTileSet.add(tile);
                }
            }
            newTileSet.sort(null);
            copy.add(newTileSet);
        }

        return copy;
    }

    // Check if the player has four triplets and one pair
    public boolean allTriplets(Player operatePlayer) {
        boolean allTriplets = false;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> checkedTileSet = joinHandLockedTile(copyHandTile, copyLockedTile);

        if (rule.getTripletNumber(checkedTileSet) == 4) {
            allTriplets = true;
        }
        return basicHu(operatePlayer) && allTriplets;
    }

    // Check if the player has a big single wait
    public boolean bigSingleWait(Player operatePlayer) {
        boolean bigSingleWaitCheck = false;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        if (copyLockedTile.get(0).size() / 3 + copyLockedTile.get(1).size() / 3 == 4) {
            if (rule.getPairNumber(copyHandTile) == 1) {
                bigSingleWaitCheck = true;
            }
        }
        return basicHu(operatePlayer) && bigSingleWaitCheck;
    }

    // Check if the player wins with Zhuo Wu Kui
    public boolean zhuoWuKui(Player operatePlayer) {
        boolean zhuoWuKuiCheck = Objects.equals(operatePlayer.getHandTileSet().getEndGetTile().toString(), "5Character");
        return (basicHu(operatePlayer) && zhuoWuKuiCheck) || (qiDuiZi(operatePlayer) && zhuoWuKuiCheck) || (superQiDuiZi(operatePlayer) && zhuoWuKuiCheck);
    }

    // Check if the player wins with Hai Di Lao Yue
    public boolean haiDiLaoYue(Player operatePlayer, LibraryTileSet libraryTileSet, int kongNumber) {
        boolean haiDiLaoYueCheck = false;
        if (libraryTileSet.getTileNumber() == (4 * kongNumber)) {
            haiDiLaoYueCheck = true;
        }
        return (basicHu(operatePlayer) && haiDiLaoYueCheck) || (qiDuiZi(operatePlayer) && haiDiLaoYueCheck) || (superQiDuiZi(operatePlayer) && haiDiLaoYueCheck);
    }

    // Check if the player wins with Yi Tiao Long
    public boolean yiTiaoLong(Player operatePlayer) {
        boolean yiTiaoLongCheck = false;

        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> checkedTileSet = joinHandLockedTile(copyHandTile, copyLockedTile);

        for (ArrayList<Tile> tileSet : checkedTileSet) {
            int j = 1;
            for (Tile tile : tileSet) {
                if (tile.getMagnitude() == j) {
                    j++;
                }
            }
            if (j == 10) {
                yiTiaoLongCheck = true;
            }
        }
        return basicHu(operatePlayer) && yiTiaoLongCheck;
    }

    // Check if the player wins with Qi Dui Zi
    public boolean qiDuiZi(Player operatePlayer) {
        boolean victory = false;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> checkedTileSet = joinHandLockedTile(copyHandTile, copyLockedTile);
        if (rule.getPairNumber(checkedTileSet) == 7 && rule.getKongNumber(checkedTileSet) == 0 && rule.getTripletNumber(checkedTileSet) == 0) {
            victory = true;
        }
        return victory;
    }

    // Check if the player wins with Gang Shang Kai Hua
    public boolean gangShangKaiHua(Player operatePlayer) {
        boolean gangShangKaiHuaCheck = false;

        Tile endKongTile = operatePlayer.getHandTileSet().getEndKongTile();
        if (endKongTile != null) {
            if (Objects.equals(operatePlayer.getHandTileSet().getEndSecondGetTile().toString(), endKongTile.toString())) {
                gangShangKaiHuaCheck = true;
            }
        }
        return (basicHu(operatePlayer) && gangShangKaiHuaCheck) || (qiDuiZi(operatePlayer) && gangShangKaiHuaCheck) || (superQiDuiZi(operatePlayer) && gangShangKaiHuaCheck);
    }

    // Check if the player wins with Qing Yi Se
    public boolean qingYiSe(Player operatePlayer) {
        boolean qingYiSeCheck = false;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> checkedTileSet = joinHandLockedTile(copyHandTile, copyLockedTile);

        if (!checkedTileSet.get(0).isEmpty() && checkedTileSet.get(1).isEmpty() && checkedTileSet.get(2).isEmpty() && checkedTileSet.get(3).isEmpty()) {
            qingYiSeCheck = true;
        }
        if (checkedTileSet.get(0).isEmpty() && !checkedTileSet.get(1).isEmpty() && checkedTileSet.get(2).isEmpty() && checkedTileSet.get(3).isEmpty()) {
            qingYiSeCheck = true;
        }
        if (checkedTileSet.get(0).isEmpty() && checkedTileSet.get(1).isEmpty() && !checkedTileSet.get(2).isEmpty() && checkedTileSet.get(3).isEmpty()) {
            qingYiSeCheck = true;
        }

        return (basicHu(operatePlayer) && qingYiSeCheck) || (qiDuiZi(operatePlayer) && qingYiSeCheck) || (qiDuiZi(operatePlayer) && qingYiSeCheck);
    }

    // Check if the player wins with Super Qi Dui Zi
    public boolean superQiDuiZi(Player operatePlayer) {
        boolean victory = false;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> checkedTileSet = joinHandLockedTile(copyHandTile, copyLockedTile);
        if (rule.getPairNumber(checkedTileSet) == 7 && rule.getKongNumber(checkedTileSet) == 1 && rule.getTripletNumber(copyHandTile) == 0) {
            victory = true;
        }
        return victory;
    }

    // Basic Hu check (3*4+2 structure)
    public boolean basicHu(Player operatePlayer) {
        return kongFirstCheck(operatePlayer) || kongLastCheck(operatePlayer);
    }

    private boolean kongFirstCheck(Player operatePlayer) {
        return checkPlayerTiles(operatePlayer, true, true) || checkPlayerTiles(operatePlayer, true, false);
    }

    private boolean kongLastCheck(Player operatePlayer) {
        return checkPlayerTiles(operatePlayer, false, true) || checkPlayerTiles(operatePlayer, false, false);
    }

    private boolean checkPlayerTiles(Player operatePlayer, boolean isKongFirst, boolean isSequenceFirst) {
        boolean victory = false;
        int pair = 1;
        int nonPair = 4;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> checkedTileSet = joinHandLockedTile(copyHandTile, copyLockedTile);

        if (isKongFirst) {
            nonPair -= rule.getKongNumber(checkedTileSet);
            for (ArrayList<Tile> tileSet : checkedTileSet) {
                tileSet.removeAll(rule.checkKong(tileSet));
            }
        }

        if (isSequenceFirst) {
            nonPair -= rule.getSequenceNumber(checkedTileSet);
            for (ArrayList<Tile> tileSet : checkedTileSet) {
                tileSet.removeAll(rule.checkSequence(tileSet));
            }

            nonPair -= rule.getTripletNumber(checkedTileSet);
            for (ArrayList<Tile> tileSet : checkedTileSet) {
                tileSet.removeAll(rule.checkTriplet(tileSet));
            }
        } else {
            nonPair -= rule.getTripletNumber(checkedTileSet);
            for (ArrayList<Tile> tileSet : checkedTileSet) {
                tileSet.removeAll(rule.checkTriplet(tileSet));
            }

            nonPair -= rule.getSequenceNumber(checkedTileSet);
            for (ArrayList<Tile> tileSet : checkedTileSet) {
                tileSet.removeAll(rule.checkSequence(tileSet));
            }
        }

        if (!isKongFirst) {
            nonPair -= rule.getKongNumber(checkedTileSet);
            for (ArrayList<Tile> tileSet : checkedTileSet) {
                tileSet.removeAll(rule.checkKong(tileSet));
            }
        }

        pair -= rule.getPairNumber(checkedTileSet);

        if (pair == 0 && nonPair == 0) {
            victory = true;
        }
        return victory;
    }
}
