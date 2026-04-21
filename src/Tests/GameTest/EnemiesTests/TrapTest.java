package Tests.GameTest.EnemiesTests;

import Game.Board.GameBoard;
import Game.Tiles.BoardComponents.Empty;
import Game.Tiles.Units.Characters.Warrior;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Enemies.Trap;
import Game.Utils.Position;
import View.CLI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrapTest {
    private GameBoard board;
    private Warrior warrior;
    private Trap trap;
    private final Position warriorPosition = new Position(0, 0);
    private final Position trapPosition = new Position(5, 5);


    @BeforeEach
    void setUp() {
        CLI cli = new CLI();
        warrior = new Warrior(warriorPosition, "Jon Snow", 300, 30, 0, 3);
        trap = new Trap(trapPosition, "Queen’s Trap", 'Q', 250, 50, 10, 100, 1, 5);
        List<Enemy> enemies = List.of(trap);
        board = new GameBoard(10, 10, warrior,enemies,cli::sendMessage, cli::sendPlayerDeath, cli::sendEnemyDeath  );
        board.placeUnit(warrior, warriorPosition);
        board.placeUnit(trap, trapPosition);
        for (int i=0;i<10;i++)
            for (int j=0;j<10;j++)
                board.setBoardTile(new Empty(new Position(i,j)), new Position(i,j));
    }

    @AfterEach
    void tearDown() {
        warrior = null;
        board = null;
        trap = null;
    }

    @Test
    void move() {
        // Ensure the trap can't move
        trap.move(board);
        assertEquals(trapPosition, trap.getPosition(), "Trap should not move from its initial position");

        // Simulate a game tick to check if trap became invisible
        trap.move(board);
        assertEquals(".", trap.toString(), "Trap should become invisible after a game tick");

        // Simulate a game tick to check if trap reappears
        for (int i = 0; i < 6; i++) {
            trap.move(board);
        }
        assertEquals("Q", trap.toString(), "Trap should become invisible after a game tick");

    }

    @Test
    void attackPlayerInRange() {
        // Move the warrior to the trap's position
        warrior.setPosition(new Position(4,4));
        trap.move(board);
        assertTrue(warrior.getHealth().getCurrentAmount() < warrior.getHealth().getCapacity(), "Warrior should take damage from the trap");
    }

    @Test
    void attackPlayerOutOfRange() {
        // Move the warrior far from the trap
        warrior.setPosition(new Position(0,0));
        trap.move(board);
        assertEquals(warrior.getHealth().getCapacity(), warrior.getHealth().getCurrentAmount(), "Warrior should not take damage from the trap when out of range");
    }
}