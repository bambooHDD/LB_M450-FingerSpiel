package Spiel.Spieler;

import Spiel.Game.GameState;

/**
 * Implementation of a human player.
 */
public class MenschSpieler extends BaseSpieler {

    /**
     * Creates a new human player with the specified name.
     *
     * @param name The player's name
     */
    public MenschSpieler(String name) {
        super(name);
    }

    @Override
    public int makeMove(GameState gameState) {
        return gameState.getPlayers().indexOf(this);
    }
}