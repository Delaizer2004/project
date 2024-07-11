package ua.edu.chdtu.mamchych.oop.lab3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
public class LoginWindow extends JFrame {
    public LoginWindow(){
        setTitle("Login");
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

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10,80,80,25);
        panel.add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String login = userText.getText();
                String password = new String(passwordText.getPassword());

                if(UserDAO.checkLogin(login, password)){
                    dispose();
                    new MainAppWindow(login);
                }
                else{
                    JOptionPane.showMessageDialog(LoginWindow.this, "Invalod username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(180,80,80,25);
        panel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                dispose();
                new RegisterWindow();
            }
        });
    }
}
