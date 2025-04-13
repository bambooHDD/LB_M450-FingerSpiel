package Spiel.Game;

import Spiel.Spieler.ISpieler;
import java.util.List;

/**
 * Handles processing of player moves in the finger game
 */
public class MoveProcessor {
    private List<ISpieler> spieler;
    private GameState gameState;
    private MatchSystem matchSystem;

    public MoveProcessor() {
    }

    public void initialize(List<ISpieler> spieler, GameState gameState, MatchSystem matchSystem) {
        this.spieler = spieler;
        this.gameState = gameState;
        this.matchSystem = matchSystem;
    }

    /**
     * Processes an attack move between players
     *
     * @param attacker The player making the attack
     * @param attackingHand 'l' for left hand, 'r' for right hand
     * @param defender The player being attacked
     * @param defendingHand 'l' for left hand, 'r' for right hand
     * @return true if the move was valid and processed successfully
     */
    public boolean processAttack(ISpieler attacker, char attackingHand, ISpieler defender, char defendingHand) {
        // Prevent a player from attacking themselves
        if (attacker == defender) {
            return false;
        }

        int attackFingers = getHandFingers(attacker, attackingHand);
        int defendFingers = getHandFingers(defender, defendingHand);

        // Validate hands are active (have between 1-4 fingers)
        if (attackFingers <= 0 || attackFingers >= 5) {
            return false;
        }

        if (defendFingers <= 0 || defendFingers >= 5) {
            return false;
        }

        // Calculate new finger count for defender's hand
        int newDefendFingers = attackFingers + defendFingers;

        // Apply the result
        setHandFingers(defender, defendingHand, newDefendFingers);

        return true;
    }

    /**
     * Processes a split move for a player with equal fingers on both hands
     *
     * @param player The player performing the split
     * @return true if the split was valid and processed successfully
     */
    public boolean processSplit(ISpieler player) {
        // First check if the player has both hands with the same number
        // or if one hand has 0 and the other has an even number
        if (player.getFingerLinks() == player.getFingerRechts()) {
            // Traditional split case - both hands have the same number
            return false; // No need to split if they're already equal
        } else if (player.getFingerLinks() == 0 && player.getFingerRechts() % 2 == 0 && player.getFingerRechts() > 0) {
            // Split case where left hand is 0 and right hand has even number
            int newCount = player.getFingerRechts() / 2;
            player.setFingerLinks(newCount);
            player.setFingerRechts(newCount);
            return true;
        } else if (player.getFingerRechts() == 0 && player.getFingerLinks() % 2 == 0 && player.getFingerLinks() > 0) {
            // Split case where right hand is 0 and left hand has even number
            int newCount = player.getFingerLinks() / 2;
            player.setFingerLinks(newCount);
            player.setFingerRechts(newCount);
            return true;
        }

        return false;  // Can't split in other cases
    }

    /**
     * Processes a move from string input
     * @param move The move string (e.g. "l1r" for attack or "s" for split)
     * @return true if the move was valid and processed successfully
     */
    public boolean processMove(String move) {
        if (!gameState.isGameActive()) {
            return false;
        }

        ISpieler currentPlayer = gameState.getActivePlayer();

        boolean validMove = false;
        if (move.equalsIgnoreCase("s")) {
            validMove = processSplit(currentPlayer);
        } else if (move.length() == 3) {
            try {
                char attackingHand = move.charAt(0);
                int defendingPlayerIndex = Character.getNumericValue(move.charAt(1)) - 1;
                char defendingHand = move.charAt(2);

                if ((attackingHand == 'l' || attackingHand == 'r') &&
                        (defendingHand == 'l' || defendingHand == 'r') &&
                        defendingPlayerIndex >= 0 && defendingPlayerIndex < spieler.size()) {
                    validMove = processAttack(
                            currentPlayer,
                            attackingHand,
                            spieler.get(defendingPlayerIndex),
                            defendingHand
                    );
                }
            } catch (Exception e) {
                return false;
            }
        }

        if (validMove) {
            // Check if game is over
            gameState.checkGameStatus();
            if (!gameState.isGameActive()) {
                ISpieler winner = gameState.determineWinner();

                // Update the score for the winner - BUT ONLY HERE, NOT IN processGameEnd
                if (winner != null) {
                    matchSystem.addPoint(spieler.indexOf(winner) + 1);
                }

                // Call processGameEnd but REMOVE the score update in that method
                // Just use it to prepare the next game
                matchSystem.processGameEnd();
            } else {
                // Move to next player
                gameState.nextPlayer();
            }
        }

        return validMove;
    }

    /**
     * Gets the number of fingers on a specific hand
     */
    private int getHandFingers(ISpieler player, char hand) {
        return (hand == 'l') ? player.getFingerLinks() : player.getFingerRechts();
    }

    /**
     * Sets the number of fingers on a specific hand
     */
    private void setHandFingers(ISpieler player, char hand, int fingers) {
        if (hand == 'l') {
            player.setFingerLinks(fingers);
        } else {
            player.setFingerRechts(fingers);
        }
    }

    public List<ISpieler> getSpieler() {
        return spieler;
    }

    public void setSpieler(List<ISpieler> spieler) {
        this.spieler = spieler;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public MatchSystem getMatchSystem() {
        return matchSystem;
    }

    public void setMatchSystem(MatchSystem matchSystem) {
        this.matchSystem = matchSystem;
    }
}