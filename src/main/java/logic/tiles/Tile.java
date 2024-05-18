package logic.tiles;

import java.io.Serial;
import java.io.Serializable;

public abstract class Tile implements Serializable,Comparable<Tile>{
    @Serial
    private static final long serialVersionUID = 1L;
    private String type;
    public void setType(String type){
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public abstract int getMagnitude();

}
