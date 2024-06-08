package reLogic.rules;

import reLogic.players.Player;
import reLogic.tiles.Tile;
import reLogic.tilesSets.HandTileSet;
import reLogic.tilesSets.LibraryTileSet;

import java.util.ArrayList;
import java.util.Objects;

// Class for checking victory conditions in the Mahjong game
public class VictoryCheckRule{
    private static VictoryCheckRule uniqueInstance = null;
    private Rule rule;

    // Returns the single instance of VictoryCheckRule
    public static VictoryCheckRule getInstance(){
        if (uniqueInstance == null){
            uniqueInstance = new VictoryCheckRule();
        }
        return uniqueInstance;
    }

    // Private constructor to prevent instantiation
    private VictoryCheckRule(){
        rule = Rule.getInstance();
    }

    // Combines hand tiles and locked tiles into a single list
    private ArrayList<ArrayList<reLogic.tiles.Tile>> joinHandLockedTile(ArrayList<ArrayList<reLogic.tiles.Tile>> handTileArrayList, ArrayList<ArrayList<reLogic.tiles.Tile>> lockedTileArrayList){
        ArrayList<ArrayList<reLogic.tiles.Tile>> copyHandTileArrayList = VictoryCheckRule.deepCopyTileSet(handTileArrayList);
        for (ArrayList<reLogic.tiles.Tile> tileArrayList:lockedTileArrayList){
            for (reLogic.tiles.Tile tile:tileArrayList){
                if (tile.getType() == "Character"){
                    copyHandTileArrayList.get(0).add(tile);
                }else if (tile.getType() == "Bamboo"){
                    copyHandTileArrayList.get(1).add(tile);
                }else if (tile.getType() == "Dot"){
                    copyHandTileArrayList.get(2).add(tile);
                }else {
                    copyHandTileArrayList.get(3).add(tile);
                }
            }
        }
        for (ArrayList<Tile> tileArrayList:copyHandTileArrayList){
            tileArrayList.sort(null);
        }
        return copyHandTileArrayList;
    }

    // Creates a deep copy of the provided tile set, removing null elements
    public static ArrayList<ArrayList<reLogic.tiles.Tile>> deepCopyTileSet(ArrayList<ArrayList<reLogic.tiles.Tile>> original) {
        ArrayList<ArrayList<reLogic.tiles.Tile>> copy = new ArrayList<>();
        for (ArrayList<reLogic.tiles.Tile> tileSet : original) {
            // Create a new ArrayList for each tileSet, removing null elements
            ArrayList<reLogic.tiles.Tile> newTileSet = new ArrayList<>();
            for (reLogic.tiles.Tile tile : tileSet) {
                if (tile != null) {
                    newTileSet.add(tile);
                }
            }
            copy.add(newTileSet);
        }

        // Sort each new tile set
        for (ArrayList<Tile> tileArrayList : copy) {
            if (tileArrayList != null) {
                tileArrayList.sort(null);
            }
        }

        return copy;
    }

    // Checks if the player has an "All Triplets" hand (four triplets and a pair)
    public boolean allTriplets(Player operatePlayer){
        boolean victory = false;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> checkedTileSet = joinHandLockedTile(copyHandTile,copyLockedTile);

        if (rule.getTripletNumber(checkedTileSet) == 4){
            for (ArrayList<Tile> tileSet : checkedTileSet){
                tileSet.removeAll(rule.checkTriplet(tileSet));
            }
            if (rule.getPairNumber(checkedTileSet) == 1){
                victory = true;
            }
        }
        return victory;
    }

    // Checks if the player has a "Big Single Wait" hand
    public boolean bigSingleWait(Player operatePlayer){
        boolean victory = false;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        if (copyLockedTile.get(0).size()/3+copyLockedTile.get(1).size()/3==4){
            if (rule.getPairNumber(copyHandTile) == 1){
                victory = true;
            }
        }
        return victory;
    }

    // Checks if the player has a "Zhuo Wu Kui" hand
    public boolean zhuoWuKui(Player operatePlayer) {
        boolean victory = false;
        int pair = 1;
        int nonPair = 4;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());

