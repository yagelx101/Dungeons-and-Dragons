package View.parser;

import Game.CallBacks.MessageCallBack;
import Game.Tiles.BoardComponents.Empty;
import Game.Tiles.BoardComponents.Wall;
import Game.Tiles.Units.Characters.*;
import Game.Tiles.Units.Enemies.Boss;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Enemies.Monster;
import Game.Tiles.Units.Enemies.Trap;
import Game.Utils.Position;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TileFactory {
    private final List<Supplier<Player>> playersList;
    private final Map<Character, Supplier<Enemy>> enemiesMap;
    private Player choice;

    public TileFactory() {
        this.playersList = initPlayers();
        this.enemiesMap = initEnemies();
    }

    private Map<Character, Supplier<Enemy>> initEnemies() {
        List<Supplier<Enemy>> enemies = Arrays.asList(
                () -> new Monster(null, "Lannister Soldier", 's', 80, 8, 3, 25, 3),
                () -> new Monster(null, "Lannister Knight", 'k', 200, 14, 8, 50, 4),
                () -> new Monster(null, "Queen’s Guard", 'q', 400, 20, 15, 100, 5),
                () -> new Monster(null, "Wright", 'z', 600, 30, 15, 100, 3),
                () -> new Monster(null, "Bear-Wright", 'b', 1000, 75, 30, 250, 4),
                () -> new Monster(null, "Giant-Wright", 'g', 1500, 100, 40, 500, 5),
                () -> new Monster(null, "White Walker", 'w', 2000, 150, 50, 1000, 6),
                () -> new Boss(null, "The Mountain", 'M', 1000, 60, 25, 500, 6, 5),
                () -> new Boss(null, "Queen Cersei", 'C', 100, 10, 10, 1000, 1, 8),
                () -> new Boss(null, "Night’s King", 'K', 5000, 300, 150, 5000, 8, 3),
                () -> new Trap(null, "Bonus Trap", 'B', 1, 1, 1, 250, 1, 5),
                () -> new Trap(null, "Queen’s Trap", 'Q', 250, 50, 10, 100, 3, 7),
                () -> new Trap(null, "Death Trap", 'D', 500, 100, 20, 250, 1, 10));

        return enemies.stream().collect(Collectors.toMap(enemy -> enemy.get().getTile(), enemy -> enemy));
    }

    private List<Supplier<Player>> initPlayers() {
        return Arrays.asList(
                () -> new Warrior(null, "Jon Snow", 300, 30, 4, 3),
                () -> new Warrior(null, "The Hound", 400, 20, 6, 5),
                () -> new Mage(null, "Melisandre", 100, 5, 1, 300, 30, 15, 5, 6),
                () -> new Mage(null, "Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4),
                () -> new Roque(null, "Arya Stark", 150, 40, 2, 20),
                () -> new Roque(null, "Bronn", 250, 35, 3, 50),
                () -> new Hunter(null, "Ygritte", 220, 30, 2, 6, 10)
        );
    }

    public List<Player> listOfPlayers() {
        return playersList.stream().map(Supplier::get).toList();
    }

    public Enemy produceEnemy(Character tileRep, Position position) {
        if (enemiesMap.containsKey(tileRep)) {
            Enemy enemy = enemiesMap.get(tileRep).get();
            enemy.setPosition(position);
            return enemy;
        } else {
            System.out.println(String.format("Error while parsing the file, enemy with tileRep %c at position %s not found!", tileRep, position));
            System.exit(-1);
            return null;
        }
    }

    public Player producePlayer(Integer id, Position position) {
        if (choice == null) {
            choice = playersList.get(id).get();
            choice.setPosition(position);
        }
        return choice;
    }

    public Empty produceEmpty(Position position) {
        return new Empty(position);
    }

    public Wall produceWall(Position position, MessageCallBack msg) {
        return new Wall(position, msg);
    }

}
