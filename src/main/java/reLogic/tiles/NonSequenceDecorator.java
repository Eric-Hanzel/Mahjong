package reLogic.tiles;

import java.io.Serial;

// Abstract class representing a Non-Sequence tile decorator in Mahjong
abstract class NonSequenceDecorator extends Tile implements Comparable<Tile> {
    @Serial
    private static final long serialVersionUID = 3L;
    private Tile tile;

    // Constructs a NonSequenceDecorator with the specified type and tile
    public NonSequenceDecorator(String type,Tile tile){
        this.tile = tile;
        this.tile.setType(type);
    }

    // Returns the type of the Non-Sequence tile
    @Override
    public String getType() {
        return tile.getType();
    }

    // Returns the magnitude of the Non-Sequence tile, which is always -1
    @Override
    public int getMagnitude(){
        return -1;
    }

    // Returns a string representation of the Non-Sequence tile
    @Override
    public String toString(){
        return tile.getType();
    }

    // Compares this Non-Sequence tile with another tile by type
    @Override
    public int compareTo(Tile other) {
        return CharSequence.compare(this.getType(), other.getType());
    }
}
