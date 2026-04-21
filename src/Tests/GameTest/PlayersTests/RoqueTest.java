package Tests.GameTest.PlayersTests;

import Game.Board.GameBoard;
import Game.Tiles.Units.Characters.Roque;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;
import View.CLI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoqueTest {
    private Roque roque;
    private GameBoard board;
    private Enemy enemyInRange;
    private Enemy enemyOutOfRange;
    private final Position roquePosition = new Position(0, 0);
    private final Position enemyPositionInRange = new Position(1, 1);
    private final Position enemyPositionOutOfRange = new Position(10, 10);


    @BeforeEach
    void setUp() {
        CLI cli = new CLI();
        roque = new Roque(roquePosition, "Arya Stark", 150, 40, 2, 20);
        enemyInRange = new Enemy(enemyPositionInRange, "Goblin", 'G', 5, 15, 5, 10) {};
        enemyOutOfRange = new Enemy(enemyPositionOutOfRange, "Orc", 'O', 100, 20, 10, 20) {};
        List<Enemy> enemies = List.of(enemyInRange, enemyOutOfRange);
        board = new GameBoard(50, 50, roque,enemies,cli::sendMessage, cli::sendPlayerDeath, cli::sendEnemyDeath  );
        board.placeUnit(roque, roquePosition);
        board.placeUnit(enemyInRange, enemyPositionInRange);
        board.placeUnit(enemyOutOfRange, enemyPositionOutOfRange);


    }

    @AfterEach
    void tearDown() {
        roque = null;
        board = null;
        enemyInRange = null;
        enemyOutOfRange = null;
    }

    @Test
    void levelUp() {
        int initialLevel = roque.getLevel();
        int initialAttack = roque.getAttackPoints();
        int initialDefense = roque.getDefensePoints();
        int initialCurrHealth = roque.getHealth().getCurrentAmount();
        int initialMaxHealth = roque.getHealth().getCapacity();
        roque.getHealth().reduceCurrentAmount(10);

        roque.getEnergy().reduceCurrentAmount(10);
        int initialEnergy = roque.getEnergy().getCurrentAmount();
        roque.GainExperience(100); // Enough experience to level up
        assertTrue(roque.getLevel() > initialLevel, "Roque should level up");
        assertTrue(roque.getAttackPoints() > initialAttack, "Roque's attack points should increase after leveling up");
        assertTrue(roque.getDefensePoints() > initialDefense, "Roque's defense points should increase after leveling up");
        assertTrue(roque.getHealth().getCurrentAmount() > initialCurrHealth, "Roque's current health should restore after leveling up");
        assertTrue(roque.getHealth().getCapacity() > initialMaxHealth, "Roque's max health should increase after leveling up");
        assertTrue(roque.getEnergy().getCurrentAmount() > initialEnergy, "Roque's energy should restore after leveling up");
    }

    @Test
    void gameTick() {
        roque.getEnergy().reduceCurrentAmount(10);
        int initialEnergy = roque.getEnergy().getCurrentAmount();
        roque.gameTick();
        assertTrue(roque.getEnergy().getCurrentAmount() > initialEnergy, "Roque's energy should increase after game tick");
    }

    @Test
    void castAbility() {
        int initialEnergy = roque.getEnergy().getCurrentAmount();
        roque.castAbility(board);
        assertTrue(initialEnergy>roque.getEnergy().getCurrentAmount(), "Roque's energy should decrease after casting ability");
        assertTrue(enemyInRange.getHealth().getCurrentAmount() < enemyInRange.getHealth().getCapacity()||enemyInRange.isDead(), "Enemy in range should take damage from Roque's ability");
        assertTrue(enemyOutOfRange.getHealth().getCurrentAmount() == enemyOutOfRange.getHealth().getCapacity(), "Enemy out of range should not take damage from Roque's ability");
    }
}