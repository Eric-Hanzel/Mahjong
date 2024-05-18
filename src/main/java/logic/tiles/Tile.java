package logic.tiles;

import logic.players.GameRole;

public interface Tile {
    String getType();
    int getMagnitude();
    int getNumber();
    String getOwner();
    boolean getFlowerState();
    boolean equal(Tile otherTile);
    void changeOwner(GameRole gameRole);

}
