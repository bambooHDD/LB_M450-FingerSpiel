package Spiel.Mocks.TestMock;

import Spiel.Game.GameState;
import Spiel.Mocks.MockSpieler;
import Spiel.Spieler.ISpieler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockSpielerTest {
    private MockSpieler mockSpieler;
    private GameState dummyGameState;

    @BeforeEach
    void setUp() {
        // Create a mock player with predefined moves
        mockSpieler = new MockSpieler("TestPlayer", new int[]{1, 2, 3});

        // Create a second mock player for the game state
        MockSpieler opponent = new MockSpieler("Opponent", new int[]{4, 5, 6});

        // Create a dummy GameState with the players
        List<ISpieler> players = Arrays.asList(mockSpieler, opponent);
        dummyGameState = new GameState(players);
    }

    @Test
    void testMakeMoveReturnsCorrectSequence() {
        // Test that the moves are returned in sequence
        assertEquals(1, mockSpieler.makeMove(dummyGameState));
        assertEquals(2, mockSpieler.makeMove(dummyGameState));
        assertEquals(3, mockSpieler.makeMove(dummyGameState));
    }

    @Test
    void testMakeMoveThrowsExceptionWhenOutOfMoves() {
        // Use up all moves
        mockSpieler.makeMove(dummyGameState);
        mockSpieler.makeMove(dummyGameState);
        mockSpieler.makeMove(dummyGameState);

        // Next call should throw exception
        assertThrows(IllegalStateException.class, () -> mockSpieler.makeMove(dummyGameState));
    }

    @Test
    void testResetMovesResetsIndex() {
        // Use some moves
        mockSpieler.makeMove(dummyGameState);
        mockSpieler.makeMove(dummyGameState);

        // Reset
        mockSpieler.resetMoves();

        // Should start from beginning again
        assertEquals(1, mockSpieler.makeMove(dummyGameState));
    }

    @Test
    void testShouldThrowExceptionWhenConfigured() {
        // Configure to throw exception
        mockSpieler.setShouldThrowException(true);

        // Should throw exception on move
        assertThrows(RuntimeException.class, () -> mockSpieler.makeMove(dummyGameState));
    }

    @Test
    void testConstructorWithoutExceptionFlag() {
        // Test the second constructor
        MockSpieler player = new MockSpieler("Player2", new int[]{5, 6});

        assertEquals(5, player.makeMove(dummyGameState));
        assertEquals(6, player.makeMove(dummyGameState));
    }

    @Test
    void testGetPlayerName() {
        assertEquals("TestPlayer", mockSpieler.getName());
    }

    // Additional tests to improve coverage

    @Test
    void testConstructorWithExceptionFlag() {
        // Test the first constructor with explicit exception flag
        MockSpieler player = new MockSpieler("ExceptionPlayer", new int[]{1, 2}, true);

        // Should throw exception immediately
        assertThrows(RuntimeException.class, () -> player.makeMove(dummyGameState));
    }

    @Test
    void testEmptyMovesArray() {
        // Test with empty moves array
        MockSpieler emptyPlayer = new MockSpieler("EmptyPlayer", new int[]{});

        // Should throw exception on first move
        assertThrows(IllegalStateException.class, () -> emptyPlayer.makeMove(dummyGameState));
    }

    @Test
    void testSwitchingExceptionBehavior() {
        // Set to throw exception
        mockSpieler.setShouldThrowException(true);
        assertThrows(RuntimeException.class, () -> mockSpieler.makeMove(dummyGameState));

        // Switch back to normal behavior
        mockSpieler.setShouldThrowException(false);
        assertEquals(1, mockSpieler.makeMove(dummyGameState));
    }

    @Test
    void testResetAfterException() {
        // Make a move
        assertEquals(1, mockSpieler.makeMove(dummyGameState));

        // Set exception flag
        mockSpieler.setShouldThrowException(true);
        assertThrows(RuntimeException.class, () -> mockSpieler.makeMove(dummyGameState));

        // Reset and disable exceptions
        mockSpieler.resetMoves();
        mockSpieler.setShouldThrowException(false);

        // Should start from beginning
        assertEquals(1, mockSpieler.makeMove(dummyGameState));
    }

    @Test
    void testMultipleResets() {
        // Reset multiple times
        mockSpieler.resetMoves();
        mockSpieler.resetMoves();
        mockSpieler.resetMoves();

        // Should still work correctly
        assertEquals(1, mockSpieler.makeMove(dummyGameState));
    }
}