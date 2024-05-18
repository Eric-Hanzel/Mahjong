package logic.players;

import logic.rules.Rule;
import logic.tiles.HandTile;
import logic.tiles.Tile;

public class Ai implements GameRole {

    String name;
    HandTile handTile;
    boolean dealerState;
    boolean turnState;
    boolean readyState;
    int scoring;
    Rule rule;

    Ai(String player){
        name = player;
        handTile = new HandTile();
        dealerState = false;
        turnState = false;
        readyState = false;
        scoring = 0;
        rule = new Rule();
    }

    public void aiAlgorithm(){

    }

    @Override
    public Tile discard(String tileType) {
//        handTile.discard(tile);
        return null;
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
    public void setTurnState(boolean b) {
        turnState = !turnState;
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
    public int getScoring() {
        return scoring;
    }

    @Override
    public void setDealerState(boolean b) {

    }


}
