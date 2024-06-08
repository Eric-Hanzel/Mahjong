package reLogic.tiles;

import java.io.Serial;
import java.io.Serializable;

// Abstract class representing a generic Tile in Mahjong
public abstract class Tile implements Serializable,Comparable<Tile>{
    @Serial
    private static final long serialVersionUID = 1L;
    private String type;

    // Sets the type of the tile
    public void setType(String type){
        this.type = type;
    }

    // Returns the type of the tile
    public String getType() {
        return type;
    }

    // Returns the magnitude of the tile
    public abstract int getMagnitude();

}
