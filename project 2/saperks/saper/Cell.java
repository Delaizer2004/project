package saper;
import javax.swing.JButton;

public class Cell {
    private boolean isMine;
    private boolean isOpened;
    private boolean isFlagged;
    private JButton button;

    public Cell() {
        this.isMine = false;
        this.isOpened = false;
        this.isFlagged = false;
        this.button = new JButton();
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public JButton getButton() {
        return button;
    }
}
