package Tests.GameTest.PlayersTests;

import Game.Board.GameBoard;
import Game.Tiles.Units.Characters.Warrior;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;
import View.CLI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WarriorTest {
    private Warrior warrior;
    private GameBoard board;
    private Enemy enemyInRange;
    private Enemy enemyOutOfRange;
    private final Position warriorPosition = new Position(0, 0);
    private final Position enemyPositionInRange = new Position(1, 1);
    private final Position enemyPositionOutOfRange = new Position(10, 10);

    @BeforeEach
    void setUp() {
        CLI cli = new CLI();
        warrior = new Warrior(warriorPosition, "Jon Snow", 300, 30, 4, 3);
        enemyInRange = new Enemy(enemyPositionInRange, "Goblin", 'G', 5, 15, 5, 10) {};
        enemyOutOfRange = new Enemy(enemyPositionOutOfRange, "Orc", 'O', 100, 20, 10, 20) {};
        List<Enemy> enemies = List.of(enemyInRange, enemyOutOfRange);
        board = new GameBoard(50, 50, warrior,enemies,cli::sendMessage, cli::sendPlayerDeath, cli::sendEnemyDeath  );
        board.placeUnit(warrior, warriorPosition);
        board.placeUnit(enemyInRange, enemyPositionInRange);
        board.placeUnit(enemyOutOfRange, enemyPositionOutOfRange);


    }

    @AfterEach
    void tearDown() {
        warrior= null;
        board = null;
        enemyInRange = null;
        enemyOutOfRange = null;
    }

    @Test
    void levelUp() {
        int initialLevel = warrior.getLevel();
        int initialAttack = warrior.getAttackPoints();
        int initialDefense = warrior.getDefensePoints();
        int initialCurrHealth = warrior.getHealth().getCurrentAmount();
        int initialMaxHealth = warrior.getHealth().getCapacity();
        warrior.castAbility(board);
        warrior.getHealth().reduceCurrentAmount(10);
        warrior.GainExperience(100); // Enough experience to level up
        assertTrue(warrior.getLevel() > initialLevel, "Warrior should level up");
        assertTrue(warrior.getAttackPoints() > initialAttack, "Warrior should gain attack points on level up");
        assertTrue(warrior.getDefensePoints() > initialDefense, "Warrior should gain defense points on level up");
        assertTrue(warrior.getHealth().getCurrentAmount() > initialCurrHealth, "Warrior should gain health on level up");
        assertTrue(warrior.getHealth().getCapacity() > initialMaxHealth, "Warrior's max health should increase on level up");
        assertEquals(0, warrior.getCooldDownRemaining(), "Cooldown should reset after leveling up");
    }

    @Test
    void gameTick() {
        warrior.castAbility(board);
        int initialCooldown = warrior.getCooldDownRemaining();
        warrior.gameTick();
        assertEquals(initialCooldown - 1, warrior.getCooldDownRemaining(), "Cooldown should decrease by 1 after game tick");

    }

    @Test
    void castAbility() {
        warrior.getHealth().reduceCurrentAmount(1);
        int initialHealth = warrior.getHealth().getCurrentAmount();
        int initialCooldown = warrior.getCooldDownRemaining();
        warrior.castAbility(board);
        assertTrue(warrior.getHealth().getCurrentAmount() > initialHealth, "Warrior's health should increase after casting ability");
        assertTrue(initialCooldown < warrior.getCooldDownRemaining(), "Cooldown should increase after casting ability");
        assertTrue(enemyInRange.getHealth().getCurrentAmount() < enemyInRange.getHealth().getCapacity()||enemyInRange.isDead(), "Enemy in range should take damage after ability cast");
        assertTrue(enemyOutOfRange.getHealth().getCurrentAmount() == enemyOutOfRange.getHealth().getCapacity(), "Enemy out of range should not take damage after ability cast");
    }
}