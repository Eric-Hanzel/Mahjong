package reLogic.tilesSets;

import reLogic.tiles.Tile;

import java.io.Serial;
import java.util.ArrayList;

// Abstract class representing a decorator for a TileSet that contains a single list of Tiles
abstract class SingleArrayListTileDecorator extends TileSet{
    @Serial
    private static final long serialVersionUID = 9L;
    private TileSet<reLogic.tiles.Tile> tileSet;
    private int tileNumber;

    // Constructs a SingleArrayListTileDecorator and initializes the tileSet
    SingleArrayListTileDecorator() {
        tileSet = new TileSet<Tile>() {
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
    public ArrayList<Tile> getTileSets(){
        return tileSet.getTileSets();
    }

    // Returns the number of tiles
    @Override
    public int getTileNumber(){
        return tileSet.getTileNumber();
    }

    // Adds a tile to the tile set
    @Override
    public void addTile(reLogic.tiles.Tile tile){
        tileSet.getTileSets().add(tile);
        tileNumber++;
        this.setTileNumber(tileNumber);
    }

    // Discards a tile from the tile set
    @Override
    public Tile discardTile(String tileType) {
        tileNumber--;
        this.setTileNumber(tileNumber);
        return tileSet.getTileSets().remove(tileSet.getTileSets().size()-1);
    }


}
