package Spiel.UI;

import Spiel.Game.GameState;
import Spiel.Spiel;
import Spiel.Spieler.ComputerSpieler;
import Spiel.Spieler.ISpieler;
import Spiel.Spieler.MenschSpieler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestPlayerSelectionMenu {

    private PlayerSelectionMenu menu;
    private InputStream originalIn;

    @BeforeEach
    void setUp() {
        // Save the original System.in to restore it after the tests
        originalIn = System.in;
    }

    private void setupInputAndMenu(String input) {
        // Create a ByteArrayInputStream with the predefined input
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        // Create a new menu instance that will use our prepared input
        menu = new PlayerSelectionMenu();
    }

    @Test
    void testPlayerVsComputer() {
        // Simulate selecting Player vs Computer mode and requiring 3 wins
        setupInputAndMenu("1\n3\n");  // Option 1: Player vs Computer, 3 wins required

        Spiel game = menu.showMenu();

        assertNotNull(game, "Game should not be null");
        GameState gameState = game.getGameState();
        List<ISpieler> players = gameState.getPlayers();
        assertEquals(2, players.size(), "Should have 2 players");
        assertTrue(players.get(0) instanceof MenschSpieler, "First player should be human");
        assertTrue(players.get(1) instanceof ComputerSpieler, "Second player should be computer");

        // Restore original System.in
        System.setIn(originalIn);
    }

    @Test
    void testPlayerVsPlayer() {
        // Simulate selecting Player vs Player mode and requiring 5 wins
        setupInputAndMenu("2\n5\n");  // Option 2: Player vs Player, 5 wins required

        Spiel game = menu.showMenu();

        assertNotNull(game, "Game should not be null");
        GameState gameState = game.getGameState();
        List<ISpieler> players = gameState.getPlayers();
        assertEquals(2, players.size(), "Should have 2 players");
        assertTrue(players.get(0) instanceof MenschSpieler, "First player should be human");
        assertTrue(players.get(1) instanceof MenschSpieler, "Second player should be human");

        // Restore original System.in
        System.setIn(originalIn);
    }

    @Test
    void testComputerVsComputer() {
        // Simulate selecting Computer vs Computer mode and requiring 1 win
        setupInputAndMenu("3\n1\n");  // Option 3: Computer vs Computer, 1 win required

        Spiel game = menu.showMenu();

        assertNotNull(game, "Game should not be null");
        GameState gameState = game.getGameState();
        List<ISpieler> players = gameState.getPlayers();
        assertEquals(2, players.size(), "Should have 2 players");
        assertTrue(players.get(0) instanceof ComputerSpieler, "First player should be computer");
        assertTrue(players.get(1) instanceof ComputerSpieler, "Second player should be computer");

        // Restore original System.in
        System.setIn(originalIn);
    }

    @Test
    void testCustomPlayersConfiguration() {
        // Simulate a custom player configuration with 3 players: Human, Computer, Human
        setupInputAndMenu(
                "4\n" +      // Option 4: Custom number of players
                        "3\n" +      // 3 players
                        "1\n" +      // Player 1: Human
                        "Alice\n" +  // Name: Alice
                        "2\n" +      // Player 2: Computer
                        "Bot1\n" +   // Name: Bot1
                        "1\n" +      // Player 3: Human
                        "Bob\n" +    // Name: Bob
                        "2\n"        // 2 wins required
        );

        Spiel game = menu.showMenu();

        assertNotNull(game, "Game should not be null");
        GameState gameState = game.getGameState();
        List<ISpieler> players = gameState.getPlayers();
        assertEquals(3, players.size(), "Should have 3 players");
        assertTrue(players.get(0) instanceof MenschSpieler, "First player should be human");
        assertEquals("Alice", players.get(0).getName(), "First player's name should be Alice");
        assertTrue(players.get(1) instanceof ComputerSpieler, "Second player should be computer");
        assertEquals("Bot1", players.get(1).getName(), "Second player's name should be Bot1");
        assertTrue(players.get(2) instanceof MenschSpieler, "Third player should be human");
        assertEquals("Bob", players.get(2).getName(), "Third player's name should be Bob");

        // Restore original System.in
        System.setIn(originalIn);
    }

    @Test
    void testInvalidInputThenValidOption() {
        // Simulate providing invalid input first, then a valid option (Player vs Computer)
        setupInputAndMenu("99\n1\n3\n");  // Invalid option, then Option 1: Player vs Computer, 3 wins

        Spiel game = menu.showMenu();

        assertNotNull(game, "Game should not be null");
        GameState gameState = game.getGameState();
        List<ISpieler> players = gameState.getPlayers();
        assertEquals(2, players.size(), "Should have 2 players");

        // Restore original System.in
        System.setIn(originalIn);
    }

    @Test
    void testCustomPlayersWithDefaultNames() {
        // Simulate custom player configuration with empty names, which should default to "Player X" and "Computer X"
        setupInputAndMenu(
                "4\n" +   // Option 4: Custom number of players
                        "2\n" +   // 2 players
                        "1\n" +   // Player 1: Human
                        "\n" +    // Empty name (should default to "Player 1")
                        "2\n" +   // Player 2: Computer
                        "\n" +    // Empty name (should default to "Computer 2")
                        "1\n"     // 1 win required
        );

        Spiel game = menu.showMenu();

        assertNotNull(game, "Game should not be null");
        GameState gameState = game.getGameState();
        List<ISpieler> players = gameState.getPlayers();
        assertEquals(2, players.size(), "Should have 2 players");
        assertEquals("Player 1", players.get(0).getName(), "First player's name should be Player 1");
        assertEquals("Computer 2", players.get(1).getName(), "Second player's name should be Computer 2");

        // Restore original System.in
        System.setIn(originalIn);
    }

    @Test
    void testInvalidWinsInput() {
        // Simulate providing invalid input for the number of wins required
        setupInputAndMenu("1\nabc\n");  // Option 1: Player vs Computer, Invalid wins input

        Spiel game = menu.showMenu();

        assertNotNull(game, "Game should not be null");
        // We'd need a getter in Spiel to test the required wins value
        // assertEquals(3, game.getRequiredWins());

        // Restore original System.in
        System.setIn(originalIn);
    }
    @Test
    void testInvalidPlayerCountInCustomConfiguration() {
        // Simulate providing invalid player counts in custom configuration, then a valid count
        setupInputAndMenu("4\n1\n10\n3\n1\nAlice\n2\nBot1\n1\nBob\n2\n");
        // Invalid counts: 1 and 10, then valid count: 3

        Spiel game = menu.showMenu();

        assertNotNull(game, "Game should not be null");
        GameState gameState = game.getGameState();
        List<ISpieler> players = gameState.getPlayers();
        assertEquals(3, players.size(), "Should have 3 players after valid input");
    }
    @Test
    void testMaximumPlayers() {
        // Simulate configuring the maximum number of players (9) in custom configuration
        setupInputAndMenu(
                "4\n9\n" +  // Option 4: Custom number of players, 9 players
                        "1\nPlayer1\n2\nBot2\n1\nPlayer3\n2\nBot4\n1\nPlayer5\n" +
                        "2\nBot6\n1\nPlayer7\n2\nBot8\n1\nPlayer9\n3\n"  // Names and types
        );

        Spiel game = menu.showMenu();

        assertNotNull(game, "Game should not be null");
        GameState gameState = game.getGameState();
        List<ISpieler> players = gameState.getPlayers();
        assertEquals(9, players.size(), "Should have 9 players");
    }
}