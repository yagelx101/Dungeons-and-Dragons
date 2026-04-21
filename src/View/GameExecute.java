package View;

import View.Input.GUIInput;
import View.Input.InputProvider;
import View.parser.Level;
import Game.Tiles.Units.Characters.Player;
import View.Input.CommandLineInput;
import View.parser.FileParser;
import View.parser.TileFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GameExecute {
    private final Scanner scanner;
    private final InputProvider inputProvider;
    private final TileFactory tileFactory;
    private final CLI cli;
    private List<Level> levels;

    public GameExecute() {
        this.cli = new CLI();
        this.scanner = new Scanner(System.in);
        this.inputProvider= new GUIInput();
        //this.inputProvider = new CommandLineInput();
        this.tileFactory = new TileFactory();
    }


    public void start() {
        int levelNumber = 1;
        for (Level currLevel : levels) {
            System.out.println("Start level " + levelNumber + ":");
            System.out.println(currLevel);
            currLevel.initializeLevel();
            while (!currLevel.levelFinished() && !currLevel.gameOver()) {
                currLevel.playOneTurn();
            }
            if (currLevel.gameOver()) {
                System.out.println("Game Over!");
                return;
            }
            levelNumber++;
        }
        System.out.println("Congratulations! You won!");
    }


    public void initialize(String levelsDirectory) {
        int id = choosePlayer();
        FileParser fileParser = new FileParser(tileFactory, cli::sendMessage, cli::sendPlayerDeath, cli::sendEnemyDeath, inputProvider, id);
        File root = new File(levelsDirectory);
        levels = Arrays.stream(Objects.requireNonNull(root.listFiles())).map(fileParser::parseFile).collect(Collectors.toList());
    }

    private int choosePlayer() {
        while (true) {
            cli.sendMessage("Select Player:");
            List<Player> players = tileFactory.listOfPlayers();
            for (int i = 0; i < players.size(); i++) {
                cli.sendMessage(String.format("%d. %s", i + 1, players.get(i).describe()));
            }
            try {
                int choice = Integer.parseInt(scanner.nextLine()) - 1;
                if (choice >= 0 && choice < players.size()) {
                    cli.sendMessage(String.format("you have selected:\n%s", players.get(choice).getName()));
                    return choice;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number corresponding to the player.");
            }
        }

    }

}
