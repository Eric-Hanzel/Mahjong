package reLogic.tilesSets;

import reLogic.tiles.Tile;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;

public class PlayedTileSet extends SingleArrayListTileDecorator {
    @Serial
    private static final long serialVersionUID = 12L;
    private ArrayList<Tile> playedTileSet;

    public PlayedTileSet(){
        playedTileSet = new ArrayList<Tile>();
        setTileSets(playedTileSet);
        setTileNumber(playedTileSet.size());
    }

    @Override
    public void sort() {
        Collections.shuffle(playedTileSet);
    }
}
