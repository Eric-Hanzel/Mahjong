package reLogic.factories;

import reLogic.tiles.HonorTile;
import reLogic.tiles.Tile;

import java.io.Serial;

// A factory class that extends TilesFactory to create non-sequential honor tiles
public class NonSequenceTilesFactory extends TilesFactory{
    @Serial
    // Unique identifier for serialization
    private static final long serialVersionUID = 17L;

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
        return null;// If the tile type does not match any known types, return null
    }
}
