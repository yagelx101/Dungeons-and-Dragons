package GUI;

import Game.Tiles.BoardComponents.Wall;
import Game.Tiles.Tile;
import Game.Utils.Position;
import View.Input.GUIInput;

import javax.swing.*;

public class GameWindow {

    private final BoardPanel panel;

    public GameWindow(Tile[][] board) {
        JFrame frame = new JFrame("Dungeons & Dragons");
        this.panel = new BoardPanel(board, new GUIInput());

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        panel.requestFocusInWindow(); // חשוב מאוד כדי שהמקלדת תעבוד

    }

    public GameWindow(){
        this.panel=null;
    }

    public void updateBoard (Tile[][] newBoard){
        panel.updateBoard(newBoard);
    }

    public boolean isKeyPressed() {
        return panel.isKeyPressed();
    }

    public BoardPanel getPanel() {
        return panel;
    }
}

