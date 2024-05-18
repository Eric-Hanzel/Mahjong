package logic.rules;

import logic.tiles.Tile;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Rule implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;

    // 传入一个类型的牌，检查顺子数量
    public ArrayList<Tile> checkSequence(ArrayList<Tile> tileSet){
        ArrayList<Tile> sequenceSet = new ArrayList<>();
        ArrayList<Tile> checkingTileSet = new ArrayList<>(tileSet);
        if (checkingTileSet.size() >= 3){
            int i = 0;
            boolean sequence;
            while (i < checkingTileSet.size()){
                sequence = false;
                for (int j = 1; j < checkingTileSet.size() && !sequence; j++){
                    if (checkingTileSet.get(i).getMagnitude() == checkingTileSet.get(j).getMagnitude() - 1) {
                        for (int k = j + 1; k < checkingTileSet.size() && !sequence; k++) {
                            if (checkingTileSet.get(j).getMagnitude() == checkingTileSet.get(k).getMagnitude() - 1) {
                                sequenceSet.add(checkingTileSet.get(i));
                                sequenceSet.add(checkingTileSet.get(j));
                                sequenceSet.add(checkingTileSet.get(k));
                                checkingTileSet.remove(k);
                                checkingTileSet.remove(j);
                                checkingTileSet.remove(i);
                                sequence = true;
                            }
                        }
                    }
                }
                if (!sequence){
                    checkingTileSet.remove(0);
                }
            }
        }
        return sequenceSet;
    }
    // 获取手牌顺子数量
    public int getSequenceNumber(ArrayList<ArrayList<Tile>> handTileSet){
        ArrayList<Tile> sequenceSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet){
            sequenceSet.addAll(checkSequence(tileSet));
        }
        return sequenceSet.size() / 3;
    }
    // 获取手牌吃的数量，只检查锁的牌
    public int getChowNumber(ArrayList<ArrayList<Tile>> handTileSet){
        ArrayList<Tile> sequenceSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet){
            for (Tile tile: tileSet){
                if (!tile.getLock()){
                    tileSet.remove(tile);
                }
            }
            sequenceSet.addAll(checkSequence(tileSet));
        }
        return sequenceSet.size() / 3;
    }


    // 杠不算在内
    public ArrayList<Tile> checkTriplet(ArrayList<Tile> tileSet){
        ArrayList<Tile> tripletSet = new ArrayList<>();
        ArrayList<Tile> checkingTileSet = new ArrayList<>(tileSet);
        if (checkingTileSet.size() >= 3){
            int i = 0;
            boolean triplet;
            boolean kong;
            while (i < checkingTileSet.size()){
                triplet = false;
                kong = false;
                if (i + 2 < checkingTileSet.size()){
                    if (checkingTileSet.get(i+1).getMagnitude() == checkingTileSet.get(i).getMagnitude()){
                        if (checkingTileSet.get(i+2).getMagnitude() == checkingTileSet.get(i).getMagnitude()) {
                            if (i+3 < checkingTileSet.size()){
                                if (checkingTileSet.get(i+3).getMagnitude() != checkingTileSet.get(i).getMagnitude()){
                                    tripletSet.add(checkingTileSet.get(i));
                                    tripletSet.add(checkingTileSet.get(i+1));
                                    tripletSet.add(checkingTileSet.get(i+2));
                                    checkingTileSet.remove(i+2);
                                    checkingTileSet.remove(i+1);
                                    checkingTileSet.remove(i);
                                    triplet = true;
                                }else {
                                    kong = true;
                                }
                            }else {
                                tripletSet.add(checkingTileSet.get(i));
                                tripletSet.add(checkingTileSet.get(i+1));
                                tripletSet.add(checkingTileSet.get(i+2));
                                checkingTileSet.remove(i+2);
                                checkingTileSet.remove(i+1);
                                checkingTileSet.remove(i);
                                triplet = true;
                            }
                        }
                    }
                }
                if (!triplet && !kong){
                    checkingTileSet.remove(0);
                }
                if (kong){
                    for (int n = 3; n > -1; n--){
                        checkingTileSet.remove(n);
                    }
                }
            }
        }
        return tripletSet;
    }
    // 仅碰，没有考虑杠
    public int getTripletNumber(ArrayList<ArrayList<Tile>> handTileSet){
        ArrayList<Tile> tripletSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet){
            tripletSet.addAll(checkTriplet(tileSet));
        }
        return tripletSet.size() / 3;
    }




    public ArrayList<Tile> checkPair(ArrayList<Tile> tileSet){
        ArrayList<Tile> pairSet = new ArrayList<>();
        ArrayList<Tile> checkingTileSet = new ArrayList<>(tileSet);
        if (checkingTileSet.size() >= 2){
            int i = 0;
            boolean pair;
            while (i < checkingTileSet.size()){
                pair = false;
                if (i + 1 < checkingTileSet.size()){
                    if (checkingTileSet.get(i+1).getMagnitude() == checkingTileSet.get(i).getMagnitude()){
                        pairSet.add(checkingTileSet.get(i));
                        pairSet.add(checkingTileSet.get(i+1));
                        checkingTileSet.remove(i+1);
                        checkingTileSet.remove(i);
                        pair = true;
                    }
                }
                if (!pair){
                    checkingTileSet.remove(0);
                }
            }
        }
        return pairSet;
    }

    public int getPairNumber(ArrayList<ArrayList<Tile>> handTileSet){
        ArrayList<Tile> pairSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet){
            pairSet.addAll(checkPair(tileSet));
        }
        return pairSet.size() / 2;
    }





    public ArrayList<Tile> checkKong(ArrayList<Tile> tileSet){
        ArrayList<Tile> kongSet = new ArrayList<>();
        ArrayList<Tile> checkingTileSet = new ArrayList<>(tileSet);
        if (checkingTileSet.size() >= 3){
            int i = 0;
            boolean kong;
            while (i < checkingTileSet.size()){
                kong = false;
                if (i + 3 < checkingTileSet.size()){
                    if (checkingTileSet.get(i+1).getMagnitude() == checkingTileSet.get(i).getMagnitude()){
                        if (checkingTileSet.get(i+2).getMagnitude() == checkingTileSet.get(i).getMagnitude()) {
                            if (checkingTileSet.get(i+3).getMagnitude() == checkingTileSet.get(i).getMagnitude()){
                                kongSet.add(checkingTileSet.get(i));
                                kongSet.add(checkingTileSet.get(i+1));
                                kongSet.add(checkingTileSet.get(i+2));
                                kongSet.add(checkingTileSet.get(i+3));
                                checkingTileSet.remove(i+3);
                                checkingTileSet.remove(i+2);
                                checkingTileSet.remove(i+1);
                                checkingTileSet.remove(i);
                                kong = true;
                            }
                        }
                    }
                }
                if (!kong){
                    checkingTileSet.remove(0);
                }
            }
        }
        return kongSet;
    }

    public int getKongNumber(ArrayList<ArrayList<Tile>> handTileSet){
        ArrayList<Tile> kongSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet){
            kongSet.addAll(checkKong(tileSet));
        }
        return kongSet.size() / 4;
    }




    public boolean checkBurningDealer(){

        return true;
    }


}
