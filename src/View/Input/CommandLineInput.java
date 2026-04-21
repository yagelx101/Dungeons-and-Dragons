package View.Input;

import Game.Tiles.Units.Actions.Action;
import Game.Tiles.Units.Actions.Move;
import Game.Tiles.Units.Characters.Player;

import java.util.Scanner;
import java.util.function.Function;

public class CommandLineInput implements InputProvider {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Function<Player, Action> inputQuery() {
        return player -> {
            System.out.print("Enter command: ");
            String input = scanner.nextLine().toLowerCase();
            return switch (input) {
                case "w" -> new Move(Move.Direction.UP);
                case "s" -> new Move(Move.Direction.DOWN);
                case "a" -> new Move(Move.Direction.LEFT);
                case "d" -> new Move(Move.Direction.RIGHT);
                case "e" -> new Move(Move.Direction.CAST_ABILITY);
                case "q" -> new Move(Move.Direction.NONE);
                default -> throw new IllegalArgumentException("Invalid command");
            };
        };
    }
}
