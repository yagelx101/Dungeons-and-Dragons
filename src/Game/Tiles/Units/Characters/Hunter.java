package Game.Tiles.Units.Characters;

import Game.Board.GameBoard;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.HeroicUnit;
import Game.Utils.Position;
import Game.Utils.Resource;

import java.util.Comparator;
import java.util.List;

public class Hunter extends Player implements HeroicUnit {
    private final int range;
    private final Resource arrows;
    private int ticksCount;

    private final int attackModifier = 2;
    private final int defenseModifier = 1;
    private final String specialAbilityName = "Shoot";

    public Hunter(Position position, String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer range, Integer arrowsCount) {
        super(position, name, '@', healthPool, attackPoints, defensePoints);
        this.range = range; // Range of the hunter's attacks
        this.arrows = new Resource(arrowsCount, arrowsCount); // Initial number of arrows
        this.ticksCount = 0; // Initialize ticks count to 0

    }

    @Override
    protected void LevelUp() {
        super.LevelUp();
        getArrows().addCapacity(10 * getLevel());
        getArrows().restore();
        this.addAttackPoints(attackModifier * getLevel());
        this.addDefensePoints(defenseModifier * getLevel());
        messageCallback().send(String.format("%s reached level %d: +%d Health, +%d Attack, +%d Defense, +%s Arrows", getName(), getLevel(), gainHealth(), gainAttack(), gainDefense(), getArrows()));

    }

    @Override
    public void gameTick() {
        if (this.ticksCount == 10) {
            ticksCount = 0; // Reset ticks count
            getArrows().addCurrentAmount(getLevel());
        } else
            ticksCount++;
    }

    @Override
    public boolean castAbility(GameBoard board) {
        if (getArrows().getCurrentAmount() <= 0) {
            messageCallback().send("No arrows left to cast ability.");
            return false;
        }

        List<Enemy> enemiesInRange = board.getEnemies().stream()
                .filter(Enemy::isAlive)
                .filter(e -> this.getPosition().range(e.getPosition()) <= range)
                .toList();

        Enemy enemyToAttack = enemiesInRange.stream()
                .min(Comparator.comparingDouble(e -> this.getPosition().range(e.getPosition())))
                .orElse(null);

        if (enemyToAttack == null) {
            messageCallback().send("No target found.");
            return false;
        }
        messageCallback().send(this.getName() + " Cast " + specialAbilityName);

        combatSystem().battleWithCast(this, enemyToAttack, getAttackPoints());
        if (enemyToAttack.isDead()) {
            this.onAbilityKill(enemyToAttack, board);
        }
        return true;
    }

    public Resource getArrows() {
        return this.arrows;
    }

    public String describe() {
        return String.format("%s\t\tArrows: %s", super.describe(), getArrows());
    }
}
