package logic.tilesSets;

import logic.tiles.Tile;

import java.io.Serial;
import java.util.ArrayList;

abstract class DoubleArrayListTileDecorator extends TileSet{
    @Serial
    private static final long serialVersionUID = 10L;
    private TileSet<ArrayList<Tile>> tileSet;
    private int tileNumber;
    // HandTile or LockedTile
    DoubleArrayListTileDecorator() {
        tileSet = new TileSet<ArrayList<Tile>>() {
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
    public ArrayList<ArrayList<Tile>> getTileSets(){
        return tileSet.getTileSets();
    }
    @Override
    public int getTileNumber(){
        int numbers = 0;
        for (ArrayList<Tile> arrayList: tileSet.getTileSets()){
            for (Tile tile: arrayList){
                numbers++;
            }
        }
        return numbers;
    }
}
