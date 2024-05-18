package reLogic.factories;

import reLogic.tiles.Tile;

import java.io.Serial;
import java.io.Serializable;

public abstract class TilesFactory implements Serializable {
    @Serial
    private static final long serialVersionUID = 15L;
    public abstract Tile getTile(String tileType);
}
