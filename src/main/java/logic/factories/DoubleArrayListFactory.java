package logic.factories;

import logic.tilesSets.*;

import java.io.Serial;

public class DoubleArrayListFactory extends TilesSetsFactory{
    @Serial
    private static final long serialVersionUID = 20L;
    // HandTile or LockedTile
    @Override
    public TileSet getTileSet(String tileSetType) {
        if (tileSetType.equalsIgnoreCase("HandTile")){
            return new HandTileSet();
        }else {
            return new LockedTileSet();
        }
    }
}
