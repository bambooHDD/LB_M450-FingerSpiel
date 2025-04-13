package Spiel.Game;

import Spiel.Spieler.ComputerSpieler;
import Spiel.Spieler.ISpieler;
import Spiel.Spieler.MenschSpieler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestMatchSystem {

    private MatchSystem matchSystem;
    private GameState gameState;
    private List<ISpieler> players;
    private MoveProcessor moveProcessor;

    @BeforeEach
    public void setup() {
        // Create players
        ISpieler player1 = new MenschSpieler("Player 1");
        ISpieler player2 = new ComputerSpieler("Computer 2");
        players = new ArrayList<>(Arrays.asList(player1, player2));

        // Initialize match components
        matchSystem = new MatchSystem(2); // 2 wins required
        gameState = new GameState(players);
        moveProcessor = new MoveProcessor();

        // Connect components
        moveProcessor.initialize(players, gameState, matchSystem);
        gameState.setMoveProcessor(moveProcessor);
        matchSystem.setCurrentGame(gameState);

        // Initialize game
        gameState.initializeGame();
    }

    @Test
    public void testMatchInitialization() {
        // Verify scores are initially empty
        assertEquals(0, matchSystem.getPlayerScore(1));
        assertEquals(0, matchSystem.getPlayerScore(2));
    }

    @Test
    public void testScoreManagement() {
        // Add points to players and verify scores
        matchSystem.addPoint(1);
        matchSystem.addPoint(2);
        matchSystem.addPoint(1);

        assertEquals(2, matchSystem.getPlayerScore(1));
        assertEquals(1, matchSystem.getPlayerScore(2));
    }

    @Test
    public void testMatchCompletion() {
        // Simulate a match where Player 1 wins
        matchSystem.addPoint(1);
        matchSystem.addPoint(1);
        matchSystem.addPoint(1);

        assertTrue(matchSystem.isMatchComplete());
        assertEquals(1, matchSystem.getWinner());
    }

    @Test
    public void testScoreReset() {
        // Add points and then reset scores
        matchSystem.addPoint(1);
        matchSystem.addPoint(2);
        matchSystem.resetScores();

        assertEquals(0, matchSystem.getPlayerScore(1));
        assertEquals(0, matchSystem.getPlayerScore(2));
    }

    // Integration test cases
    @Test
    public void testScoreTracking() {
        // Get the initial active player (winner)
        ISpieler winner = gameState.getActivePlayer();
        for (ISpieler player : players) {
            if (player != winner) {
                player.setFingerLinks(0);
                player.setFingerRechts(0);
            }
        }
        gameState.checkGameStatus();
        assertFalse(gameState.isGameActive());
        boolean matchComplete = matchSystem.processGameEnd();
        assertEquals(1, matchSystem.getPlayerScore(1));
        assertFalse(matchComplete);
        gameState.checkGameStatus();
        matchComplete = matchSystem.processGameEnd();
        assertEquals(2, matchSystem.getPlayerScore(1));
        assertTrue(matchComplete);
        assertEquals(1, matchSystem.getWinner());
    }

    @Test
    public void testMultipleRounds() {
        players.get(1).setFingerLinks(0);
        players.get(1).setFingerRechts(0);
        gameState.checkGameStatus();
        matchSystem.processGameEnd();
        assertEquals(1, matchSystem.getPlayerScore(1));
        assertEquals(2, gameState.getCurrentGameNumber());
        players.get(0).setFingerLinks(0);
        players.get(0).setFingerRechts(0);
        players.get(1).setFingerLinks(1);
        players.get(1).setFingerRechts(1);
        gameState.checkGameStatus();
        matchSystem.processGameEnd();
        assertEquals(1, matchSystem.getPlayerScore(1));
        assertEquals(1, matchSystem.getPlayerScore(2));
        assertEquals(3, gameState.getCurrentGameNumber());
    }

    @Test
    public void testScoreOutput() {
        matchSystem.addPoint(1);
        matchSystem.addPoint(2);
        matchSystem.addPoint(1);
        String scoreString = matchSystem.getScoreString(players);
        assertTrue(scoreString.contains("Player 1: 2"));
        assertTrue(scoreString.contains("Computer 2: 1"));
    }

    @Test
    public void testMatchCompleteDetection() {
        assertFalse(matchSystem.isMatchComplete());
        matchSystem.addPoint(1);
        matchSystem.addPoint(1);
        assertTrue(matchSystem.isMatchComplete());
        assertEquals(1, matchSystem.getWinner());
    }

    @Test
    public void testGameStateIntegration() {
        players.get(1).setFingerLinks(0);
        players.get(1).setFingerRechts(0);
        gameState.checkGameStatus();
        assertFalse(gameState.isGameActive());
        boolean matchComplete = matchSystem.processGameEnd();
        assertFalse(matchComplete);
        assertEquals(1, matchSystem.getPlayerScore(1));
        assertEquals(2, gameState.getCurrentGameNumber());
        assertTrue(gameState.isGameActive());
    }
}