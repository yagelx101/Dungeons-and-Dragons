package Tests.GameTest.EnemiesTests;

import Game.Board.GameBoard;
import Game.Tiles.BoardComponents.Empty;
import Game.Tiles.Units.Characters.Warrior;
import Game.Tiles.Units.Enemies.Boss;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;
import View.CLI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BossTest {
    private Warrior warrior;
    private GameBoard board;
private Boss boss;
    private final Position warriorPosition = new Position(0, 0);
    private final Position bossPosition = new Position(10, 10);



    @BeforeEach
    void setUp() {
        CLI cli = new CLI();
        warrior = new Warrior(warriorPosition, "Jon Snow", 300, 30, 0, 3);
        boss = new Boss(bossPosition,  "Night’s King", 'K', 5000, 300, 150, 5000, 50, 2) ;
        List<Enemy> enemies = List.of(boss);
        board = new GameBoard(50, 50, warrior,enemies,cli::sendMessage, cli::sendPlayerDeath, cli::sendEnemyDeath  );
        board.placeUnit(warrior, warriorPosition);
        board.placeUnit(boss, bossPosition);
        for (int i=0;i<50;i++)
            for (int j=0;j<50;j++)
                board.setBoardTile(new Empty(new Position(i,j)), new Position(i,j));
    }

    @AfterEach
    void tearDown() {
        warrior = null;
        board = null;
        boss = null;
    }

    @Test
    void move() {
        boss.move(board);
        assertNotEquals(bossPosition, boss.getPosition(), "Boss should move to a new position");
        for(int i=0;i<4;i++)
            boss.move(board);
        System.out.println(warrior.getHealth().getCurrentAmount()+ " " + warrior.getHealth().getCapacity());
        assertTrue(warrior.getHealth().getCurrentAmount() < warrior.getHealth().getCapacity()||warrior.isDead(), "Warrior should take damage after boss moves");
    }

}