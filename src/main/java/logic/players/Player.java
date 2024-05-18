package logic.players;

import logic.factories.TilesSetsFactory;
import logic.factories.TilesSetsFactoryProducer;
import logic.rules.Rule;
import logic.tiles.Tile;
import logic.tilesSets.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 21L;

    private HandTileSet handTileSet;
    private LockedTileSet lockedTileSet;
    private PlayedTileSet playedTileSet;
    private String name;
    private boolean dealerState;
    private boolean turnState;
    private boolean canGet;
    private boolean canDiscard;
    private boolean canChow;
    private boolean canPong;
    private boolean canBrightKong;
    private boolean canDarkKong;
    private boolean canHu;
    private boolean skip;
    private int scoring;

    // Constructor to initialize a player with a name
    public Player(String player) {
        TilesSetsFactory singleArrayListTileSet = TilesSetsFactoryProducer.getTilesSetFactory(true);
        TilesSetsFactory doubleArrayListTileSet = TilesSetsFactoryProducer.getTilesSetFactory(false);
        handTileSet = (HandTileSet) doubleArrayListTileSet.getTileSet("HandTile");
        lockedTileSet = (LockedTileSet) doubleArrayListTileSet.getTileSet("LockedTile");
        playedTileSet = (PlayedTileSet) singleArrayListTileSet.getTileSet("PlayedTile");
        name = player;
        dealerState = false;
        turnState = false;
        canGet = false;
        canDiscard = false;
        canChow = false;
        canPong = false;
        canBrightKong = false;
        canDarkKong = false;
        canHu = false;
        skip = false;
        scoring = 0;
    }

    // Getters for player states
    public String getName() {
        return name;
    }

    public boolean getDealerState() {
        return dealerState;
    }

    public boolean getTurnState() {
        return turnState;
    }

    public boolean getCanGet() {
        return canGet;
    }

    public boolean getCanDiscard() {
        return canDiscard;
    }

    public boolean getCanChow() {
        return canChow;
    }

    public boolean getCanPong() {
        return canPong;
    }

    public boolean getCanBrightKong() {
        return canBrightKong;
    }

    public boolean getCanDarkKong() {
        return canDarkKong;
    }

    public boolean getCanHu() {
        return canHu;
    }

    public boolean getSkip() {
        return skip;
    }

    public int getScoring() {
        return scoring;
    }

    // Setters for player states
    public void setDealerState(boolean b) {
        dealerState = b;
    }

    public void setTurnState(boolean b) {
        turnState = b;
    }

    public void setCanGet(boolean b) {
        canGet = b;
    }

    public void setCanDiscard(boolean b) {
        canDiscard = b;
    }

    public void setCanChow(boolean b) {
        canChow = b;
    }

    public void setCanPong(boolean b) {
        canPong = b;
    }

    public void setCanBrightKong(boolean b) {
        canBrightKong = b;
    }

    public void setCanDarkKong(boolean b) {
        canDarkKong = b;
    }

    public void setCanHu(boolean b) {
        canHu = b;
    }

    public void setScoring(int s) {
        scoring = s;
    }

    public void setSkip(boolean b) {
        skip = b;
    }

    // Player actions
    public void getTile(Tile tile) {
        handTileSet.addTile(tile);
    }

    public Tile discard(String tileType) {
        return handTileSet.discardTile(tileType);
    }

    public void chow(Tile tile, String type) {
        handTileSet.setEndSecondGetTile(handTileSet.getEndGetTile());
        handTileSet.setEndGetTile(tile);

        ArrayList<Tile> targetSubHandTileSet = null;
        targetSubHandTileSet = setTargetSubHandTileSet(handTileSet.getTileSets(), tile, targetSubHandTileSet);
        chowByTypes(tile, type, targetSubHandTileSet, lockedTileSet);
    }

    private ArrayList<Tile> setTargetSubHandTileSet(ArrayList<ArrayList<Tile>> handTileSets, Tile tile, ArrayList<Tile> targetTileSet) {
        String tileType = tile.getType();
        if (tileType.equals("Character")) {
            targetTileSet = handTileSets.get(0);
        } else if (tileType.equals("Bamboo")) {
            targetTileSet = handTileSets.get(1);
        } else if (tileType.equals("Dot")) {
            targetTileSet = handTileSets.get(2);
        }
        return targetTileSet;
    }

    private void chowByTypes(Tile tile, String type, ArrayList<Tile> targetTileSet, LockedTileSet lockedTileSets) {
        int magnitude = tile.getMagnitude();
        ArrayList<Tile> removedTile;
        ArrayList<Tile> chowTileSet = lockedTileSets.getTileSets().get(0);
        boolean check1 = false;
        boolean check2 = false;

        if (Objects.equals(type, "1")) {
            removedTile = new ArrayList<>();
            for (Tile targetTile : targetTileSet) {
                if (targetTile.getMagnitude() == magnitude - 1 && !check1) {
                    chowTileSet.add(targetTile);
                    removedTile.add(targetTile);
                    check1 = true;
                }
                if (targetTile.getMagnitude() == magnitude - 2 && !check2) {
                    chowTileSet.add(targetTile);
                    removedTile.add(targetTile);
                    check2 = true;
                }
            }
            targetTileSet.removeAll(removedTile);
            chowTileSet.add(tile);
        } else if (Objects.equals(type, "2")) {
            removedTile = new ArrayList<>();
            for (Tile targetTile : targetTileSet) {
                if (targetTile.getMagnitude() == magnitude - 1 && !check1) {
                    chowTileSet.add(targetTile);
                    chowTileSet.add(tile);
                    removedTile.add(targetTile);
                    check1 = true;
                }
                if (targetTile.getMagnitude() == magnitude + 1 && !check2) {
                    chowTileSet.add(targetTile);
                    removedTile.add(targetTile);
                    check2 = true;
                }
            }
            targetTileSet.removeAll(removedTile);
        } else if (Objects.equals(type, "3")) {
            removedTile = new ArrayList<>();
            chowTileSet.add(tile);
            for (Tile targetTile : targetTileSet) {
                if (targetTile.getMagnitude() == magnitude + 1 && !check1) {
                    chowTileSet.add(targetTile);
                    removedTile.add(targetTile);
                    check1 = true;
                }
                if (targetTile.getMagnitude() == magnitude + 2 && !check2) {
                    chowTileSet.add(targetTile);
                    removedTile.add(targetTile);
                    check2 = true;
                }
            }
            targetTileSet.removeAll(removedTile);
        }
    }

    public void pong(Tile tile) {
        handTileSet.setEndSecondGetTile(handTileSet.getEndGetTile());
        handTileSet.setEndGetTile(tile);
        ArrayList<Tile> pongTileSet = lockedTileSet.getTileSets().get(1);
        pongTileSet.add(tile);

        int count = 0;
        for (ArrayList<Tile> tileSet : handTileSet.getTileSets()) {
            ArrayList<Tile> removedTile = new ArrayList<>();
            for (Tile targetTile : tileSet) {
                if (Objects.equals(targetTile.getType(), tile.getType()) && targetTile.getMagnitude() == tile.getMagnitude()) {
                    if (count < 2) {
                        count++;
                        pongTileSet.add(targetTile);
                        removedTile.add(targetTile);
                    }
                }
            }
            tileSet.removeAll(removedTile);
        }
    }

    public void brightKong(Tile tile) {
        handTileSet.setEndSecondGetTile(handTileSet.getEndGetTile());
        handTileSet.setEndGetTile(tile);
        handTileSet.setEndKongTile(tile);
        ArrayList<Tile> brightKongTileSet = lockedTileSet.getTileSets().get(2);
        brightKongTileSet.add(tile);

        int count = 0;
        for (ArrayList<Tile> tileSet : handTileSet.getTileSets()) {
            ArrayList<Tile> removedTile = new ArrayList<>();
            for (Tile targetTile : tileSet) {
                if (Objects.equals(targetTile.getType(), tile.getType()) && targetTile.getMagnitude() == tile.getMagnitude()) {
                    if (count < 3) {
                        count++;
                        brightKongTileSet.add(targetTile);
                        removedTile.add(targetTile);
                    }
                }
            }
            tileSet.removeAll(removedTile);
        }
    }

    public void Kong() {
        darkKong();
    }

    private void darkKong() {
        Rule rule = Rule.getInstance();
        ArrayList<Tile> newKongSet = new ArrayList<>();
        ArrayList<Tile> kongSet = lockedTileSet.getTileSets().get(3);
        for (ArrayList<Tile> tileArrayList : handTileSet.getTileSets()) {
            newKongSet.addAll(rule.checkKong(tileArrayList));
        }
        int count = 0;
        for (ArrayList<Tile> tileSet : handTileSet.getTileSets()) {
            ArrayList<Tile> removedTile = new ArrayList<>();
            for (Tile tile : tileSet) {
                if (Objects.equals(tile.getType(), newKongSet.get(0).getType()) && tile.getMagnitude() == newKongSet.get(0).getMagnitude()) {
                    if (count < 4) {
                        count++;
                        handTileSet.setEndKongTile(tile);
                        kongSet.add(tile);
                        removedTile.add(tile);
                    }
                }
            }
            tileSet.removeAll(removedTile);
        }
    }

    // Getters for player's tile sets
    public HandTileSet getHandTileSet() {
        return handTileSet;
    }

    public LockedTileSet getLockedTileSet() {
        return lockedTileSet;
    }

    public PlayedTileSet getPlayedTileSet() {
        return playedTileSet;
    }
}
