package Game.Tiles.Units.Actions;

import Game.CallBacks.MessageCallBack;
import Game.Tiles.Units.Unit;

import java.util.Random;

public class CombatSystem {
    private final Random RAND = new Random();
    private final MessageCallBack messageCallBack;

    public CombatSystem(MessageCallBack msg) {
        this.messageCallBack = msg;
    }

    public void battle(Unit attacker, Unit defender) {
        int damage = RAND.nextInt(attacker.getAttackPoints() + 1);
        int defense = RAND.nextInt(defender.getDefensePoints() + 1);
        messageCallBack.send(String.format("%s rolled %d attack points\n%s rolled %d defense points", attacker.getName(), damage, defender.getName(), defense));

        if (damage >= defense) {
            messageCallBack.send(String.format("%s dealt %d damage to %s", attacker.getName(), damage - defense, defender.getName()));
            defender.reduceHealth(damage - defense);
        } else
            messageCallBack.send("not enough damage, nothing happened");

    }


    public void battleWithCast(Unit attacker, Unit defender, int AbilityDamage) {
        int defense = RAND.nextInt(defender.getDefensePoints() + 1);
        messageCallBack.send(String.format("%s attack with %d attack points\n%s rolled %d defense points", attacker.getName(), AbilityDamage, defender.getName(), defense));
        if (AbilityDamage > defense) {
            messageCallBack.send(String.format("%s dealt %d damage to %s", attacker.getName(), AbilityDamage - defense, defender.getName()));
            defender.reduceHealth(AbilityDamage - defense);
        } else
            messageCallBack.send("not enough damage, nothing happened");
    }
}
