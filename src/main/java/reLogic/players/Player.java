package reLogic.players;

import reLogic.factories.TilesSetsFactory;
import reLogic.factories.TilesSetsFactoryProducer;
import reLogic.rules.Rule;
import reLogic.tiles.Tile;
import reLogic.tilesSets.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

// Represents a player in the Mahjong game, handling the player's tile sets and game actions
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

    // Constructs a Player with the specified name and initializes their tile sets
    public Player(String player){
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

    // Getter methods for player state
    public String getName() {
        return name;
    }
    public boolean getDealerState() {
        return dealerState;
    }
    public boolean getTurnState() {
        return turnState;
    }
    public boolean getCanGet(){
        return canGet;
    }
    public boolean getCanDiscard(){
        return canDiscard;
    }
    public boolean getCanChow(){
        return canChow;
    }
    public boolean getCanPong(){
        return canPong;
    }
    public boolean getCanBrightKong(){
        return canBrightKong;
    }
    public boolean getCanDarkKong(){
        return canDarkKong;
    }
    public boolean getcanHu(){
        return canHu;
    }
    public boolean getSkip(){
        return skip;
    }

    // Setter methods for player state
    public void setDealerState(boolean b){
        dealerState = b;
    }
    public void setTurnState(boolean b) {
        turnState = b;
    }
    public void setCanGet(boolean b){
        canGet = b;
    }
    public void setCanDiscard(boolean b){
        canDiscard = b;
    }
    public void setCanChow(boolean b){
        canChow = b;
    }
    public void setCanPong(boolean b){
        canPong = b;
    }
    public void setCanBrightKong(boolean b){
        canBrightKong = b;
    }
    public void setCanDarkKong(boolean b){
        canDarkKong = b;
    }
    public void setCanHu(boolean b){
        canHu = b;
    }
    public void setSkip(boolean b){
        skip = b;
    }

    // Player actions
    public void getTile(Tile tile) {
        handTileSet.addTile(tile);
    }
    public Tile discard(String tileType) {
        return handTileSet.discardTile(tileType);
    }
    public void chow(Tile tile,String type){
        handTileSet.setEndSecondGetTile(handTileSet.getEndGetTile());
        handTileSet.setEndGetTile(tile);

        ArrayList<Tile> targetSubHandTileSet = null;
        targetSubHandTileSet = setTargetSubHandTileSet(handTileSet.getTileSets(),tile,targetSubHandTileSet);
        chowByTypes(tile,type,targetSubHandTileSet,lockedTileSet);

    }

    // Sets the target sub-hand tile set based on the tile type
    private ArrayList<Tile> setTargetSubHandTileSet(ArrayList<ArrayList<Tile>> handTileSets, Tile tile, ArrayList<Tile> targetTileSet){
        String tileType = tile.getType();
        if (tileType == "Character"){
            targetTileSet = handTileSets.get(0);
        }else if (tileType == "Bamboo"){
            targetTileSet = handTileSets.get(1);
        }else if (tileType == "Dot"){
            targetTileSet = handTileSets.get(2);
        }
        return targetTileSet;
    }

    // Performs the Chow action based on the tile type and updates the locked tile sets
    private void chowByTypes(Tile tile,String type,ArrayList<Tile> targetTileSet,LockedTileSet lockedTileSets){
        int magnitude = tile.getMagnitude();
        ArrayList<Tile> removedTile;
        ArrayList<Tile> chowTileSet = lockedTileSets.getTileSets().get(0);
        boolean check1 = false;
        boolean check2 = false;

        if (Objects.equals(type, "1")){
            removedTile = new ArrayList<>();
            for (Tile targetTile: targetTileSet){
                if (targetTile.getMagnitude() == magnitude - 1 && !check1){
                    chowTileSet.add(targetTile);
                    removedTile.add(targetTile);
                    check1 = true;
                }
                if (targetTile.getMagnitude() == magnitude - 2 && !check2){
                    chowTileSet.add(targetTile);
                    removedTile.add(targetTile);
                    check2 = true;
                }
            }
            targetTileSet.removeAll(removedTile);
            chowTileSet.add(tile);
        }else if (Objects.equals(type, "2")){
            removedTile = new ArrayList<>();
            for (Tile targetTile: targetTileSet){
                if (targetTile.getMagnitude() == magnitude - 1 && !check1 ){
                    chowTileSet.add(targetTile);
                    chowTileSet.add(tile);
                    removedTile.add(targetTile);

                    check1 = true;
                }
                if (targetTile.getMagnitude() == magnitude + 1 && !check2 ){
                    chowTileSet.add(targetTile);
                    removedTile.add(targetTile);

                    check2 = true;
                }
            }
            targetTileSet.removeAll(removedTile);

        }else if (Objects.equals(type, "3")){
            removedTile = new ArrayList<>();
            chowTileSet.add(tile);
            for (Tile targetTile: targetTileSet){
                if (targetTile.getMagnitude() == magnitude + 1 && !check1 ){
                    chowTileSet.add(targetTile);
                    removedTile.add(targetTile);

                    check1 = true;
                }
                if (targetTile.getMagnitude() == magnitude + 2 && !check2 ){
                    chowTileSet.add(targetTile);
                    removedTile.add(targetTile);

                    check2 = true;
                }

            }
            targetTileSet.removeAll(removedTile);
        }
    }

    // Performs a Pong action with the specified tile
    public void pong(Tile tile){
        handTileSet.setEndSecondGetTile(handTileSet.getEndGetTile());
        handTileSet.setEndGetTile(tile);
        ArrayList<Tile> pongTileSet = lockedTileSet.getTileSets().get(1);
        pongTileSet.add(tile);

        int count = 0;
        for (ArrayList<Tile> tileSet : handTileSet.getTileSets()){
            ArrayList<Tile> removedTile = new ArrayList<>();
            for (Tile targetTile: tileSet){
                if (Objects.equals(targetTile.getType(), tile.getType()) && targetTile.getMagnitude() == tile.getMagnitude()){
                    if (count < 2){
                        count++;
                        pongTileSet.add(targetTile);
                        removedTile.add(targetTile);
                    }
                }
            }
            tileSet.removeAll(removedTile);
        }

    }

    // Performs a Bright Kong action with the specified tile
    public void brightKong(Tile tile){
        handTileSet.setEndSecondGetTile(handTileSet.getEndGetTile());
        handTileSet.setEndGetTile(tile);
        handTileSet.setEndKongTile(tile);
        ArrayList<Tile> brightKongTileSet = lockedTileSet.getTileSets().get(2);
        brightKongTileSet.add(tile);

        int count = 0;
        for (ArrayList<Tile> tileSet : handTileSet.getTileSets()){
            ArrayList<Tile> removedTile = new ArrayList<>();
            for (Tile targetTile: tileSet){
                if (Objects.equals(targetTile.getType(), tile.getType()) && targetTile.getMagnitude() == tile.getMagnitude()){
                    if (count < 3){
                        count++;
                        brightKongTileSet.add(targetTile);
                        removedTile.add(targetTile);
                    }
                }
            }
            tileSet.removeAll(removedTile);
        }
    }

    // Performs a Kong action by calling the appropriate Kong method
    public void Kong(){
        darkKong(); // Perform Dark Kong
        // Uncomment below to use another Kong type
        // kaiKong();
    }

    // Performs a Dark Kong action, which involves checking and setting tiles for a concealed Kong
    private void darkKong(){
        Rule rule = Rule.getInstance();
        ArrayList<Tile> newKongSet = new ArrayList<>();
        ArrayList<Tile> kongSet = lockedTileSet.getTileSets().get(3);
        for (ArrayList<Tile> tileArrayList: handTileSet.getTileSets()){
            newKongSet.addAll(rule.checkKong(tileArrayList));
        }
        int count = 0;
        for (ArrayList<Tile> tileSet : handTileSet.getTileSets()){
            ArrayList<Tile> removedTile = new ArrayList<>();
            for (Tile tile: tileSet){
                if (Objects.equals(tile.getType(), newKongSet.get(0).getType()) && tile.getMagnitude() == newKongSet.get(0).getMagnitude()){
                    if (count < 4){
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

    // Performs a Kai Kong action, converting a Pong to a Kong
    private void kaiKong(){
        ArrayList<Tile> kongSet = lockedTileSet.getTileSets().get(3);
        ArrayList<Tile> pongTileSet = lockedTileSet.getTileSets().get(1);
        for (ArrayList<Tile> tileSet : handTileSet.getTileSets()){
            ArrayList<Tile> removed = new ArrayList<>();
            for (Tile tile: tileSet){
                Tile kaiKongTile = null;
                ArrayList<Tile> removedTile = new ArrayList<>();
                for (Tile targetTile: pongTileSet){
                    if (Objects.equals(tile.getType(), targetTile.getType()) &&tile.getMagnitude()==targetTile.getMagnitude()){
                        kaiKongTile = tile;
                        kongSet.add(targetTile);
                        removedTile.add(targetTile);
                    }
                }
                if (kaiKongTile!=null){
                    removed.add(kaiKongTile);
                }
                kongSet.add(kaiKongTile);
                pongTileSet.removeAll(removedTile);
            }
            tileSet.removeAll(removed);
        }
    }

    // Getter methods for player's tile sets
    public HandTileSet getHandTileSet(){
        return handTileSet;
    }
    public LockedTileSet getLockedTileSet(){
        return lockedTileSet;
    }
    public PlayedTileSet getPlayedTileSet(){
        return playedTileSet;
    }
}
