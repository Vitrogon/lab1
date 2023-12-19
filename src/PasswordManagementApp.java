import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class PasswordManagementApp extends JFrame {

    private JTextArea passwordText;
    private Map<String, Map<String, String>> data = new HashMap<>();

    public PasswordManagementApp() {
        setTitle("Password Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JButton generateButton = new JButton("Generate Password");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePasswordButton();
            }
        });
        add(generateButton);

        JButton storeButton = new JButton("Store Password");
        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                storePasswordButton();
            }
        });
        add(storeButton);

        JButton deleteButton = new JButton("Delete Password");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePasswordButton();
            }
        });
        add(deleteButton);

        JButton retrieveButton = new JButton("Retrieve Password");
        retrieveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displaySavedResources();
            }
        });
        add(retrieveButton);

        passwordText = new JTextArea(3, 30);
        passwordText.setEditable(false);
        add(passwordText);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(exitButton);

        setLayout(new FlowLayout());
    }

    private String generatePassword(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_-+=<>?";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    private void storePassword(String resource, String username, String password) {
        data.computeIfAbsent(resource, k -> new HashMap<>()).put(username, password);
        JOptionPane.showMessageDialog(this, "Password stored successfully.");
    }

    private void deletePassword(String resource, String username) {
        Map<String, String> resourceData = data.get(resource);
        if (resourceData != null) {
            resourceData.remove(username);
            if (resourceData.isEmpty()) {
                data.remove(resource);
            }
            JOptionPane.showMessageDialog(this, "Password deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Password not found.");
        }
    }

    private void displaySavedResources() {
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No passwords found.");
            return;
        }

        JFrame resourceWindow = new JFrame("Saved Resources");
        resourceWindow.setLayout(new FlowLayout());

        for (String resource : data.keySet()) {
            JButton resourceButton = new JButton(resource);
            resourceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    displaySavedLogins(resource);
                }
            });
            resourceWindow.add(resourceButton);
        }

        resourceWindow.setSize(300, 200);
        resourceWindow.setVisible(true);
    }

    private void displaySavedLogins(String resource) {
        Map<String, String> resourceData = data.get(resource);

        JFrame loginWindow = new JFrame("Logins for " + resource);
        loginWindow.setLayout(new FlowLayout());

        for (String username : resourceData.keySet()) {
            JButton loginButton = new JButton(username);
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    displayPassword(resource, username);
                }
            });
            loginWindow.add(loginButton);
        }

        loginWindow.setSize(300, 200);
        loginWindow.setVisible(true);
    }

    private void displayPassword(String resource, String username) {
        String storedPassword = data.get(resource).get(username);

        JFrame passwordWindow = new JFrame("Password for " + username + " in " + resource);
        passwordWindow.setLayout(new FlowLayout());

        JPasswordField passwordField = new JPasswordField(storedPassword);
        passwordField.setEditable(false);
        passwordWindow.add(passwordField);

        JButton showButton = new JButton("Show");
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField.setEchoChar((char) 0);
            }
        });
        passwordWindow.add(showButton);

        passwordWindow.setSize(300, 200);
        passwordWindow.setVisible(true);
    }

    private void generatePasswordButton() {
        int length = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the password length:"));
        if (length > 0) {
            String password = generatePassword(length);
            passwordText.setText(password);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid password length.");
        }
    }

    private void storePasswordButton() {
        String resource = JOptionPane.showInputDialog(this, "Enter the resource name:");
        String username = JOptionPane.showInputDialog(this, "Enter a username:");
        String password = JOptionPane.showInputDialog(this, "Enter the password:");

        if (resource != null && username != null && password != null) {
            storePassword(resource, username, password);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void deletePasswordButton() {
        String resource = JOptionPane.showInputDialog(this, "Enter the resource name:");
        String username = JOptionPane.showInputDialog(this, "Enter the username:");

        if (resource != null && username != null) {
            deletePassword(resource, username);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PasswordManagementApp().setVisible(true);
            }
        });
    }
}