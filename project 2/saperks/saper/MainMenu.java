package saper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

    public MainMenu(ActionListener smallListener, ActionListener mediumListener, ActionListener largeListener, ActionListener exitListener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Sapper", JLabel.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton smallButton = new JButton("Small (10x10, 15 mines)");
        smallButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        smallButton.addActionListener(smallListener);

        JButton mediumButton = new JButton("Medium (15x15, 30 mines)");
        mediumButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mediumButton.addActionListener(mediumListener);

        JButton largeButton = new JButton("Large (20x20, 45 mines)");
        largeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        largeButton.addActionListener(largeListener);

        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(exitListener);

        add(Box.createVerticalStrut(20));
        add(titleLabel);
        add(Box.createVerticalStrut(20));
        add(smallButton);
        add(mediumButton);
        add(largeButton);
        add(exitButton);
    }
}
