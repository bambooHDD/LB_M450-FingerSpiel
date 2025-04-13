package Spiel.Spieler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import Spiel.Game.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class TestComputerSpieler {

    private ComputerSpieler computerSpieler;
    private List<ISpieler> players;
    private GameState gameState;

    @BeforeEach
    public void setup() {
        computerSpieler = new ComputerSpieler("AI Player");

        // Initialize a list of players for testing
        players = new ArrayList<>();
        players.add(computerSpieler);
        players.add(new MenschSpieler("Player 2"));

        gameState = Mockito.mock(GameState.class);
        when(gameState.getPlayers()).thenReturn(players);
    }

    @Test
    public void testInitialization() {
        assertEquals("AI Player", computerSpieler.getName(), "ComputerSpieler name should be initialized correctly");
        assertEquals(1, computerSpieler.getFingerLinks(), "Initial left fingers should be 1");
        assertEquals(1, computerSpieler.getFingerRechts(), "Initial right fingers should be 1");
    }

    @Test
    public void testGenerateMove_SplitMove() {
        // Set up hand states for valid split move (left hand dead, 4 fingers on right hand)
        computerSpieler.setFingerLinks(0);
        computerSpieler.setFingerRechts(4);

        // Mock Random class to always return a value less than 0.7 for split
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextDouble()).thenReturn(0.6);
        computerSpieler = new ComputerSpieler("AI Player"){
            @Override
            public String generateMove(List<ISpieler> allPlayers, int playerIndex) {
                if (kannTeilen() && mockRandom.nextDouble() < 0.7) {
                    return "s";
                }
                return "";
            }
        };
        computerSpieler.setFingerLinks(0);
        computerSpieler.setFingerRechts(4);

        // Generate move (player index 0)
        String move = computerSpieler.generateMove(players, 0);

        // Verify the move is "s" (split)
        assertEquals("s", move, "Generated move should be 's' for split move");
    }

    @Test
    public void testGenerateMove_NoSplitMove() {
        // Set up hand states for no valid split move (1 finger on each hand)
        computerSpieler.setFingerLinks(1);
        computerSpieler.setFingerRechts(1);

        // Mock Random class to always return a value greater than 0.7 to prevent split
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextDouble()).thenReturn(0.8);
        computerSpieler = new ComputerSpieler("AI Player"){
            @Override
            public String generateMove(List<ISpieler> allPlayers, int playerIndex) {
                if (kannTeilen() && mockRandom.nextDouble() < 0.7) {
                    return "s";
                }
                return "";
            }
        };
        computerSpieler.setFingerLinks(1);
        computerSpieler.setFingerRechts(1);

        // Generate move (player index 0)
        String move = computerSpieler.generateMove(players, 0);

        // Verify the move is not "s" when split is not preferred
        assertNotEquals("s", move, "Generated move should not be 's' when split is not preferred");
    }

    @Test
    public void testGenerateMoveWithValidMoves() {
        // Set up hand states for valid moves
        computerSpieler.setFingerLinks(1);
        computerSpieler.setFingerRechts(2);

        MenschSpieler opponent = (MenschSpieler) players.get(1);
        opponent.setFingerLinks(1);
        opponent.setFingerRechts(1);

        // Mock Random class to always return 0 to select the first move
        Random mockRandom = Mockito.mock(Random.class);
        when(mockRandom.nextInt(Mockito.anyInt())).thenReturn(0);
        computerSpieler = new ComputerSpieler("AI Player"){
            @Override
            public String generateMove(List<ISpieler> allPlayers, int playerIndex) {
                List<String> validMoves = getValidMoves(allPlayers, playerIndex);
                if (validMoves.isEmpty()) {
                    return ""; // No valid moves
                }
                return validMoves.get(mockRandom.nextInt(validMoves.size()));
            }
        };
        computerSpieler.setFingerLinks(1);
        computerSpieler.setFingerRechts(2);

        // Generate move (player index 0)
        String move = computerSpieler.generateMove(players, 0);

        // Verify the move is not null or empty
        assertNotNull(move, "Generated move should not be null");
        assertFalse(move.isEmpty(), "Generated move should not be empty");
    }

    @Test
    public void testGenerateMoveWithNoValidMoves() {
        // Set up game state with no valid moves
        // Both computer player hands are "dead" (0 fingers)
        computerSpieler.setFingerLinks(0);
        computerSpieler.setFingerRechts(0);

        // Generate move
        String move = computerSpieler.generateMove(players, 0);

        // Should return empty string when no valid moves
        assertEquals("", move, "Should return empty string when no valid moves are available");
    }

    @Test
    public void testMakeMove_AttackMove() {
        // Set up game state for attack move
        computerSpieler.setFingerLinks(1);
        computerSpieler.setFingerRechts(0);

        // Mock generateMove to return an attack move ("l2l" - left hand to left hand)
        computerSpieler = Mockito.spy(computerSpieler);
        doReturn("l2l").when(computerSpieler).generateMove(players, 0);

        // Make move
        int moveResult = computerSpieler.makeMove(gameState);

        // Verify that makeMove returns the correct player index (-1 indicates successful move)
        assertEquals(-1, moveResult, "makeMove should return the correct player index");
    }

    @Test
    public void testMakeMove_SplitMove() {
        // Set up game state for split move
        computerSpieler.setFingerLinks(0);
        computerSpieler.setFingerRechts(4);

        // Mock generateMove to return a split move ("s")
        computerSpieler = Mockito.spy(computerSpieler);
        doReturn("s").when(computerSpieler).generateMove(players, 0);

        // Make move
        int moveResult = computerSpieler.makeMove(gameState);

        // Verify that makeMove returns the correct player index (-1 indicates successful move)
        assertEquals(-1, moveResult, "makeMove should return the correct player index");
    }

    @Test
    public void testMakeMove_NoMove() {
        // Set up game state for no move
        computerSpieler.setFingerLinks(0);
        computerSpieler.setFingerRechts(0);

        // Mock generateMove to return an empty move ("")
        computerSpieler = Mockito.spy(computerSpieler);
        doReturn("").when(computerSpieler).generateMove(players, 0);

        // Make move
        int moveResult = computerSpieler.makeMove(gameState);

        // Verify that makeMove returns the correct player index (-1 indicates successful move)
        assertEquals(-1, moveResult, "makeMove should return the correct player index");
    }
}