package View.Input;

import Game.Tiles.Units.Actions.Action;
import Game.Tiles.Units.Actions.Move;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

public class KeyboardListener implements KeyListener {
    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Consumer<Action> actionConsumer;

    public KeyboardListener(Consumer<Action> actionConsumer) {
        this.actionConsumer = actionConsumer;
        addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        System.out.println("Key pressed: " + KeyEvent.getKeyText(code));
        if (!pressedKeys.contains(code)) { // שלא יישלח פעמיים ברצף
            pressedKeys.add(code);
            Action action = switch (code) {
                case KeyEvent.VK_W -> new Move(Move.Direction.UP);
                case KeyEvent.VK_S -> new Move(Move.Direction.DOWN);
                case KeyEvent.VK_A -> new Move(Move.Direction.LEFT);
                case KeyEvent.VK_D -> new Move(Move.Direction.RIGHT);
                case KeyEvent.VK_E -> new Move(Move.Direction.CAST_ABILITY);
                case KeyEvent.VK_Q -> new Move(Move.Direction.NONE);
                default -> null;
            };
            if (action != null) {
                actionConsumer.accept(action); // שולח את הפעולה ל-Level
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
