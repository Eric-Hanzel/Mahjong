package reLogic.factories;

import reLogic.tiles.Tile;
import reLogic.tilesSets.TileSet;

import java.util.ArrayList;

public class test {
    public static void main(String[] args){
        // Get a factory instance for creating single tile sets
        TilesSetsFactory singleTilesSetsProducer = TilesSetsFactoryProducer.getTilesSetFactory(true);

        // Get a factory instance for creating double tile sets
        TilesSetsFactory doubleTilesSetsProducer = TilesSetsFactoryProducer.getTilesSetFactory(false);

        // Create a LibraryTileSet using the single tile set factory
        TileSet libraryTileSet = singleTilesSetsProducer.getTileSet("Library");

        // Create a PlayedTileSet using the same single tile set factory
        TileSet playedTileSet = singleTilesSetsProducer.getTileSet("Played");

        // Create a HandTileSet using the double tile set factory
        TileSet handTileSet = doubleTilesSetsProducer.getTileSet("HandTile");

        // Sort the tiles in the LibraryTileSet
        libraryTileSet.sort();
        for (Object tile:libraryTileSet.getTileSets()){

            System.out.println(tile);
        }

        // Move 13 tiles from the LibraryTileSet to the HandTileSet
        for (int i = 0; i < 13; i++){
            handTileSet.addTile(libraryTileSet.discardTile(""));
        }

        // Get the list of tile sets in the HandTileSet and sort each list of tiles
        ArrayList<ArrayList<Tile>> arrayLists = handTileSet.getTileSets();
        for (ArrayList<Tile> arrayList: arrayLists){
            arrayList.sort(null);
        }

        // Print the tile sets of the PlayedTileSet, LibraryTileSet and HandTileSet
        System.out.println(playedTileSet.getTileSets());
        System.out.println(libraryTileSet.getTileNumber());
        System.out.println(handTileSet.getTileSets());
    }
}
