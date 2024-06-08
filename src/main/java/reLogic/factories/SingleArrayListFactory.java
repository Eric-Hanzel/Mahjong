package reLogic.factories;

import reLogic.tiles.Tile;
import reLogic.tilesSets.LibraryTileSet;
import reLogic.tilesSets.PlayedTileSet;
import reLogic.tilesSets.TileSet;

import java.io.Serial;

// Factory class for creating specific types of TileSet objects used in a Mahjong game
public class SingleArrayListFactory extends TilesSetsFactory{
    @Serial
    // Serial version UID for serialization
    private static final long serialVersionUID = 19L;

    @Override
    public TileSet getTileSet(String tileSetType) {
        if (tileSetType.equalsIgnoreCase("Library")){
            return new LibraryTileSet(); // Creates and returns a new instance of LibraryTileSet
        }else {
            return new PlayedTileSet(); // Creates and returns a new instance of PlayedTileSet for any other types
        }
    }
}
