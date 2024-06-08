package reLogic.factories;

import reLogic.tiles.Tile;
import reLogic.tilesSets.TileSet;

import java.io.Serial;
import java.io.Serializable;

// Abstract factory class for creating different types of TileSets
public abstract class TilesSetsFactory implements Serializable {
    @Serial
    private static final long serialVersionUID = 18L; // Serial version UID for serialization

    // Abstract method to create a TileSet based on the specified type
    public abstract TileSet getTileSet(String tileSetType);
}
