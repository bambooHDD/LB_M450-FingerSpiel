package Spiel.Game;

import Spiel.Spieler.ISpieler;

/**
 * Implements the standard game mode for the finger game.
 */
public class StandardGameMode extends GameMode {
    /**
     * Checks if a move is valid according to the standard game rules.
     *
     * @param source The player making the move.
     * @param target The player being targeted.
     * @param move The move to be made (1-4).
     * @return True if the move is valid, false otherwise.
     */
    @Override
    public boolean isValidMove(ISpieler source, ISpieler target, int move) {
        if (!hasValidFingers(source) || !hasValidFingers(target)) {
            return false;
        }

        if (move < 1 || move > 4) {
            return false;
        }

        int targetLeft = target.getFingerLinks();
        int targetRight = target.getFingerRechts();

        return (move == 1 && targetLeft > 0) ||
                (move == 2 && targetRight > 0) ||
                (move == 3 && targetLeft > 0) ||
                (move == 4 && targetRight > 0);
    }

    /**
     * Checks if the game is over according to the standard game rules.
     * The game is over when the active player has no fingers left.
     *
     * @param state The current game state.
     * @return True if the game is over, false otherwise.
     */
    @Override
    public boolean isGameOver(GameState state) {
        ISpieler activePlayer = state.getActivePlayer();
        return activePlayer.getFingerLinks() == 0 && activePlayer.getFingerRechts() == 0;
    }

    /**
     * Determines the winner of the game.
     * The winner is the player who still has fingers remaining.
     *
     * @param state The current game state.
     * @return The winning player, or null if there is no winner.
     */
    @Override
    public ISpieler determineWinner(GameState state) {
        // The winner is the player who didn't lose all their fingers
        return state.getPlayers().stream()
                .filter(p -> p.getFingerLinks() > 0 || p.getFingerRechts() > 0)
                .findFirst()
                .orElse(null);
    }
}