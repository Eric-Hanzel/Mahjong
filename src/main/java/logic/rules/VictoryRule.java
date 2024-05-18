package logic.rules;

import logic.tiles.HandTile;
import logic.tiles.LibraryTile;
import logic.tiles.Tile;

import java.util.ArrayList;

public class VictoryRule extends Rule{
    public boolean allTriplets(HandTile handTile){
        boolean victory = false;
        ArrayList<ArrayList<Tile>> copyHandTile = new ArrayList<ArrayList<Tile>>(handTile.getTileSet());
        if (getTripletNumber(copyHandTile) == 4){
            for (ArrayList<Tile> tileSet : copyHandTile){
                tileSet.removeAll(checkTriplet(tileSet));
            }
            if (getPairNumber(copyHandTile) == 1){
                victory = true;
            }
        }
        return victory;
    }

    public boolean bigSingleWait(HandTile handTile){
        boolean victory = false;
        int pair = 1;
        int nonPair = 4;
        ArrayList<ArrayList<Tile>> copyHandTile = new ArrayList<ArrayList<Tile>>(handTile.getTileSet());
        nonPair = nonPair - getKongNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkKong(tileSet));
        }
        nonPair = nonPair - getSequenceNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkSequence(tileSet));
        }
        nonPair = nonPair - getTripletNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkTriplet(tileSet));
        }
        pair = pair - getPairNumber(copyHandTile);
        if (pair == 0 && nonPair == 0){
            victory = true;
        }
        return victory;
    }

    public boolean zhuoWuKui(HandTile handTile) {
        boolean victory = false;
        int pair = 1;
        int nonPair = 4;
        ArrayList<ArrayList<Tile>> copyHandTile = new ArrayList<ArrayList<Tile>>(handTile.getTileSet());
        nonPair = nonPair - getKongNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkKong(tileSet));
        }
        nonPair = nonPair - getSequenceNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkSequence(tileSet));
        }
        nonPair = nonPair - getTripletNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkTriplet(tileSet));
        }
        pair = pair - getPairNumber(copyHandTile);
        if (pair == 0 && nonPair == 0 && handTile.getEndTile().getType() == "Character" && handTile.getEndTile().getMagnitude() == 5){
            victory = true;
        }
        return victory;
    }

    public boolean haiDiLaoYue(HandTile handTile, LibraryTile libraryTile){
        boolean victory = false;
        if (libraryTile.getTileNumber()==0){
            int pair = 1;
            int nonPair = 4;
            ArrayList<ArrayList<Tile>> copyHandTile = new ArrayList<ArrayList<Tile>>(handTile.getTileSet());
            nonPair = nonPair - getKongNumber(copyHandTile);
            for (ArrayList<Tile> tileSet : copyHandTile){
                tileSet.removeAll(checkKong(tileSet));
            }
            nonPair = nonPair - getSequenceNumber(copyHandTile);
            for (ArrayList<Tile> tileSet : copyHandTile){
                tileSet.removeAll(checkSequence(tileSet));
            }
            nonPair = nonPair - getTripletNumber(copyHandTile);
            for (ArrayList<Tile> tileSet : copyHandTile){
                tileSet.removeAll(checkTriplet(tileSet));
            }
            pair = pair - getPairNumber(copyHandTile);
            if (pair == 0 && nonPair == 0){
                victory = true;
            }
        }
        return victory;
    }

    public boolean yiTiaoLong(HandTile handTile){
        boolean victory = false;
        boolean yiTiaoLong = false;
        int pair = 1;
        int nonPair = 4;
        ArrayList<ArrayList<Tile>> copyHandTile = new ArrayList<ArrayList<Tile>>(handTile.getTileSet());

        if (getSequenceNumber(copyHandTile) >= 3){
            for (ArrayList<Tile> tileSet : copyHandTile){
                ArrayList<Tile> checkingTileSet = checkSequence(tileSet);
                int i = 1;
                for (Tile tile: checkingTileSet){
                   if (tile.getMagnitude()==i){
                       i++;
                   }
                }
                if (i == 10){
                    yiTiaoLong = true;
                }
            }
        }

        nonPair = nonPair - getKongNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkKong(tileSet));
        }
        nonPair = nonPair - getSequenceNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkSequence(tileSet));
        }
        nonPair = nonPair - getTripletNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkTriplet(tileSet));
        }
        pair = pair - getPairNumber(copyHandTile);
        if (pair == 0 && nonPair == 0 && yiTiaoLong){
            victory = true;
        }
        return victory;
    }


    public boolean qiDuiZi(HandTile handTile){
        boolean victory = false;
        ArrayList<ArrayList<Tile>> copyHandTile = new ArrayList<ArrayList<Tile>>(handTile.getTileSet());
        if (getPairNumber(copyHandTile) == 7 && getKongNumber(copyHandTile) == 0 && getTripletNumber(copyHandTile) == 0){
            victory = true;
        }
        return victory;
    }
    // 杠
    public boolean gangShangKaiHua(HandTile handTile){
        boolean victory = false;

        return victory;
    }

    public boolean qingYiSe(HandTile handTile){
        boolean victory = false;
        boolean qingYiSe = false;
        int pair = 1;
        int nonPair = 4;
        ArrayList<ArrayList<Tile>> copyHandTile = new ArrayList<ArrayList<Tile>>(handTile.getTileSet());

        if (copyHandTile.size() == 1){
            qingYiSe = true;
        }

        nonPair = nonPair - getKongNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkKong(tileSet));
        }
        nonPair = nonPair - getSequenceNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkSequence(tileSet));
        }
        nonPair = nonPair - getTripletNumber(copyHandTile);
        for (ArrayList<Tile> tileSet : copyHandTile){
            tileSet.removeAll(checkTriplet(tileSet));
        }
        pair = pair - getPairNumber(copyHandTile);
        if (pair == 0 && nonPair == 0 && qingYiSe){
            victory = true;
        }
        return victory;
    }
    //暗杠
    public boolean superQiDuiZi(HandTile handTile){
        boolean victory = false;
        ArrayList<ArrayList<Tile>> copyHandTile = new ArrayList<ArrayList<Tile>>(handTile.getTileSet());
        if (getPairNumber(copyHandTile) == 7 && getKongNumber(copyHandTile) == 1 && getTripletNumber(copyHandTile) == 0){
            victory = true;
        }
        return victory;
    }
}
