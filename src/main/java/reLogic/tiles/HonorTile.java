package reLogic.tiles;


import java.io.Serial;

// Class representing an Honor tile in Mahjong
public class HonorTile extends NonSequenceDecorator{
    @Serial
    private static final long serialVersionUID = 7L;

    // Constructs an HonorTile with the specified type
    public HonorTile(String type) {
        super(type,new Tile() {
            @Override
            public int compareTo(Tile o) {
                return CharSequence.compare(this.getType(), o.getType());
            }

            @Override
            public int getMagnitude() {
                return -1;
            } // Honor tiles do not have a magnitude
        });
    }
}
