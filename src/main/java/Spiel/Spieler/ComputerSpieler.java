package Spiel.Spieler;

import Spiel.Game.GameState;

import java.util.List;
import java.util.Random;

/**
 * Implementation of a computer-controlled player.
 */
public class ComputerSpieler extends BaseSpieler {
    private final Random random = new Random();

    /**
     * Creates a new computer player with the specified name.
     *
     * @param name The player's name
     */
    public ComputerSpieler(String name) {
        super(name);
    }

    /**
     * Generates a move for the computer player based on the current game state.
     *
     * @param allPlayers List of all players in the game
     * @param playerIndex Index of this computer player in the list
     * @return A string representing the move to make
     */
    public String generateMove(List<ISpieler> allPlayers, int playerIndex) {
        // Check for split move first
        if (kannTeilen() && random.nextDouble() < 0.7) {
            return "s";
        }

        // Collect valid moves
        List<String> validMoves = getValidMoves(allPlayers, playerIndex);

        if (validMoves.isEmpty()) {
            return ""; // No valid moves
        }

        // Select random move from valid moves
        return validMoves.get(random.nextInt(validMoves.size()));
    }

    /**
     * Collects all valid moves for this computer player.
     *
     * @param allPlayers List of all players in the game
     * @param playerIndex Index of this computer player in the list
     * @return List of valid move strings
     */
    List<String> getValidMoves(List<ISpieler> allPlayers, int playerIndex) {
        List<String> moves = new java.util.ArrayList<>();

        // Add attack moves
        for (int targetIdx = 0; targetIdx < allPlayers.size(); targetIdx++) {
            // Skip self
            if (targetIdx == playerIndex) {
                continue;
            }

            ISpieler targetPlayer = allPlayers.get(targetIdx);

            // Skip players who are completely out
            if (targetPlayer.getFingerLinks() == 0 && targetPlayer.getFingerRechts() == 0) {
                continue;
            }

            // Try left hand as source
            if (this.getFingerLinks() > 0) {
                // Attack target's left hand if it's not dead
                if (targetPlayer.getFingerLinks() > 0) {
                    moves.add("l" + (targetIdx + 1) + "l");
                }
                // Attack target's right hand if it's not dead
                if (targetPlayer.getFingerRechts() > 0) {
                    moves.add("l" + (targetIdx + 1) + "r");
                }
            }

            // Try right hand as source
            if (this.getFingerRechts() > 0) {
                // Attack target's left hand if it's not dead
                if (targetPlayer.getFingerLinks() > 0) {
                    moves.add("r" + (targetIdx + 1) + "l");
                }
                // Attack target's right hand if it's not dead
                if (targetPlayer.getFingerRechts() > 0) {
                    moves.add("r" + (targetIdx + 1) + "r");
                }
            }
        }

        return moves;
    }

    /**
     * Makes a move based on the current game state.
     *
     * @param gameState The current game state.
     * @return An integer representing the move.
     */
    @Override
    public int makeMove(GameState gameState) {
        List<ISpieler> allPlayers = gameState.getPlayers();
        int playerIndex = allPlayers.indexOf(this);
        String move = generateMove(allPlayers, playerIndex);

        // Process the move through the move processor
        if (!move.isEmpty()) {
            if (move.equals("s")) {
                gameState.executeSplit();
            } else {
                char attackingHand = move.charAt(0);
                int defendingPlayerIndex = Character.getNumericValue(move.charAt(1))-1;
                char defendingHand = move.charAt(2);
                gameState.executeAttack(attackingHand, defendingPlayerIndex, defendingHand);
            }
        }
        // Always return the player index
        return playerIndex;
    }
}