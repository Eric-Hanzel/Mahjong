package reLogic.tiles;



import java.io.Serial;

// Class representing a Dot tile in Mahjong
public class DotTile extends SequenceDecorator {
    @Serial
    private static final long serialVersionUID = 6L;

    // Constructs a DotTile with the specified magnitude
    public DotTile(int magnitude){
        super("Dot",magnitude,new Tile() {
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
