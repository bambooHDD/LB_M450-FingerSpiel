package Spiel.Mocks.TestMock;

import Spiel.Game.GameState;
import Spiel.Mocks.MockMatchSystem;
import Spiel.Mocks.MockSpieler;
import Spiel.Spieler.ISpieler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockIntegrationTest {
    private MockSpieler player1;
    private MockSpieler player2;
    private MockMatchSystem matchSystem;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        // Create mock objects
        player1 = new MockSpieler("Player1", new int[]{1, 2, 3});
        player2 = new MockSpieler("Player2", new int[]{4, 5, 6});
        matchSystem = new MockMatchSystem(2);

        // Create player list for GameState
        List<ISpieler> players = Arrays.asList(player1, player2);

        // Initialize game state
        gameState = new GameState(players);
    }

    @Test
    void testMocksWorkTogetherInGameScenario() {
        // Player 1 makes a move
        int move1 = player1.makeMove(gameState);
        assertTrue(move1 >= 1 && move1 <= 3);

        // Player 2 makes a move
        int move2 = player2.makeMove(gameState);
        assertTrue(move2 >= 4 && move2 <= 6);

        // Force match to complete with player 1 as winner
        matchSystem.setMatchComplete(true);
        matchSystem.setForcedWinner(0);

        assertTrue(matchSystem.isMatchComplete());
        assertEquals(1, matchSystem.getWinner());
    }

    @Test
    void testResetBehaviorAcrossMocks() {
        // Make some moves
        player1.makeMove(gameState);
        player2.makeMove(gameState);

        // Set match state
        matchSystem.setMatchComplete(true);
        matchSystem.setForcedWinner(1);

        // Reset all mocks
        player1.resetMoves();
        player2.resetMoves();
        matchSystem.reset();

        // Verify reset state
        assertEquals(1, player1.makeMove(gameState)); // First move again
        assertEquals(4, player2.makeMove(gameState)); // First move again
        assertFalse(matchSystem.isMatchComplete());
        assertEquals(0, matchSystem.getWinner());
    }

    @Test
    void testExceptionHandlingBetweenMocks() {
        // Configure player1 to throw exception
        player1.setShouldThrowException(true);

        // Verify exception propagation
        assertThrows(RuntimeException.class, () -> player1.makeMove(gameState));

        // Player2 should still work
        assertEquals(4, player2.makeMove(gameState));

        // Set match system to throw exception
        matchSystem.setShouldThrowException(true);

        // Verify both components throw exceptions
        assertThrows(RuntimeException.class, () -> matchSystem.isMatchComplete());
        assertThrows(RuntimeException.class, () -> matchSystem.getWinner());
    }

    @Test
    void testMultipleGameRounds() {
        // Round 1
        assertEquals(1, player1.makeMove(gameState));
        assertEquals(4, player2.makeMove(gameState));

        // Round 2
        assertEquals(2, player1.makeMove(gameState));
        assertEquals(5, player2.makeMove(gameState));

        // Reset for new game
        player1.resetMoves();
        player2.resetMoves();

        // New game should start with first moves
        assertEquals(1, player1.makeMove(gameState));
        assertEquals(4, player2.makeMove(gameState));
    }
}