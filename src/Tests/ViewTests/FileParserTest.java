package Tests.ViewTests;

import Game.CallBacks.*;
import View.parser.Level;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;
import View.Input.CommandLineInput;
import View.parser.FileParser;
import View.parser.TileFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FileParserTest {

    private FileParser fileParser;
    private File levelFile;
    private final int playerID = 0;

    @BeforeEach
    public void setUp() {
        TileFactory tileFactory = new TileFactory();
        MessageCallBack messageCallBack = System.out::println;
        PlayerDeathCallback playerDeathCallback = name -> System.out.println(name + " died");
        EnemyDeathCallback enemyDeathCallback = name -> System.out.println(name + " died");
        CommandLineInput inputProvider = new CommandLineInput();

        fileParser = new FileParser(tileFactory, messageCallBack, playerDeathCallback, enemyDeathCallback, inputProvider, playerID);
        levelFile = new File("C:\\Users\\yagel\\IdeaProjects\\semester_B\\hw3\\levels/level1.txt");
    }

    @Test
    public void testLevelLoadsCorrectly() {
        assertTrue(levelFile.exists(), "Level file does not exist");

        Level level = fileParser.parseFile(levelFile);

        assertNotNull(level, "Level failed to load");
        assertNotNull(level.getPlayer(), "Player not initialized");
        assertNotNull(level.getGameBoard(), "Game board not initialized");
        assertTrue(level.getEnemies().size() > 0, "No enemies loaded");
    }

    @Test
    public void testPlayerSpawnPosition() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(levelFile));
        String line;
        int y = 0;
        Position expectedPosition = null;

        while ((line = reader.readLine()) != null) {
            int x = line.indexOf('@');
            if (x != -1) {
                expectedPosition = new Position(x, y);
                break;
            }
            y++;
        }
        reader.close();

        assertNotNull(expectedPosition, "No '@' character found in level file");

        Level level = fileParser.parseFile(levelFile);
        Position actualPosition = level.getPlayer().getPosition();

        assertEquals(expectedPosition, actualPosition, "Player not placed at correct position");
    }

    @Test
    public void testEnemiesSpawnPositions() throws Exception {
        Set<Character> nonEnemyTiles = Set.of('.', '#', '@');
        List<Position> expectedEnemyPositions = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(levelFile));
        String line;
        int y = 0;

        while ((line = reader.readLine()) != null) {
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (!nonEnemyTiles.contains(c)) {
                    expectedEnemyPositions.add(new Position(x, y));
                }
            }
            y++;
        }
        reader.close();

        Level level = fileParser.parseFile(levelFile);
        List<Position> actualEnemyPositions = level.getEnemies().stream()
                .map(Enemy::getPosition)
                .toList();

        assertEquals(expectedEnemyPositions.size(), actualEnemyPositions.size(), "Enemy count mismatch");

        for (Position expectedPos : expectedEnemyPositions) {
            assertTrue(actualEnemyPositions.contains(expectedPos),
                    "Expected enemy at position " + expectedPos + " not found");
        }
    }
}
