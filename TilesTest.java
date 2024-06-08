package logic.tiles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TilesTest {

    @Test
    public void testBambooTile() {
        for (int i = 1; i <= 9; i++) {
            BambooTile tile = new BambooTile(i);
            assertNotNull(tile);
            assertEquals("Bamboo", tile.getType());
            assertEquals(i, tile.getMagnitude());
            assertEquals(i + "Bamboo", tile.toString());
        }
    }

    @Test
    public void testCharacterTile() {
        for (int i = 1; i <= 9; i++) {
            CharacterTile tile = new CharacterTile(i);
            assertNotNull(tile);
            assertEquals("Character", tile.getType());
            assertEquals(i, tile.getMagnitude());
            assertEquals(i + "Character", tile.toString());
        }
    }

    @Test
    public void testDotTile() {
        for (int i = 1; i <= 9; i++) {
            DotTile tile = new DotTile(i);
            assertNotNull(tile);
            assertEquals("Dot", tile.getType());
            assertEquals(i, tile.getMagnitude());
            assertEquals(i + "Dot", tile.toString());
        }
    }

    @Test
    public void testHonorTile() {
        String[] honorTypes = {"East", "South", "West", "North", "Red", "Green", "White"};
        for (String type : honorTypes) {
            HonorTile tile = new HonorTile(type);
            assertNotNull(tile);
            assertEquals(type, tile.getType());
            assertEquals(-1, tile.getMagnitude());
            assertEquals(type, tile.toString());
        }
    }




    @Test
    public void testTile() {
        Tile tile = new Tile() {
            @Override
            public int getMagnitude() {
                return 5;
            }

            @Override
            public int compareTo(Tile o) {
                return Integer.compare(this.getMagnitude(), o.getMagnitude());
            }
        };
        tile.setType("TestType");
        assertNotNull(tile);
        assertEquals("TestType", tile.getType());
        assertEquals(5, tile.getMagnitude());
        assertEquals("5TestType", tile.toString());
    }
}
