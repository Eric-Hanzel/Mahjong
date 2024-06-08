package reLogic.tilesSets;

import reLogic.tiles.Tile;

import java.io.Serial;
import java.util.ArrayList;

// Abstract class representing a decorator for a TileSet that contains a list of lists of Tiles
abstract class DoubleArrayListTileDecorator extends TileSet{
    @Serial
    private static final long serialVersionUID = 10L;
    private TileSet<ArrayList<Tile>> tileSet;
    private int tileNumber;

    // Constructs a DoubleArrayListTileDecorator and initializes the tileSet
    DoubleArrayListTileDecorator() {
        tileSet = new TileSet<ArrayList<Tile>>() {
            @Override
            public void addTile(Tile tile) {
                // Implementation should be provided by subclass
            }

            @Override
            public Tile discardTile(String tileType) {
                return null;
            }// Implementation should be provided by subclass

            @Override
            public void sort() {
                // Implementation should be provided by subclass
            }
        };
    }

    // Sets the tile sets
    @Override
    public void setTileSets(ArrayList tileSets){
        tileSet.setTileSets(tileSets);
    }

    // Sets the number of tiles
    @Override
    public void setTileNumber(int tileNumber){
        this.tileNumber = tileNumber;
        tileSet.setTileNumber(tileNumber);
    }

    // Returns the tile sets
    @Override
    public ArrayList<ArrayList<Tile>> getTileSets(){
        return tileSet.getTileSets();
    }

    // Returns the number of tiles
    @Override
    public int getTileNumber(){
        return tileSet.getTileNumber();
    }
}
