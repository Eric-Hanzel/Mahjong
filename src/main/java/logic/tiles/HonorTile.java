package logic.tiles;

import logic.players.GameRole;

import java.util.Objects;

public class HonorTile implements Tile,Comparable<Tile>{
    private final String type;
    private final int magnitude;
    private final int number;
    private String owner;
    private boolean flowerState;

    public HonorTile(String type,int number){
        this.type = type;
        this.magnitude = 0;
        this.number = number;
        this.owner = "Library";
        this.flowerState = false;
    }

    @Override
    public boolean equal(Tile otherTile) {
        return Objects.equals(type, otherTile.getType()) && magnitude == otherTile.getMagnitude();
    }

    @Override
    public void changeOwner(GameRole otherOwner) {
        owner = otherOwner.getName();
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
    public int getNumber() {
        return number;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public boolean getFlowerState() {
        return flowerState;
    }

    @Override
    public int compareTo(Tile o) {
        return Integer.compare(this.magnitude,o.getMagnitude());
    }
    public String toString(){
        return type;
        //return type + " " + magnitude + " " + number + " " + owner + " " + flowerState;
    }
}
