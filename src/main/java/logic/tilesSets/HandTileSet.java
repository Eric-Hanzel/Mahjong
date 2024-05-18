package logic.tilesSets;

import logic.tiles.Tile;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

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

    @Override
    public void sort() {
        character.sort(null);
        bamboo.sort(null);
        dot.sort(null);
        honor.sort(null);
    }
    public Tile getEndGetTile(){
        return endGetTile;
    }
    public Tile getEndSecondGetTile(){
        return endSecondGetTile;
    }
    public Tile getEndKongTile(){
        return endKongTile;
    }
    public void setEndGetTile(Tile tile){
        endGetTile = tile;
    }
    public void setEndSecondGetTile(Tile tile){
        endSecondGetTile = tile;
    }
    public void setEndKongTile(Tile tile){
        endKongTile = tile;
    }
}
