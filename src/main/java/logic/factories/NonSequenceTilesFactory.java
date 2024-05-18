package logic.factories;

import logic.tiles.HonorTile;
import logic.tiles.Tile;

import java.io.Serial;

public class NonSequenceTilesFactory extends TilesFactory{
    @Serial
    private static final long serialVersionUID = 17L;
    // Red White Green East West South North
    @Override
    public Tile getTile(String tileType) {
        if (tileType.equalsIgnoreCase("East")){
            return new HonorTile("East");
        }else if (tileType.equalsIgnoreCase("South")){
            return new HonorTile("South");
        }else if (tileType.equalsIgnoreCase("West")){
            return new HonorTile("West");
        }else if (tileType.equalsIgnoreCase("North")){
            return new HonorTile("North");
        }else if (tileType.equalsIgnoreCase("Red")){
            return new HonorTile("Red");
        }else if (tileType.equalsIgnoreCase("Green")){
            return new HonorTile("Green");
        }else if (tileType.equalsIgnoreCase("White")){
            return new HonorTile("White");
        }
        return null;
    }
}
