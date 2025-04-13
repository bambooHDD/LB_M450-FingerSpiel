package Spiel.Game;

import Spiel.Spieler.ISpieler;

public abstract class GameMode {

    /**
     * Checks if a move is valid for a given player.
     *
     * @param source The player making the move.
     * @param target The target player.
     * @param move   The move to be made.
     * @return true if the move is valid, false otherwise.
     */
    public abstract boolean isValidMove(ISpieler source, ISpieler target, int move);

    /**
     * Checks if the game is over based on the current game state.
     *
     * @param state The current game state.
     * @return true if the game is over, false otherwise.
     */
    public abstract boolean isGameOver(GameState state);

    /**
     * Determines the winner of the game based on the current game state.
     *
     * @param state The current game state.
     * @return The winning player, or null if there is no winner.
     */
    public abstract ISpieler determineWinner(GameState state);

    /**
     * Checks if a finger value is valid.
     *
     * @param value The finger value to check.
     * @return true if the finger value is valid (between 0 and 5), false otherwise.
     */
    protected boolean isValidFingerValue(int value) {
        return value >= 0 && value <= 5;
    }

    /**
     * Checks if a player has valid finger values.
     *
     * @param player The player to check.
     * @return true if both finger values are valid, false otherwise.
     */
    protected boolean hasValidFingers(ISpieler player) {
        return isValidFingerValue(player.getFingerLinks()) && isValidFingerValue(player.getFingerRechts());
    }
}