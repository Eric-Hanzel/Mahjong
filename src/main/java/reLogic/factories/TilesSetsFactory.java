package reLogic.factories;

import reLogic.tiles.Tile;
import reLogic.tilesSets.TileSet;

import java.io.Serial;
import java.io.Serializable;

public abstract class TilesSetsFactory implements Serializable {
    @Serial
    private static final long serialVersionUID = 18L;
    public abstract TileSet getTileSet(String tileSetType);
}
