// path=Spiel/UI/PlayerSelectionMenu.java
package Spiel.UI;

import Spiel.Spieler.ComputerSpieler;
import Spiel.Spieler.ISpieler;
import Spiel.Spieler.MenschSpieler;
import Spiel.Spiel;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the player selection menu and game setup.
 */
public class PlayerSelectionMenu {
    protected Scanner scanner;

    public PlayerSelectionMenu() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Handles the custom player setup.
     *
     * @return A configured Spiel instance
     */
    private Spiel handleCustomPlayers() {
        List<ISpieler> players = new ArrayList<>();
        int playerCount;

        while (true) {
            System.out.print("Enter number of players (2-9): ");
            playerCount = getIntegerInput();

            if (playerCount >= 2 && playerCount <= 9) {
                break;
            } else {
                System.out.println("Please enter a number between 2 and 9.");
            }
        }

        for (int i = 0; i < playerCount; i++) {
            System.out.println("\nPlayer " + (i + 1) + ":");
            System.out.println("1. Human player");
            System.out.println("2. Computer player");
            System.out.print("Select type: ");

            int type = getIntegerInput();
            System.out.print("Enter name: ");
            String name = getScanner().nextLine().trim();
            if (name.isEmpty()) {
                name = type == 1 ? "Player " + (i + 1) : "Computer " + (i + 1);
            }

            if (type == 1) {
                players.add(new MenschSpieler(name));
            } else {
                players.add(new ComputerSpieler(name));
            }
        }

        // Ask for match format
        System.out.print("\nEnter number of wins needed (best-of-N format): ");
        int requiredWins = getIntegerInput();
        if (requiredWins < 1) requiredWins = 3; // Default to best of 3

        return new Spiel(players, requiredWins);
    }

    /**
     * Creates a game with the specified players and default match settings.
     *
     * @param players List of players
     * @return A configured Spiel instance
     */
    private Spiel createGame(List<ISpieler> players) {
        System.out.print("\nEnter number of wins needed (best-of-N format): ");
        int requiredWins = getIntegerInput();
        if (requiredWins < 1) requiredWins = 3; // Default to best of 3

        return new Spiel(players, requiredWins);
    }

    /**
     * Safely reads an integer from user input.
     *
     * @return The integer value entered by the user
     */
    private int getIntegerInput() {
        try {
            String input = getScanner().nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Displays the main menu and returns the configured game.
     *
     * @return A configured Spiel instance
     */
    public Spiel showMenu() {
        List<ISpieler> players = new ArrayList<>();
        int option;

        while (true) {
            System.out.println("\n===== FINGER GAME - PLAYER SELECTION =====");
            System.out.println("1. Player vs Computer");
            System.out.println("2. Player vs Player");
            System.out.println("3. Computer vs Computer");
            System.out.println("4. Custom number of players");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            option = getIntegerInput();

            switch (option) {
                case 0:
                    System.exit(0);
                    break;
                case 1:
                    players.add(new MenschSpieler("Player 1"));
                    players.add(new ComputerSpieler("Computer"));
                    return createGame(players);
                case 2:
                    players.add(new MenschSpieler("Player 1"));
                    players.add(new MenschSpieler("Player 2"));
                    return createGame(players);
                case 3:
                    players.add(new ComputerSpieler("Computer 1"));
                    players.add(new ComputerSpieler("Computer 2"));
                    return createGame(players);
                case 4:
                    return handleCustomPlayers();
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    protected Scanner getScanner() {
        return scanner;
    }
}