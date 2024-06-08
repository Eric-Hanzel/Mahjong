package reLogic.tilesSets;

import reLogic.tiles.Tile;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;

// Class representing the set of played tiles for a player in Mahjong
public class PlayedTileSet extends SingleArrayListTileDecorator {
    @Serial
    private static final long serialVersionUID = 12L;
    private ArrayList<Tile> playedTileSet;

    // Constructs a PlayedTileSet and initializes the list of played tiles
    public PlayedTileSet(){
        playedTileSet = new ArrayList<Tile>();
        setTileSets(playedTileSet);
        setTileNumber(playedTileSet.size());
    }

    // Shuffles the played tiles
    @Override
    public void sort() {
        Collections.shuffle(playedTileSet);
    }
}
