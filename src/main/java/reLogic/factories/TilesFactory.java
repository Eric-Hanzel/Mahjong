package reLogic.factories;

import reLogic.tiles.Tile;

import java.io.Serial;
import java.io.Serializable;

/*
  Abstract factory class for creating tiles. This class serves as the base for concrete factory classes
  that will implement the method to create specific types of tiles based on a string identifier.
 */
public abstract class TilesFactory implements Serializable {
    @Serial
    // Serial version UID for serialization
    private static final long serialVersionUID = 15L;

    // Abstract method to create a tile based on the given tile type
    public abstract Tile getTile(String tileType);
}
