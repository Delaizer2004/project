package saper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GamePanel extends JPanel {
    private int size;
    private int mines;
    private Cell[][] cells;
    private int cellsToOpen;
    private boolean gameOver = false;
    private boolean flagMode = false;
    private Image bombImage;
    private ActionListener backToMenuListener;

    public GamePanel(int size, int mines, Image bombImage, ActionListener backToMenuListener) {
        this.size = size;
        this.mines = mines;
        this.bombImage = bombImage;
        this.backToMenuListener = backToMenuListener;
        this.cells = new Cell[size][size]; 

        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell();  
            }
        }
        
        setLayout(new BorderLayout());
        JPanel grid = new JPanel(new GridLayout(size, size));

        initializeField();
        placeMines();

        cellsToOpen = size * size - mines;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = cells[i][j];
                int x = i;
                int y = j;
                cell.getButton().setPreferredSize(new Dimension(40, 40));
                cell.getButton().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (flagMode) {
                            toggleFlag(x, y);
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            openCell(x, y, true);
                        }
                    }
                });
                grid.add(cell.getButton());
            }
        }

        JPanel controls = new JPanel();
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(backToMenuListener);

        JButton modeButton = new JButton("Switch to Flag Mode");
        modeButton.addActionListener(event -> {
            flagMode = !flagMode;
            modeButton.setText(flagMode ? "Switch to Open Mode" : "Switch to Flag Mode");
        });

        controls.add(backButton);
        controls.add(modeButton);

        add(grid, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);
    }

    private void initializeField() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j].setOpened(false);
                cells[i][j].setMine(false);
                cells[i][j].setFlagged(false);
            }
        }
        gameOver = false;
    }

    private void placeMines() {
        Random random = new Random();
        int count = 0;
        while (count < mines) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            if (!cells[x][y].isMine()) {
                cells[x][y].setMine(true);
                count++;
            }
        }
    }

    private int countMinesAround(int x, int y) {
        int count = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < size && j >= 0 && j < size && cells[i][j].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void toggleFlag(int x, int y) {
        if (cells[x][y].isOpened() || gameOver) {
            return;
        }
        cells[x][y].setFlagged(!cells[x][y].isFlagged());
        cells[x][y].getButton().setText(cells[x][y].isFlagged() ? "F" : "");
    }

    private void openCell(int x, int y, boolean byPlayer) {
        if (x < 0 || x >= size || y < 0 || y >= size || cells[x][y].isOpened() || cells[x][y].isFlagged() || gameOver) {
            return;
        }
        cells[x][y].setOpened(true);
        if (cells[x][y].isMine()) {
            cells[x][y].getButton().setIcon(new ImageIcon(bombImage));
            showAllMines();
            gameOver = true;
            showGameOverDialog();
            return;
        }
        cellsToOpen--;
        int minesAround = countMinesAround(x, y);
        if (minesAround > 0) {
            cells[x][y].getButton().setText(String.valueOf(minesAround));
            cells[x][y].getButton().setBackground(byPlayer ? Color.GREEN : Color.LIGHT_GRAY);
            if (cellsToOpen == 0) {
                gameOver = true;
                showWinDialog();
            }
            return;
        }
        cells[x][y].getButton().setText(" ");
        cells[x][y].getButton().setBackground(byPlayer ? Color.GREEN : Color.LIGHT_GRAY);
        openCell(x - 1, y - 1, false);
        openCell(x - 1, y, false);
        openCell(x - 1, y + 1, false);
        openCell(x, y - 1, false);
        openCell(x, y + 1, false);
        openCell(x + 1, y - 1, false);
        openCell(x + 1, y, false);
        openCell(x + 1, y + 1, false);
    }

    private void showAllMines() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j].isMine()) {
                    cells[i][j].getButton().setIcon(new ImageIcon(bombImage));
                    cells[i][j].getButton().setBackground(Color.RED);
                }
            }
        }
    }

    private void showGameOverDialog() {
        JOptionPane.showMessageDialog(this, "Game Over! You hit a mine!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        backToMenuListener.actionPerformed(null);
    }

    private void showWinDialog() {
        JOptionPane.showMessageDialog(this, "Congratulations! You won the game!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
        backToMenuListener.actionPerformed(null);
    }
}
