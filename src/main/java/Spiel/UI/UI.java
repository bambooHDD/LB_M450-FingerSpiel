// path=Spiel/UI/UI.java
package Spiel.UI;

import Spiel.Game.MoveProcessor;
import Spiel.Spiel;
import Spiel.Spieler.ComputerSpieler;
import Spiel.Spieler.ISpieler;

import java.util.Scanner;

/**
 * User interface for the finger game.
 */
public class UI {
    private Spiel spiel;
    private MoveProcessor moveProcessor;
    private final Scanner scanner;
    private boolean slowMode = false; // For computer vs computer games

    /**
     * Creates a new UI with an optional game instance.
     * If no game is provided, one will be created via the player selection menu.
     *
     * @param spiel Optional game instance
     */
    public UI(Spiel spiel) {
        this.scanner = new Scanner(System.in);
        this.spiel = spiel;
        this.moveProcessor = new MoveProcessor();
    }

    /**
     * Formats a move for display.
     *
     * @param move The move string
     * @return A human-readable description of the move
     */
    private String formatMove(String move) {
        if (move.equals("s")) {
            return "split";
        }

        if (move.length() != 3) {
            return move;
        }

        String sourceHand = move.charAt(0) == 'l' ? "left" : "right";
        int targetPlayer = Character.getNumericValue(move.charAt(1));
        String targetHand = move.charAt(2) == 'l' ? "left" : "right";

        return sourceHand + " hand → Player " + targetPlayer + "'s " + targetHand + " hand";
    }

    /**
     * Displays the current game state.
     */
    private void displayGameState() {
        System.out.println("\n----- Game " + spiel.getCurrentMatchNumber() + " -----");
        System.out.println("Match score: " + spiel.getMatchScore());

        for (ISpieler player : spiel.getSpieler()) {
            String indicator = player == spiel.getAktiverSpieler() ? "→ " : "  ";
            System.out.println(indicator + player.toString());
        }
        System.out.println();
    }

    /**
     * Starts a new game in the match.
     */
    private void startNewGame() {
        System.out.println("\n===== Game " + (spiel.getCurrentMatchNumber() - 1) + " Over =====");

        ISpieler winner = spiel.getLastGameWinner();
        if (winner != null) {
            System.out.println(winner.getName() + " won the game!");
        } else {
            System.out.println("Game ended in a draw!");
        }

        System.out.println("Match score: " + spiel.getMatchScore());
        System.out.println("Starting game " + spiel.getCurrentMatchNumber() + "...");

        if (!spiel.getSpieler().get(0).getName().startsWith("Computer")) {
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * Displays the final results of the match.
     */
    private void displayFinalResults() {
        System.out.println("\n===== MATCH COMPLETE =====");
        System.out.println("Final score: " + spiel.getMatchScore());

        ISpieler winner = spiel.getGewinner();
        if (winner != null) {
            System.out.println(winner.getName() + " wins the match!");
        } else {
            System.out.println("The match ended in a tie!");
        }
    }

    /**
     * Plays one round of the game.
     */
    private void playOneRound() {
        if (!spiel.isSpielLaeuft()) {
            startNewGame();
            return;
        }

        displayGameState();

        ISpieler currentPlayer = spiel.getAktiverSpieler();
        String move;

        if (currentPlayer instanceof ComputerSpieler) {
            // Get computer move
            move = ((ComputerSpieler) currentPlayer).generateMove(
                    spiel.getSpieler(),
                    spiel.getSpieler().indexOf(currentPlayer)
            );

            System.out.println(currentPlayer.getName() + " is thinking...");
            if (slowMode) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Ignore
                }
            }

            System.out.println(currentPlayer.getName() + " plays: " + formatMove(move));
        } else {
            // Human move
            System.out.println(currentPlayer.getName() + "'s turn");
            System.out.println("Available commands:");
            System.out.println("  - [source hand][target player][target hand] (e.g., 'l2r' attacks player 2's right hand with your left)");
            System.out.println("  - s (split move)");
            System.out.print("Enter your move: ");
            move = scanner.nextLine().trim().toLowerCase();
        }

        boolean validMove = moveProcessor.processMove(move);
        if (!validMove) {
            System.out.println("Invalid move! Try again.");
        }
    }

    /**
     * Starts the UI and game.
     */
    public void start() {
        if (this.spiel == null) {
            // Create new game through player selection menu
            PlayerSelectionMenu menu = new PlayerSelectionMenu();
            this.spiel = menu.showMenu();
        }

        // Initialize the MoveProcessor with the game components
        moveProcessor.initialize(spiel.getSpieler(), spiel.getGameState(), spiel.getMatchSystem());

        // Ask about slow mode if all players are computers
        boolean allComputers = true;
        for (ISpieler player : spiel.getSpieler()) {
            if (!(player instanceof ComputerSpieler)) {
                allComputers = false;
                break;
            }
        }

        if (allComputers) {
            System.out.print("Enable slow mode for computer game? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            slowMode = input.startsWith("y");
        }

        System.out.println("\n===== GAME STARTED =====");
        System.out.println("Match format: Best of " + spiel.getRequiredWins());

        // Game loop
        while (!spiel.isMatchComplete()) {
            playOneRound();
        }

        // Display final results
        displayFinalResults();
    }
}