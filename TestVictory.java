package logic.rules;

import logic.tiles.HandTile;
import logic.tiles.LibraryTile;
import logic.tiles.Tile;
import logic.tiles.TileType;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestVictory {

    private VictoryRule victoryRule;

    @Before
    public void setUp() {
        victoryRule = new VictoryRule();
    }

    @Test
    public void testAllTriplets() {
        HandTile handTile = createHandTile(
                new int[][]{{1, 1, 1}, {2, 2, 2}, {3, 3, 3}, {4, 4, 4}, {5, 5}}
        );
        assertTrue(victoryRule.allTriplets(handTile));

        handTile = createHandTile(
                new int[][]{{1, 1, 1}, {2, 2, 2}, {3, 3, 3}, {4, 4, 4}, {5, 6}}
        );
        assertFalse(victoryRule.allTriplets(handTile));
    }

    @Test
    public void testBigSingleWait() {
        HandTile handTile = createHandTile(
                new int[][]{{1, 1, 1, 1}, {2, 3, 4}, {5, 6, 7}, {8, 9, 10}, {11, 11}}
        );
        assertTrue(victoryRule.bigSingleWait(handTile));

        handTile = createHandTile(
                new int[][]{{1, 1, 1, 1}, {2, 3, 4}, {5, 6, 7}, {8, 9, 10}, {11, 12}}
        );
        assertFalse(victoryRule.bigSingleWait(handTile));
    }

    @Test
    public void testZhuoWuKui() {
        HandTile handTile = createHandTileWithEndTile(
                new int[][]{{1, 1, 1, 1}, {2, 3, 4}, {5, 6, 7}, {8, 9, 10}, {11, 11}},
                new Tile() {
                    @Override
                    public String getType() {
                        return null;
                    }

                    @Override
                    public int getMagnitude() {
                        return 0;
                    }

                    @Override
                    public boolean equal(Tile otherTile) {
                        return false;
                    }
                }
        );
        assertTrue(victoryRule.zhuoWuKui(handTile));

        handTile = createHandTileWithEndTile(
                new int[][]{{1, 1, 1, 1}, {2, 3, 4}, {5, 6, 7}, {8, 9, 10}, {11, 11}},
                new Tile() {
                    @Override
                    public String getType() {
                        return null;
                    }

                    @Override
                    public int getMagnitude() {
                        return 0;
                    }

                    @Override
                    public boolean equal(Tile otherTile) {
                        return false;
                    }
                }
        );
        assertFalse(victoryRule.zhuoWuKui(handTile));
    }

    @Test
    public void testHaiDiLaoYue() {
        HandTile handTile = createHandTile(
                new int[][]{{1, 1, 1, 1}, {2, 3, 4}, {5, 6, 7}, {8, 9, 10}, {11, 11}}
        );
        LibraryTile libraryTile = new LibraryTile(0);
        assertTrue(victoryRule.haiDiLaoYue(handTile, libraryTile));

        libraryTile = new LibraryTile(1);
        assertFalse(victoryRule.haiDiLaoYue(handTile, libraryTile));
    }

    @Test
    public void testYiTiaoLong() {
        HandTile handTile = createHandTile(
                new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {11, 11, 11}, {12, 12}}
        );
        assertTrue(victoryRule.yiTiaoLong(handTile));

        handTile = createHandTile(
                new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 10}, {11, 11, 11}, {12, 12}}
        );
        assertFalse(victoryRule.yiTiaoLong(handTile));
    }

    @Test
    public void testQiDuiZi() {
        HandTile handTile = createHandTile(
                new int[][]{{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}}
        );
        assertTrue(victoryRule.qiDuiZi(handTile));

        handTile = createHandTile(
                new int[][]{{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 8}}
        );
        assertFalse(victoryRule.qiDuiZi(handTile));
    }

    @Test
    public void testGangShangKaiHua() {
        HandTile handTile = createHandTile(
                new int[][]{{1, 1, 1, 1}, {2, 3, 4}, {5, 6, 7}, {8, 9, 10}, {11, 11}}
        );
        assertTrue(victoryRule.gangShangKaiHua(handTile, true));

        handTile = createHandTile(
                new int[][]{{1, 1, 1, 1}, {2, 3, 4}, {5, 6, 7}, {8, 9, 10}, {11, 12}}
        );
        assertFalse(victoryRule.gangShangKaiHua(handTile, true));
    }

    @Test
    public void testQingYiSe() {
        HandTile handTile = createHandTile(
                new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {11, 11, 11}, {12, 12}},
                TileType.CHARACTER
        );
        assertTrue(victoryRule.qingYiSe(handTile));

        handTile = createHandTile(
                new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {11, 11, 11}, {12, 12}},
                TileType.BAMBOO
        );
        assertFalse(victoryRule.qingYiSe(handTile));
    }

    @Test
    public void testSuperQiDuiZi() {
        HandTile handTile = createHandTile(
                new int[][]{{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}},
                1
        );
        assertTrue(victoryRule.superQiDuiZi(handTile));

        handTile = createHandTile(
                new int[][]{{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}},
                0
        );
        assertFalse(victoryRule.superQiDuiZi(handTile));
    }

    private HandTile createHandTile(int[][] tiles) {
        return createHandTile(tiles, TileType.DOT);
    }

    private HandTile createHandTile(int[][] tiles, TileType type) {
        ArrayList<ArrayList<Tile>> tileSet = new ArrayList<>();
        for (int[] set : tiles) {
            ArrayList<Tile> tileList = new ArrayList<>();
            for (int tile : set) {
                tileList.add(new Tile() {
                    @Override
                    public String getType() {
                        return null;
                    }

                    @Override
                    public int getMagnitude() {
                        return 0;
                    }

                    @Override
                    public boolean equal(Tile otherTile) {
                        return false;
                    }
                });
            }
            tileSet.add(tileList);
        }
        return new HandTile(tileSet);
    }

    private HandTile createHandTileWithEndTile(int[][] tiles, Tile endTile) {
        HandTile handTile = createHandTile(tiles);
        handTile.setEndTile(endTile);
        return handTile;
    }

    private HandTile createHandTile(int[][] tiles, int kongs) {
        HandTile handTile = createHandTile(tiles);
        handTile.setKongNumber(kongs);
        return handTile;
    }
}
