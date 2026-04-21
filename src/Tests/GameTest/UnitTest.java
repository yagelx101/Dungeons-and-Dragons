package Tests.GameTest;

import Game.Board.GameBoard;
import Game.CallBacks.*;
import Game.Tiles.Units.Characters.Player;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Unit;
import Game.Utils.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {

    private static class TestUnit extends Unit {
        public boolean died;
        public boolean attacked;

        public TestUnit(Position position) {
            super(position, "TestUnit", 'T', 100, 10, 5);
            died = false;
            attacked = false;
        }

        @Override
        public void onDeath(GameBoard board) {
            died = true;
        }

        @Override
        public void interact(Player player, GameBoard board) {
        }

        @Override
       public void interact(Enemy enemy, GameBoard board) {
            attacked = true;
        }
    }

    private static class MockEnemyForTest extends Enemy {
        public boolean wasAttacked = false;

        public MockEnemyForTest(Position position) {
            super(position, "MockEnemy", 'E', 50, 5, 2, 10);
        }

        @Override
        public void interact(Player player, GameBoard board) {
        }

        @Override
        public void interact(Enemy enemy, GameBoard board) {
        }

    }

    public static class MockPlayerForTest extends Player {
        public boolean wasAttacked;

        public MockPlayerForTest(Position position) {
            super(position, "MockPlayer", '@', 100, 10, 5);
            wasAttacked = false;
        }

        @Override
        public void interact(Player player, GameBoard board) {
        }

        @Override
        public void interact(Enemy enemy, GameBoard board) {
            wasAttacked = true;
        }

        @Override
        public void gameTick() {

        }

        @Override
        public boolean castAbility(GameBoard board) {
            return false;
        }
    }

    private TestUnit unit;

    @BeforeEach
    public void setUp() {
        unit = new TestUnit(new Position(0, 0));
        unit.initialize((f, t) -> {}, msg -> {}, name -> {}, name -> {});
    }

    @Test
    public void testMoveAndCallback() {
        final boolean[] called = {false};
        final Position[] from = new Position[1];
        final Position[] to = new Position[1];

        unit.initialize((f, t) -> {
            called[0] = true;
            from[0] = f;
            to[0] = t;
        }, msg -> {}, name -> {}, name -> {});

        Position newPos = new Position(2, 3);
        unit.move(newPos);

        assertTrue(called[0], "Callback not triggered");
        assertEquals(new Position(0, 0), from[0]);
        assertEquals(newPos, to[0]);
        assertEquals(newPos, unit.getPosition(), "Position not updated");
    }

    @Test
    public void testReduceHealthAndDeath() {
        assertFalse(unit.isDead(), "Unit should be alive initially");

        unit.reduceHealth(30);
        assertFalse(unit.isDead(), "Unit should still be alive");

        unit.reduceHealth(100);
        assertTrue(unit.isDead(), "Unit should be dead");
    }

    @Test
    public void testGetTile() {
        assertEquals('T', unit.getTile());
    }

    @Test
    public void testInitialStats() {
        assertEquals("TestUnit", unit.getName());
        assertEquals(100, unit.getHealth().getCapacity());
        assertEquals(10, unit.getAttackPoints());
        assertEquals(5, unit.getDefensePoints());
    }

    @Test
    public void testReduceHealthZero() {
        int current = unit.getHealth().getCurrentAmount();
        unit.reduceHealth(0);
        assertEquals(current, unit.getHealth().getCurrentAmount(), "HP should remain the same after 0 damage");
    }

    @Test
    public void testReduceHealthAfterDeath() {
        unit.reduceHealth(100); // מת
        assertTrue(unit.isDead());

        int before = unit.getHealth().getCurrentAmount();
        unit.reduceHealth(50); // נסה לפגוע שוב
        assertEquals(before, unit.getHealth().getCurrentAmount(), "HP should not change after death");
    }

    @Test
    public void testInteractWithEnemy() {
        TestUnit player = new TestUnit(new Position(1, 1));
        player.initialize((f, t) -> {}, msg -> {}, name -> {}, name -> {});

        MockEnemyForTest enemy = new MockEnemyForTest(new Position(1, 2));
        enemy.initialize((f, t) -> {}, msg -> {}, name -> {}, name -> {});

        player.interact(enemy, null);

        assertTrue(player.attacked, "Enemy's attack method should have been called");
    }

    @Test
    public void testInteractWithPlayerDoesNothing() {
        Player other = new MockPlayerForTest(new Position(1, 1));
        other.initialize((f, t) -> {}, msg -> {}, name -> {}, name -> {});

        unit.interact(other, null);
        assertEquals(new Position(0, 0), unit.getPosition(), "Position should remain unchanged");
        assertEquals(new Position(1, 1), other.getPosition(), "Other unit should not be affected");
    }
}
