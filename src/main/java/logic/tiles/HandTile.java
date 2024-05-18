package logic.tiles;

import logic.rules.Rule;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class HandTile implements TileSet, Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    ArrayList<ArrayList<Tile>> handTile;
    ArrayList<Tile> character;
    ArrayList<Tile> bamboo;
    ArrayList<Tile> dot;
    ArrayList<Tile> wind;
    ArrayList<Tile> dragon;
    ArrayList<ArrayList<Tile>> lockedTile;
    ArrayList<Tile> chowTileSet;
    ArrayList<Tile> pongTileSet;
    ArrayList<Tile> brightKongTileSet;
    ArrayList<Tile> darkKongTileSet;
    Tile endTile;

    public HandTile(){
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
        lockedTile = new ArrayList<ArrayList<Tile>>();
        chowTileSet = new ArrayList<Tile>();
        pongTileSet = new ArrayList<Tile>();
        brightKongTileSet = new ArrayList<Tile>();
        darkKongTileSet = new ArrayList<Tile>();
        lockedTile.add(chowTileSet);
        lockedTile.add(pongTileSet);
        lockedTile.add(brightKongTileSet);
        lockedTile.add(darkKongTileSet);
        endTile = null;
    }

    public Tile discard(String tileType) {
        for (ArrayList<Tile> tileSet: handTile){
            for (Tile tile: tileSet){
                if (Objects.equals(tile.toString(), tileType) && !tile.getLock()){
                    tileSet.remove(tile);
                    return tile;
                }
            }
        }
        return null;
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
    public ArrayList<ArrayList<Tile>> getLockedTile(){
        return lockedTile;
    }
    @Override
    public ArrayList<ArrayList<Tile>> getTileSet() {
        return handTile;
    }

    @Override
    public int getTileNumber() {
        return character.size()+bamboo.size()+dot.size()+wind.size()+dragon.size();
    }


    public boolean checkCanChow(Tile endDiscardTile) {
        if (Objects.equals(endDiscardTile.getType(), "Character")){
            if (endDiscardTile.getMagnitude() == 1){
                if (tileInList(character,2,3)){
                    return true;
                }else {
                    return false;
                }
            }else if (endDiscardTile.getMagnitude() == 2){
                if (tileInList(character,1,3) || tileInList(character,3,4)){
                    return true;
                }else {
                    return false;
                }
            }else if (endDiscardTile.getMagnitude() == 8){
                if (tileInList(character,6,7) || tileInList(character,7,9)){
                    return true;
                }else {
                    return false;
                }
            }else if (endDiscardTile.getMagnitude() == 9){
                if (tileInList(character,7,8)){
                    return true;
                }else {
                    return false;
                }
            }else {
                int magnitude = endDiscardTile.getMagnitude();
                if (tileInList(character,magnitude-1,magnitude-2)||tileInList(character,magnitude-1,magnitude+1)||tileInList(character,magnitude+1,magnitude+2)){
                    return true;
                }else {
                    return false;
                }
            }
        }else if (Objects.equals(endDiscardTile.getType(), "Dot")){
            if (endDiscardTile.getMagnitude() == 1){
                if (tileInList(dot,2,3)){
                    return true;
                }else {
                    return false;
                }
            }else if (endDiscardTile.getMagnitude() == 2){
                if (tileInList(dot,1,3) || tileInList(dot,3,4)){
                    return true;
                }else {
                    return false;
                }
            }else if (endDiscardTile.getMagnitude() == 8){
                if (tileInList(dot,6,7) || tileInList(dot,7,9)){
                    return true;
                }else {
                    return false;
                }
            }else if (endDiscardTile.getMagnitude() == 9){
                if (tileInList(dot,7,8)){
                    return true;
                }else {
                    return false;
                }
            }else {
                int magnitude = endDiscardTile.getMagnitude();
                if (tileInList(dot,magnitude-1,magnitude-2)||tileInList(dot,magnitude-1,magnitude+1)||tileInList(dot,magnitude+1,magnitude+2)){
                    return true;
                }else {
                    return false;
                }
            }

        }else if (Objects.equals(endDiscardTile.getType(), "Bamboo")){
            if (endDiscardTile.getMagnitude() == 1){
                if (tileInList(bamboo,2,3)){
                    return true;
                }else {
                    return false;
                }
            }else if (endDiscardTile.getMagnitude() == 2){
                if (tileInList(bamboo,1,3) || tileInList(bamboo,3,4)){
                    return true;
                }else {
                    return false;
                }
            }else if (endDiscardTile.getMagnitude() == 8){
                if (tileInList(bamboo,6,7) || tileInList(bamboo,7,9)){
                    return true;
                }else {
                    return false;
                }
            }else if (endDiscardTile.getMagnitude() == 9){
                if (tileInList(bamboo,7,8)){
                    return true;
                }else {
                    return false;
                }
            }else {
                int magnitude = endDiscardTile.getMagnitude();
                if (tileInList(bamboo,magnitude-1,magnitude-2)||tileInList(bamboo,magnitude-1,magnitude+1)||tileInList(bamboo,magnitude+1,magnitude+2)){
                    return true;
                }else {
                    return false;
                }
            }
        }else {
            return false;
        }
    }
    public String chowTypes(Tile endDiscardTile){
        String types = "chowTypes " + endDiscardTile.toString() + " ";
        if (Objects.equals(endDiscardTile.getType(), "Character")){
            if (tileInList(character,endDiscardTile.getMagnitude()-1,endDiscardTile.getMagnitude()-2)){
                types = types + "1 ";
            }
            if (tileInList(character,endDiscardTile.getMagnitude()-1,endDiscardTile.getMagnitude()+1)){
                types = types + "2 ";
            }
            if (tileInList(character,endDiscardTile.getMagnitude()+1,endDiscardTile.getMagnitude()+2)){
                types = types + "3 ";
            }
        }else if (Objects.equals(endDiscardTile.getType(), "Dot")){
            if (tileInList(dot,endDiscardTile.getMagnitude()-1,endDiscardTile.getMagnitude()-2)){
                types = types + "1 ";
            }
            if (tileInList(dot,endDiscardTile.getMagnitude()-1,endDiscardTile.getMagnitude()+1)){
                types = types + "2 ";
            }
            if (tileInList(dot,endDiscardTile.getMagnitude()+1,endDiscardTile.getMagnitude()+2)){
                types = types + "3 ";
            }
        }else if (Objects.equals(endDiscardTile.getType(), "Bamboo")){
            if (tileInList(bamboo,endDiscardTile.getMagnitude()-1,endDiscardTile.getMagnitude()-2)){
                types = types + "1 ";
            }
            if (tileInList(bamboo,endDiscardTile.getMagnitude()-1,endDiscardTile.getMagnitude()+1)){
                types = types + "2 ";
            }
            if (tileInList(bamboo,endDiscardTile.getMagnitude()+1,endDiscardTile.getMagnitude()+2)){
                types = types + "3 ";
            }
        }
        return types;
    }

    private boolean tileInList(ArrayList<Tile> tileArrayList, int i, int j) {
        boolean checkI = false;
        boolean checkJ = false;
        for (Tile tile: tileArrayList){
            if (tile.getMagnitude() == i){
                checkI = true;
            }else if (tile.getMagnitude() == j){
                checkJ = true;
            }
        }
        if (checkI && checkJ){
            return true;
        }else {
            return false;
        }
    }

    public void chow(Tile tile,String type) {
        endTile = tile;
        ArrayList<Tile> tileSet = null;
        String tileType = tile.getType();
        boolean check1 = false;
        boolean check2 = false;
        int magnitude = tile.getMagnitude();
        if (tileType == "Character"){
            tileSet = character;
        }else if (tileType == "Bamboo"){
            tileSet = bamboo;
        }else if (tileType == "Dot"){
            tileSet = dot;
        }
        if (Objects.equals(type, "1")){
            ArrayList<Tile> removedTile = new ArrayList<>();
            for (Tile tile1: tileSet){
                if (tile1.getMagnitude() == magnitude - 1 && !check1){
                    chowTileSet.add(tile1);
                    removedTile.add(tile1);
                    check1 = true;
                }
                if (tile1.getMagnitude() == magnitude - 2 && !check2){
                    chowTileSet.add(tile1);
                    removedTile.add(tile1);
                    check2 = true;
                }
            }
            tileSet.removeAll(removedTile);
            chowTileSet.add(tile);
        }else if (Objects.equals(type, "2")){
            ArrayList<Tile> removedTile = new ArrayList<>();
            for (Tile tile1: tileSet){
                if (tile1.getMagnitude() == magnitude - 1 && !check1 ){
                    chowTileSet.add(tile1);
                    chowTileSet.add(tile);
                    removedTile.add(tile1);

                    check1 = true;
                }
                if (tile1.getMagnitude() == magnitude + 1 && !check2 ){
                    chowTileSet.add(tile1);
                    removedTile.add(tile1);


                    check2 = true;
                }
            }
            tileSet.removeAll(removedTile);
        }else if (Objects.equals(type, "3")){
            ArrayList<Tile> removedTile = new ArrayList<>();
            chowTileSet.add(tile);
            for (Tile tile1: tileSet){
                if (tile1.getMagnitude() == magnitude + 1 && !check1 ){
                    chowTileSet.add(tile1);
                    removedTile.add(tile1);

                    check1 = true;
                }
                if (tile1.getMagnitude() == magnitude + 2 && !check2 ){
                    chowTileSet.add(tile1);
                    removedTile.add(tile1);

                    check2 = true;
                }

            }
            tileSet.removeAll(removedTile);
        }
    }

    public boolean checkCanPong(Tile endDiscardTile) {
        int count = 0;
        for (ArrayList<Tile> tileSet : handTile){
            for (Tile tile: tileSet){
                if (Objects.equals(tile.getType(), endDiscardTile.getType()) && tile.getMagnitude() == endDiscardTile.getMagnitude()){
                    count++;
                    if (count==2){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkCanBrightKong(Tile endDiscardTile) {
        int count = 0;
        for (ArrayList<Tile> tileSet : handTile){
            for (Tile tile: tileSet){
                if (Objects.equals(tile.getType(), endDiscardTile.getType()) && tile.getMagnitude() == endDiscardTile.getMagnitude()){
                    count++;
                    if (count==3){
                        return true;
                    }
                }
            }
        }
        return false;
    }
//    public boolean checkCanDarkKong(Tile getTile) {
//        int count = 0;
//        for (ArrayList<Tile> tileSet : handTile){
//            for (Tile tile: tileSet){
//                if (Objects.equals(tile.getType(), getTile.getType()) && tile.getMagnitude() == getTile.getMagnitude()){
//                    count++;
//                    if (count==4){
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
    public boolean checkCanDarkKong() {
        Rule rule = new Rule();
        ArrayList<Tile> kongSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTile){
            kongSet.addAll(rule.checkKong(tileSet));
        }
        return kongSet.size() == 4;
    }


    public void pong(Tile endDiscardTile) {
        pongTileSet.add(endDiscardTile);
        int count = 0;
        for (ArrayList<Tile> tileSet : handTile){
            ArrayList<Tile> removedTile = new ArrayList<>();
            for (Tile tile: tileSet){
                if (Objects.equals(tile.getType(), endDiscardTile.getType()) && tile.getMagnitude() == endDiscardTile.getMagnitude()){
                    if (count < 2){
                        count++;
                        pongTileSet.add(tile);
                        removedTile.add(tile);

                    }
                }
            }
            tileSet.removeAll(removedTile);
        }

    }

    public void brightKong(Tile endDiscardTile) {
        brightKongTileSet.add(endDiscardTile);
        int count = 0;
        for (ArrayList<Tile> tileSet : handTile){
            ArrayList<Tile> removedTile = new ArrayList<>();
            for (Tile tile: tileSet){
                if (Objects.equals(tile.getType(), endDiscardTile.getType()) && tile.getMagnitude() == endDiscardTile.getMagnitude()){
                    if (count < 3){
                        count++;
                        brightKongTileSet.add(tile);
                        removedTile.add(tile);
                    }
                }
            }
            tileSet.removeAll(removedTile);
        }
    }

    public void darkKong() {
        Rule rule = new Rule();
        ArrayList<Tile> kongSet = new ArrayList<>();
        for (ArrayList<Tile> tileSet : handTile){
            kongSet.addAll(rule.checkKong(tileSet));
        }
        int count = 0;
        for (ArrayList<Tile> tileSet : handTile){
            ArrayList<Tile> removedTile = new ArrayList<>();
            for (Tile tile: tileSet){
                if (Objects.equals(tile.getType(), kongSet.get(0).getType()) && tile.getMagnitude() == kongSet.get(0).getMagnitude()){
                    if (count < 4){
                        count++;
                        darkKongTileSet.add(tile);
                        removedTile.add(tile);
                    }
                }
            }
            tileSet.removeAll(removedTile);
        }
    }
}
