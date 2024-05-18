package reLogic.factories;

import reLogic.tiles.Tile;
import reLogic.tilesSets.TileSet;

import java.util.ArrayList;

public class test {
    public static void main(String[] args){
        TilesSetsFactory singleTilesSetsProducer = TilesSetsFactoryProducer.getTilesSetFactory(true);
        TilesSetsFactory doubleTilesSetsProducer = TilesSetsFactoryProducer.getTilesSetFactory(false);
        TileSet libraryTileSet = singleTilesSetsProducer.getTileSet("Library");
        TileSet playedTileSet = singleTilesSetsProducer.getTileSet("Played");
        TileSet handTileSet = doubleTilesSetsProducer.getTileSet("HandTile");
        libraryTileSet.sort();
        for (Object tile:libraryTileSet.getTileSets()){

            System.out.println(tile);
        }
        for (int i = 0; i < 13; i++){
            handTileSet.addTile(libraryTileSet.discardTile(""));
        }
        ArrayList<ArrayList<Tile>> arrayLists = handTileSet.getTileSets();
        for (ArrayList<Tile> arrayList: arrayLists){
            arrayList.sort(null);
        }
        System.out.println(playedTileSet.getTileSets());
        System.out.println(libraryTileSet.getTileNumber());
        System.out.println(handTileSet.getTileSets());
    }
}
