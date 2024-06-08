package logic.games;

import logic.players.Player;
import logic.tiles.CharacterTile;
import logic.tiles.Tile;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameTest {
    public static void main(String[] args) {
        Game game = new Game();

        // 创建模拟 Socket 连接
        List<Socket> sockets = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            sockets.add(new Socket());
        }


        // 测试 getPlayerList 方法
        try {
            List<Player> players = game.getPlayerList();
            assert players.size() == 4 : "getPlayerList 测试失败：玩家数量不正确";
            System.out.println("getPlayerList 测试通过");
        } catch (Exception e) {
            System.err.println("getPlayerList 测试失败");
            e.printStackTrace();
        }

        // 测试 getLibraryTileSet 方法
        try {
            assert game.getLibraryTileSet() != null : "getLibraryTileSet 测试失败：LibraryTileSet 为空";
            System.out.println("getLibraryTileSet 测试通过");
        } catch (Exception e) {
            System.err.println("getLibraryTileSet 测试失败");
            e.printStackTrace();
        }

        // 测试 getCheckState 方法
        try {
            boolean checkState = game.getCheckState();
            System.out.println("getCheckState 测试通过，checkState: " + checkState);
        } catch (Exception e) {
            System.err.println("getCheckState 测试失败");
            e.printStackTrace();
        }

        // 测试 getGameState 方法
        try {
            boolean gameState = game.getGameState();
            System.out.println("getGameState 测试通过，gameState: " + gameState);
        } catch (Exception e) {
            System.err.println("getGameState 测试失败");
            e.printStackTrace();
        }

        // 测试 getEndPlayerDiscardTile 方法
        try {
            Tile endPlayerDiscardTile = game.getEndPlayerDiscardTile();
            System.out.println("getEndPlayerDiscardTile 测试通过，endPlayerDiscardTile: " + endPlayerDiscardTile);
        } catch (Exception e) {
            System.err.println("getEndPlayerDiscardTile 测试失败");
            e.printStackTrace();
        }

        // 测试 getEndPlayerGetTile 方法
        try {
            Tile endPlayerGetTile = game.getEndPlayerGetTile();
            System.out.println("getEndPlayerGetTile 测试通过，endPlayerGetTile: " + endPlayerGetTile);
        } catch (Exception e) {
            System.err.println("getEndPlayerGetTile 测试失败");
            e.printStackTrace();
        }

        // 测试 getKongNumber 方法
        try {
            int kongNumber = game.getKongNumber();
            System.out.println("getKongNumber 测试通过，kongNumber: " + kongNumber);
        } catch (Exception e) {
            System.err.println("getKongNumber 测试失败");
            e.printStackTrace();
        }
    }
}
