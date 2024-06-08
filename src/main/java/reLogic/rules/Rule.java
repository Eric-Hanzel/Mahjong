package reLogic.rules;

import reLogic.tiles.Tile;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

// Class for defining and checking rules in the Mahjong game
public class Rule {

    // Returns the single instance of Rule
    private static Rule uniqueInstance = null;

    public static Rule getInstance(){
        if (uniqueInstance == null){
            uniqueInstance = new Rule();
        }
        return uniqueInstance;
    }

    // // Private constructor to prevent instantiation
    private Rule(){

    }

    // Checks and returns a list of tiles that form sequences (chow)
    public ArrayList<Tile> checkSequence(ArrayList<Tile> tileSet){
        ArrayList<Tile> sequenceSet = new ArrayList<>();
        ArrayList<Tile> checkingTileSet = new ArrayList<>(tileSet);
        // Check if there are enough tiles to form sequences
        if (checkingTileSet.size() >= 3){
            int i = 0;
            boolean sequence;
            // Iterate through the tiles to find sequences
            while (i < checkingTileSet.size()){
                sequence = false;
                // Check for sequences and remove them from the tile set
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
                // If no sequence found, remove the tile and check the next one
                if (!sequence){
                    checkingTileSet.remove(0);
                }
            }
        }
        return sequenceSet;
    }

    // Gets the number of sequences in a player's hand
    public int getSequenceNumber(ArrayList<ArrayList<Tile>> handTileSet){
        ArrayList<Tile> sequenceSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet){
            sequenceSet.addAll(checkSequence(tileSet));
        }
        return sequenceSet.size() / 3;
    }

    // Checks and returns a list of tiles that form triplets (pongs), excluding kongs
    public ArrayList<Tile> checkTriplet(ArrayList<Tile> tileSet){
        ArrayList<Tile> tripletSet = new ArrayList<>();
        ArrayList<Tile> checkingTileSet = new ArrayList<>(tileSet);
        if (checkingTileSet.size() >= 3){
            int i = 0;
            boolean triplet;
            boolean kong;
            // Iterate through the tiles to find triplets
            while (i < checkingTileSet.size()){
                triplet = false;
                kong = false;
                if (i + 2 < checkingTileSet.size()){
                    if (checkingTileSet.get(i+1).getMagnitude() == checkingTileSet.get(i).getMagnitude()&&Objects.equals(checkingTileSet.get(i + 1).getType(), checkingTileSet.get(i).getType())){
                        if (checkingTileSet.get(i+2).getMagnitude() == checkingTileSet.get(i).getMagnitude()&&Objects.equals(checkingTileSet.get(i + 2).getType(), checkingTileSet.get(i).getType())) {
                            if (i+3 < checkingTileSet.size()){
                                // Exclude kongs
                                if (checkingTileSet.get(i+3).getMagnitude() != checkingTileSet.get(i).getMagnitude()&&Objects.equals(checkingTileSet.get(i + 3).getType(), checkingTileSet.get(i).getType())){
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

    // Gets the number of triplets in a player's hand, excluding kongs
    public int getTripletNumber(ArrayList<ArrayList<Tile>> handTileSet){
        ArrayList<Tile> tripletSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet){
            tripletSet.addAll(checkTriplet(tileSet));
        }
        return tripletSet.size() / 3;
    }

    //  Checks and returns a list of tiles that form pairs
    public ArrayList<Tile> checkPair(ArrayList<Tile> tileSet){
        ArrayList<Tile> pairSet = new ArrayList<>();
        ArrayList<Tile> checkingTileSet = new ArrayList<>(tileSet);
        if (checkingTileSet.size() >= 2){
            int i = 0;
            boolean pair;
            // Iterate through the tiles to find pairs
            while (i < checkingTileSet.size()){
                pair = false;
                if (i + 1 < checkingTileSet.size()){
                    if (checkingTileSet.get(i+1).getMagnitude() == checkingTileSet.get(i).getMagnitude()&& Objects.equals(checkingTileSet.get(i + 1).getType(), checkingTileSet.get(i).getType())){
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

    // Gets the number of pairs in a player's hand
    public int getPairNumber(ArrayList<ArrayList<Tile>> handTileSet){
        ArrayList<Tile> pairSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet){
            pairSet.addAll(checkPair(tileSet));
        }
        return pairSet.size() / 2;
    }

    // Checks and returns a list of tiles that form kongs
    public ArrayList<reLogic.tiles.Tile> checkKong(ArrayList<reLogic.tiles.Tile> tileSet){
        ArrayList<reLogic.tiles.Tile> kongSet = new ArrayList<>();
        ArrayList<reLogic.tiles.Tile> checkingTileSet = new ArrayList<>(tileSet);
        if (checkingTileSet.size() >= 3){
            int i = 0;
            boolean kong;
            // Iterate through the tiles to find kongs
            while (i < checkingTileSet.size()){
                kong = false;
                if (i + 3 < checkingTileSet.size()){
                    if (checkingTileSet.get(i+1).getMagnitude() == checkingTileSet.get(i).getMagnitude()&& Objects.equals(checkingTileSet.get(i + 1).getType(), checkingTileSet.get(i).getType())){
                        if (checkingTileSet.get(i+2).getMagnitude() == checkingTileSet.get(i).getMagnitude()&& Objects.equals(checkingTileSet.get(i + 2).getType(), checkingTileSet.get(i).getType())) {
                            if (checkingTileSet.get(i+3).getMagnitude() == checkingTileSet.get(i).getMagnitude()&& Objects.equals(checkingTileSet.get(i + 3).getType(), checkingTileSet.get(i).getType())){
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

    // Gets the number of kongs in a player's hand
    public int getKongNumber(ArrayList<ArrayList<reLogic.tiles.Tile>> handTileSet){
        ArrayList<reLogic.tiles.Tile> kongSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet){
            kongSet.addAll(checkKong(tileSet));
        }
        return kongSet.size() / 4;
    }
}
