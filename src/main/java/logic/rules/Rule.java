package logic.rules;

import logic.tiles.Tile;

import java.util.ArrayList;
import java.util.Objects;

public class Rule {

    private static Rule uniqueInstance = null;

    // Singleton pattern to ensure only one instance of Rule exists
    public static Rule getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Rule();
        }
        return uniqueInstance;
    }

    // Private constructor for Singleton pattern
    private Rule() {
    }

    // Return a new list of sequences (Shun Zi)
    public ArrayList<Tile> checkSequence(ArrayList<Tile> tileSet) {
        ArrayList<Tile> sequenceSet = new ArrayList<>();
        ArrayList<Tile> checkingTileSet = new ArrayList<>(tileSet);

        // Check if size is greater than or equal to 3
        if (checkingTileSet.size() >= 3) {
            int i = 0;
            boolean sequence;

            // Iterate through all tiles
            while (i < checkingTileSet.size()) {
                sequence = false;

                // Identify i, j, k positions, remove from the original hand copy, and add to the sequence set
                for (int j = i + 1; j < checkingTileSet.size() && !sequence; j++) {
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

                // If no sequence is found, remove and check the next one
                if (!sequence) {
                    checkingTileSet.remove(0);
                }
            }
        }
        return sequenceSet;
    }

    // Get the number of sequences (Shun Zi) in the list
    public int getSequenceNumber(ArrayList<ArrayList<Tile>> handTileSet) {
        ArrayList<Tile> sequenceSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet) {
            sequenceSet.addAll(checkSequence(tileSet));
        }
        return sequenceSet.size() / 3;
    }

    // Exclude Kongs (Gang), return a new list of Triplets (Peng)
    public ArrayList<Tile> checkTriplet(ArrayList<Tile> tileSet) {
        ArrayList<Tile> tripletSet = new ArrayList<>();
        ArrayList<Tile> checkingTileSet = new ArrayList<>(tileSet);
        if (checkingTileSet.size() >= 3) {
            int i = 0;
            boolean triplet;
            boolean kong;
            while (i < checkingTileSet.size()) {
                triplet = false;
                kong = false;
                if (i + 2 < checkingTileSet.size()) {
                    if (checkingTileSet.get(i + 1).getMagnitude() == checkingTileSet.get(i).getMagnitude() && Objects.equals(checkingTileSet.get(i + 1).getType(), checkingTileSet.get(i).getType())) {
                        if (checkingTileSet.get(i + 2).getMagnitude() == checkingTileSet.get(i).getMagnitude() && Objects.equals(checkingTileSet.get(i + 2).getType(), checkingTileSet.get(i).getType())) {
                            if (i + 3 < checkingTileSet.size()) {
                                // Exclude Kongs (Gang)
                                if (checkingTileSet.get(i + 3).getMagnitude() != checkingTileSet.get(i).getMagnitude() && Objects.equals(checkingTileSet.get(i + 3).getType(), checkingTileSet.get(i).getType())) {
                                    tripletSet.add(checkingTileSet.get(i));
                                    tripletSet.add(checkingTileSet.get(i + 1));
                                    tripletSet.add(checkingTileSet.get(i + 2));
                                    checkingTileSet.remove(i + 2);
                                    checkingTileSet.remove(i + 1);
                                    checkingTileSet.remove(i);
                                    triplet = true;
                                } else {
                                    kong = true;
                                }
                            } else {
                                tripletSet.add(checkingTileSet.get(i));
                                tripletSet.add(checkingTileSet.get(i + 1));
                                tripletSet.add(checkingTileSet.get(i + 2));
                                checkingTileSet.remove(i + 2);
                                checkingTileSet.remove(i + 1);
                                checkingTileSet.remove(i);
                                triplet = true;
                            }
                        }
                    }
                }

                if (!triplet && !kong) {
                    checkingTileSet.remove(0);
                }
                if (kong) {
                    checkingTileSet.subList(0, 4).clear();
                }
            }
        }
        return tripletSet;
    }

    // Get the number of Triplets (Peng) in the list, excluding Kongs (Gang)
    public int getTripletNumber(ArrayList<ArrayList<Tile>> handTileSet) {
        ArrayList<Tile> tripletSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet) {
            tripletSet.addAll(checkTriplet(tileSet));
        }
        return tripletSet.size() / 3;
    }

    // Return a new list of pairs, considering Peng and Gang
    public ArrayList<Tile> checkPair(ArrayList<Tile> tileSet) {
        ArrayList<Tile> pairSet = new ArrayList<>();
        ArrayList<Tile> checkingTileSet = new ArrayList<>(tileSet);
        if (checkingTileSet.size() >= 2) {
            int i = 0;
            boolean pair;
            while (i < checkingTileSet.size()) {
                pair = false;
                if (i + 1 < checkingTileSet.size()) {
                    if (checkingTileSet.get(i + 1).getMagnitude() == checkingTileSet.get(i).getMagnitude() && Objects.equals(checkingTileSet.get(i + 1).getType(), checkingTileSet.get(i).getType())) {
                        pairSet.add(checkingTileSet.get(i));
                        pairSet.add(checkingTileSet.get(i + 1));
                        checkingTileSet.remove(i + 1);
                        checkingTileSet.remove(i);
                        pair = true;
                    }
                }
                if (!pair) {
                    checkingTileSet.remove(0);
                }
            }
        }
        return pairSet;
    }

    // Get the number of pairs in the list
    public int getPairNumber(ArrayList<ArrayList<Tile>> handTileSet) {
        ArrayList<Tile> pairSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet) {
            pairSet.addAll(checkPair(tileSet));
        }
        return pairSet.size() / 2;
    }

    // Return a new list of Kongs (Gang)
    public ArrayList<Tile> checkKong(ArrayList<Tile> tileSet) {
        ArrayList<Tile> kongSet = new ArrayList<>();
        ArrayList<Tile> checkingTileSet = new ArrayList<>(tileSet);
        if (checkingTileSet.size() >= 4) {
            int i = 0;
            boolean kong;
            while (i < checkingTileSet.size()) {
                kong = false;
                if (i + 3 < checkingTileSet.size()) {
                    if (checkingTileSet.get(i + 1).getMagnitude() == checkingTileSet.get(i).getMagnitude() && Objects.equals(checkingTileSet.get(i + 1).getType(), checkingTileSet.get(i).getType())) {
                        if (checkingTileSet.get(i + 2).getMagnitude() == checkingTileSet.get(i).getMagnitude() && Objects.equals(checkingTileSet.get(i + 2).getType(), checkingTileSet.get(i).getType())) {
                            if (checkingTileSet.get(i + 3).getMagnitude() == checkingTileSet.get(i).getMagnitude() && Objects.equals(checkingTileSet.get(i + 3).getType(), checkingTileSet.get(i).getType())) {
                                kongSet.add(checkingTileSet.get(i));
                                kongSet.add(checkingTileSet.get(i + 1));
                                kongSet.add(checkingTileSet.get(i + 2));
                                kongSet.add(checkingTileSet.get(i + 3));
                                checkingTileSet.remove(i + 3);
                                checkingTileSet.remove(i + 2);
                                checkingTileSet.remove(i + 1);
                                checkingTileSet.remove(i);
                                kong = true;
                            }
                        }
                    }
                }
                if (!kong) {
                    checkingTileSet.remove(0);
                }
            }
        }
        return kongSet;
    }

    // Get the number of Kongs (Gang) in the list
    public int getKongNumber(ArrayList<ArrayList<Tile>> handTileSet) {
        ArrayList<Tile> kongSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTileSet) {
            kongSet.addAll(checkKong(tileSet));
        }
        return kongSet.size() / 4;
    }
}
