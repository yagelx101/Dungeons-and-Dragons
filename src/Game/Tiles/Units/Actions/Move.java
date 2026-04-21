package Game.Tiles.Units.Actions;

import Game.Board.GameBoard;
import Game.Tiles.Tile;
import Game.Tiles.Units.Characters.Player;
import Game.Utils.Position;

public class Move implements Action {

    public enum Direction {UP, DOWN, LEFT, RIGHT, CAST_ABILITY, NONE}

    private final Direction direction;

    public Move(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void execute(Player player, GameBoard board) {
        if (direction == Direction.CAST_ABILITY) {
            if (!player.castAbility(board))
                throw new IllegalArgumentException("");
            return;
        }

        Position current = player.getPosition();
        Position target = switch (direction) {
            case UP -> current.up();
            case DOWN -> current.down();
            case LEFT -> current.left();
            case RIGHT -> current.right();
            case NONE, CAST_ABILITY -> current.stay();
        };

        Tile targetTile = board.getBoardTile(target);
        targetTile.interact(player, board);
    }

}
