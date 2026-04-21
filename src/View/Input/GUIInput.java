package View.Input;

import Game.Tiles.Units.Actions.Action;
import Game.Tiles.Units.Actions.Move;
import Game.Tiles.Units.Characters.Player;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

public class GUIInput implements InputProvider {
    private final BlockingQueue<Action> actions = new LinkedBlockingQueue<>();

    public Action getNextAction(){
        return actions.poll();
    }

    public boolean hasNextAction(){
        return !actions.isEmpty();
    }

    public void keyPressed(int keyCode) {
        System.out.println("Key pressed: " + KeyEvent.getKeyText(keyCode));
        switch (keyCode) {
            case KeyEvent.VK_W -> actions.add(new Move(Move.Direction.UP));
            case KeyEvent.VK_S -> actions.add(new Move(Move.Direction.DOWN));
            case KeyEvent.VK_A -> actions.add(new Move(Move.Direction.LEFT));
            case KeyEvent.VK_D -> actions.add(new Move(Move.Direction.RIGHT));
            case KeyEvent.VK_E -> actions.add(new Move(Move.Direction.CAST_ABILITY));
            case KeyEvent.VK_Q -> actions.add(new Move(Move.Direction.NONE));
        }
        System.out.println("Action added to queue. Queue size: " + actions.size());
    }


    @Override
    public Function<Player, Action> inputQuery() {
        return player -> {
            try {
                // מחכה עד שיש פעולה – חוסם כמו scanner.nextLine()
                return actions.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new Move(Move.Direction.NONE);
            }
        };
    }


//    @Override
//    public Function<Player, Action> inputQuery() {
//        return player -> {
//            // אם יש פקודה מוכנה מהתור – מחזירים אותה
//            Action action = actions.poll();
//            if (action != null) {
//                return action;
//            }
//            // אם אין – השחקן לא עושה כלום (או מחכים)
//            return new Move(Move.Direction.NONE);
//        };
//    }


//    private Action move;

//    public void keyPressed(int keyCode) {
//        move = switch (keyCode) {
//            case KeyEvent.VK_W -> new Move(Move.Direction.UP);
//            case KeyEvent.VK_S -> new Move(Move.Direction.DOWN);
//            case KeyEvent.VK_A -> new Move(Move.Direction.LEFT);
//            case KeyEvent.VK_D -> new Move(Move.Direction.RIGHT);
//            case KeyEvent.VK_E -> new Move(Move.Direction.CAST_ABILITY);
//            case KeyEvent.VK_Q -> new Move(Move.Direction.NONE);
//            default -> new Move(Move.Direction.NONE);
//        };
//    }

//    @Override
//    public Function<Player, Action> inputQuery() {
//        return player -> {
//            // מחכים עד שיש קלט
//            while (move == null) {
//                try {
//                    Thread.sleep(10); // לא להעמיס על המעבד
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//            Action result = move;
//            move = null; // איפוס – כדי לא לחזור על אותה פעולה שוב ושוב
//            return result;
//        };
//    }
}

