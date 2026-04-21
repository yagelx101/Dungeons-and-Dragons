package Game.Tiles.Units.Enemies;

import Game.Board.GameBoard;
import Game.Tiles.BoardComponents.Empty;
import Game.Tiles.Tile;
import Game.Tiles.Units.Characters.Player;
import Game.Tiles.Units.Unit;
import Game.Utils.Position;

import java.util.Random;


public abstract class Enemy extends Unit {
    private final int expValue;

    public Enemy(Position position, String name, char tileRep, int health, int attack, int defense, Integer expValue) {
        super(position, name, tileRep, health, attack, defense);
        this.expValue = expValue;
    }

    @Override
    public void interact(Player player, GameBoard board) {
        player.attack(this, board);
    }

    @Override
    public void interact(Enemy enemy, GameBoard board) {

    } //Do nothing, stack by another enemy

    public void move(GameBoard board) {
        Random rand = new Random();
        Position current = this.getPosition();
        int direction = rand.nextInt(5);
        Position target = switch (direction) {
            case 0 -> current.up();
            case 1 -> current.down();
            case 2 -> current.left();
            case 3 -> current.right();
            case 4 -> current.stay();
            default -> null;
        };
        Tile targetTile = board.getBoardTile(target);
        targetTile.interact(this, board);
    }

    protected void playerInSight(GameBoard board, Position playerPosition) {
        Position current = this.getPosition();
        Position target;
        int dx = this.getPosition().getX() - playerPosition.getX();
        int dy = this.getPosition().getY() - playerPosition.getY();
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0)
                target = current.left();
            else
                target = current.right();
        } else {
            if (dy > 0)
                target = current.up();
            else
                target = current.down();
        }
        Tile targetTile = board.getBoardTile(target);
        targetTile.interact(this, board);
    }

    public void attack(Player player, GameBoard board) {
        messageCallback().send(String.format("%s engaged in combat with %s", this.getName(), player.getName()));
        messageCallback().send(this.describe() + "\n" + player.describe());
        do {
            combatSystem().battle(this, player);
            if (player.isAlive())
                combatSystem().battle(player, this);
        } while (player.isAlive() && this.isAlive());
        if (player.isDead()) {
            messageCallback().send(this.describe());
            player.onDeath(board);
        } else {
            messageCallback().send(player.describe());
            this.onDeath(board);
            playerKillEnemy(board);
        }
    }

    public Integer getExpValue() {
        return expValue;
    }

    @Override
    public void onDeath(GameBoard board) {
        board.setBoardTile(new Empty(this.getPosition()), this.getPosition());
        enemyDeathCallback().sendEnemyDeath(getName());
    }

    private void playerKillEnemy(GameBoard board){
        board.getPlayer().onDefense(this);
    }


    @Override
    public String describe() {
        return String.format("%s\t\tExperience value: %d", super.describe(), getExpValue());
    }
}
