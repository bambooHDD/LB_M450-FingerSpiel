package Spiel.Game;

import Spiel.Spieler.ISpieler;
import Spiel.Spieler.MenschSpieler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestGameState {

    private GameState gameState;
    private ISpieler player1;
    private ISpieler player2;
    private MoveProcessor moveProcessor;
    private List<ISpieler> players;

    @BeforeEach
    public void setup() {
        // Create real player objects
        player1 = new MenschSpieler("Player 1");
        player2 = new MenschSpieler("Player 2");

        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        // Create a mock MoveProcessor
        moveProcessor = mock(MoveProcessor.class);

        // Initialize GameState and inject the mock MoveProcessor
        gameState = new GameState(players);
        gameState.setMoveProcessor(moveProcessor);
    }

    @Test
    public void testInitializationOfGameState() {
        // Test initial state
        assertEquals(1, gameState.getCurrentGameNumber());
        assertTrue(gameState.isGameActive());

        // Test after initialization
        gameState.initializeGame();

        // Check both players' fingers were set to 1
        assertEquals(1, player1.getFingerLinks());
        assertEquals(1, player1.getFingerRechts());
        assertEquals(1, player2.getFingerLinks());
        assertEquals(1, player2.getFingerRechts());

        // First player should be active
        assertEquals(players.get(0), gameState.getActivePlayer());
        assertTrue(gameState.isGameActive());
    }

    @Test
    public void testNextGame() {
        gameState.initializeGame();

        // First game should have player 1 starting
        assertEquals(players.get(0), gameState.getActivePlayer());
        assertEquals(1, gameState.getCurrentGameNumber());

        gameState.prepareNextGame();

        // Second game should increment game number and rotate starting player
        assertEquals(2, gameState.getCurrentGameNumber());
        assertEquals(players.get(1), gameState.getActivePlayer());

        // Verify fingers reset to 1
        assertEquals(1, player1.getFingerLinks());
        assertEquals(1, player1.getFingerRechts());
        assertEquals(1, player2.getFingerLinks());
        assertEquals(1, player2.getFingerRechts());
    }

    @Test
    public void testNextPlayer() {
        gameState.initializeGame();

        // Initial active player should be player1
        assertEquals(player1, gameState.getActivePlayer());

        // Move to next player
        gameState.nextPlayer();
        assertEquals(player2, gameState.getActivePlayer());

        // Move to next player (should wrap around to player1)
        gameState.nextPlayer();
        assertEquals(player1, gameState.getActivePlayer());
    }

    @Test
    public void testNextPlayerWithInactivePlayer() {
        gameState.initializeGame();

        // Make player2 inactive
        player2.setFingerLinks(0);
        player2.setFingerRechts(0);

        // Move to next player should skip player2
        gameState.nextPlayer();
        // Should stay on player1 since player2 is inactive
        assertEquals(player1, gameState.getActivePlayer());

        // Now make player1 inactive too
        player1.setFingerLinks(0);
        player1.setFingerRechts(0);

        // Should end the game since no active players
        gameState.nextPlayer();
        assertFalse(gameState.isGameActive());
    }

    @Test
    public void testIsPlayerActive() {
        // Player with both hands having fingers
        player1.setFingerLinks(2);
        player1.setFingerRechts(3);
        assertTrue(gameState.isPlayerActive(player1));

        // Player with left hand having no fingers
        player1.setFingerLinks(0);
        player1.setFingerRechts(3);
        assertTrue(gameState.isPlayerActive(player1));

        // Player with right hand having no fingers
        player1.setFingerLinks(2);
        player1.setFingerRechts(0);
        assertTrue(gameState.isPlayerActive(player1));

        // Player with both hands having no fingers
        player1.setFingerLinks(0);
        player1.setFingerRechts(0);
        assertFalse(gameState.isPlayerActive(player1));

        // Player with hand that should become inactive (5 or more fingers)
        player1.setFingerLinks(5);
        player1.setFingerRechts(2);
        assertTrue(gameState.isPlayerActive(player1));
        assertEquals(0, player1.getFingerLinks()); // Should set to 0 when >= 5
    }

    @Test
    public void testCheckGameStatus() {
        gameState.initializeGame();

        // Both players active - game should continue
        player1.setFingerLinks(1);
        player1.setFingerRechts(1);
        player2.setFingerLinks(1);
        player2.setFingerRechts(1);

        gameState.checkGameStatus();
        assertTrue(gameState.isGameActive());
        assertNull(gameState.getLastWinner());
    }

    @Test
    public void testDetermineWinner() {
        // Only player1 active
        player1.setFingerLinks(1);
        player1.setFingerRechts(1);
        player2.setFingerLinks(0);
        player2.setFingerRechts(0);

        ISpieler winner = gameState.determineWinner();
        assertEquals(player1, winner);
        assertEquals(player1, gameState.getLastWinner());

        // No active players
        player1.setFingerLinks(0);
        player1.setFingerRechts(0);

        winner = gameState.determineWinner();
        assertNull(winner);
        assertNull(gameState.getLastWinner());
    }

    @Test
    public void testPlayerGetters() {
        gameState.initializeGame();

        // Test getPlayerByIndex
        assertEquals(player1, gameState.getPlayerByIndex(0));
        assertEquals(player2, gameState.getPlayerByIndex(1));
        assertNull(gameState.getPlayerByIndex(2)); // Out of bounds

        // Test getPlayerIndex
        assertEquals(0, gameState.getPlayerIndex(player1));
        assertEquals(1, gameState.getPlayerIndex(player2));

        // Test getPlayers
        List<ISpieler> retrievedPlayers = gameState.getPlayers();
        assertEquals(2, retrievedPlayers.size());
        assertEquals(player1, retrievedPlayers.get(0));
        assertEquals(player2, retrievedPlayers.get(1));
    }

    @Test
    public void testEndGame() {
        gameState.initializeGame();
        assertTrue(gameState.isGameActive());

        gameState.endGame();
        assertFalse(gameState.isGameActive());
    }

    @Test
    public void testSetWinner() {
        gameState.setWinner(player2);
        // This should have no effect since setWinner is empty
        // We're testing it just for coverage
    }

    @Test
    public void testExecuteAttack_ValidMove() {
        gameState.initializeGame();

        // Mock the behavior of MoveProcessor
        when(moveProcessor.processAttack(player1, 'l', player2, 'r')).thenReturn(true);

        boolean result = gameState.executeAttack('l', 1, 'r');

        assertTrue(result);
        verify(moveProcessor).processAttack(player1, 'l', player2, 'r');
    }

    @Test
    public void testExecuteAttack_InvalidMove() {
        gameState.initializeGame();

        // Mock the behavior of MoveProcessor
        when(moveProcessor.processAttack(player1, 'l', player2, 'r')).thenReturn(false);

        boolean result = gameState.executeAttack('l', 1, 'r');

        assertFalse(result);
        verify(moveProcessor).processAttack(player1, 'l', player2, 'r');
    }

    @Test
    public void testExecuteSplit_ValidMove() {
        gameState.initializeGame();

        // Mock the behavior of MoveProcessor
        when(moveProcessor.processSplit(player1)).thenReturn(true);

        boolean result = gameState.executeSplit();

        assertTrue(result);
        verify(moveProcessor).processSplit(player1);
    }

    @Test
    public void testExecuteSplit_InvalidMove() {
        gameState.initializeGame();

        // Mock the behavior of MoveProcessor
        when(moveProcessor.processSplit(player1)).thenReturn(false);

        boolean result = gameState.executeSplit();

        assertFalse(result);
        verify(moveProcessor).processSplit(player1);
    }

    @Test
    public void testGameFlow_EndToEnd() {
        gameState.initializeGame();

        // Mock the behavior of MoveProcessor for attacks
        when(moveProcessor.processAttack(player1, 'l', player2, 'r')).thenReturn(true);
        when(moveProcessor.processAttack(player2, 'r', player1, 'l')).thenReturn(true);

        // Player 1 attacks Player 2
        boolean attack1 = gameState.executeAttack('l', 1, 'r');
        assertTrue(attack1);

        // Verify that the active player is now Player 2
        assertEquals(player2, gameState.getActivePlayer());

        // Player 2 attacks Player 1
        boolean attack2 = gameState.executeAttack('r', 0, 'l');
        assertTrue(attack2);

        // Verify game status
        gameState.checkGameStatus();
        assertTrue(gameState.isGameActive());
    }
}