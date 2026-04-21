package Game.Tiles.Units;

import Game.Board.GameBoard;
import Game.CallBacks.EnemyDeathCallback;
import Game.CallBacks.MessageCallBack;
import Game.CallBacks.PlayerDeathCallback;
import Game.CallBacks.PositionChangedCallback;
import Game.Tiles.Units.Actions.CombatSystem;
import Game.Utils.Position;
import Game.Utils.Resource;

public abstract class Unit extends Game.Tiles.Tile {
    private PositionChangedCallback positionChangedCallback = (f, t) -> {
    };
    private MessageCallBack messageCallback = msg -> {
    };
    private EnemyDeathCallback enemyDeathCallback = name -> {
    };
    private PlayerDeathCallback playerDeathCallback = name -> {
    };
    private CombatSystem combatSystem;

    private final String name;
    private final Resource health;
    private int attackPoints;
    private int defensePoints;

    public Unit(Position position, String name, Character tileRep, Integer healthPool, Integer attackPoints, Integer defensePoints) {
        super(tileRep, position);
        this.name = name;
        this.health = new Resource(healthPool, healthPool);
        this.attackPoints = attackPoints;
        this.defensePoints = defensePoints;
    }

    public void initialize(PositionChangedCallback pcc, MessageCallBack msg, PlayerDeathCallback pdc, EnemyDeathCallback edc) {
        this.positionChangedCallback = pcc;
        this.messageCallback = msg;
        this.enemyDeathCallback = edc;
        this.playerDeathCallback = pdc;
        this.combatSystem = new CombatSystem(msg);
    }

    public void move(Position newPos) {
        Position oldPos = this.getPosition();
        this.setPosition(newPos);
        positionChangedCallback.send(oldPos, newPos);
    }

    public boolean isAlive() {
        return health.getCurrentAmount() > 0;
    }

    public boolean isDead() {
        return health.getCurrentAmount() <= 0;
    }

    public String getName() {
        return name;
    }

    public Resource getHealth() {
        return health;
    }

    public void reduceHealth(int amount) {
        health.reduceCurrentAmount(amount);
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public void addAttackPoints(int attackPoints) {
        this.attackPoints += attackPoints;
    }

    public int getDefensePoints() {
        return defensePoints;
    }

    public void addDefensePoints(int defensePoints) {
        this.defensePoints += defensePoints;
    }

    public MessageCallBack messageCallback() {
        return messageCallback;
    }

    public PlayerDeathCallback playerDeathCallback() {
        return playerDeathCallback;
    }

    public EnemyDeathCallback enemyDeathCallback(){
        return enemyDeathCallback;
    }

    public CombatSystem combatSystem(){
        return combatSystem;
    }

    public abstract void onDeath(GameBoard board);

    public String describe() {
        return String.format("%s\t\tHealth: %s\t\tAttack: %d\t\tDefence: %d", getName(), getHealth(), getAttackPoints(), getDefensePoints());
    }

}
