package logic.tiles;


import java.io.Serial;

public class HonorTile extends NonSequenceDecorator{
    @Serial
    private static final long serialVersionUID = 7L;
    // Red White Green East West South North
    public HonorTile(String type) {
        super(type,new Tile() {
            @Override
            public int compareTo(Tile o) {
                return CharSequence.compare(this.getType(), o.getType());
            }

            @Override
            public int getMagnitude() {
                return -1;
            }
        });
    }
}
