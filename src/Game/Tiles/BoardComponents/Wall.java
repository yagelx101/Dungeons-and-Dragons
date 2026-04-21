package Game.Tiles.BoardComponents;

import Game.Board.GameBoard;
import Game.CallBacks.MessageCallBack;
import Game.Tiles.Tile;
import Game.Tiles.Units.Characters.Player;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;

public class Wall extends Tile {
    private final MessageCallBack messageCallBack;

    public Wall(Position position, MessageCallBack msg) {
        super('#', position);
        this.messageCallBack= msg;
    }

    @Override
    public void interact(Player player, GameBoard board) {
        messageCallBack.send("You reached wall, can't go trough");
    }

    @Override
    public void interact(Enemy enemy, GameBoard board) {
    } //Do nothing, stack at wall

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile tile) {
            return tile.getTile() == this.getTile();
        }
        return false;
    }
}
