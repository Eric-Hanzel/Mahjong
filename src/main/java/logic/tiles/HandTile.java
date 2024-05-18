package logic.tiles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class HandTile implements TileSet{
    ArrayList<ArrayList<Tile>> handTile;
    ArrayList<Tile> character;
    ArrayList<Tile> bamboo;
    ArrayList<Tile> dot;
    ArrayList<Tile> wind;
    ArrayList<Tile> dragon;
    ArrayList<Tile> lockedTile;

    Tile endTile;

    String owner;

    public HandTile(String player){
        handTile = new ArrayList<ArrayList<Tile>>();
        character = new ArrayList<Tile>();
        bamboo = new ArrayList<Tile>();
        dot = new ArrayList<Tile>();
        wind = new ArrayList<Tile>();
        dragon = new ArrayList<Tile>();
        handTile.add(character);
        handTile.add(bamboo);
        handTile.add(dot);
        handTile.add(wind);
        handTile.add(dragon);
        lockedTile = new ArrayList<Tile>();
        owner = player;
        endTile = null;
    }

    public void lock(Tile tile){
        lockedTile.add(tile);
    }

    public void discard(Tile tile) {

        if (Objects.equals(tile.getType(), "Character")) character.remove(tile);
        if (Objects.equals(tile.getType(), "Bamboo")) bamboo.remove(tile);
        if (Objects.equals(tile.getType(), "Dot")) dot.remove(tile);
        if (Objects.equals(tile.getType(), "East")) wind.remove(tile);
        if (Objects.equals(tile.getType(), "South")) wind.remove(tile);
        if (Objects.equals(tile.getType(), "North")) wind.remove(tile);
        if (Objects.equals(tile.getType(), "West")) wind.remove(tile);
        if (Objects.equals(tile.getType(), "Red")) dragon.remove(tile);
        if (Objects.equals(tile.getType(), "Green")) dragon.remove(tile);
        if (Objects.equals(tile.getType(), "White")) dragon.remove(tile);
    }

    public void addTile(Tile tile){
        endTile = tile;
        if (Objects.equals(tile.getType(), "Character")) character.add(tile);
        if (Objects.equals(tile.getType(), "Bamboo")) bamboo.add(tile);
        if (Objects.equals(tile.getType(), "Dot")) dot.add(tile);
        if (Objects.equals(tile.getType(), "East")) wind.add(tile);
        if (Objects.equals(tile.getType(), "South")) wind.add(tile);
        if (Objects.equals(tile.getType(), "North")) wind.add(tile);
        if (Objects.equals(tile.getType(), "West")) wind.add(tile);
        if (Objects.equals(tile.getType(), "Red")) dragon.add(tile);
        if (Objects.equals(tile.getType(), "Green")) dragon.add(tile);
        if (Objects.equals(tile.getType(), "White")) dragon.add(tile);
    }

    @Override
    public void sort(){
        Comparator<Tile> tilecomparator = Comparator.comparingInt(Tile::getMagnitude);
        character.sort(tilecomparator);
        bamboo.sort(tilecomparator);
        dot.sort(tilecomparator);
        wind.sort(tilecomparator);
        dragon.sort(tilecomparator);
    }


    public Tile getEndTile(){
        return endTile;
    }
    @Override
    public ArrayList<ArrayList<Tile>> getTileSet() {
        return handTile;
    }

    @Override
    public int getTileNumber() {
        return character.size()+bamboo.size()+dot.size()+wind.size()+dragon.size();
    }




}
