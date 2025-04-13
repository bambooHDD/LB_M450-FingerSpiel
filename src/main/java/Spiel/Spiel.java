// path=Spiel/Spiel.java
package Spiel;

import Spiel.Game.GameMode;
import Spiel.Game.GameState;
import Spiel.Game.MatchSystem;
import Spiel.Game.MoveProcessor;
import Spiel.Game.ScoreManager;
import Spiel.Game.StandardGameMode;
import Spiel.Spieler.ISpieler;
import Spiel.UI.UI;

import java.util.List;

/**
 * Main game class that coordinates the game components and provides an interface for the UI.
 */
public class Spiel {
    private final List<ISpieler> spieler;
    private final MatchSystem matchSystem;
    private final GameState gameState;
    private final MoveProcessor moveProcessor;
    private final ScoreManager scoreManager;
    private final GameMode gameMode;

    /**
     * Creates a new game with the specified players and match format.
     */
    public Spiel(List<ISpieler> spieler, int requiredWins) {
        this.spieler = spieler;
        this.gameState = new GameState(spieler);
        this.matchSystem = new MatchSystem(requiredWins);
        this.moveProcessor = gameState.getMoveProcessor();
        this.scoreManager = new ScoreManager(spieler);
        this.gameMode = new StandardGameMode();
        this.matchSystem.setCurrentGame(this.gameState);

        // Initialize the first game
        gameState.initializeGame();
    }

    /**
     * Main method to start the game.
     */
    public static void main(String[] args) {
        UI ui = new UI(null); // The UI will create the game instance
        ui.start();
    }

    /**
     * Updates the score for a given winner.
     *
     * @param winner The player who won the game.
     */
    public void updateScore(ISpieler winner) {
        scoreManager.updateScore(winner);
    }

    /**
     * Gets the winning player for the entire match.
     *
     * @return The winning player, or null if the match is not complete or there is no winner.
     */
    public ISpieler getGewinner() {
        if (!isMatchComplete()) return null;
        return spieler.stream().filter(p -> scoreManager.hasPlayerWon(p, matchSystem.getRequiredWins())).findFirst().orElse(null);
    }

    /**
     * Gets the formatted match score string.
     *
     * @return A string representing the match score.
     */
    public String getMatchScore() {
        return matchSystem.getScoreString(spieler);
    }
    /**
     * Skips the current player's turn.
     */
    public void skipTurn() {
        gameState.nextPlayer();
    }

    public GameState getGameState() {
        return gameState;
    }

    public MatchSystem getMatchSystem() {
        return matchSystem;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public List<ISpieler> getSpieler() {
        return spieler;
    }

    public boolean isSpielLaeuft() {
        return gameState.isGameActive();
    }

    public ISpieler getAktiverSpieler() {
        return gameState.getActivePlayer();
    }

    public int getCurrentMatchNumber() {
        return gameState.getCurrentGameNumber();
    }

    public boolean isMatchComplete() {
        return matchSystem.isMatchComplete();
    }

    public int getRequiredWins() {
        return matchSystem.getRequiredWins();
    }

    public ISpieler getLastGameWinner() {
        return gameState.getLastWinner();
    }
}