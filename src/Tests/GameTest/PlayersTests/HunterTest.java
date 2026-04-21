package Tests.GameTest.PlayersTests;

import Game.Board.GameBoard;
import Game.Tiles.Units.Characters.Hunter;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;
import Game.Utils.Resource;
import View.CLI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HunterTest {

    private Hunter hunter;
    private GameBoard board;
    private Enemy enemyInRange;
    private Enemy enemyOutOfRange;
    private final Position hunterPosition = new Position(0, 0);
    private final Position enemyPositionInRange = new Position(2, 2);
    private final Position enemyPositionOutOfRange = new Position(10, 10);

    @BeforeEach
    void setUp() {
        CLI cli = new CLI();
        hunter = new Hunter(hunterPosition, "Hunter", 100, 20, 10, 5, 10);
        enemyInRange = new Enemy(enemyPositionInRange, "Goblin", 'G', 5, 15, 5, 10) {};
        enemyOutOfRange = new Enemy(enemyPositionOutOfRange, "Orc", 'O', 100, 20, 10, 20) {};
        List<Enemy> enemies = List.of(enemyInRange, enemyOutOfRange);
        board = new GameBoard(50, 50, hunter,enemies,cli::sendMessage, cli::sendPlayerDeath, cli::sendEnemyDeath  );
        board.placeUnit(hunter, hunterPosition);
        board.placeUnit(enemyInRange, enemyPositionInRange);
        board.placeUnit(enemyOutOfRange, enemyPositionOutOfRange);


    }

    @AfterEach
    void tearDown() {
        hunter = null;
        board = null;
        enemyInRange = null;
        enemyOutOfRange = null;
    }

    @Test
    void levelUp() {
        int initialLevel = hunter.getLevel();
        int initialArrows = hunter.getArrows().getCurrentAmount();
        int initialCurrHealth = hunter.getHealth().getCurrentAmount();
        int initialMaxHealth = hunter.getHealth().getCapacity();
        hunter.getHealth().reduceCurrentAmount(10);
        int initialAttack = hunter.getAttackPoints();
        int initialDefense = hunter.getDefensePoints();
        hunter.getArrows().reduceCurrentAmount(5);
        hunter.GainExperience(100); // Enough experience to level up
        assertTrue(hunter.getLevel() > initialLevel, "Hunter should level up");
        assertTrue(hunter.getHealth().getCurrentAmount() > initialCurrHealth, "Hunter's current health should restore after leveling up");
        assertTrue(hunter.getHealth().getCapacity() > initialMaxHealth, "Hunter's max health should increase after leveling up");
        assertTrue(hunter.getAttackPoints() > initialAttack, "Hunter's attack points should increase after leveling up");
        assertTrue(hunter.getDefensePoints() > initialDefense, "Hunter's defense points should increase after leveling up");
        assertTrue(hunter.getArrows().getCurrentAmount() > initialArrows, "Hunter's arrows should be restored after leveling up");
    }

    @Test
    void gameTick() {
        Resource arrows = hunter.getArrows();

        hunter.getArrows().reduceCurrentAmount(1);
        int initialArrows = arrows.getCurrentAmount();
        for (int i = 0; i <=10; i++) {
            hunter.gameTick();
        }

        assertTrue(arrows.getCurrentAmount() > initialArrows, "Hunter should gain arrows after 10 ticks");

    }

    @Test
    void castAbility() {
        assertTrue(hunter.castAbility(board), "Hunter should successfully cast ability on enemy in range");
        assertTrue(enemyInRange.isDead(), "Enemy in range should be dead after ability cast");
        assertFalse(hunter.castAbility(board), "Hunter should fail to cast ability when no enemies are in range");

    }
}