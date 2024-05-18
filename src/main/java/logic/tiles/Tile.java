package logic.tiles;

import logic.players.GameRole;

public interface Tile {
    String getType();
    int getMagnitude();
    boolean getLock();
    void setLock(boolean b);
    boolean equal(Tile otherTile);
}
