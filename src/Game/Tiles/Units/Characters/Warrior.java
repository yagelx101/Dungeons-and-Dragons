package Game.Tiles.Units.Characters;

import Game.Board.GameBoard;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;

import java.util.List;
import java.util.Random;

public class Warrior extends Player implements Game.Tiles.Units.HeroicUnit {
    private final int abilityCooldown;
    private final int healthModifier = 5;
    private final int attackModifier = 2;
    private final int defenseModifier = 1;

    private int cooldDownRemaining;
    private final String specialAbilityName = "Avenger's shield";


    public Warrior(Position position, String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer abilityCooldown) {
        super(position, name, '@', healthPool, attackPoints, defensePoints);
        this.abilityCooldown = abilityCooldown; // Cooldown time for the warrior's ability
        this.cooldDownRemaining = 0;
    }

    @Override
    protected void LevelUp() {
        super.LevelUp();
        this.cooldDownRemaining = 0;
        this.getHealth().addCapacity(healthModifier * getLevel());
        this.getHealth().restore();
        this.addAttackPoints(attackModifier * getLevel());
        this.addDefensePoints(defenseModifier * getLevel());
        messageCallback().send(String.format("%s reached level %d: +%d Health, +%d Attack, +%d Defense", getName(), getLevel(), gainHealth(), gainAttack(), gainDefense()));
    }

    @Override
    public void gameTick() {
        if (this.cooldDownRemaining > 0) {
            this.cooldDownRemaining--;
        }
    }

    @Override
    public boolean castAbility(GameBoard board) {
        if (getCooldDownRemaining() > 0) {
            messageCallback().send("Ability is on cooldown – please wait " + getCooldDownRemaining() + " more turn(s).");
            return false;
        }
        messageCallback().send(this.getName() + " Cast " + specialAbilityName);

        this.cooldDownRemaining = abilityCooldown;
        this.getHealth().addCurrentAmount(10 * getDefensePoints());

        List<Enemy> enemiesInRange = board.getEnemies().stream()
                .filter(e -> e.isAlive() && this.getPosition().range(e.getPosition()) < 3)
                .toList();

        Random rand = new Random();
        Enemy enemyToAttack = enemiesInRange.get(rand.nextInt(enemiesInRange.size()));

        combatSystem().battleWithCast(this, enemyToAttack, (int) Math.floor(this.getHealth().getCapacity() * 0.1));
        if (enemyToAttack.isDead()) {
            this.onAbilityKill(enemyToAttack, board);
        }
        return true;
    }

    public int getCooldDownRemaining() {
        return cooldDownRemaining;
    }

    public String describe() {
        return String.format("%s\t\tRemaining cooldown: %d", super.describe(), getCooldDownRemaining());
    }
}
