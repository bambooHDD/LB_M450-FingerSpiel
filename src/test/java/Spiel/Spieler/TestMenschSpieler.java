package Spiel.Spieler;

import Spiel.Game.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMenschSpieler {

    private MenschSpieler menschSpieler;
    private GameState gameState;
    private List<ISpieler> players;

    @BeforeEach
    void setUp() {
        menschSpieler = new MenschSpieler("TestSpieler");
        gameState = Mockito.mock(GameState.class);
        players = new ArrayList<>();
        players.add(menschSpieler);
        Mockito.when(gameState.getPlayers()).thenReturn(players);
    }

    @Test
    void makeMove_returnsIndexOfPlayer() {
        // Verify that makeMove returns the index of the player in the GameState's player list.
        int expectedIndex = 0;
        int actualIndex = menschSpieler.makeMove(gameState);
        assertEquals(expectedIndex, actualIndex, "makeMove should return the index of the player in the GameState's player list.");
    }

    @Test
    void constructor_setsNameCorrectly() {
        // Verify that the constructor sets the name correctly.
        assertEquals("TestSpieler", menschSpieler.getName(), "The constructor should set the name correctly.");
    }
}