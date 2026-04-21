package Game.Board;

import Game.CallBacks.EnemyDeathCallback;
import Game.CallBacks.MessageCallBack;
import Game.CallBacks.PlayerDeathCallback;
import Game.CallBacks.PositionChangedCallback;
import Game.Tiles.Tile;
import Game.Tiles.Units.Characters.Player;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Unit;
import Game.Utils.Position;
import View.parser.TileFactory;

import java.util.List;

public class GameBoard {
    private final Tile[][] board;
    private final TileFactory tileFactory;
    private final MessageCallBack messageCallBack;
    private final PlayerDeathCallback playerDeathCallback;
    private final EnemyDeathCallback enemyDeathCallback;
    private Player player;
    private final List<Enemy> enemies;

    public GameBoard(int rows, int cols, Player player, List<Enemy> enemyList, MessageCallBack msg, PlayerDeathCallback pdc, EnemyDeathCallback edc) {
        this.tileFactory = new TileFactory();
        this.board = new Tile[rows][cols];
        this.messageCallBack = msg;
        this.playerDeathCallback = pdc;
        this.enemyDeathCallback = edc;
        this.player = player;
        this.enemies = enemyList;

    }

    public void placeUnit(Unit unit, Position pos) {
        if (!inBounds(pos)) {
            throw new IllegalArgumentException("Invalid position: " + pos);
        }
        unit.initialize(generateCallback(), messageCallBack, playerDeathCallback, enemyDeathCallback);
        setBoardTile(unit, pos);
    }

    private PositionChangedCallback generateCallback() {
        return (from, to) -> {
            Tile tile = getBoardTile(from);
            setBoardTile(empty(from), from);
            setBoardTile(tile, to);
            //messageCallBack.send(tile.toString() + " moved from " + from + " to " + to);
        };
    }

    public Tile getBoardTile(Position position) {
        return board[position.getY()][position.getX()];
    }

    public void setBoardTile(Tile tile, Position position) {
        board[position.getY()][position.getX()] = tile;
    }

    private Tile empty(Position position) {
        return tileFactory.produceEmpty(position);
    }

    private boolean inBounds(Position pos) {
        return pos.getY() >= 0 && pos.getY() < board.length &&
                pos.getX() >= 0 && pos.getX() < board[pos.getY()].length;
    }

    public Position getPlayerPosition() {
        return player.getPosition();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Tile[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(board[i][j].toString());
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}