package GUI;

import Game.Tiles.Tile;
import View.Input.GUIInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BoardPanel extends JPanel {
    private Tile[][] board;  // כאן נשמור את המפה מהמשחק שלך
    private final GUIInput guiInput;
    private boolean keyPressed = false;

    public BoardPanel(Tile[][] board, GUIInput guiInput) {
        this.board = board;
        setPreferredSize(new Dimension(board[0].length * 15, board.length * 15)); // גודל החלון

        this.guiInput = guiInput;
        setFocusable(true);
        //addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int tileSize = 30; // גודל בלוק אחד

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                Tile tile = board[y][x];
                switch (tile.toString().charAt(0)) {
                    case '#': g.setColor(Color.DARK_GRAY); break; // קיר
                    case '.': g.setColor(Color.LIGHT_GRAY); break; // ריק
                    case '@': g.setColor(Color.GREEN); break;     // שחקן
                    case 'X': g.setColor(Color.BLACK); break;     // שחקן מת
                    default:  g.setColor(Color.RED); break;       // אויב
                }
                g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
    }

    public void updateBoard(Tile[][] newBoard) {
        this.board = newBoard;
        repaint(); // מצייר מחדש
    }

//    @Override
//    public void keyPressed(KeyEvent e) {
//        if (guiInput != null) {
//            guiInput.keyPressed(e.getKeyCode());
//            keyPressed = true;
//        }
//    }

    public boolean isKeyPressed() {
        return keyPressed;
    }

//    @Override
//    public void keyReleased(KeyEvent e) {}
//
//    @Override
//    public void keyTyped(KeyEvent e) {}
}


