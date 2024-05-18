package reLogic.tilesSets;

import reLogic.tiles.Tile;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class TileSet<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 8L;
    private ArrayList<T> tileSets;
    private int tileNumber;
    abstract public void addTile(Tile tile);
    abstract public Tile discardTile(String tileType);

//    abstract public Tile discardTile(Tile tile);
    abstract public void sort();

    public void setTileSets(ArrayList<T> tileSets){
        this.tileSets = tileSets;
    }
    public void setTileNumber(int tileNumber){
        this.tileNumber = tileNumber;
    }
    public ArrayList<T> getTileSets(){
        return tileSets;
    }
    public int getTileNumber(){
        return tileNumber;
    }
}
