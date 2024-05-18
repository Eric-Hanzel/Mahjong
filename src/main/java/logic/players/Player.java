package logic.players;

import logic.rules.Rule;
import logic.tiles.HandTile;
import logic.tiles.LibraryTile;
import logic.tiles.PlayedTile;
import logic.tiles.Tile;

import java.io.Serial;
import java.io.Serializable;

public class Player implements GameRole, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    String name;
    HandTile handTile;
    LibraryTile libraryTile;
    PlayedTile playedTile;
    boolean dealerState;
    boolean turnState;
    boolean canGet;
    boolean canDiscard;
    boolean canChow;
    boolean canPong;
    boolean canBrightKong;
    boolean canDarkKong;
    boolean skip;
    int scoring;
    Rule rule;

    public Player(String player,LibraryTile libraryTile){
        name = player;
        handTile = new HandTile();
        this.libraryTile = libraryTile;
        playedTile = new PlayedTile();
        dealerState = false;
        turnState = false;
        scoring = 0;
        canGet = false;
        canDiscard = false;
        canChow = false;
        canPong = false;
        canBrightKong = false;
        canDarkKong = false;
        skip = false;
        rule = new Rule();
    }

    private LibraryTile getLibraryTile() {
        return libraryTile;
    }

    @Override
    public Tile discard(String tileType) {
        return handTile.discard(tileType);
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
    public boolean getCanGet(){
        return canGet;
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
    public boolean getSkip(){
        return skip;
    }
    public boolean getCanDiscard(){
        return canDiscard;
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
    public void setSkip(boolean b){
        skip = b;
    }


    @Override
    public int getScoring() {
        return scoring;
    }
    @Override
    public void setDealerState(boolean b){
        dealerState = b;
    }
    @Override
    public void setTurnState(boolean b) {
        turnState = b;
    }

    public PlayedTile getPlayedTile() {
        return playedTile;
    }

    public boolean checkCanChow(Tile endDiscardTile) {
        return handTile.checkCanChow(endDiscardTile);
    }

    public boolean checkCanPong(Tile endDiscardTile) {
        return handTile.checkCanPong(endDiscardTile);
    }

    public boolean checkCanBrightKong(Tile endDiscardTile) {
        return handTile.checkCanBrightKong(endDiscardTile);
    }
    public boolean checkCanDarkKong() {
        return handTile.checkCanDarkKong();
    }
}
