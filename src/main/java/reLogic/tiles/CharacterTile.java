package reLogic.tiles;


import java.io.Serial;

// Class representing a Character tile in Mahjong
public class CharacterTile extends SequenceDecorator{
    @Serial
    private static final long serialVersionUID = 5L;

    // Constructs a CharacterTile with the specified magnitude
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
