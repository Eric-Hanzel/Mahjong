package logic.tiles;


import java.io.Serial;

public class CharacterTile extends SequenceDecorator{
    @Serial
    private static final long serialVersionUID = 5L;
    public CharacterTile(int magnitude) {
        super("Character", magnitude,new Tile() {
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
