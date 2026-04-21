package Tests.GameTest.PlayersTests;

import Game.Board.GameBoard;
import Game.Tiles.Units.Characters.Mage;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;
import View.CLI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MageTest {
    private Mage mage;
    private GameBoard board;
    private Enemy enemyInRange;
    private Enemy enemyOutOfRange;
    private final Position magePosition = new Position(0, 0);
    private final Position enemyPositionInRange = new Position(2, 2);
    private final Position enemyPositionOutOfRange = new Position(10, 10);


    @BeforeEach
    void setUp() {
        CLI cli = new CLI();
        mage = new Mage(magePosition, "Melisandre", 100, 5, 1, 300, 30, 15, 5, 6);
        enemyInRange = new Enemy(enemyPositionInRange, "Goblin", 'G', 5, 15, 5, 10) {};
        enemyOutOfRange = new Enemy(enemyPositionOutOfRange, "Orc", 'O', 100, 20, 10, 20) {};
        List<Enemy> enemies = List.of(enemyInRange, enemyOutOfRange);
        board = new GameBoard(50, 50, mage,enemies,cli::sendMessage, cli::sendPlayerDeath, cli::sendEnemyDeath  );
        board.placeUnit(mage, magePosition);
        board.placeUnit(enemyInRange, enemyPositionInRange);
        board.placeUnit(enemyOutOfRange, enemyPositionOutOfRange);

    }

    @AfterEach
    void tearDown() {
        mage = null;
        board = null;
        enemyInRange = null;
        enemyOutOfRange = null;
    }

    @Test
    void castAbility() {
        int initialMana = mage.getMana().getCurrentAmount();
        int initialSpellPower = mage.getSpellPower();
        mage.castAbility(board);
        assertTrue(enemyInRange.getHealth().getCurrentAmount() < enemyInRange.getHealth().getCapacity(), "Enemy should take damage after ability cast");
        assertTrue(mage.getMana().getCurrentAmount() < initialMana, "Mage's mana should decrease after casting ability");
        //assertTrue(mage.getSpellPower() > initialSpellPower, "Mage's spell power should increase after casting ability");
    }

    @Test
    void levelUp() {
        int initialLevel = mage.getLevel();
        int initialCurrMana = mage.getMana().getCurrentAmount();
        int initialMaxMana = mage.getMana().getCapacity();
        int initialSpellPower = mage.getSpellPower();
        //regular level up
        int initialAttack = mage.getAttackPoints();
        int initialDefense = mage.getDefensePoints();
        int inittialCurrHealth = mage.getHealth().getCurrentAmount();
        int initialMaxHealth = mage.getHealth().getCapacity();
        mage.getHealth().reduceCurrentAmount(10);
        mage.getMana().reduceCurrentAmount(5);
        mage.GainExperience(100); // Enough experience to level up
        assertTrue(mage.getLevel() > initialLevel, "Mage should level up");
        assertTrue(mage.getAttackPoints() > initialAttack, "Mage's attack points should increase after leveling up");
        assertTrue(mage.getDefensePoints() > initialDefense, "Mage's defense points should increase after leveling up");
        assertTrue(mage.getHealth().getCurrentAmount() > inittialCurrHealth, "Mage's current health should restore after leveling up");
        assertTrue(mage.getHealth().getCapacity() > initialMaxHealth, "Mage's max health should increase after leveling up");
        assertTrue(mage.getMana().getCurrentAmount() > initialCurrMana, "Mage's current mana should increase after leveling up");
        assertTrue(mage.getMana().getCapacity() > initialMaxMana, "Mage's max mana should increase after leveling up");
        assertTrue(mage.getSpellPower() > initialSpellPower, "Mage's spell power should increase after leveling up");
    }

    @Test
    void gameTick() {
        int initialMana = mage.getMana().getCurrentAmount();
        //Resource mana = mage.getMana();
        mage.getMana().reduceCurrentAmount(1);
        mage.gameTick();
        assertEquals(mage.getMana().getCurrentAmount() , initialMana, "Mage's mana should regenerate by 1 after game tick");
    }
}