package logic.players;

import logic.tiles.CharacterTile;
import logic.tiles.Tile;

import java.lang.reflect.Method;

public class PlayerTest {
    public static void main(String[] args) {
        // 创建一个 Player 对象
        Player player = new Player("TestPlayer");

        // 测试 getName 方法
        try {
            assert "TestPlayer".equals(player.getName()) : "getName 测试失败";
            System.out.println("getName 测试通过");
        } catch (Exception e) {
            System.err.println("getName 测试失败");
            e.printStackTrace();
        }

        // 测试 getDealerState 和 setDealerState 方法
        try {
            player.setDealerState(true);
            assert player.getDealerState() : "setDealerState/getDealerState 测试失败";
            System.out.println("setDealerState/getDealerState 测试通过");
        } catch (Exception e) {
            System.err.println("setDealerState/getDealerState 测试失败");
            e.printStackTrace();
        }

        // 测试 getTurnState 和 setTurnState 方法
        try {
            player.setTurnState(true);
            assert player.getTurnState() : "setTurnState/getTurnState 测试失败";
            System.out.println("setTurnState/getTurnState 测试通过");
        } catch (Exception e) {
            System.err.println("setTurnState/getTurnState 测试失败");
            e.printStackTrace();
        }

        // 测试 getTile 方法
        try {
            Tile tile = new CharacterTile(1);
            player.getTile(tile);
            assert player.getHandTileSet().getTileSets().get(0).contains(tile) : "getTile 测试失败";
            System.out.println("getTile 测试通过");
        } catch (Exception e) {
            System.err.println("getTile 测试失败");
            e.printStackTrace();
        }

        // 测试 discard 方法
        try {
            Tile tile = player.discard("Character 1");
            assert tile != null && "Character".equals(tile.getType()) && tile.getMagnitude() == 1 : "discard 测试失败";
            System.out.println("discard 测试通过");
        } catch (Exception e) {
            System.err.println("discard 测试失败");
            e.printStackTrace();
        }

        // 测试 chow 方法
        try {
            player.getTile(new CharacterTile(2));
            player.getTile(new CharacterTile(3));
            Tile chowTile = new CharacterTile(1);
            player.chow(chowTile, "3");
            assert player.getLockedTileSet().getTileSets().get(0).contains(chowTile) : "chow 测试失败";
            System.out.println("chow 测试通过");
        } catch (Exception e) {
            System.err.println("chow 测试失败");
            e.printStackTrace();
        }

        // 测试 pong 方法
        try {
            player.getTile(new CharacterTile(1));
            player.getTile(new CharacterTile(1));
            Tile pongTile = new CharacterTile(1);
            player.pong(pongTile);
            assert player.getLockedTileSet().getTileSets().get(1).contains(pongTile) : "pong 测试失败";
            System.out.println("pong 测试通过");
        } catch (Exception e) {
            System.err.println("pong 测试失败");
            e.printStackTrace();
        }

        // 测试 brightKong 方法
        try {
            player.getTile(new CharacterTile(1));
            player.getTile(new CharacterTile(1));
            player.getTile(new CharacterTile(1));
            Tile kongTile = new CharacterTile(1);
            player.brightKong(kongTile);
            assert player.getLockedTileSet().getTileSets().get(2).contains(kongTile) : "brightKong 测试失败";
            System.out.println("brightKong 测试通过");
        } catch (Exception e) {
            System.err.println("brightKong 测试失败");
            e.printStackTrace();
        }

        // 测试 Kong 方法
        try {
            player.getTile(new CharacterTile(1));
            player.getTile(new CharacterTile(1));
            player.getTile(new CharacterTile(1));
            player.getTile(new CharacterTile(1));
            player.Kong();
            assert player.getLockedTileSet().getTileSets().get(3).size() == 4 : "Kong 测试失败";
            System.out.println("Kong 测试通过");
        } catch (Exception e) {
            System.err.println("Kong 测试失败");
            e.printStackTrace();
        }

    }

}
