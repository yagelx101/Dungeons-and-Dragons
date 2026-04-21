package View.parser;

import Game.Board.GameBoard;
import Game.CallBacks.EnemyDeathCallback;
import Game.CallBacks.MessageCallBack;
import Game.CallBacks.PlayerDeathCallback;
import Game.Tiles.Units.Characters.Player;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;
import View.Input.CommandLineInput;
import View.Input.InputProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

    private final TileFactory tileFactory;
    private final MessageCallBack messageCallback;
    private final PlayerDeathCallback playerDeathCallback;
    private final EnemyDeathCallback enemyDeathCallback;
    private final InputProvider inputProvider;
    private final int playerID;

    public FileParser(TileFactory tileFactory, MessageCallBack messageCallback, PlayerDeathCallback playerDeathCallback,
                      EnemyDeathCallback enemyDeathCallback, InputProvider inputProvider, int playerID) {
        this.tileFactory = tileFactory;
        this.messageCallback = messageCallback;
        this.playerDeathCallback = playerDeathCallback;
        this.enemyDeathCallback = enemyDeathCallback;
        this.inputProvider = inputProvider;
        this.playerID = playerID;
    }

    public Level parseFile(File file) {
        return parseFile(file.getAbsolutePath());
    }

    public Level parseFile(String filePath) {
        List<String> lines = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read level file: " + e.getMessage());
        }

        int rows = lines.size();
        int cols = lines.getFirst().length();
        Player player = null;
        Position playerStaringPos = null;

        GameBoard gameBoard = new GameBoard(rows, cols, player, enemies, messageCallback, playerDeathCallback, enemyDeathCallback);

        for (int y = 0; y < rows; y++) {
            String line = lines.get(y);
            for (int x = 0; x < cols; x++) {
                char c = line.charAt(x);
                Position pos = new Position(x, y);
                if (c == '.') {
                    gameBoard.setBoardTile(tileFactory.produceEmpty(pos), pos);
                } else if (c == '#') {
                    gameBoard.setBoardTile(tileFactory.produceWall(pos, messageCallback), pos);
                } else if (c == '@') {
                    player = tileFactory.producePlayer(playerID, pos);
                    playerStaringPos = pos;
                } else {
                    Enemy newEnemy = tileFactory.produceEnemy(c, pos);
                    gameBoard.setBoardTile(newEnemy, pos);
                    gameBoard.placeUnit(newEnemy, pos);
                    enemies.add(newEnemy);
                }
            }
        }

        if (player == null) {
            throw new RuntimeException("Level file must contain a player '@'");
        }

        return new Level(gameBoard, player, playerStaringPos, enemies, inputProvider);
    }
}
