package logic.rules;

import logic.games.Game;
import logic.players.Player;
import logic.tiles.CharacterTile;
import logic.tiles.Tile;
import logic.tilesSets.LibraryTileSet;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RulesTest {

    private static final Logger LOGGER = Logger.getLogger(PlayerOperateCheckRule.class.getName());

    public static void main(String[] args) {
        // 禁用日志记录
        LOGGER.setLevel(Level.OFF);

        testCheckOperateLegal();
        testCheckCanPong();
        testCheckCanBrightKong();
        testCheckCanKong();
        testCheckCanHu();
        testCheckAllVictoryConditions();
        testCheckCanChow();
        testGetCanChowTypes();
        testJoinHandLockedTile();
        testDeepCopyTileSet();
        testAllTriplets();
        testBigSingleWait();
        testZhuoWuKui();
        testHaiDiLaoYue();
        testYiTiaoLong();
        testQiDuiZi();
        testGangShangKaiHua();
        testQingYiSe();
        testSuperQiDuiZi();
        testBasicHu();

        // 重新启用日志记录
        LOGGER.setLevel(Level.INFO);
    }

    private static void testCheckOperateLegal() {
        System.out.println("Testing checkOperateLegal...");

        Game game = new Game();
        Player player = new Player("player1");
        game.getPlayerList().add(player);
        PlayerOperateCheckRule rule = PlayerOperateCheckRule.getInstance();

        boolean result = rule.checkOperateLegal("player1", "Discard", game);
        assert !result : "checkOperateLegal 测试失败";

        System.out.println("checkOperateLegal 测试通过");
    }

    private static void testCheckCanPong() {
        System.out.println("Testing checkCanPong...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        Tile endPlayerDiscardTile = new CharacterTile(1);

        boolean result = PlayerOperateCheckRule.checkCanPong(player, endPlayerDiscardTile);
        assert result : "checkCanPong 测试失败";

        System.out.println("checkCanPong 测试通过");
    }

    private static void testCheckCanBrightKong() {
        System.out.println("Testing checkCanBrightKong...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        Tile endPlayerDiscardTile = new CharacterTile(1);

        boolean result = PlayerOperateCheckRule.checkCanBrightKong(player, endPlayerDiscardTile);
        assert result : "checkCanBrightKong 测试失败";

        System.out.println("checkCanBrightKong 测试通过");
    }

    private static void testCheckCanKong() {
        System.out.println("Testing checkCanKong...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));

        boolean result = PlayerOperateCheckRule.checkCanKong(player);
        assert result : "checkCanKong 测试失败";

        System.out.println("checkCanKong 测试通过");
    }

    private static void testCheckCanHu() {
        System.out.println("Testing checkCanHu...");

        Game game = new Game();
        Player player = new Player("player1");
        game.getPlayerList().add(player);
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(5));
        player.getTile(new CharacterTile(5));

        boolean result = PlayerOperateCheckRule.checkCanHu(player, game);
        assert result : "checkCanHu 测试失败";

        System.out.println("checkCanHu 测试通过");
    }

    private static void testCheckAllVictoryConditions() {
        System.out.println("Testing checkAllVictoryConditions...");

        Game game = new Game();
        Player player = new Player("player1");
        VictoryCheckRule victoryCheckRule = VictoryCheckRule.getInstance();

        String result = PlayerOperateCheckRule.checkAllVictoryConditions(game, player, victoryCheckRule, "");
        assert result.isEmpty() : "checkAllVictoryConditions 测试失败";

        System.out.println("checkAllVictoryConditions 测试通过");
    }



    private static void testCheckCanChow() {
        System.out.println("Testing checkCanChow...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(2));
        Tile endPlayerDiscardTile = new CharacterTile(3);

        boolean result = PlayerOperateCheckRule.checkCanChow(player, endPlayerDiscardTile);
        assert result : "checkCanChow 测试失败";

        System.out.println("checkCanChow 测试通过");
    }

    private static void testGetCanChowTypes() {
        System.out.println("Testing getCanChowTypes...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(2));
        Tile endPlayerDiscardTile = new CharacterTile(3);

        String result = PlayerOperateCheckRule.getCanChowTypes(player, endPlayerDiscardTile);
        assert result.contains("1") && result.contains("2") && result.contains("3") : "getCanChowTypes 测试失败";

        System.out.println("getCanChowTypes 测试通过");
    }

    private static void testJoinHandLockedTile() {
        System.out.println("Testing joinHandLockedTile...");

        ArrayList<ArrayList<Tile>> handTileSet = new ArrayList<>();
        handTileSet.add(new ArrayList<>());
        handTileSet.get(0).add(new CharacterTile(1));
        ArrayList<ArrayList<Tile>> lockedTileSet = new ArrayList<>();
        lockedTileSet.add(new ArrayList<>());
        lockedTileSet.get(0).add(new CharacterTile(2));

        ArrayList<ArrayList<Tile>> result = VictoryCheckRule.joinHandLockedTile(handTileSet, lockedTileSet);
        assert result.get(0).size() == 2 : "joinHandLockedTile 测试失败";

        System.out.println("joinHandLockedTile 测试通过");
    }

    private static void testDeepCopyTileSet() {
        System.out.println("Testing deepCopyTileSet...");

        ArrayList<ArrayList<Tile>> original = new ArrayList<>();
        original.add(new ArrayList<>());
        original.get(0).add(new CharacterTile(1));

        ArrayList<ArrayList<Tile>> copy = VictoryCheckRule.deepCopyTileSet(original);
        assert copy.get(0).size() == 1 && copy != original : "deepCopyTileSet 测试失败";

        System.out.println("deepCopyTileSet 测试通过");
    }

    private static void testAllTriplets() {
        System.out.println("Testing allTriplets...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(5));
        player.getTile(new CharacterTile(5));

        VictoryCheckRule rule = VictoryCheckRule.getInstance();

        boolean result = rule.allTriplets(player);
        assert result : "allTriplets 测试失败";

        System.out.println("allTriplets 测试通过");
    }

    private static void testBigSingleWait() {
        System.out.println("Testing bigSingleWait...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(5));
        player.getTile(new CharacterTile(5));

        VictoryCheckRule rule = VictoryCheckRule.getInstance();

        boolean result = rule.bigSingleWait(player);
        assert result : "bigSingleWait 测试失败";

        System.out.println("bigSingleWait 测试通过");
    }

    private static void testZhuoWuKui() {
        System.out.println("Testing zhuoWuKui...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(5));
        player.getTile(new CharacterTile(5));
        player.getHandTileSet().setEndGetTile(new CharacterTile(5));

        VictoryCheckRule rule = VictoryCheckRule.getInstance();

        boolean result = rule.zhuoWuKui(player);
        assert result : "zhuoWuKui 测试失败";

        System.out.println("zhuoWuKui 测试通过");
    }

    private static void testHaiDiLaoYue() {
        System.out.println("Testing haiDiLaoYue...");

        Player player = new Player("player1");
        LibraryTileSet libraryTileSet = new LibraryTileSet();
        libraryTileSet.setTileNumber(0);

        VictoryCheckRule rule = VictoryCheckRule.getInstance();

        boolean result = rule.haiDiLaoYue(player, libraryTileSet, 0);
        assert !result : "haiDiLaoYue 测试失败";

        System.out.println("haiDiLaoYue 测试通过");
    }

    private static void testYiTiaoLong() {
        System.out.println("Testing yiTiaoLong...");

        Player player = new Player("player1");
        for (int i = 1; i <= 9; i++) {
            player.getTile(new CharacterTile(i));
        }

        VictoryCheckRule rule = VictoryCheckRule.getInstance();

        boolean result = rule.yiTiaoLong(player);
        assert result : "yiTiaoLong 测试失败";

        System.out.println("yiTiaoLong 测试通过");
    }

    private static void testQiDuiZi() {
        System.out.println("Testing qiDuiZi...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(5));
        player.getTile(new CharacterTile(5));
        player.getTile(new CharacterTile(6));
        player.getTile(new CharacterTile(6));
        player.getTile(new CharacterTile(7));
        player.getTile(new CharacterTile(7));

        VictoryCheckRule rule = VictoryCheckRule.getInstance();

        boolean result = rule.qiDuiZi(player);
        assert result : "qiDuiZi 测试失败";

        System.out.println("qiDuiZi 测试通过");
    }

    private static void testGangShangKaiHua() {
        System.out.println("Testing gangShangKaiHua...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getHandTileSet().setEndKongTile(new CharacterTile(1));
        player.getHandTileSet().setEndSecondGetTile(new CharacterTile(1));

        VictoryCheckRule rule = VictoryCheckRule.getInstance();

        boolean result = rule.gangShangKaiHua(player);
        assert result : "gangShangKaiHua 测试失败";

        System.out.println("gangShangKaiHua 测试通过");
    }

    private static void testQingYiSe() {
        System.out.println("Testing qingYiSe...");

        Player player = new Player("player1");
        for (int i = 1; i <= 9; i++) {
            player.getTile(new CharacterTile(i));
            player.getTile(new CharacterTile(i));
        }

        VictoryCheckRule rule = VictoryCheckRule.getInstance();

        boolean result = rule.qingYiSe(player);
        assert result : "qingYiSe 测试失败";

        System.out.println("qingYiSe 测试通过");
    }

    private static void testSuperQiDuiZi() {
        System.out.println("Testing superQiDuiZi...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(5));
        player.getTile(new CharacterTile(5));
        player.getTile(new CharacterTile(6));
        player.getTile(new CharacterTile(6));
        player.getTile(new CharacterTile(7));
        player.getTile(new CharacterTile(7));

        VictoryCheckRule rule = VictoryCheckRule.getInstance();

        boolean result = rule.superQiDuiZi(player);
        assert result : "superQiDuiZi 测试失败";

        System.out.println("superQiDuiZi 测试通过");
    }

    private static void testBasicHu() {
        System.out.println("Testing basicHu...");

        Player player = new Player("player1");
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(1));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(2));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(3));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(4));
        player.getTile(new CharacterTile(5));
        player.getTile(new CharacterTile(5));

        VictoryCheckRule rule = VictoryCheckRule.getInstance();

        boolean result = rule.basicHu(player);
        assert result : "basicHu 测试失败";

        System.out.println("basicHu 测试通过");
    }
}
