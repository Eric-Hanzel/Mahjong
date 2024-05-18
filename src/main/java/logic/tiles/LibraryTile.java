package logic.tiles;

import java.util.ArrayList;
import java.util.Collections;

public class LibraryTile implements TileSet{
    ArrayList<Tile> tileList;
    int tileNumber;

    LibraryTile(){
        tileList = new ArrayList<Tile>();
        for (int i = 1; i < 10; ++i){
            for (int j = 1; j < 5; ++j){
                tileList.add(new CharacterTile("Character",i,j));
                tileList.add(new BambooTile("Bamboo",i,j));
                tileList.add(new DotTile("Dot",i,j));
                if (i == 1) tileList.add(new HonorTile("East",j));
                if (i == 2) tileList.add(new HonorTile("South",j));
                if (i == 3) tileList.add(new HonorTile("West",j));
                if (i == 4) tileList.add(new HonorTile("North",j));
                if (i == 5) tileList.add(new HonorTile("Red",j));
                if (i == 6) tileList.add(new HonorTile("Green",j));
                if (i == 7) tileList.add(new HonorTile("White",j));
            }
        }
        tileNumber = 136;
    }

    public Tile discard() {
        Tile tempTile = tileList.get(0);
        tileList.remove(0);
        tileNumber--;
        return tempTile;
    }

    public Tile discardBack(){
        tileList.remove(tileNumber-1);
        tileNumber--;
        return tileList.get(tileNumber-1);
    }

    @Override
    public void sort() {
        Collections.shuffle(tileList);
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
