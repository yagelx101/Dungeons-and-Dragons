package View.Input;

import Game.Tiles.Units.Actions.Action;
import Game.Tiles.Units.Characters.Player;

import java.util.function.Function;

public interface InputProvider {
    Function<Player, Action> inputQuery();
}
