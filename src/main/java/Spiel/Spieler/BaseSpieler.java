// path=Spiel/Spieler/BaseSpieler.java
package Spiel.Spieler;

import Spiel.Game.GameState;

/**
 * Base implementation of the ISpieler interface with common functionality.
 */
public abstract class BaseSpieler implements ISpieler {
    private int fingerLinks;
    private int fingerRechts;
    private final String name;

    /**
     * Creates a new base player with the specified name.
     *
     * @param name The player's name
     */
    public BaseSpieler(String name) {
        this.name = name;
        this.fingerLinks = 1;
        this.fingerRechts = 1;
    }

    /**
     * Checks if the player can perform a split move.
     * The player can split if one hand has 0 fingers and the other has an even number > 0.
     *
     * @return True if the player can split, false otherwise.
     */
    @Override
    public boolean kannTeilen() {
        // Can split if one hand has 0 fingers and the other has an even number > 0
        if (fingerLinks == 0 && fingerRechts > 0 && fingerRechts % 2 == 0) {
            return true;
        } else return fingerRechts == 0 && fingerLinks > 0 && fingerLinks % 2 == 0;
    }

    /**
     * Performs the split move, distributing fingers evenly between the hands.
     * This method does nothing if the player cannot split.
     */
    @Override
    public void teilen() {
        if (!kannTeilen()) {
            return;
        }

        // Split the fingers evenly between hands
        if (fingerLinks == 0 && fingerRechts > 0) {
            fingerLinks = fingerRechts / 2;
            fingerRechts = fingerRechts / 2;
        } else if (fingerRechts == 0 && fingerLinks > 0) {
            fingerRechts = fingerLinks / 2;
            fingerLinks = fingerLinks / 2;
        }
    }

    /**
     * Returns a string representation of the player, including their name and finger counts.
     *
     * @return A string representation of the player.
     */
    @Override
    public String toString() {
        return name + " [L:" + fingerLinks + ", R:" + fingerRechts + "]";
    }

    /**
     * Abstract method to make a move based on the current game state.
     *
     * @param gameState The current game state.
     * @return An integer representing the move.
     */
    @Override
    public abstract int makeMove(GameState gameState);

    /**
     * Gets the player's name.
     *
     * @return The player's name.
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getFingerLinks() {
        return fingerLinks;
    }

    @Override
    public int getFingerRechts() {
        return fingerRechts;
    }

    @Override
    public void setFingerLinks(int value) {
        this.fingerLinks = value;
    }

    @Override
    public void setFingerRechts(int value) {
        this.fingerRechts = value;
    }
}