import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class LoginUI extends JFrame {

    // username -> [password, role]
    private HashMap<String, String[]> users = new HashMap<>();

    public LoginUI() {
        setTitle("Company Login");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Predefined users
        users.put("amer", new String[]{"1234", "Manager"});
        users.put("mohammed", new String[]{"pass", "Manager"});
        users.put("fadi", new String[]{"abcd", "Production Supervisor"});
        users.put("alaa", new String[]{"prod123", "Production Supervisor"});

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Title
        JLabel title = new JLabel("Welcome to Production System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(50, 50, 50));
        mainPanel.add(title, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(passwordField, gbc);

        // Role selection
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        String[] roles = {"Manager", "Production Supervisor"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setFont(new Font("Arial", Font.PLAIN, 16));
        formPanel.add(roleBox, gbc);

        // Login button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setEnabled(false); // الزر معطل في البداية
        formPanel.add(loginButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);

        // Document listeners لتمكين الزر تلقائيًا
        usernameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                checkFields(usernameField, passwordField, loginButton);
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                checkFields(usernameField, passwordField, loginButton);
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                checkFields(usernameField, passwordField, loginButton);
            }
        });

        passwordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                checkFields(usernameField, passwordField, loginButton);
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                checkFields(usernameField, passwordField, loginButton);
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                checkFields(usernameField, passwordField, loginButton);
            }
        });

        // Action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String selectedRole = roleBox.getSelectedItem().toString();

            if (users.containsKey(username)) {
                String[] data = users.get(username);

                if (!data[0].equals(password)) {
                    JOptionPane.showMessageDialog(this, "Incorrect password!");
                    return;
                }

                if (!data[1].equals(selectedRole)) {
                    JOptionPane.showMessageDialog(this,
                            "Role mismatch!\nYou are not a " + selectedRole);
                    return;
                }

                JOptionPane.showMessageDialog(this,
                        "Login Successful!\nWelcome " + selectedRole);
            } else {
                JOptionPane.showMessageDialog(this, "User not found!");
            }
        });
    }

    // الميثود لتمكين زر Login عند إدخال النصوص
    private void checkFields(JTextField username, JPasswordField password, JButton loginButton) {
        boolean enable =
                !username.getText().trim().isEmpty() &&
                password.getPassword().length > 0;

        loginButton.setEnabled(enable);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new LoginUI();
    }
}
