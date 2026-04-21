package Game.Tiles.Units.Actions;

import Game.Board.GameBoard;
import Game.Tiles.Units.Characters.Player;

public interface Action {
    void execute(Player player, GameBoard board);
}
