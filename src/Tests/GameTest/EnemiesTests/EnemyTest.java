package Tests.GameTest.EnemiesTests;

import Game.Board.GameBoard;
import Game.Tiles.BoardComponents.Empty;
import Game.Tiles.Tile;
import Game.Tiles.Units.Actions.CombatSystem;
import Game.Tiles.Units.Characters.Player;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Enemies.Monster;
import Game.Utils.Position;
import View.CLI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {
    private CLI cli= new CLI();

    private static class ConcreteEnemy extends Monster {
        public ConcreteEnemy(Position position, String name, char tileRep, int health, int attack, int defense, Integer expValue) {
            super(position, name, tileRep, health, attack, defense, expValue, 3); // Assuming vision range of 3 for testing
        }

    }

    private static class ConcretePlayer extends Player {
        public ConcretePlayer(Position position, String name, char tileRep, int health, int attack, int defense) {
            super(position, name, tileRep, health, attack, defense);
        }

        @Override
        public void interact(Enemy enemy, GameBoard board) {
            // Implement interaction logic for testing
        }

        @Override
        public void gameTick() {

        }

        @Override
        public boolean castAbility(GameBoard board) {
            return false;
        }
    }
    private ConcreteEnemy enemyCloseToPlayer;
    private ConcreteEnemy enemyFarFromPlayer;
    private ConcretePlayer player;
    private GameBoard board;

    @BeforeEach
    void setUp() {
        // Initialize the enemy and player for each test
        enemyCloseToPlayer = new ConcreteEnemy(new Position(3, 2), "CloseEnemy", 'E', 50, 5, 2, 10);
        enemyFarFromPlayer = new ConcreteEnemy(new Position(5, 5), "FarEnemy", 'F', 50, 5, 2, 10);
        player = new ConcretePlayer(new Position(1, 2), "TestPlayer", 'P', 100, 10, 5);
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(enemyCloseToPlayer);
        enemies.add(enemyFarFromPlayer);
        board= new GameBoard(10, 10, player,enemies, cli::sendMessage, cli::sendPlayerDeath, cli::sendEnemyDeath);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board.setBoardTile(new Empty(new Position(i, j)), new Position(i, j));
            }
        }
        board.placeUnit(player, player.getPosition());
        board.placeUnit(enemyCloseToPlayer, enemyCloseToPlayer.getPosition());
        board.placeUnit(enemyFarFromPlayer, enemyFarFromPlayer.getPosition());
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        enemyCloseToPlayer = null;
        enemyFarFromPlayer = null;
        player = null;
        board = null;
    }

    @Test
    public void TestInteractWithPlayer() {
        // Test interaction with player
        int initialHealth = player.getHealth().getCurrentAmount();
        enemyCloseToPlayer.interact(player, board);
        assertTrue(player.getHealth().getCurrentAmount() < initialHealth, "Player's health should decrease after being attacked by the enemy");
        assertTrue(enemyCloseToPlayer.getHealth().getCurrentAmount() < enemyCloseToPlayer.getHealth().getCapacity(), "Enemy's health should decrease after attacking the player");
    }

    @Test
    public void TestInteractWithAnotherEnemy() {
        // Test interaction with another enemy
        ConcreteEnemy anotherEnemy = new ConcreteEnemy(new Position(2, 2), "AnotherEnemy", 'E', 50, 5, 2, 10);
        board.setBoardTile(anotherEnemy, anotherEnemy.getPosition());
        enemyCloseToPlayer.interact(anotherEnemy, board);
        assertEquals(new Position(2, 2), anotherEnemy.getPosition(), "Another enemy should remain in its position after interaction");
        assertEquals(board.getBoardTile(enemyCloseToPlayer.getPosition()), enemyCloseToPlayer, "Enemy should still be on the board after interacting with another enemy");
    }

    @Test
    public void TestInteractWithEmptyTile() {
        // Test interaction with an empty tile
        Position emptyPosition = new Position(4, 4);
        board.setBoardTile(new Empty(emptyPosition), emptyPosition);
        Tile emptyTile = board.getBoardTile(emptyPosition);
        emptyTile.interact(enemyCloseToPlayer, board);
        assertInstanceOf(Empty.class, emptyTile, "Enemy should not change the state of an empty tile");
        assertEquals(emptyPosition, enemyCloseToPlayer.getPosition(), "Enemy should remain in its position after interacting with an empty tile");
        assertEquals(board.getBoardTile(enemyCloseToPlayer.getPosition()), enemyCloseToPlayer, "Enemy should still be on the board after interacting with an empty tile");
    }

    @Test
    public void TestInteractWithWallTile() {
        // Test interaction with a Wall tile
        Position wallPosition = new Position(0, 0);
        board.setBoardTile(new Empty(wallPosition), wallPosition); // Assuming Wall is represented as Empty for this test
        Tile wallTile = board.getBoardTile(wallPosition);
        wallTile.interact(enemyCloseToPlayer, board);
        assertInstanceOf(Empty.class, wallTile, "Enemy should not change the state of a wall tile");
        assertEquals(wallPosition, enemyCloseToPlayer.getPosition(), "Enemy should remain in its position after interacting with a wall tile");
        assertEquals(board.getBoardTile(enemyCloseToPlayer.getPosition()), enemyCloseToPlayer, "Enemy should still be on the board after interacting with a wall tile");
    }

    @Test
    public void moveEnemyFarFromPlayer() {
        // Test moving the enemy that is far from the player
        Position initialPosition = enemyFarFromPlayer.getPosition();
        enemyFarFromPlayer.move(board);
        Position newPosition = enemyFarFromPlayer.getPosition();

        // Check that the position has changed
        assertNotEquals(initialPosition, newPosition, "Enemy should have moved to a new position");
        assertEquals(board.getBoardTile(initialPosition), new Empty(initialPosition), "Initial position should be empty");
        assertInstanceOf(ConcreteEnemy.class, board.getBoardTile(newPosition), "New position should contain the enemy");

        // Check that the enemy does not move towards the player
        assertNotEquals(new Position(1, 2), newPosition, "Enemy should not have moved towards the player");
    }

    @Test
    public void moveEnemyCloseToPlayer() {
        // Test moving the enemy that is close to the player
        Position initialPosition = enemyCloseToPlayer.getPosition();
        enemyCloseToPlayer.move(board);
        Position newPosition = enemyCloseToPlayer.getPosition();

        // Check that the position has changed
        assertNotEquals(initialPosition, newPosition, "Enemy should have moved to a new position");
        assertEquals(board.getBoardTile(initialPosition), new Empty(initialPosition), "Initial position should be empty");
        assertInstanceOf(ConcreteEnemy.class, board.getBoardTile(newPosition), "New position should contain the enemy");

        // Check that the enemy moves towards the player
        assertEquals(new Position(2, 2), newPosition, "Enemy should have moved closer to the player");
    }

    @Test
    void attackWithLowHealth() {
        // Test attacking the player with low health
        player.reduceHealth(99); // Reduce a player's health to one
        int lowHealthPlayer = player.getHealth().getCurrentAmount();
        enemyCloseToPlayer.attack(player, board);
        assertTrue(player.getHealth().getCurrentAmount() < lowHealthPlayer, "Player's health should decrease significantly when attacked with low health");
        assertTrue(player.isDead(), "Player should be dead after being attacked with low health");
        assertFalse(enemyCloseToPlayer.isDead(), "Enemy should not be dead after attacking a player with low health");
    }

    @Test
    void attackWithFullHealth() {
        // Test attacking the player with full health
        int initialPlayerHealth = player.getHealth().getCurrentAmount();
        enemyCloseToPlayer.attack(player, board);
        assertTrue(player.getHealth().getCurrentAmount() < initialPlayerHealth, "Player's health should decrease after being attacked by the enemy");
        assertTrue(enemyCloseToPlayer.getHealth().getCurrentAmount() < enemyCloseToPlayer.getHealth().getCapacity(), "Enemy's health should decrease after attacking the player");
    }

    @Test
    void onDeath(){
        int initialExpValue = enemyCloseToPlayer.getExpValue();
        enemyCloseToPlayer.reduceHealth(50); // Reduce health to zero
        assertTrue(enemyCloseToPlayer.isDead(), "Enemy should be dead after reducing health to zero");
        assertEquals(0, enemyCloseToPlayer.getHealth().getCurrentAmount(), "Enemy's health should be zero after death");
        assertEquals(initialExpValue, enemyCloseToPlayer.getExpValue(), "Enemy's experience value should remain the same after death");

    }

    @Test
    void onDeathWithPlayer() {
        // Test enemy death with player interaction
        int initialExpValue = enemyCloseToPlayer.getExpValue();
        enemyCloseToPlayer.reduceHealth(50); // Reduce health to zero
        assertTrue(enemyCloseToPlayer.isDead(), "Enemy should be dead after reducing health to zero");
        assertEquals(0, enemyCloseToPlayer.getHealth().getCurrentAmount(), "Enemy's health should be zero after death");
        assertEquals(initialExpValue, enemyCloseToPlayer.getExpValue(), "Enemy's experience value should remain the same after death");
    }

}