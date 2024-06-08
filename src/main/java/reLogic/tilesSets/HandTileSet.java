package reLogic.tilesSets;

import reLogic.tiles.Tile;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

// Class representing the set of hand tiles for a player in Mahjong
public class HandTileSet extends DoubleArrayListTileDecorator {
    @Serial
    private static final long serialVersionUID = 14L;
    private ArrayList<ArrayList<Tile>> handTileSet;
    ArrayList<Tile> character;
    ArrayList<Tile> bamboo;
    ArrayList<Tile> dot;
    ArrayList<Tile> honor;
    Tile endGetTile;
    Tile endSecondGetTile;
    Tile endKongTile;

    // Constructs a HandTileSet and initializes the tile categories
    public HandTileSet(){
        handTileSet = new ArrayList<ArrayList<Tile>>();
        character = new ArrayList<Tile>();
        bamboo = new ArrayList<Tile>();
        dot = new ArrayList<Tile>();
        honor = new ArrayList<Tile>();
        handTileSet.add(character);
        handTileSet.add(bamboo);
        handTileSet.add(dot);
        handTileSet.add(honor);
        endGetTile = null;
        endSecondGetTile = null;
        endKongTile = null;
        setTileSets(handTileSet);
    }

    // Adds a tile to the hand tile set
    @Override
    public void addTile(Tile tile) {
        endSecondGetTile = endGetTile;
        endGetTile = tile;
        if (Objects.equals(tile.getType(), "Character")) {
            character.add(tile);
        }else if (Objects.equals(tile.getType(), "Bamboo")) {
            bamboo.add(tile);
        }else if (Objects.equals(tile.getType(), "Dot")) {
            dot.add(tile);
        }else {
            honor.add(tile);
        }
    }

    // Discards a tile from the hand tile set
    @Override
    public Tile discardTile(String tileType) {
        for (ArrayList<Tile> tileSet: handTileSet){
            for (Tile tile: tileSet){
                if (Objects.equals(tile.toString(), tileType)){
                    tileSet.remove(tile);
                    return tile;
                }
            }
        }
        return null;
    }

    // Sorts the tiles in the hand tile set by magnitude
    @Override
    public void sort() {
        Comparator<Tile> tilecomparator = Comparator.comparingInt(Tile::getMagnitude);
        character.sort(tilecomparator);
        bamboo.sort(tilecomparator);
        dot.sort(tilecomparator);
        honor.sort(tilecomparator);
    }

    // Returns the last tile drawn by the player
    public Tile getEndGetTile(){
        return endGetTile;
    }

    // Returns the second to last tile drawn by the player
    public Tile getEndSecondGetTile(){
        return endSecondGetTile;
    }

    // Returns the last tile used for a Kong
    public Tile getEndKongTile(){
        return endKongTile;
    }

    // Sets the last tile drawn by the player
    public void setEndGetTile(Tile tile){
        endGetTile = tile;
    }

    // Sets the second to last tile drawn by the player
    public void setEndSecondGetTile(Tile tile){
        endSecondGetTile = tile;
    }

    // Sets the last tile used for a Kong
    public void setEndKongTile(Tile tile){
        endKongTile = tile;
    }
}
