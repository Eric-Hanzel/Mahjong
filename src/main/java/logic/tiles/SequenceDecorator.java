package logic.tiles;

import java.io.Serial;
import java.io.Serializable;

abstract class SequenceDecorator extends Tile implements Serializable, Comparable<Tile>{
    @Serial
    private static final long serialVersionUID = 2L;
    private Tile tile;

    private int magnitude;
    // Character, Bamboo, Dot
    public SequenceDecorator(String type,int magnitude,Tile tile){
        this.tile = tile;
        this.magnitude = magnitude;
        this.tile.setType(type);
    }
    @Override
    public String getType() {
        return tile.getType();
    }
    @Override
    public int getMagnitude(){
        return magnitude;
    }
    @Override
    public String toString(){
        return magnitude+tile.getType();
    }
    @Override
    public int compareTo(Tile other) {
        return Integer.compare(this.getMagnitude(), other.getMagnitude());
    }
}
