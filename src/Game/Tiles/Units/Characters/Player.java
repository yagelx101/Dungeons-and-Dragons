package Game.Tiles.Units.Characters;

import Game.Board.GameBoard;
import Game.Tiles.BoardComponents.DeadPlayer;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.HeroicUnit;
import Game.Tiles.Units.Unit;
import Game.Utils.Position;

public abstract class Player extends Unit implements HeroicUnit {
    private final int expModifier = 50;
    private final int healthModifier = 10;
    private final int attackModifier = 4;
    private final int defenseModifier = 1;

    private int experience;
    private int level;

    public Player(Position position, String name, Character tileRep, Integer healthPool, Integer attackPoints, Integer defensePoints) {
        super(position, name, tileRep, healthPool, attackPoints, defensePoints);
        this.experience = 0;
        this.level = 1;
    }

    @Override
    public void interact(Player player, GameBoard board) {
    }

    @Override
    public void interact(Enemy enemy, GameBoard board) {
        enemy.attack(this, board);
    }

    public void attack(Enemy enemy, GameBoard board) {
        messageCallback().send(String.format("%s engaged in combat with %s", this.getName(), enemy.getName()));
        messageCallback().send(this.describe() + "\n" + enemy.describe());
        do {
            combatSystem().battle(this, enemy);
            if (enemy.isAlive())
                combatSystem().battle(enemy, this);
        } while (enemy.isAlive() && this.isAlive());
        if (enemy.isDead()) {
            messageCallback().send(this.describe());
            this.onKill(enemy, board);
        } else {
            messageCallback().send(enemy.describe());
            onDeath(board);
        }
    }

    public void onKill(Enemy enemy, GameBoard board) {
        enemy.onDeath(board);
        this.move(enemy.getPosition());
        messageCallback().send(String.format("%s gained %d experience", this.getName(), enemy.getExpValue()));
        GainExperience(enemy.getExpValue());
    }

    public void onDefense(Enemy enemy) {
        messageCallback().send(String.format("%s gained %d experience", this.getName(), enemy.getExpValue()));
        GainExperience(enemy.getExpValue());
    }

    public void onAbilityKill(Enemy enemy, GameBoard board) {
        enemy.onDeath(board);
        messageCallback().send(String.format("%s gained %d experience", this.getName(), enemy.getExpValue()));
        GainExperience(enemy.getExpValue());
    }

    @Override
    public void onDeath(GameBoard board) {
        board.setBoardTile(new DeadPlayer(this.getPosition()), this.getPosition());
        playerDeathCallback().sendPlayerDeath(getName());
    }

    public void GainExperience(Integer experience) {
        this.experience += experience;
        while (this.experience >= levelUpRequirement()) {
            this.LevelUp();
        }
    }

    protected void LevelUp() {
        this.experience -= levelUpRequirement();
        this.level++;
        this.getHealth().addCapacity(gainHealth());
        this.getHealth().restore();
        this.addAttackPoints(gainAttack());
        this.addDefensePoints(gainDefense());
    }

    public Integer getLevel() {
        return level;
    }

    public Integer levelUpRequirement() {
        return getLevel() * expModifier;
    }

    public Integer gainHealth() {
        return getLevel() * healthModifier;
    }

    public Integer gainAttack() {
        return getLevel() * attackModifier;
    }

    public Integer gainDefense() {
        return getLevel() * defenseModifier;
    }

    public Integer getExperience() {
        return experience;
    }

    public String describe() {
        return String.format("%s\t\tLevel: %d\t\tExperience: %d/%d", super.describe(), getLevel(), getExperience(), levelUpRequirement());
    }

    public abstract void gameTick();

    @Override
    public String toString() {
        return isAlive() ? super.toString() : "X";
    }

}