        nonPair = nonPair  - copyLockedTile.get(2).size()/4-copyLockedTile.get(3).size()/4;


        nonPair = nonPair - rule.getSequenceNumber(copyHandTile) - copyLockedTile.get(0).size()/3;
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(rule.checkSequence(tileSet));
        }

        nonPair = nonPair - rule.getTripletNumber(copyHandTile)-copyLockedTile.get(1).size()/3;
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(rule.checkTriplet(tileSet));
        }


        pair = pair - rule.getPairNumber(copyHandTile);
        if (pair == 0 && nonPair == 0 && Objects.equals(operatePlayer.getHandTileSet().getEndGetTile().toString(), "5Character")){
            victory = true;
        }
        return victory;
    }

    // Checks if the player has a "Hai Di Lao Yue" hand
    public boolean haiDiLaoYue(Player operatePlayer, LibraryTileSet libraryTileSet){
        boolean victory = false;
        if (libraryTileSet.getTileNumber()==0){
            int pair = 1;
            int nonPair = 4;
            ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
            ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());

            nonPair = nonPair  - copyLockedTile.get(2).size()/4-copyLockedTile.get(3).size()/4;



            nonPair = nonPair - rule.getSequenceNumber(copyHandTile) - copyLockedTile.get(0).size()/3;
            for (ArrayList<Tile> tileSet : copyHandTile){
                tileSet.removeAll(rule.checkSequence(tileSet));
            }

            nonPair = nonPair - rule.getTripletNumber(copyHandTile)-copyLockedTile.get(1).size()/3;
            for (ArrayList<Tile> tileSet : copyHandTile){
                tileSet.removeAll(rule.checkTriplet(tileSet));
            }

            pair = pair - rule.getPairNumber(copyHandTile);

            if (pair == 0 && nonPair == 0){
                victory = true;
            }
        }
        return victory;
    }

    // Checks if the player has a "Yi Tiao Long" hand
    public boolean yiTiaoLong(Player operatePlayer) {
        boolean victory = false;
        boolean yiTiaoLong = false;

        int pair = 1;
        int nonPair = 4;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> checkedTileSet = joinHandLockedTile(copyHandTile, copyLockedTile);

        for (ArrayList<Tile> tileSet : checkedTileSet){
            int j = 1;
            for (Tile tile: tileSet){
                if (tile.getMagnitude()==j){
                    j++;
                }
            }
            if (j == 10){
                yiTiaoLong = true;
            }
        }

        nonPair = nonPair  - copyLockedTile.get(2).size()/4-copyLockedTile.get(3).size()/4;



        nonPair = nonPair - rule.getSequenceNumber(copyHandTile) - copyLockedTile.get(0).size()/3;
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(rule.checkSequence(tileSet));
        }

        nonPair = nonPair - rule.getTripletNumber(copyHandTile)-copyLockedTile.get(1).size()/3;
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(rule.checkTriplet(tileSet));
        }

        pair = pair - rule.getPairNumber(copyHandTile);


        if (pair == 0 && nonPair == 0 && yiTiaoLong){
            victory = true;
        }
        return victory;

    }

    // Checks if the player has a "Qi Dui Zi" hand
    public boolean qiDuiZi(Player operatePlayer){
        boolean victory = false;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        if (rule.getPairNumber(copyHandTile) == 7 && rule.getKongNumber(copyHandTile) == 0 && rule.getTripletNumber(copyHandTile) == 0){
            victory = true;
        }
        return victory;
    }

    // Checks if the player has a "Gang Shang Kai Hua" hand
    public boolean gangShangKaiHua(Player operatePlayer){
        boolean victory = false;
        int pair = 1;
        int nonPair = 4;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());

        nonPair = nonPair  - copyLockedTile.get(2).size()/4-copyLockedTile.get(3).size()/4;

        nonPair = nonPair - rule.getSequenceNumber(copyHandTile) - copyLockedTile.get(0).size()/3;
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(rule.checkSequence(tileSet));
        }

        nonPair = nonPair - rule.getTripletNumber(copyHandTile)-copyLockedTile.get(1).size()/3;
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(rule.checkTriplet(tileSet));
        }

        pair = pair - rule.getPairNumber(copyHandTile);

        Tile endKongTile = operatePlayer.getHandTileSet().getEndKongTile();
        if (endKongTile!=null){
            if (pair == 0 && nonPair == 0 && Objects.equals(operatePlayer.getHandTileSet().getEndSecondGetTile().toString(), endKongTile.toString())){
                victory = true;
            }
        }
        return victory;
    }

    // Checks if the player has a "Qing Yi Se" hand
    public boolean qingYiSe(Player operatePlayer){
        boolean victory = false;
        boolean qingYiSe = false;
        int pair = 1;
        int nonPair = 4;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> checkedTileSet = joinHandLockedTile(copyHandTile, copyLockedTile);


        if (!checkedTileSet.get(0).isEmpty() && checkedTileSet.get(1).isEmpty() && checkedTileSet.get(2).isEmpty() && checkedTileSet.get(3).isEmpty()){
            qingYiSe = true;
        }
        if (checkedTileSet.get(0).isEmpty() && !checkedTileSet.get(1).isEmpty() && checkedTileSet.get(2).isEmpty() && checkedTileSet.get(3).isEmpty()){
            qingYiSe = true;
        }
        if (checkedTileSet.get(0).isEmpty() && checkedTileSet.get(1).isEmpty() && !checkedTileSet.get(2).isEmpty() && checkedTileSet.get(3).isEmpty()){
            qingYiSe = true;
        }

        nonPair = nonPair  - copyLockedTile.get(2).size()/4-copyLockedTile.get(3).size()/4;

        nonPair = nonPair - rule.getSequenceNumber(copyHandTile) - copyLockedTile.get(0).size()/3;
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(rule.checkSequence(tileSet));
        }

        nonPair = nonPair - rule.getTripletNumber(copyHandTile)-copyLockedTile.get(1).size()/3;
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(rule.checkTriplet(tileSet));
        }

        pair = pair - rule.getPairNumber(copyHandTile);


        pair = pair - rule.getPairNumber(copyHandTile);
        if (pair == 0 && nonPair == 0 && qingYiSe){
            victory = true;
        }
        return victory;
    }


    // Checks if the player has a "Super Qi Dui Zi" hand
    public boolean superQiDuiZi(Player operatePlayer){
        boolean victory = false;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> checkedTileSet = joinHandLockedTile(copyHandTile, copyLockedTile);
        if (rule.getPairNumber(checkedTileSet) == 7 && rule.getKongNumber(checkedTileSet) == 1 && rule.getTripletNumber(copyHandTile) == 0){
            victory = true;
        }
        return victory;
    }

    // Checks if the player has a "Basic Hu" hand
    public boolean basicHu(Player operatePlayer){
        boolean victory = false;
        int pair = 1;
        int nonPair = 4;
        ArrayList<ArrayList<Tile>> copyHandTile = deepCopyTileSet(operatePlayer.getHandTileSet().getTileSets());
        ArrayList<ArrayList<Tile>> copyLockedTile = deepCopyTileSet(operatePlayer.getLockedTileSet().getTileSets());
        System.out.println(copyHandTile);
        System.out.println(copyLockedTile);
        nonPair = nonPair  - copyLockedTile.get(2).size()/4-copyLockedTile.get(3).size()/4;
        System.out.println(nonPair);

        nonPair = nonPair - rule.getSequenceNumber(copyHandTile) - copyLockedTile.get(0).size()/3;
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(rule.checkSequence(tileSet));
        }
        System.out.println(nonPair);
        nonPair = nonPair - rule.getTripletNumber(copyHandTile)-copyLockedTile.get(1).size()/3;
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(rule.checkTriplet(tileSet));
        }

        System.out.println(nonPair);
        System.out.println(copyHandTile);
        System.out.println(copyLockedTile);
        pair = pair - rule.getPairNumber(copyHandTile);
        if (pair == 0 && nonPair == 0){
            victory = true;
        }
        System.out.println(copyHandTile);
        System.out.println(copyLockedTile);
        System.out.println(pair);
        System.out.println(nonPair);
        return victory;

    }

}
