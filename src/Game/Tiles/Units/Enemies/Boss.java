package Game.Tiles.Units.Enemies;

import Game.Board.GameBoard;
import Game.Tiles.Units.HeroicUnit;
import Game.Utils.Position;

public class Boss extends Enemy implements HeroicUnit {
    private final int visionRange;
    private final int abilityFrequency; // Frequency of the boss's ability
    private int combatTicks;

    private final String specialAbilityName = "Shoebodybop";


    public Boss(Position position, String name, char tile, int health, int attack, int defense, Integer expValue, Integer visionRange, Integer abilityFrequency) {
        super(position, name, tile, health, attack, defense, expValue);
        this.visionRange = visionRange; // Vision range of the boss
        this.abilityFrequency = abilityFrequency; // Frequency of the boss's ability
        this.combatTicks = 0; // Initialize combat ticks to 0
    }

    @Override
    public void move(GameBoard board) {
        Position playerPosition = board.getPlayerPosition();
        if (this.getPosition().range(playerPosition) >= getVisionRange()) {
            combatTicks = 0;
            super.move(board);
        } else {
            if (combatTicks == abilityFrequency) {
                combatTicks = 0;
                castAbility(board);
            } else {
                combatTicks++;
                playerInSight(board, playerPosition);
            }
        }
    }

    private int getVisionRange() {
        return visionRange;
    }

    @Override
    public boolean castAbility(GameBoard board) {
        messageCallback().send(this.getName() + " Cast " + specialAbilityName);
        combatSystem().battleWithCast(this, board.getPlayer(), getAttackPoints());
        return true;
    }

}
