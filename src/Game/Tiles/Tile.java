package Game.Tiles;

import Game.Board.GameBoard;
import Game.Tiles.Units.Characters.Player;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;

public abstract class Tile {
    private final char tile;
    private Position position;

    public Tile(char tile, Position position) {
        this.tile = tile;
        this.position = position;
    }

    public char getTile() {
        return tile;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract void interact(Player player, GameBoard board);

    public abstract void interact(Enemy enemy, GameBoard board);

    @Override
    public String toString() {
        return getTile() + "";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile tile = (Tile) obj;
            return tile.getTile() == this.getTile() && tile.getPosition().equals(this.getPosition());
        }
        return false;
    }

}
