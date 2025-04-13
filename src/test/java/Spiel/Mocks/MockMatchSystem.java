package Spiel.Mocks;

import Spiel.Game.MatchSystem;

/**
 * Mock implementation of MatchSystem for testing purposes.
 * Extends the real MatchSystem but allows for controlled behavior in tests.
 */
public class MockMatchSystem extends MatchSystem {
    private boolean shouldCompleteMatch;
    private int forcedWinnerIndex;
    private boolean shouldThrowException;

    /**
     * Creates a mock match system with the specified required wins.
     * @param requiredWins Number of wins required to complete a match
     */
    public MockMatchSystem(int requiredWins) {
        super(requiredWins);
        this.shouldCompleteMatch = false;
        this.forcedWinnerIndex = -1;
        this.shouldThrowException = false;
    }

    @Override
    public boolean isMatchComplete() {
        if (shouldThrowException) {
            throw new RuntimeException("Mock match system throwing exception as configured");
        }
        return shouldCompleteMatch || super.isMatchComplete();
    }

    @Override
    public int getWinner() {
        if (shouldThrowException) {
            throw new RuntimeException("Mock match system throwing exception as configured");
        }
        if (forcedWinnerIndex >= 0) {
            return forcedWinnerIndex + 1; // Convert to 1-based index
        }
        return super.getWinner();
    }

    /**
     * Forces the match to be considered complete.
     * @param complete Whether the match should be considered complete
     */
    public void setMatchComplete(boolean complete) {
        this.shouldCompleteMatch = complete;
    }

    /**
     * Forces a specific player to be the winner (0-based index).
     * @param playerIndex The index of the player to force as winner
     */
    public void setForcedWinner(int playerIndex) {
        this.forcedWinnerIndex = playerIndex;
    }

    /**
     * Sets whether the system should throw exceptions on method calls.
     * @param shouldThrow Whether to throw exceptions
     */
    public void setShouldThrowException(boolean shouldThrow) {
        this.shouldThrowException = shouldThrow;
    }

    /**
     * Resets all forced behaviors to their default values.
     */
    public void reset() {
        this.shouldCompleteMatch = false;
        this.forcedWinnerIndex = -1;
        this.shouldThrowException = false;
    }
} 