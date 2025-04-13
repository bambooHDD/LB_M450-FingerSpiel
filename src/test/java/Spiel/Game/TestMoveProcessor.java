package Spiel.Game;

import Spiel.Spieler.ISpieler;
import Spiel.Spieler.MenschSpieler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TestMoveProcessor {

    private MoveProcessor moveProcessor;
    private ISpieler player1;
    private ISpieler player2;
    private GameState gameState;
    private MatchSystem matchSystem;
    private List<ISpieler> players;

    @BeforeEach
    public void setup() {
        moveProcessor = new MoveProcessor();
        player1 = new MenschSpieler("Player 1");
        player2 = new MenschSpieler("Player 2");
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        gameState = Mockito.mock(GameState.class);
        matchSystem = Mockito.mock(MatchSystem.class);

        when(gameState.isGameActive()).thenReturn(true);
        when(gameState.getPlayers()).thenReturn(players);
        when(gameState.getActivePlayer()).thenReturn(player1);
        when(gameState.isPlayerActive(player1)).thenReturn(true);
        when(gameState.isPlayerActive(player2)).thenReturn(true);

        moveProcessor.initialize(players, gameState, matchSystem);

        // Initialize with standard starting position (1 finger on each hand)
        player1.setFingerLinks(1);
        player1.setFingerRechts(1);
        player2.setFingerLinks(1);
        player2.setFingerRechts(1);
    }

    @Test
    public void testProcessAttack_LeftToLeft() {
        player1.setFingerLinks(2);
        player2.setFingerLinks(1);

        boolean result = moveProcessor.processAttack(player1, 'l', player2, 'l');

        assertTrue(result);
        assertEquals(2, player1.getFingerLinks());  // Attacker's hand unchanged
        assertEquals(3, player2.getFingerLinks());  // Defender's hand incremented by attacker's hand value
        assertEquals(1, player1.getFingerRechts()); // Other hands remain unchanged
        assertEquals(1, player2.getFingerRechts()); // Other hands remain unchanged
    }

    @Test
    public void testProcessAttack_RightToRight() {
        player1.setFingerRechts(3);
        player2.setFingerRechts(2);

        boolean result = moveProcessor.processAttack(player1, 'r', player2, 'r');

        assertTrue(result);
        assertEquals(3, player1.getFingerRechts()); // Attacker's hand unchanged
        assertEquals(5, player2.getFingerRechts()); // Defender's hand becomes 5, which results in 0 (hand dies)
        assertEquals(1, player1.getFingerLinks());  // Other hands remain unchanged
        assertEquals(1, player2.getFingerLinks());  // Other hands remain unchanged
    }

    @Test
    public void testProcessAttack_LeftToRight() {
        player1.setFingerLinks(2);
        player2.setFingerRechts(1);

        boolean result = moveProcessor.processAttack(player1, 'l', player2, 'r');

        assertTrue(result);
        assertEquals(2, player1.getFingerLinks());  // Attacker's hand unchanged
        assertEquals(3, player2.getFingerRechts()); // Defender's hand incremented by attacker's hand value
        assertEquals(1, player1.getFingerRechts()); // Other hands remain unchanged
        assertEquals(1, player2.getFingerLinks());  // Other hands remain unchanged
    }

    @Test
    public void testProcessAttack_RightToLeft() {
        player1.setFingerRechts(4);
        player2.setFingerLinks(2);

        boolean result = moveProcessor.processAttack(player1, 'r', player2, 'l');

        assertTrue(result);
        assertEquals(4, player1.getFingerRechts()); // Attacker's hand unchanged
        assertEquals(6, player2.getFingerLinks());  // Defender's hand becomes 6, which results in 0 (hand dies)
        assertEquals(1, player1.getFingerLinks());  // Other hands remain unchanged
        assertEquals(1, player2.getFingerRechts()); // Other hands remain unchanged
    }

    @Test
    public void testProcessAttack_AttackerHandDead() {
        player1.setFingerLinks(0); // Dead hand

        boolean result = moveProcessor.processAttack(player1, 'l', player2, 'l');

        assertFalse(result);
        // Verify state unchanged
        assertEquals(0, player1.getFingerLinks());
        assertEquals(1, player1.getFingerRechts());
        assertEquals(1, player2.getFingerLinks());
        assertEquals(1, player2.getFingerRechts());
    }

    @Test
    public void testProcessAttack_DefenderHandDead() {
        player2.setFingerLinks(0); // Dead hand

        boolean result = moveProcessor.processAttack(player1, 'l', player2, 'l');

        assertFalse(result);
        // Verify state unchanged
        assertEquals(1, player1.getFingerLinks());
        assertEquals(1, player1.getFingerRechts());
        assertEquals(0, player2.getFingerLinks());
        assertEquals(1, player2.getFingerRechts());
    }

    @Test
    public void testProcessSplit_EvenDistribution() {
        player1.setFingerLinks(0);
        player1.setFingerRechts(4);

        boolean result = moveProcessor.processSplit(player1);

        assertTrue(result);
        assertEquals(2, player1.getFingerLinks());
        assertEquals(2, player1.getFingerRechts());
    }

    @Test
    public void testProcessSplit_OddDistribution() {
        player1.setFingerLinks(1);
        player1.setFingerRechts(4);

        boolean result = moveProcessor.processSplit(player1);

        assertFalse(result);
        assertEquals(1, player1.getFingerLinks());
        assertEquals(4, player1.getFingerRechts());
    }

    @Test
    public void testProcessSplit_InsufficientTotal() {
        // Only 1 finger total - cannot split
        player1.setFingerLinks(0);
        player1.setFingerRechts(1);

        boolean result = moveProcessor.processSplit(player1);

        assertFalse(result);
        assertEquals(0, player1.getFingerLinks());
        assertEquals(1, player1.getFingerRechts());
    }

    @Test
    public void testProcessSplit_AlreadyBalanced() {
        // Already evenly distributed, cannot split
        player1.setFingerLinks(2);
        player1.setFingerRechts(2);

        boolean result = moveProcessor.processSplit(player1);

        assertFalse(result);
        assertEquals(2, player1.getFingerLinks());
        assertEquals(2, player1.getFingerRechts());
    }

    @Test
    public void testProcessSplit_OneHandDead() {
        // One hand dead, other has 4 fingers
        player1.setFingerLinks(0);
        player1.setFingerRechts(4);

        boolean result = moveProcessor.processSplit(player1);

        assertTrue(result);
        assertEquals(2, player1.getFingerLinks());
        assertEquals(2, player1.getFingerRechts());
    }

    @Test
    public void testProcessSplit_BothHandsDead() {
        player1.setFingerLinks(0);
        player1.setFingerRechts(0);

        boolean result = moveProcessor.processSplit(player1);

        assertFalse(result);
        assertEquals(0, player1.getFingerLinks());
        assertEquals(0, player1.getFingerRechts());
    }

    @Test
    public void testProcessAttack_AttackerIsDefender() {
        boolean result = moveProcessor.processAttack(player1, 'l', player1, 'l');

        assertFalse(result);
        // Verify state unchanged
        assertEquals(1, player1.getFingerLinks());
        assertEquals(1, player1.getFingerRechts());
    }

    @Test
    public void testProcessMove_ValidAttackMove() {
        when(gameState.isGameActive()).thenReturn(true);
        when(gameState.getActivePlayer()).thenReturn(player1);

        boolean result = moveProcessor.processMove("l2l");

        assertTrue(result);
    }

    @Test
    public void testProcessMove_ValidSplitMove() {
        player1.setFingerLinks(0);
        player1.setFingerRechts(4);
        when(gameState.isGameActive()).thenReturn(true);
        when(gameState.getActivePlayer()).thenReturn(player1);

        boolean result = moveProcessor.processMove("s");

        assertTrue(result);
        assertEquals(2, player1.getFingerLinks());
        assertEquals(2, player1.getFingerRechts());
    }

    @Test
    public void testProcessMove_InvalidMove_GameNotActive() {
        when(gameState.isGameActive()).thenReturn(false);

        boolean result = moveProcessor.processMove("l2l");

        assertFalse(result);
    }

    @Test
    public void testProcessMove_InvalidMove_InvalidFormat() {
        when(gameState.isGameActive()).thenReturn(true);
        when(gameState.getActivePlayer()).thenReturn(player1);

        boolean result = moveProcessor.processMove("invalid");

        assertFalse(result);
    }

    @Test
    public void testProcessMove_InvalidMove_InvalidPlayerIndex() {
        when(gameState.isGameActive()).thenReturn(true);
        when(gameState.getActivePlayer()).thenReturn(player1);

        boolean result = moveProcessor.processMove("l3l"); // Player 3 does not exist

        assertFalse(result);
    }

    @Test
    public void testProcessMove_InvalidMove_InvalidHand() {
        when(gameState.isGameActive()).thenReturn(true);
        when(gameState.getActivePlayer()).thenReturn(player1);

        boolean result = moveProcessor.processMove("x2y"); // Invalid hand

        assertFalse(result);
    }
}