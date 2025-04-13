// path=Spiel/Game/GameState.java
package Spiel.Game;

import Spiel.Spieler.ISpieler;

import java.util.List;

/**
 * Manages the state of a single game including player turns and game status
 */
public class GameState {
    private final List<ISpieler> players;
    private boolean gameActive;
    private ISpieler activePlayer;
    private ISpieler lastWinner;
    private int currentGameNumber;
    private MoveProcessor moveProcessor;
    private int lastStartingPlayerIndex;

    public GameState(List<ISpieler> players) {
        this.players = players;
        this.currentGameNumber = 1;
        this.gameActive = true;
        this.lastStartingPlayerIndex = -1; // Start with -1 so first game starts with player 0
        // Remove MoveProcessor initialization from here
    }

    /**
     * Initializes a new game
     */
    public void initializeGame() {
        // Reset all players' fingers to 1 (starting position)
        for (ISpieler player : players) {
            player.setFingerLinks(1);
            player.setFingerRechts(1);
        }

        gameActive = true;
        // Rotate starting player
        lastStartingPlayerIndex = (lastStartingPlayerIndex + 1) % players.size();
        activePlayer = players.get(lastStartingPlayerIndex);
        System.out.println("Game " + currentGameNumber + " started with player " + (lastStartingPlayerIndex + 1));
    }

    /**
     * Prepares for the next game in the match
     */
    public void prepareNextGame() {
        currentGameNumber++;
        initializeGame();
    }

    /**
     * Advances to the next player's turn
     */
    public void nextPlayer() {
        int currentIndex = players.indexOf(activePlayer);
        int nextIndex = (currentIndex + 1) % players.size();
        int originalNextIndex = nextIndex;
        int attempts = 0;

        // Try to find the next active player
        while (!isPlayerActive(players.get(nextIndex))) {
            nextIndex = (nextIndex + 1) % players.size();
            attempts++;

            // If we've gone full circle and found no active players, something is wrong
            if (nextIndex == originalNextIndex || attempts >= players.size()) {
                System.out.println("Warning: No active players found!");
                gameActive = false;
                return;
            }
        }

        activePlayer = players.get(nextIndex);
        System.out.println("Turn changed to player " + (nextIndex + 1));
    }

    /**
     * Checks if a player is still active in the game (has fingers)
     * Sets fingers to 0 when they reach or exceed 5
     */
    public boolean isPlayerActive(ISpieler player) {
        // Check if left hand is 5 or more, set to 0
        if (player.getFingerLinks() >= 5) {
            player.setFingerLinks(0);
        }

        // Check if right hand is 5 or more, set to 0
        if (player.getFingerRechts() >= 5) {
            player.setFingerRechts(0);
        }

        // A player is active if they have at least one hand with 1-4 fingers
        boolean leftActive = player.getFingerLinks() > 0;
        boolean rightActive = player.getFingerRechts() > 0;

        if (!leftActive && !rightActive) {
            System.out.println("Player " + (players.indexOf(player) + 1) + " is eliminated!");
        }

        return leftActive || rightActive;
    }

    /**
     * Determines if the game has ended
     */
    public void checkGameStatus() {
        int activePlayers = 0;
        ISpieler lastActivePlayer = null;

        for (ISpieler player : players) {
            if (isPlayerActive(player)) {
                activePlayers++;
                lastActivePlayer = player;
            }
        }

        // Game ends when only one player remains
        if (activePlayers <= 1) {
            gameActive = false;
            if (lastActivePlayer != null) {
                lastWinner = lastActivePlayer;
                System.out.println("Game ended! Winner: Player " + (players.indexOf(lastActivePlayer) + 1));
            }
        }
    }

    /**
     * Executes an attack move
     *
     * @param attackingHand 'l' for left hand, 'r' for right hand
     * @param defendingPlayerIndex Index of the defending player
     * @param defendingHand 'l' for left hand, 'r' for right hand
     * @return true if the move was valid and processed successfully
     */
    public boolean executeAttack(char attackingHand, int defendingPlayerIndex, char defendingHand) {
        // Validate player index
        if (defendingPlayerIndex < 0 || defendingPlayerIndex >= players.size()) {
            return false;
        }

        ISpieler defender = players.get(defendingPlayerIndex);
        boolean moveSuccess = moveProcessor.processAttack(activePlayer, attackingHand, defender, defendingHand);

        if (moveSuccess) {
            checkGameStatus();
            if (gameActive) {
                // Only advance to next player if the game is still active
                nextPlayer();
            } else {
                // Game ended, determine winner
                determineWinner();
            }
        }

        return moveSuccess;
    }

    /**
     * Executes a split move for the active player
     *
     * @return true if the split was valid and processed successfully
     */
    public boolean executeSplit() {
        boolean moveSuccess = moveProcessor.processSplit(activePlayer);

        if (moveSuccess) {
            checkGameStatus();
            if (gameActive) {
                // Only advance to next player if the game is still active
                nextPlayer();
            } else {
                // Game ended, determine winner
                determineWinner();
            }
        }

        return moveSuccess;
    }

    /**
     * Determines the winner of the current game.
     * The winner is the last active player.
     * If no player is active, the winner is null.
     *
     * @return The winning player, or null if there is no winner (draw).
     */
    public ISpieler determineWinner() {
        for (ISpieler player : players) {
            if (isPlayerActive(player)) {
                lastWinner = player;
                return player;
            }
        }
        // No winner (draw)
        lastWinner = null;
        return null;
    }

    // Methods needed for gameplay
    public void endGame() {
        gameActive = false;
    }

    /**
     * Gets player by their index
     */
    public ISpieler getPlayerByIndex(int index) {
        if (index >= 0 && index < players.size()) {
            return players.get(index);
        }
        return null;
    }

    /**
     * Gets the index of a player
     */
    public int getPlayerIndex(ISpieler player) {
        return players.indexOf(player);
    }

    public void setWinner(ISpieler winner) {
    }

    // Getters
    public boolean isGameActive() { return gameActive; }
    public ISpieler getActivePlayer() { return activePlayer; }
    public int getCurrentGameNumber() { return currentGameNumber; }
    public ISpieler getLastWinner() { return lastWinner; }
    public List<ISpieler> getPlayers() { return players; }
    public MoveProcessor getMoveProcessor() { return moveProcessor; }

    public void setMoveProcessor(MoveProcessor moveProcessor) {
        this.moveProcessor = moveProcessor;
    }
}