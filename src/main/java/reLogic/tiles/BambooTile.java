package reLogic.tiles;


import java.io.Serial;

// Class representing a Bamboo tile in Mahjong
public class BambooTile extends SequenceDecorator{
    @Serial
    private static final long serialVersionUID = 4L;

    // Constructs a BambooTile with the specified magnitude
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
