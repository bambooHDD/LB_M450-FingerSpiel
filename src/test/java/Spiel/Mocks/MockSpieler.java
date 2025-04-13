package Spiel.Mocks;

import Spiel.Spieler.BaseSpieler;
import Spiel.Game.GameState;

/**
 * Mock implementation of ISpieler for testing purposes.
 * Allows configuration of behavior through constructor parameters.
 */
public class MockSpieler extends BaseSpieler {
    private final int[] moves;
    private int currentMoveIndex;
    private boolean shouldThrowException;

    /**
     * Creates a mock player with predefined moves.
     * @param name Player name
     * @param moves Array of moves to make in sequence
     * @param shouldThrowException Whether to throw an exception when making a move
     */
    public MockSpieler(String name, int[] moves, boolean shouldThrowException) {
        super(name);
        this.moves = moves;
        this.currentMoveIndex = 0;
        this.shouldThrowException = shouldThrowException;
    }

    /**
     * Creates a mock player with predefined moves that won't throw exceptions.
     * @param name Player name
     * @param moves Array of moves to make in sequence
     */
    public MockSpieler(String name, int[] moves) {
        this(name, moves, false);
    }

    @Override
    public int makeMove(GameState gameState) {
        if (shouldThrowException) {
            throw new RuntimeException("Mock player throwing exception as configured");
        }

        if (currentMoveIndex >= moves.length) {
            throw new IllegalStateException("No more moves configured for mock player");
        }

        return moves[currentMoveIndex++];
    }

    /**
     * Resets the move index to start from the beginning of the moves array.
     */
    public void resetMoves() {
        currentMoveIndex = 0;
    }

    /**
     * Sets whether the player should throw an exception on the next move.
     * @param shouldThrow Whether to throw an exception
     */
    public void setShouldThrowException(boolean shouldThrow) {
        this.shouldThrowException = shouldThrow;
    }
} 