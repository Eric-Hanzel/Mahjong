package logic.tiles;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class PlayedTile implements TileSet, Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    ArrayList<Tile> tileList;
    int tileNumber;

    public PlayedTile(){
        tileList = new ArrayList<Tile>();
        tileNumber = 0;
    }

    public void discard() {
        tileList.remove(tileNumber-1);
        tileNumber--;
    }

    public void addTile(Tile tile){
        tileList.add(tile);
        tileNumber++;
    }

    @Override
    public void sort() {

    }

    @Override
    public ArrayList<Tile> getTileSet() {
        return tileList;
    }

    @Override
    public int getTileNumber() {
        return tileNumber;
    }
}
