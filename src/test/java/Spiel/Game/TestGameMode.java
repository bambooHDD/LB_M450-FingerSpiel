package Spiel.Game;

import Spiel.Spieler.ISpieler;
import Spiel.Spieler.MenschSpieler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameMode {
    private GameMode gameMode;
    private GameState gameState;
    private List<ISpieler> spieler;

    @BeforeEach
    void setUp() {
        spieler = Arrays.asList(
                new MenschSpieler("Spieler1"),
                new MenschSpieler("Spieler2")
        );
        gameState = new GameState(spieler);
        gameState.initializeGame();
        gameMode = new StandardGameMode();
    }

    @Test
    void testValidMove() {
        // Test valid moves within the allowed range (1-4)
        assertTrue(gameMode.isValidMove(spieler.get(0), spieler.get(1), 1));
        assertTrue(gameMode.isValidMove(spieler.get(0), spieler.get(1), 4));

        // Test invalid moves outside the allowed range
        assertFalse(gameMode.isValidMove(spieler.get(0), spieler.get(1), 0));
        assertFalse(gameMode.isValidMove(spieler.get(0), spieler.get(1), 5));
    }

    @Test
    void testGameOver() {
        // Set both fingers of Spieler1 to 0 to simulate game over condition
        spieler.get(0).setFingerLinks(0);
        spieler.get(0).setFingerRechts(0);
        assertTrue(gameMode.isGameOver(gameState));
    }

    @Test
    void testDetermineWinner() {
        // Set both fingers of Spieler1 to 0 to simulate game over condition
        spieler.get(0).setFingerLinks(0);
        spieler.get(0).setFingerRechts(0);

        // Assert that Spieler2 is the winner
        assertEquals(spieler.get(1), gameMode.determineWinner(gameState));
    }
}