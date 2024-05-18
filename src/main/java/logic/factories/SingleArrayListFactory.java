package logic.factories;

import logic.tilesSets.LibraryTileSet;
import logic.tilesSets.PlayedTileSet;
import logic.tilesSets.TileSet;

import java.io.Serial;

public class SingleArrayListFactory extends TilesSetsFactory{
    @Serial
    private static final long serialVersionUID = 19L;
    // Library or Played TileSet
    @Override
    public TileSet getTileSet(String tileSetType) {
        if (tileSetType.equalsIgnoreCase("Library")){
            return new LibraryTileSet();
        }else {
            return new PlayedTileSet();
        }
    }
}
