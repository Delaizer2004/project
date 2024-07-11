package saper;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class Sapper extends JFrame {
    private Image bombImage;

    public Sapper() {
        setTitle("Sapper");
        setSize(200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        try {
            bombImage = new ImageIcon(getClass().getResource("bomb.png")).getImage();
        } catch (Exception e) {
            showError("Error", "Bomb image not found");
            return;
        }

        
        showMainMenu();
        setVisible(true);
    }

    private void showMainMenu() {
        MainMenu mainMenu = new MainMenu(
            event -> startGame(10, 15),
            event -> startGame(15, 30),
            event -> startGame(20, 45),
            event -> System.exit(0)
        );
        setContentPane(mainMenu);
        pack();
    }

    private void startGame(int size, int mines) {
        GamePanel gamePanel = new GamePanel(size, mines, bombImage, event -> showMainMenu());
        setContentPane(gamePanel);
        pack();
    }

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {    
        SwingUtilities.invokeLater(Sapper::new);
    }
}
