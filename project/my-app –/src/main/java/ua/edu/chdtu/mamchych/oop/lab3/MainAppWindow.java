package ua.edu.chdtu.mamchych.oop.lab3;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

public class MainAppWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel buttonsPanel;
    private JPanel cardsPanel;
    private JPanel cartPanel;

    private List<Product> cart;
    private List<Product> allProducts;

    private JTextField searchField;
    private JComboBox<String> filterCriteria;
    private JTextField filterValue;

    public MainAppWindow(String accountName) {
        cart = new java.util.ArrayList<>();

        setTitle("Main Application");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = createHeaderPanel(accountName);
        JPanel footerPanel = createFooterPanel();
        JPanel centerPanel = createCenterPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createHeaderPanel(String accountName) {
        JPanel headerPanel = new JPanel(new BorderLayout());

        JButton headerButton = new JButton(accountName);
        headerButton.setPreferredSize(new Dimension(300, 30));
        headerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPopupMenu menu = createPopupMenu(accountName);
                menu.show(headerButton, headerButton.getWidth() / 2, headerButton.getHeight());
            }
        });

        ImageIcon trolley = new ImageIcon("C:\\Users\\Home\\Desktop\\мой\\project\\trolley.png");
        JButton trolleyButton = new JButton(trolley);
        trolleyButton.setPreferredSize(new Dimension(30, 30));
        trolleyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "cartPanel");
                updateCartPanel();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(headerButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(trolleyButton);
        buttonPanel.add(Box.createHorizontalGlue());

        headerPanel.add(buttonPanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createCenterPanel() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton main = new JButton("main");
        main.setPreferredSize(new Dimension(100, 50));
        main.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Main button clicked");
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonsPanel.add(main, gbc);

        JButton main2 = new JButton("main2");
        main2.setPreferredSize(new Dimension(100, 50));
        main2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Main2 button clicked");
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        buttonsPanel.add(main2, gbc);

        cardsPanel = new JPanel(new BorderLayout());
        JPanel productPanel = new JPanel(new GridLayout(2, 3, 10, 10));

        JPanel searchPanel = createSearchPanel();
        cardsPanel.add(searchPanel, BorderLayout.NORTH);
        cardsPanel.add(new JScrollPane(productPanel), BorderLayout.CENTER);

        loadProductsFromDatabase(productPanel);

        cartPanel = new JPanel(new BorderLayout());
        updateCartPanel();

        cardPanel.add(buttonsPanel, "buttonsPanel");
        cardPanel.add(cardsPanel, "cardsPanel");
        cardPanel.add(cartPanel, "cartPanel");

        return cardPanel;
    }

    private JPanel createProfilePanel(String accountName, String password) {
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
    
        JLabel nameLabel = new JLabel("Account Name: " + accountName);
        profilePanel.add(nameLabel);
    
        JLabel passwordLabel = new JLabel("Password: ");
        profilePanel.add(passwordLabel);
    
        JPasswordField passwordField = new JPasswordField(password, 10); 
        profilePanel.add(passwordField);
    
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newPassword = new String(passwordField.getPassword());
                boolean success = UserDAO.updatePassword(accountName, newPassword);
                if (success) {
                    JOptionPane.showMessageDialog(profilePanel, "Password updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(profilePanel, "Failed to update password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        profilePanel.add(saveButton);
    
        return profilePanel;
    }
    

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout());

        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(10);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterProducts();
            }
        });

        JLabel filterLabel = new JLabel("Filter by:");
        String[] criteria = {"Price"};
        filterCriteria = new JComboBox<>(criteria);
        filterValue = new JTextField(5);
        JButton filterButton = new JButton("Filter");

        filterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterProducts();
            }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(filterLabel);
        searchPanel.add(filterCriteria);
        searchPanel.add(filterValue);
        searchPanel.add(filterButton);

        return searchPanel;
    }

    private void loadProductsFromDatabase(JPanel productPanel) {
        UserDAO userDAO = new UserDAO();
        allProducts = userDAO.getAllProducts();
        displayProducts(allProducts, productPanel);
    }

    private void displayProducts(List<Product> products, JPanel productPanel) {
        productPanel.removeAll();

        for (Product product : products) {
            JPanel cardPanel = new JPanel(new BorderLayout());
            cardPanel.setBorder(BorderFactory.createLineBorder(Color.black));

            
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File(product.getImagePath()));
                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                cardPanel.add(imageLabel, BorderLayout.CENTER);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            
            JLabel nameLabel = new JLabel(product.getName() + " - $" + product.getPrice());
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cardPanel.add(nameLabel, BorderLayout.NORTH);

            // Натискання на картку
            cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    showProductDetails(product.getName(), product.getPrice(), product.getImagePath(), product.getDescription());
                }
            });

            JButton addToCardButton = new JButton("Add to Cart");
            addToCardButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cart.add(product);
                    JOptionPane.showMessageDialog(cardPanel, product.getName() + " added to cart.");
                }
            });
            cardPanel.add(addToCardButton, BorderLayout.PAGE_END);

            productPanel.add(cardPanel);
        }

        productPanel.revalidate();
        productPanel.repaint();
    }

    private void filterProducts() {
        String searchText = searchField.getText().trim().toLowerCase();
        String selectedCriteria = (String) filterCriteria.getSelectedItem();
        String filterText = filterValue.getText().trim();
    
        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> product.getName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    
        if ("Price".equals(selectedCriteria) && !filterText.isEmpty()) {
            try {
                double price = Double.parseDouble(filterText);
                filteredProducts = filteredProducts.stream()
                        .filter(product -> product.getPrice() <= price)
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid price format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
        JScrollPane scrollPane = (JScrollPane) cardsPanel.getComponent(1);
        JPanel productPanel = (JPanel) scrollPane.getViewport().getView(); // Отримуємо вкладену панель з JScrollPane
        displayProducts(filteredProducts, productPanel); // Оновлюємо панель з продуктами
    }
    

    private void updateCartPanel() {
        cartPanel.removeAll();

        JPanel cartContentPanel = new JPanel();
        cartContentPanel.setLayout(new BoxLayout(cartContentPanel, BoxLayout.Y_AXIS));

        for (Product product : cart) {
            JPanel productPanel = new JPanel(new BorderLayout());
            JLabel productLabel = new JLabel(product.toString());
            JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(product.getImagePath()).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            productPanel.add(productLabel, BorderLayout.CENTER);
            productPanel.add(imageLabel, BorderLayout.WEST);
            cartContentPanel.add(productPanel);
        }

        cartPanel.add(new JScrollPane(cartContentPanel), BorderLayout.CENTER);
        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private void showProductDetails(String productName, double productPrice, String imagePath, String descriptions) {
        JOptionPane.showMessageDialog(this,
                "Product Name: " + productName + "\nPrice: $" + productPrice + "\nDescriptions " + descriptions,
                "Product Details",
                JOptionPane.INFORMATION_MESSAGE,
                new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(346, 211, Image.SCALE_SMOOTH)));
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout());

        JButton footerButton = new JButton("Footer Button");
        footerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        footerPanel.add(footerButton);

        return footerPanel;
    }

    private JPopupMenu createPopupMenu(String accountName) {
        JPopupMenu menu = new JPopupMenu();
    
        JMenuItem menu1 = new JMenuItem("element 1");
        JMenuItem menu2 = new JMenuItem("element 2");
        JMenuItem profileMenu = new JMenuItem("Profile");
    
        menu.add(menu1);
        menu.add(menu2);
        menu.add(profileMenu);
    
        menu1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "buttonsPanel");
            }
        });
    
        menu2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "cardsPanel");
            }
        });
    
        profileMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String password = "password"; 
                JPanel profilePanel = createProfilePanel(accountName, password);
                cardPanel.add(profilePanel, "profilePanel");
                cardLayout.show(cardPanel, "profilePanel");
            }
        });
    
        return menu;
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainAppWindow("Account Name"));
    }
}
