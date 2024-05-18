package logic.players;

import logic.tiles.HandTile;
import logic.tiles.Tile;

public interface GameRole {

    Tile discard(String tileType);

    void getTile(Tile tile);

    void changeDealerState();



    void changeScoring();

    HandTile getHandTile();

    String getName();

    boolean getDealerState();

    boolean getTurnState();


    int getScoring();


    void setDealerState(boolean b);

    void setTurnState(boolean b);
}
