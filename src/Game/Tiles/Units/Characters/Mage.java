package Game.Tiles.Units.Characters;

import Game.Board.GameBoard;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Position;
import Game.Utils.Resource;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Mage extends Player implements Game.Tiles.Units.HeroicUnit {
    private final Resource mana;
    private final int abilityCost;
    private final int hitsCount;
    private final int abilityRange;
    private int spellPower;

    private final int manaModifier = 25;
    private final int spellPowerModifier = 10;

    private final String specialAbilityName = "Blizzard";

    public Mage(Position position, String name, Integer healthPool, Integer attackPoints, Integer defensePoints, Integer ManaPool, Integer AbilityCost, Integer SpellPower, Integer HitsCount, Integer abilityRange) {
        super(position, name, '@', healthPool, attackPoints, defensePoints);
        this.mana = new Resource(ManaPool / 4, ManaPool);
        this.abilityCost = AbilityCost;
        this.spellPower = SpellPower;
        this.hitsCount = HitsCount;
        this.abilityRange = abilityRange;
    }

    @Override
    public boolean castAbility(GameBoard board) {
        if (mana.getCurrentAmount() < getAbilityCost()) {
            messageCallback().send("Not enough mana to cast ability!");
            return false;
        }
        messageCallback().send(this.getName() + " Cast " + specialAbilityName);
        mana.reduceCurrentAmount(getAbilityCost());

        int hits = 0;
        Random rand = new Random();

        List<Enemy> enemiesInRange = board.getEnemies().stream()
                .filter(e -> e.isAlive() && this.getPosition().range(e.getPosition()) < abilityRange)
                .collect(Collectors.toList());

        while (hits < hitsCount && !enemiesInRange.isEmpty()) {
            Enemy enemyToAttack = enemiesInRange.get(rand.nextInt(enemiesInRange.size()));

            combatSystem().battleWithCast(this, enemyToAttack, getSpellPower());
            messageCallback().send(this.describe() + "\n" + enemyToAttack.describe());

            if (enemyToAttack.isDead()) {
                this.onAbilityKill(enemyToAttack, board);
                enemiesInRange.remove(enemyToAttack);
            }
            hits++;
        }

        if (hits == 0) {
            messageCallback().send("No enemies in range for ability.");
        }
        return true;
    }

    @Override
    protected void LevelUp() {
        super.LevelUp();
        getMana().addCapacity(gainManaCapacity());
        getMana().addCurrentAmount(gainManaAmount());
        addSpellPower(gainSpellPower());
        messageCallback().send(String.format("%s reached level %d: +%d Health, +%d Attack, +%d Defense, +%d Mana amount, +%d Mana capacity, +%d Spell power", getName(), getLevel(), gainHealth(), gainAttack(), gainDefense(), gainManaAmount(),gainManaCapacity(), gainSpellPower()));
    }

    public int gainManaCapacity() {
        return this.getLevel() * this.manaModifier;
    }

    public int gainManaAmount() {
        return getMana().getCapacity() / 4;
    }

    public int gainSpellPower() {
        return this.getLevel() * this.spellPowerModifier;
    }


    @Override
    public void gameTick() {
        this.mana.mulCurrentAmount(1, this.getLevel());
    }

    public Resource getMana() {
        return mana;
    }

    public int getAbilityCost() {
        return abilityCost;
    }

    public int getSpellPower() {
        return spellPower;
    }

    private void addSpellPower(int power) {
        this.spellPower += power;
    }

    public String describe() {
        return String.format("%s\t\tMana: %s\t\tSpell power: %d\t\tAbility cost: %d", super.describe(), getMana(), getSpellPower(), getAbilityCost());
    }
}
