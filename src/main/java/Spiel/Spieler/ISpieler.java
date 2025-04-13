package Spiel.Spieler;

import Spiel.Game.GameState;

/**
 * Interface for player implementations in the finger game.
 */
public interface ISpieler {
    int getFingerLinks();
    int getFingerRechts();
    void setFingerLinks(int value);
    void setFingerRechts(int value);
    boolean kannTeilen();
    void teilen();
    String getName();
    int makeMove(GameState gameState);
}