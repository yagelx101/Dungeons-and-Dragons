package Game.Tiles.BoardComponents;

import Game.Board.GameBoard;
import Game.Tiles.Tile;
import Game.Tiles.Units.Characters.Player;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;

public class DeadPlayer extends Tile {
    public DeadPlayer(Position pos) {
        super('X', pos);
    }

    @Override
    public void interact(Player player, GameBoard board) {
    } //Do nothing, if show up the game is over

    @Override
    public void interact(Enemy enemy, GameBoard board) {
    } //Do nothing, if show up the game is over

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile tile = (Tile) obj;
            return tile.getTile() == this.getTile();
        }
        return false;
    }
}
