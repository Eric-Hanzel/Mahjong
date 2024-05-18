package logic.tilesSets;

import logic.tiles.Tile;

import java.io.Serial;
import java.util.ArrayList;

abstract class SingleArrayListTileDecorator extends TileSet{
    @Serial
    private static final long serialVersionUID = 9L;
    private TileSet<logic.tiles.Tile> tileSet;
    private int tileNumber;
    // Library or PLayed TileSet
    SingleArrayListTileDecorator() {
        tileSet = new TileSet<Tile>() {
            @Override
            public void addTile(Tile tile) {

            }

            @Override
            public Tile discardTile(String tileType) {
                return null;
            }

            @Override
            public void sort() {

            }
        };
    }

    @Override
    public void setTileSets(ArrayList tileSets){
        tileSet.setTileSets(tileSets);
    }
    @Override
    public void setTileNumber(int tileNumber){
        this.tileNumber = tileNumber;
        tileSet.setTileNumber(tileNumber);
    }
    @Override
    public ArrayList<Tile> getTileSets(){
        return tileSet.getTileSets();
    }
    @Override
    public int getTileNumber(){
        return tileSet.getTileNumber();
    }

    @Override
    public void addTile(logic.tiles.Tile tile){
        tileSet.getTileSets().add(tile);
        tileNumber++;
        this.setTileNumber(tileNumber);
    }
    @Override
    public Tile discardTile(String tileType) {
        tileNumber--;
        this.setTileNumber(tileNumber);
        return tileSet.getTileSets().remove(tileSet.getTileSets().size()-1);
    }


}
