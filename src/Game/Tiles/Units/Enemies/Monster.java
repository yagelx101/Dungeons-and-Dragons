package Game.Tiles.Units.Enemies;

import Game.Board.GameBoard;
import Game.Utils.Position;

public class Monster extends Enemy {
    private final int visionRange;

    public Monster(Position position, String name, char tile, Integer health, Integer attack, Integer defense, Integer expValue, Integer visionRange) {
        super(position, name, tile, health, attack, defense, expValue);
        this.visionRange = visionRange; // Vision range for the monster
    }

    @Override
    public void move(GameBoard board) {
        Position playerPosition = board.getPlayerPosition();
        if (this.getPosition().range(playerPosition) >= getVisionRange())
            super.move(board);
        else {
            playerInSight(board, playerPosition);
        }
    }

    public int getVisionRange() {
        return visionRange;
    }

}
