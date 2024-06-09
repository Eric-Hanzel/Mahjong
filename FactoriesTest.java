package logic.factories;

import logic.tiles.*;
import logic.tilesSets.*;
import org.junit.Test;


import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

public class FactoriesTest {

    @Test
    public void testDoubleArrayListFactory() {
        DoubleArrayListFactory factory = new DoubleArrayListFactory();

        // Test for HandTileSet
        TileSet handTileSet = factory.getTileSet("HandTile");
        assertNotNull(handTileSet);
        assertTrue(handTileSet instanceof HandTileSet);

        // Test for LockedTileSet
        TileSet lockedTileSet = factory.getTileSet("LockedTile");
        assertNotNull(lockedTileSet);
        assertTrue(lockedTileSet instanceof LockedTileSet);
    }

    @Test
    public void testNonSequenceTilesFactory() {
        NonSequenceTilesFactory factory = new NonSequenceTilesFactory();

        // Test for various HonorTile types
        String[] honorTypes = {"East", "South", "West", "North", "Red", "Green", "White"};
        for (String type : honorTypes) {
            Tile tile = factory.getTile(type);
            assertNotNull(tile);
            assertTrue(tile instanceof HonorTile);
        }
    }

    @Test
    public void testSequenceTilesFactory() {
        SequenceTilesFactory factory = new SequenceTilesFactory();

        // Test for CharacterTile types
        for (int i = 1; i <= 9; i++) {
            Tile tile = factory.getTile(i + "Character");
            assertNotNull(tile);
            assertTrue(tile instanceof CharacterTile);
        }

        // Test for BambooTile types
        for (int i = 1; i <= 9; i++) {
            Tile tile = factory.getTile(i + "Bamboo");
            assertNotNull(tile);
            assertTrue(tile instanceof BambooTile);
        }

        // Test for DotTile types
        for (int i = 1; i <= 9; i++) {
            Tile tile = factory.getTile(i + "Dot");
            assertNotNull(tile);
            assertTrue(tile instanceof DotTile);
        }
    }

    @Test
    public void testSingleArrayListFactory() {
        SingleArrayListFactory factory = new SingleArrayListFactory();

        // Test for LibraryTileSet
        TileSet libraryTileSet = factory.getTileSet("Library");
        assertNotNull(libraryTileSet);
        assertTrue(libraryTileSet instanceof LibraryTileSet);

        // Test for PlayedTileSet
        TileSet playedTileSet = factory.getTileSet("Played");
        assertNotNull(playedTileSet);
        assertTrue(playedTileSet instanceof PlayedTileSet);
    }

    @Test
    public void testTilesFactoryProducer() {
        // Test for SequenceTilesFactory
        TilesFactory sequenceFactory = TilesFactoryProducer.getTilesFactory(true);
        assertNotNull(sequenceFactory);
        assertTrue(sequenceFactory instanceof SequenceTilesFactory);

        // Test for NonSequenceTilesFactory
        TilesFactory nonSequenceFactory = TilesFactoryProducer.getTilesFactory(false);
        assertNotNull(nonSequenceFactory);
        assertTrue(nonSequenceFactory instanceof NonSequenceTilesFactory);
    }

    
}
