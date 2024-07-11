package ua.edu.chdtu.mamchych.oop.lab3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class RegisterWindow extends JFrame {
    public RegisterWindow(){
        setTitle("Register");
        setSize(300,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }
    private void placeComponents(JPanel panel){
        panel.setLayout(null);

        JTextField userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(10,80,80,25);
        panel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String login = userText.getText();
                String password = new String(passwordText.getPassword());

                if (UserDAO.registerUser(login, password)) {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new LoginWindow();
                } else {
                    JOptionPane.showMessageDialog(RegisterWindow.this, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                
            }
        });
    }
}
