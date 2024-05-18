package logic.tiles;

import logic.players.GameRole;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class HonorTile implements Tile,Comparable<Tile>, Serializable {
    @Serial
    private static final long serialVersionUID = 9L;
    private final String type;
    private final int magnitude;
    private boolean lock;

    public HonorTile(String type,int magnitude){
        this.type = type;
        this.magnitude = magnitude;
        lock = false;
    }
    @Override
    public boolean equal(Tile otherTile) {
        return Objects.equals(type, otherTile.getType()) && magnitude == otherTile.getMagnitude();
    }
    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getMagnitude() {
        return magnitude;
    }
    @Override
    public boolean getLock(){
        return lock;
    }
    @Override
    public void setLock(boolean b){
        lock = b;
    }

    @Override
    public int compareTo(Tile o) {
        return Integer.compare(this.magnitude,o.getMagnitude());
    }
    public String toString(){
        return type;
    }
}
