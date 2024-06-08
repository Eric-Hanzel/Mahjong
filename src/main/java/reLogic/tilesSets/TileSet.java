package reLogic.tilesSets;

import reLogic.tiles.Tile;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

// Abstract class representing a set of tiles in Mahjong
public abstract class TileSet<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 8L;
    private ArrayList<T> tileSets;
    private int tileNumber;

    // Adds a tile to the tile set
    abstract public void addTile(Tile tile);

    // Discards a tile from the tile set
    abstract public Tile discardTile(String tileType);

    // Sorts the tiles in the tile set
    abstract public void sort();

    // Sets the tile sets
    public void setTileSets(ArrayList<T> tileSets){
        this.tileSets = tileSets;
    }

    // Sets the number of tiles
    public void setTileNumber(int tileNumber){
        this.tileNumber = tileNumber;
    }

    // Returns the tile sets
    public ArrayList<T> getTileSets(){
        return tileSets;
    }

    // Returns the number of tiles
    public int getTileNumber(){
        return tileNumber;
    }
}
