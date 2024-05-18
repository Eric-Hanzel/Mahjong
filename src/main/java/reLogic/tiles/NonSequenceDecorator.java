package reLogic.tiles;

import java.io.Serial;

abstract class NonSequenceDecorator extends Tile implements Comparable<Tile> {
    @Serial
    private static final long serialVersionUID = 3L;
    private Tile tile;
    public NonSequenceDecorator(String type,Tile tile){
        this.tile = tile;
        this.tile.setType(type);
    }
    @Override
    public String getType() {
        return tile.getType();
    }
    @Override
    public int getMagnitude(){
        return -1;
    }
    @Override
    public String toString(){
        return tile.getType();
    }
    @Override
    public int compareTo(Tile other) {
        return CharSequence.compare(this.getType(), other.getType());
    }
}
