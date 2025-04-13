package Spiel.Game;

import Spiel.Spieler.ISpieler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreManager {
    private final Map<ISpieler, Integer> scores;

    public ScoreManager(List<ISpieler> players) {
        scores = new HashMap<>();
        players.forEach(p -> scores.put(p, 0));
    }

    /**
     * Updates the score for a given winner.
     *
     * @param winner The player who won.
     */
    public void updateScore(ISpieler winner) {
        scores.merge(winner, 1, Integer::sum);
    }

    /**
     * Checks if a player has won based on the required wins.
     *
     * @param player The player to check.
     * @param requiredWins The number of wins required to win.
     * @return True if the player has won, false otherwise.
     */
    public boolean hasPlayerWon(ISpieler player, int requiredWins) {
        return getScore(player) >= requiredWins;
    }

    /**
     * Gets the score string for a list of players.
     *
     * @param players The list of players.
     * @return A string representing the scores of all players.
     */
    public String getScoreString(List<ISpieler> players) {
        return players.stream()
                .map(p -> p.getName() + ": " + scores.get(p))
                .collect(Collectors.joining(", "));
    }

    /**
     * Gets the score for a given player.
     *
     * @param player The player to get the score for.
     * @return The score of the player, or 0 if the player is not in the map.
     */
    public int getScore(ISpieler player) {
        return scores.getOrDefault(player, 0);
    }
}