package reLogic.factories;

import reLogic.tilesSets.*;

import java.io.Serial;

public class DoubleArrayListFactory extends TilesSetsFactory{
    @Serial
    private static final long serialVersionUID = 20L;
    @Override
    public TileSet getTileSet(String tileSetType) {
        if (tileSetType.equalsIgnoreCase("HandTile")){
            return new HandTileSet();
        }else {
            return new LockedTileSet();
        }
    }
}
