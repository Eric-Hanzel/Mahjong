package reLogic.factories;

import reLogic.tilesSets.*;

import java.io.Serial;

// A factory class that extends TilesSetsFactory to provide specific types of TileSet instances
public class DoubleArrayListFactory extends TilesSetsFactory{
    @Serial
    // Unique identifier for serialization
    private static final long serialVersionUID = 20L;
    @Override
    public TileSet getTileSet(String tileSetType) {
        if (tileSetType.equalsIgnoreCase("HandTile")){
            return new HandTileSet();// Returns a new instance of HandTileSet if the type matches "HandTile"
        }else {
            return new LockedTileSet();// Returns a new instance of LockedTileSet for all other types
        }
    }
}
