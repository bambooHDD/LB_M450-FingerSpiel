package Spiel.Mocks.TestMock;

import Spiel.Mocks.MockMatchSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockMatchSystemTest {
    private MockMatchSystem mockMatchSystem;

    @BeforeEach
    void setUp() {
        mockMatchSystem = new MockMatchSystem(3); // Requires 3 wins to complete
    }

    @Test
    void testIsMatchCompleteDefault() {
        // By default, the match should not be complete
        assertFalse(mockMatchSystem.isMatchComplete());
    }

    @Test
    void testIsMatchCompleteWhenForced() {
        // Force match to complete
        mockMatchSystem.setMatchComplete(true);
        assertTrue(mockMatchSystem.isMatchComplete());
    }

    @Test
    void testGetWinnerDefault() {
        // By default, there should be no winner (0)
        assertEquals(0, mockMatchSystem.getWinner());
    }

    @Test
    void testGetWinnerWhenForced() {
        // Force player 0 to be the winner (will return 1 because of 1-based indexing)
        mockMatchSystem.setForcedWinner(0);
        assertEquals(1, mockMatchSystem.getWinner());

        // Force player 1 to be the winner
        mockMatchSystem.setForcedWinner(1);
        assertEquals(2, mockMatchSystem.getWinner());
    }

    @Test
    void testThrowExceptionOnIsMatchComplete() {
        // Configure to throw exception
        mockMatchSystem.setShouldThrowException(true);

        // Should throw exception
        assertThrows(RuntimeException.class, () -> mockMatchSystem.isMatchComplete());
    }

    @Test
    void testThrowExceptionOnGetWinner() {
        // Configure to throw exception
        mockMatchSystem.setShouldThrowException(true);

        // Should throw exception
        assertThrows(RuntimeException.class, () -> mockMatchSystem.getWinner());
    }

    @Test
    void testReset() {
        // Set some forced behaviors
        mockMatchSystem.setMatchComplete(true);
        mockMatchSystem.setForcedWinner(1);
        mockMatchSystem.setShouldThrowException(true);

        // Reset
        mockMatchSystem.reset();

        // Verify all settings are back to default
        assertFalse(mockMatchSystem.isMatchComplete());
        assertEquals(0, mockMatchSystem.getWinner());

        // This would throw an exception if shouldThrowException was still true
        mockMatchSystem.isMatchComplete();
    }
}