package logic.players;

import logic.tiles.HandTile;
import logic.tiles.Tile;

public interface GameRole {

    void discard(Tile tile);

    void getTile(Tile tile);

    void changeDealerState();

    void changeTurnState();

    void changeReadyState();

    void changeScoring();

    HandTile getHandTile();

    String getName();

    boolean getDealerState();

    boolean getTurnState();

    boolean getReadyState();

    int getScoring();


}
