package logic.players;

import logic.rules.Rule;
import logic.tiles.HandTile;
import logic.tiles.Tile;

public class Player implements GameRole {

    String name;
    HandTile handTile;
    boolean dealerState;
    boolean turnState;
    boolean readyState;
    int scoring;
    Rule rule;

    Player(String player){
        name = player;
        handTile = new HandTile(name);
        dealerState = false;
        turnState = false;
        readyState = false;
        scoring = 0;
        rule = new Rule();
    }


    @Override
    public void discard(Tile tile) {
        handTile.discard(tile);
    }

    @Override
    public void getTile(Tile tile) {
        handTile.addTile(tile);
    }

    @Override
    public void changeDealerState() {
        dealerState = !dealerState;
    }

    @Override
    public void changeTurnState() {
        turnState = !turnState;
    }

    @Override
    public void changeReadyState() {
        readyState = !readyState;
    }


    //依赖rule
    @Override
    public void changeScoring() {

    }

    @Override
    public HandTile getHandTile() {
        return handTile;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean getDealerState() {
        return dealerState;
    }

    @Override
    public boolean getTurnState() {
        return turnState;
    }

    @Override
    public boolean getReadyState() {
        return readyState;
    }

    @Override
    public int getScoring() {
        return scoring;
    }


}
