package View.parser;

import GUI.GameWindow;
import Game.Board.GameBoard;
import Game.CallBacks.MessageCallBack;
import Game.Tiles.Units.Actions.Action;
import Game.Tiles.Units.Characters.Player;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;
import View.Input.GUIInput;
import View.Input.InputProvider;
import View.Input.KeyboardListener;

import java.util.List;

public class Level implements LevelInitializer {
    private GameBoard gameBoard = null;
    private Player player = null;
    private final Position playerStartingPos;
    private final InputProvider inputProvider;
    private final List<Enemy> enemies;

    private GameWindow gameWindow;
    private KeyboardListener listener = new KeyboardListener(action -> {
        // פה אתה מגדיר מה לעשות כשיש פעולה
        action.execute(player, gameBoard);
        playEnemiesTurn();
        notifyObservers();
    });

    public Level(GameBoard board, Player player, Position playerStaringPos, List<Enemy> enemies, InputProvider inputProvider) {
        this.gameBoard = board;
        this.player = player;
        gameBoard.setPlayer(player);
        this.playerStartingPos = playerStaringPos;
        gameBoard.placeUnit(player, playerStartingPos);
        this.enemies = enemies;
        this.inputProvider = inputProvider;
        this.gameWindow = new GameWindow();

        KeyboardListener listener = new KeyboardListener(this::playOneTurn);
        gameWindow.getPanel().addKeyListener(listener);
        gameWindow.getPanel().setFocusable(true);
        gameWindow.getPanel().requestFocusInWindow();
    }

    // מבצעים תור שלם
    private void playOneTurn(Action action) {
        // תור השחקן
        action.execute(player, gameBoard);

        // תור האויבים
        playEnemiesTurn();

        // עדכון התצוגה
        gameWindow.updateBoard(gameBoard.getBoard());

        // אם יש observers במשחק
        notifyObservers();
    }

    public void initializeLevel() {
        player.setPosition(playerStartingPos);
        gameBoard.placeUnit(player, player.getPosition());
        gameWindow = new GameWindow(gameBoard.getBoard());
    }

    // הוספה ל-panel או ל-frame שלך
//    panel.addKeyListener(listener);
//    panel.setFocusable(true);
//    panel.requestFocusInWindow();

    private void playEnemiesTurn() {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive())
                enemy.move(gameBoard); //enemy move
            if (enemy.isDead() && !enemies.isEmpty())
                gameWindow.updateBoard(gameBoard.getBoard());
            System.out.println(gameBoard.toString());
        }
        enemies.removeIf(Enemy::isDead);
    }

    private void notifyObservers() {
        player.gameTick();
        System.out.println(player.describe());
        gameWindow.updateBoard(gameBoard.getBoard());
    }

    public void playOneTurn() {
//        try {
//            Action action = inputProvider.inputQuery().apply(player);
//            action.execute(player, gameBoard); //player move
//            for (Enemy enemy : enemies) {
//                if (enemy.isAlive())
//                    enemy.move(gameBoard); //enemy move
//                if (enemy.isDead() && !enemies.isEmpty())
//                    gameWindow.updateBoard(gameBoard.getBoard());
//                System.out.println(gameBoard.toString());
//            }
//            enemies.removeIf(Enemy::isDead);
//
//        } catch (Exception e) {
//            System.out.println("Error: invalid input, try again");
//        }
//        player.gameTick();
//        System.out.println(player.describe());
//        gameWindow.updateBoard(gameBoard.getBoard());

        //System.out.println(gameBoard.toString());
    }

    public boolean levelFinished() {
        return enemies.isEmpty();
    }

    public boolean gameOver() {
        return player.isDead();
    }

    public Player getPlayer() {
        return player;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    @Override
    public String toString() {
        return gameBoard.toString();
    }
}
