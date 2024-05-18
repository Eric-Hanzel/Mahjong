package reLogic.factories;

import reLogic.tiles.Tile;
import reLogic.tilesSets.LibraryTileSet;
import reLogic.tilesSets.PlayedTileSet;
import reLogic.tilesSets.TileSet;

import java.io.Serial;

public class SingleArrayListFactory extends TilesSetsFactory{
    @Serial
    private static final long serialVersionUID = 19L;

    @Override
    public TileSet getTileSet(String tileSetType) {
        if (tileSetType.equalsIgnoreCase("Library")){
            return new LibraryTileSet();
        }else {
            return new PlayedTileSet();
        }
    }
}
