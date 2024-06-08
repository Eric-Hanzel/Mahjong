package reLogic.tiles;

import java.io.Serial;
import java.io.Serializable;

// Abstract class representing a Sequence tile decorator in Mahjong
abstract class SequenceDecorator extends Tile implements Serializable, Comparable<Tile>{
    @Serial
    private static final long serialVersionUID = 2L;
    private Tile tile;

    private int magnitude;

    // Constructs a SequenceDecorator with the specified type, magnitude, and tile
    public SequenceDecorator(String type,int magnitude,Tile tile){
        this.tile = tile;
        this.magnitude = magnitude;
        this.tile.setType(type);
    }

    // Returns the type of the Sequence tile
    @Override
    public String getType() {
        return tile.getType();
    }

    // Returns the magnitude of the Sequence tile
    @Override
    public int getMagnitude(){
        return magnitude;
    }

    // Returns a string representation of the Sequence tile
    @Override
    public String toString(){
        return magnitude+tile.getType();
    }

    // Compares this Sequence tile with another tile by magnitude
    @Override
    public int compareTo(Tile other) {
        return Integer.compare(this.getMagnitude(), other.getMagnitude());
    }
}
