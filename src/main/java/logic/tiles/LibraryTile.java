package logic.tiles;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class LibraryTile implements TileSet, Serializable {
    @Serial
    private static final long serialVersionUID = 3L;
    ArrayList<Tile> tileList;
    int tileNumber;

    public LibraryTile(){
        tileList = new ArrayList<Tile>();
        for (int i = 1; i < 10; ++i){
            for (int j = 1; j < 5; ++j){
                tileList.add(new CharacterTile("Character",i));
                tileList.add(new BambooTile("Bamboo",i));
                tileList.add(new DotTile("Dot",i));
                if (i == 1) tileList.add(new HonorTile("East",1));
                if (i == 2) tileList.add(new HonorTile("South",2));
                if (i == 3) tileList.add(new HonorTile("West",3));
                if (i == 4) tileList.add(new HonorTile("North",4));
                if (i == 5) tileList.add(new HonorTile("Red",5));
                if (i == 6) tileList.add(new HonorTile("Green",6));
                if (i == 7) tileList.add(new HonorTile("White",7));
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
