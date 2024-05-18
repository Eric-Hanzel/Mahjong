package reLogic.tilesSets;

import reLogic.tiles.Tile;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class LockedTileSet extends DoubleArrayListTileDecorator {
    @Serial
    private static final long serialVersionUID = 13L;
    private ArrayList<ArrayList<Tile>> lockedTileSet;
    ArrayList<Tile> chowTileSet;
    ArrayList<Tile> pongTileSet;
    ArrayList<Tile> brightKongTileSet;
    ArrayList<Tile> darkKongTileSet;
    public LockedTileSet(){
        lockedTileSet = new ArrayList<ArrayList<Tile>>();
        chowTileSet = new ArrayList<Tile>();
        pongTileSet = new ArrayList<Tile>();
        brightKongTileSet = new ArrayList<Tile>();
        darkKongTileSet = new ArrayList<Tile>();
        lockedTileSet.add(chowTileSet);
        lockedTileSet.add(pongTileSet);
        lockedTileSet.add(brightKongTileSet);
        lockedTileSet.add(darkKongTileSet);
        setTileSets(lockedTileSet);
    }

    @Override
    public void addTile(Tile tile) {
        if (Objects.equals(tile.getType(), "Chow")) {
            chowTileSet.add(tile);
        }else if (Objects.equals(tile.getType(), "Pong")) {
            pongTileSet.add(tile);
        }else if (Objects.equals(tile.getType(), "BrightKong")) {
            brightKongTileSet.add(tile);
        }else {
            darkKongTileSet.add(tile);
        }
    }

    @Override
    public Tile discardTile(String tileType) {
        return null;
    }

    @Override
    public void sort() {
        Comparator<Tile> tilecomparator = Comparator.comparingInt(Tile::getMagnitude);
        chowTileSet.sort(tilecomparator);
        pongTileSet.sort(tilecomparator);
        brightKongTileSet.sort(tilecomparator);
        darkKongTileSet.sort(tilecomparator);
    }
}
