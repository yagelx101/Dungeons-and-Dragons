package View;

public class CLI {
    public void sendMessage(String message) {
        System.out.println(message);
    }

    public void sendEnemyDeath(String enemyName) {
        System.out.println(String.format("%s got killed", enemyName));
    }

    public void sendPlayerDeath(String playerName) {
        System.out.println(String.format("%s is dead, you lose!", playerName));
    }
}
