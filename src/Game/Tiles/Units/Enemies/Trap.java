package Game.Tiles.Units.Enemies;

import Game.Board.GameBoard;
import Game.Tiles.Units.Characters.Player;
import Game.Utils.Position;

public class Trap extends Enemy {
    private final int visibilityTime;
    private final int invisiblyTime;
    private int ticksCount;
    private boolean visible;

    public Trap(Position position, String name, char tile, int health, int attack, int defense, Integer expValue, Integer visibilityTime, Integer invisibilityTime) {
        super(position, name, tile, health, attack, defense, expValue);
        this.visibilityTime = visibilityTime; // Time the trap is visible
        this.invisiblyTime = invisibilityTime; // Time the trap is invisible
        this.ticksCount = 0; // Counter for ticks to manage visibility
        this.visible = true; // The initial state of the trap is visible
    }

    @Override
    public void move(GameBoard board) {
        onTurn();
        if (this.getPosition().range(board.getPlayerPosition()) < 2)
            this.attack(board.getPlayer(), board);
    }

    @Override
    public void attack(Player player, GameBoard board) {
        messageCallback().send(String.format("%s dealt %d damage to %s", this.getName(), this.getAttackPoints(), player.getName()));
        player.reduceHealth(this.getAttackPoints());
    }

    private void onTurn() {
        visible = ticksCount < visibilityTime;
        if (ticksCount == visibilityTime + invisiblyTime) {
            ticksCount = 0; // Reset the tick count after a full cycle
        } else {
            ticksCount++; // Increment the tick count
        }
    }

    @Override
    public String toString() {
        if (visible) {
            return super.toString();
        } else {
            return ".";
        }
    }
}