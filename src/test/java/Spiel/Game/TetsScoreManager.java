package Spiel.Game;

import Spiel.Spieler.ISpieler;
import Spiel.Spieler.MenschSpieler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TetsScoreManager {
    private ScoreManager scoreManager;
    private List<ISpieler> spieler;

    @BeforeEach
    void setUp() {
        spieler = Arrays.asList(
                new MenschSpieler("Spieler1"),
                new MenschSpieler("Spieler2")
        );
        scoreManager = new ScoreManager(spieler);
    }

    @Test
    void testInitialScore() {
        // Verify that both players start with a score of 0
        assertEquals(0, scoreManager.getScore(spieler.get(0)));
        assertEquals(0, scoreManager.getScore(spieler.get(1)));
    }

    @Test
    void testUpdateScore() {
        // Update the score of player 1 and verify the changes
        scoreManager.updateScore(spieler.get(0));
        assertEquals(1, scoreManager.getScore(spieler.get(0)));
        assertEquals(0, scoreManager.getScore(spieler.get(1)));
    }

    @Test
    void testScoreString() {
        // Update the score of player 1 and verify the score string
        scoreManager.updateScore(spieler.get(0));
        String expected = "Spieler1: 1, Spieler2: 0";
        assertEquals(expected, scoreManager.getScoreString(spieler));
    }

    @Test
    void testHasPlayerWon() {
        // Update the score of player 1 to reach the winning score and verify the result
        for (int i = 0; i < 3; i++) {
            scoreManager.updateScore(spieler.get(0));
        }
        assertTrue(scoreManager.hasPlayerWon(spieler.get(0), 3));
        assertFalse(scoreManager.hasPlayerWon(spieler.get(1), 3));
    }
}