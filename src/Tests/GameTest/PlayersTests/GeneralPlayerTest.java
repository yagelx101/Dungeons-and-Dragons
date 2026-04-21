package Tests.GameTest.PlayersTests;

import Game.Board.GameBoard;
import Game.Tiles.Units.Characters.Player;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Enemies.Monster;
import Game.Utils.Position;
import View.CLI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeneralPlayerTest {
    private Player player;
    private Monster enemy;
    private GameBoard board;
    private final Position playerPosition = new Position(0, 0);
    private final Position enemyPosition = new Position(1, 0);


    @BeforeEach
    void setUp() {

        CLI cli = new CLI();

        player = new ConcretePlayer(playerPosition, "Hero", '@', 100, 20, 10);

        enemy = new Monster(enemyPosition, "Goblin", 'G', 50, 15, 5, 10,1);
        List<Enemy> enemies = List.of(enemy);
        board = new GameBoard(5, 5, player,enemies,cli::sendMessage, cli::sendPlayerDeath, cli::sendEnemyDeath  );
        board.placeUnit(player, playerPosition);
        board.placeUnit(enemy, enemyPosition);

    }

    @AfterEach
    void tearDown() {
        player = null;
        enemy = null;
        board = null;
    }

    @Test
    void interact() {
        player.interact(enemy, board);
        assertTrue(enemy.getHealth().getCurrentAmount() < enemy.getHealth().getCapacity(), "Enemy should take damage after interaction");

    }

    @Test
    void attack() {
        player.attack(enemy, board);
        assertTrue(enemy.isDead() || player.isDead(), "Either the player or the enemy should die after combat");

    }

    @Test
    void onKill() {
        player.onKill(enemy, board);
        assertEquals(player.getPosition(), enemy.getPosition(), "Player should move to the enemy's position after killing");
        assertTrue(player.getExperience() > 0, "Player should gain experience after killing an enemy");
        assertEquals(board.getBoardTile(enemyPosition).toString(), "@", "The player should have moved to the enemy tile");
        assertEquals(board.getBoardTile(playerPosition).toString(), ".", "Player tile should remain the same after killing an enemy");

    }

    @Test
    void onDefense() {
        player.onDefense(enemy);
        assertTrue(player.getExperience() > 0, "Player should gain experience after defending");
        assertEquals(player.getPosition(),playerPosition, "Player should remain at the same position after defending");
        assertEquals(board.getBoardTile(playerPosition).toString(), "@", "Player tile should remain the same after defending");
    }


    @Test
    void onDeath() {
        //player.getHealth().reduce(player.getHealth().getCurrent()); // Kill the player
        player.onDeath(board);
        assertEquals("X", board.getBoardTile(playerPosition).toString(), "Player should be marked as dead");

    }

    @Test
    void gainExperience() {
        int initialLevel = player.getLevel();
        player.GainExperience(100);
        assertTrue(player.getLevel() > 0, "Player should level up after gaining enough experience");

    }

    private static class ConcretePlayer extends Player {
        public ConcretePlayer(Position position, String name, Character tileRep, Integer healthPool, Integer attackPoints, Integer defensePoints) {
            super(position, name, tileRep, healthPool, attackPoints, defensePoints);
        }

        @Override
        public void gameTick() {
            // No-op for testing
        }

        @Override
        public boolean castAbility(GameBoard board) {
            return false; // No-op for testing
            // No-op for testing
        }
    }
}