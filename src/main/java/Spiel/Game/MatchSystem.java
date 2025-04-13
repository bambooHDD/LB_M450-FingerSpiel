package Spiel.Game;

import Spiel.Spiel;
import Spiel.Spieler.ISpieler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the match scoring and determines the overall winner.
 */
public class MatchSystem {
    private final Map<Integer, Integer> playerScores;
    private final int requiredWins;
    private GameState currentGame;

    /**
     * Constructs a MatchSystem with a specified number of required wins.
     *
     * @param requiredWins The number of wins required to complete the match.
     */
    public MatchSystem(int requiredWins) {
        this.requiredWins = requiredWins;
        this.playerScores = new HashMap<>();
    }

    /**
     * Processes the end of a game, updates scores, and prepares for the next game.
     *
     * @return true if the match is complete, false if more games are needed.
     */
    public boolean processGameEnd() {
        if (currentGame == null) {
            return false;
        }



        if (isMatchComplete()) {
            return true;
        } else {
            currentGame.prepareNextGame();
            return false;
        }
    }

    /**
     * Adds a point to a player's score.
     *
     * @param playerIndex 1-based player index.
     */
    // Modify MatchSystem.java
    public void addPoint(int playerIndex) {
        // Update internal score tracking
        int currentScore = playerScores.getOrDefault(playerIndex, 0);
        playerScores.put(playerIndex, currentScore + 1);
    }

    /**
     * Resets all scores in the match.
     */
    public void resetScores() {
        playerScores.clear();
    }

    /**
     * Gets the winning player index (1-based).
     *
     * @return The index of the winning player, or 0 if there is no winner yet.
     */
    public int getWinner() {
        for (Map.Entry<Integer, Integer> entry : playerScores.entrySet()) {
            if (entry.getValue() >= requiredWins) {
                return entry.getKey();
            }
        }
        return 0; // No winner yet
    }

    /**
     * Checks if the match is complete (any player has reached the required wins).
     *
     * @return true if the match is complete, false otherwise.
     */
    public boolean isMatchComplete() {
        for (int score : playerScores.values()) {
            if (score >= requiredWins) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the formatted score string.
     *
     * @param players A list of players in the match.
     * @return A string representing the scores of all players.
     */
    public String getScoreString(List<ISpieler> players) {
        StringBuilder scoreBuilder = new StringBuilder();

        for (int i = 0; i < players.size(); i++) {
            ISpieler player = players.get(i);
            int score = playerScores.getOrDefault(i + 1, 0);

            if (i > 0) {
                scoreBuilder.append(" | ");
            }

            scoreBuilder.append(player.getName()).append(": ").append(score);
        }

        return scoreBuilder.toString();
    }

    /**
     * Gets a player's current score.
     *
     * @param playerIndex 1-based player index.
     * @return The player's score, or 0 if the player has no score.
     */
    public int getPlayerScore(int playerIndex) {
        return playerScores.getOrDefault(playerIndex, 0);
    }


    public int getRequiredWins() {
        return requiredWins;
    }


    public void setCurrentGame(GameState gameState) {
        this.currentGame = gameState;
    }
}