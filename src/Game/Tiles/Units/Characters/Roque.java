package Game.Tiles.Units.Characters;

import Game.Board.GameBoard;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.HeroicUnit;
import Game.Utils.Position;
import Game.Utils.Resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Roque extends Player implements HeroicUnit {
    private final int cost;
    private final Resource energy;
    private final int maxEnergy = 100;// Maximum energy capacity for the roque
    private final int energyRestoration = 10;
    private final int attackModifier = 3;

    private final String specialAbilityName = "Fan of Knives";

    public Roque(Position position, String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer Cost) {
        super(position, name, '@', healthPool, attackPoints, defensePoints);
        this.cost = Cost; // Cost of the roque's ability
        this.energy = new Resource(maxEnergy, maxEnergy); // Initialize energy resource with max capacity of 100
    }

    @Override
    protected void LevelUp() {
        super.LevelUp();
        this.energy.restore();
        this.addAttackPoints(attackModifier * getLevel());
        messageCallback().send(String.format("%s reached level %d: +%d Health, +%d Attack, +%d Defense, +%s Energy", getName(), getLevel(), gainHealth(), gainAttack(), gainDefense(), getEnergy()));
    }

    @Override
    public void gameTick() {
        this.energy.addCurrentAmount(energyRestoration);
    }

    @Override
    public boolean castAbility(GameBoard board) {
        if (getEnergy().getCurrentAmount() < getCost()) {
            messageCallback().send("Not enough energy to cast ability!");
            return false;
        }

        messageCallback().send(this.getName() + " casts " + specialAbilityName);
        getEnergy().reduceCurrentAmount(getCost());
        List<Enemy> enemiesInRange = new ArrayList<>(
                board.getEnemies().stream()
                        .filter(e -> e.isAlive() && this.getPosition().range(e.getPosition()) < 2)
                        .toList()
        );

        for (Iterator<Enemy> it = enemiesInRange.iterator(); it.hasNext(); ) {
            Enemy enemy = it.next();
            combatSystem().battleWithCast(this, enemy, getAttackPoints());

            if (enemy.isDead()) {
                this.onAbilityKill(enemy, board);
                it.remove();
            }
        }

        return true;
    }


    public Resource getEnergy() {
        return energy;
    }

    public int getCost() {
        return cost;
    }

    public String describe() {
        return String.format("%s\t\tEnergy: %s\t\tAbility cost: %d", super.describe(), getEnergy(), getCost());
    }

}
