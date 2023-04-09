import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen extends JFrame implements ActionListener {

    private JLabel usernameLabel, passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginScreen() {
        // Set window properties
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create UI components
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);
        loginButton = new JButton("Login");

        // Add action listener to the button
        loginButton.addActionListener(this);

        // Create panel for labels and fields
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);

        // Add components to the window
        add(inputPanel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.SOUTH);

        // Show the window
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // Get username and password from input fields
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Validate username and password
        if (username.equals("admin") && password.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose(); // Close the window
            POSGUI pos = new POSGUI();
            pos.createGUI();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
        }
    }

    public static void main(String[] args) {
        // Create an instance of the login screen
        new LoginScreen();
    }
}
