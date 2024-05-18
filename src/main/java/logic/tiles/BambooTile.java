package logic.tiles;


import java.io.Serial;

public class BambooTile extends SequenceDecorator{
    @Serial
    private static final long serialVersionUID = 4L;
    public BambooTile(int magnitude){
        super("Bamboo",magnitude,new Tile() {
            @Override
            public int compareTo(Tile other) {
                return Integer.compare(this.getMagnitude(), other.getMagnitude());

            }

            @Override
            public int getMagnitude() {
                return magnitude;
            }
        });
    }
}
