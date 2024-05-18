package logic.rules;

import logic.games.Game;
import logic.players.Player;

import java.util.ArrayList;
import java.util.Objects;

public class ScoringRule {
    private static ScoringRule uniqueInstance = null;

    // Singleton pattern to ensure only one instance of ScoringRule exists
    public static ScoringRule getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ScoringRule();
        }
        return uniqueInstance;
    }

    // Private constructor for Singleton pattern
    private ScoringRule() {
    }

    // Calculate the scoring based on victory types, the game state, and the list of players
    public void getScoring(String victoryTypes, Game game, ArrayList<Player> playerArrayList) {
        String[] parts = victoryTypes.split(" ");
        int[] scoringList = new int[]{1, 1, 1, 1};
        int indexOfVictoryPlayer = 0;

        // Find the index of the victory player
        for (Player player : playerArrayList) {
            if (Objects.equals(player.getName(), game.getVictoryPlayer().getName())) {
                indexOfVictoryPlayer = playerArrayList.indexOf(player);
            }
        }

        int indexOfLastPlayer = playerArrayList.indexOf(game.getLastPlayer());

        // Calculate the scoring based on the victory types
        for (String s : parts) {
            switch (s) {
                case "0":
                case "1":
                case "2":
                case "3":
                    scoringList[indexOfVictoryPlayer] *= 2;
                    break;
                case "4":
                    scoringList[indexOfVictoryPlayer] *= 3;
                    break;
                case "5":
                    scoringList[indexOfVictoryPlayer] *= 4;
                    break;
                case "6":
                    scoringList[indexOfVictoryPlayer] *= 5;
                    break;
                case "7":
                    scoringList[indexOfVictoryPlayer] *= 6;
                    break;
                case "8":
                    scoringList[indexOfVictoryPlayer] *= 8;
                    break;
            }
        }

        // Apply additional scoring rules
        if (game.getSelfMo()) { // Self-draw condition
            scoringList[indexOfVictoryPlayer] *= 3;
            for (Player player : playerArrayList) {
                if (!Objects.equals(player.getName(), game.getVictoryPlayer().getName())) {
                    int index = playerArrayList.indexOf(player);
                    scoringList[index] *= -1;
                }
            }
        } else { // Normal win condition
            scoringList[indexOfLastPlayer] *= -1;
        }

        // Update the scoring for each player
        int i = 0;
        for (int s : scoringList) {
            playerArrayList.get(i).setScoring(s);
            i++;
        }
    }
}
