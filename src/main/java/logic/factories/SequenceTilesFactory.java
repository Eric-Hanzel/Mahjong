package logic.factories;

import logic.tiles.BambooTile;
import logic.tiles.CharacterTile;
import logic.tiles.DotTile;
import logic.tiles.Tile;

import java.io.Serial;

public class SequenceTilesFactory extends TilesFactory{
    @Serial
    private static final long serialVersionUID = 16L;
    // Character, Bamboo, Dot
    @Override
    public Tile getTile(String tileType){
        if (tileType.equalsIgnoreCase("1Character")){
            return new CharacterTile(1);
        }else if (tileType.equalsIgnoreCase("2Character")){
            return new CharacterTile(2);
        }else if (tileType.equalsIgnoreCase("3Character")){
            return new CharacterTile(3);
        }else if (tileType.equalsIgnoreCase("4Character")){
            return new CharacterTile(4);
        }else if (tileType.equalsIgnoreCase("5Character")){
            return new CharacterTile(5);
        }else if (tileType.equalsIgnoreCase("6Character")){
            return new CharacterTile(6);
        }else if (tileType.equalsIgnoreCase("7Character")){
            return new CharacterTile(7);
        }else if (tileType.equalsIgnoreCase("8Character")){
            return new CharacterTile(8);
        }else if (tileType.equalsIgnoreCase("9Character")){
            return new CharacterTile(9);
        }else if (tileType.equalsIgnoreCase("1Bamboo")){
            return new BambooTile(1);
        }else if (tileType.equalsIgnoreCase("2Bamboo")){
            return new BambooTile(2);
        }else if (tileType.equalsIgnoreCase("3Bamboo")){
            return new BambooTile(3);
        }else if (tileType.equalsIgnoreCase("4Bamboo")){
            return new BambooTile(4);
        }else if (tileType.equalsIgnoreCase("5Bamboo")){
            return new BambooTile(5);
        }else if (tileType.equalsIgnoreCase("6Bamboo")){
            return new BambooTile(6);
        }else if (tileType.equalsIgnoreCase("7Bamboo")){
            return new BambooTile(7);
        }else if (tileType.equalsIgnoreCase("8Bamboo")){
            return new BambooTile(8);
        }else if (tileType.equalsIgnoreCase("9Bamboo")){
            return new BambooTile(9);
        }else if (tileType.equalsIgnoreCase("1Dot")){
            return new DotTile(1);
        }else if (tileType.equalsIgnoreCase("2Dot")){
            return new DotTile(2);
        }else if (tileType.equalsIgnoreCase("3Dot")){
            return new DotTile(3);
        }else if (tileType.equalsIgnoreCase("4Dot")){
            return new DotTile(4);
        }else if (tileType.equalsIgnoreCase("5Dot")){
            return new DotTile(5);
        }else if (tileType.equalsIgnoreCase("6Dot")){
            return new DotTile(6);
        }else if (tileType.equalsIgnoreCase("7Dot")){
            return new DotTile(7);
        }else if (tileType.equalsIgnoreCase("8Dot")){
            return new DotTile(8);
        }else if (tileType.equalsIgnoreCase("9Dot")){
            return new DotTile(9);
        }
        return null;
    }
}
