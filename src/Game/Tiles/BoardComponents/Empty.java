package Game.Tiles.BoardComponents;

import Game.Board.GameBoard;
import Game.Tiles.Tile;
import Game.Tiles.Units.Characters.Player;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;

public class Empty extends Tile {

    public Empty(Position pos) {
        super('.', pos);
    }

    @Override
    public void interact(Player player, GameBoard board) {
        player.move(this.getPosition());
    }

    @Override
    public void interact(Enemy enemy, GameBoard board) {
        enemy.move(this.getPosition());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile tile = (Tile) obj;
            return tile.getTile() == this.getTile();
        }
        return false;
    }
}
