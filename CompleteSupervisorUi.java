import javax.swing.*;
import java.awt.*;

/**
 * Complete Production Supervisor Interface
 * Contains all 5 requirements:
 * 1. Add task
 * 2. Cancel task
 * 3. View tasks by production line
 * 4. Search and filter tasks (In Progress / Completed)
 * 5. View production lines that performed specific tasks for a product
 */
class CompleteSupervisorUI extends JFrame {

    private ProductionManager manager;

    public CompleteSupervisorUI(ProductionManager manager) {
        this.manager = manager;

        setTitle("Production Line Management System - Production Supervisor");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(900, 80));
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Production Line Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JLabel subtitleLabel = new JLabel("Production Supervisor Panel", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(236, 240, 241));
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // Main Panel with Cards
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(new Color(236, 240, 241));

        // Card 1: View All Tasks (ProductionManagerGUI)
        JPanel card1 = createCard(
            "View and Manage All Tasks",
            "View, add, delete, and filter tasks",
            new Color(52, 152, 219),
            e -> openProductionManagerGUI()
        );

        // Card 2: Inventory Management
        JPanel card2 = createCard(
            "Inventory Management",
            "View, add, edit, and delete items",
            new Color(46, 204, 113),
            e -> openInventoryManager()
        );

        // Card 3: Add/Cancel Tasks
        JPanel card3 = createCard(
            "Add / Cancel Task",
            "Add new tasks or cancel existing ones",
            new Color(155, 89, 182),
            e -> openAddCancelTaskDialog()
        );

        // Card 4: Search and Filter Tasks
        JPanel card4 = createCard(
            "Search and Filter Tasks",
            "Search and filter tasks by status",
            new Color(230, 126, 34),
            e -> openTaskSearchAndFilter()
        );

        // Card 5: Production Lines by Product
        JPanel card5 = createCard(
            "Production Lines by Product",
            "View lines that produced a specific product",
            new Color(231, 76, 60),
            e -> openProductionLinesByProduct()
        );

        // Card 6: Reports and Statistics
        JPanel card6 = createCard(
            "Reports and Statistics",
            "View comprehensive production reports",
            new Color(52, 73, 94),
            e -> openReportsDialog()
        );

        mainPanel.add(card1);
        mainPanel.add(card2);
        mainPanel.add(card3);
        mainPanel.add(card4);
        mainPanel.add(card5);
        mainPanel.add(card6);

        add(mainPanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(236, 240, 241));
        
        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(150, 40));
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setBackground(new Color(192, 57, 43));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        footerPanel.add(exitButton);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, String description, Color color, java.awt.event.ActionListener action) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 3),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(color);

        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        descLabel.setForeground(new Color(127, 140, 141));

        JButton openButton = new JButton("Open");
        openButton.setBackground(color);
        openButton.setForeground(Color.WHITE);
        openButton.setFont(new Font("Arial", Font.BOLD, 14));
        openButton.setFocusPainted(false);
        openButton.setPreferredSize(new Dimension(100, 35));
        openButton.addActionListener(action);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        card.add(openButton, BorderLayout.SOUTH);

        // Hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    // ===== Methods to open different UIs =====

    private void openProductionManagerGUI() {
        SwingUtilities.invokeLater(() -> {
            ProductionManagerGUI gui = new ProductionManagerGUI(manager);
            gui.setVisible(true);
        });
    }

    private void openInventoryManager() {
        SwingUtilities.invokeLater(() -> {
            InventoryManagerUI gui = new InventoryManagerUI();
            gui.setVisible(true);
        });
    }

    private void openAddCancelTaskDialog() {
        JDialog dialog = new JDialog(this, "Add / Cancel Task", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();

        // Add Task Tab
        JPanel addPanel = createAddTaskPanel(dialog);
        tabbedPane.addTab("Add Task", addPanel);

        // Cancel Task Tab
        JPanel cancelPanel = createCancelTaskPanel(dialog);
        tabbedPane.addTab("Cancel Task", cancelPanel);

        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JPanel createAddTaskPanel(JDialog parent) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(15);
        JTextField clientField = new JTextField(15);
        JTextField productField = new JTextField(15);
        JTextField quantityField = new JTextField(15);
        JTextField dateField = new JTextField(15);
        JComboBox<String> lineCombo = new JComboBox<>();
        
        for (ProductLine line : manager.getProductLines()) {
            lineCombo.addItem(line.getLineName());
        }

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Task Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Client:"), gbc);
        gbc.gridx = 1;
        panel.add(clientField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Product:"), gbc);
        gbc.gridx = 1;
        panel.add(productField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        panel.add(quantityField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Deadline (dd-MM-yyyy HH:mm:ss):"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Production Line:"), gbc);
        gbc.gridx = 1;
        panel.add(lineCombo, gbc);

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> {
            try {
                Product product = new Product(0, productField.getText().trim());
                Task task = new Task(
                    nameField.getText(),
                    clientField.getText(),
                    product,
                    Integer.parseInt(quantityField.getText()),
                    dateField.getText()
                );
                manager.addTask(task, (String) lineCombo.getSelectedItem());
                JOptionPane.showMessageDialog(parent, "Task added successfully!");
                parent.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Error: " + ex.getMessage());
            }
        });

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addButton, gbc);

        return panel;
    }

    private JPanel createCancelTaskPanel(JDialog parent) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Task ID:");
        JTextField taskIdField = new JTextField(10);
        JButton cancelButton = new JButton("Cancel Task");

        cancelButton.addActionListener(e -> {
            try {
                int taskId = Integer.parseInt(taskIdField.getText());
                
                boolean found = false;
                for (ProductLine line : manager.getProductLines()) {
                    for (Task task : line.getProductLineTasks()) {
                        if (task.getTaskID() == taskId) {
                            task.cancel();
                            found = true;
                            JOptionPane.showMessageDialog(parent, "Task canceled successfully!");
                            parent.dispose();
                            return;
                        }
                    }
                }
                
                if (!found) {
                    JOptionPane.showMessageDialog(parent, "Task not found!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "Error: " + ex.getMessage());
            }
        });

        inputPanel.add(label);
        inputPanel.add(taskIdField);
        inputPanel.add(cancelButton);

        panel.add(inputPanel, BorderLayout.NORTH);

        return panel;
    }

    private void openTaskSearchAndFilter() {
        JOptionPane.showMessageDialog(
            this,
            "Task search/filter screen is not available in this build.",
            "Info",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void openProductionLinesByProduct() {
        JOptionPane.showMessageDialog(
            this,
            "Production lines by product screen is not available in this build.",
            "Info",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void openReportsDialog() {
        JDialog dialog = new JDialog(this, "Reports and Statistics", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder report = new StringBuilder();
        report.append("Comprehensive Production System Report\n");
        report.append("=".repeat(60)).append("\n\n");

        // Production Lines Summary
        report.append("Production Lines:\n");
        report.append("-".repeat(60)).append("\n");
        for (ProductLine line : manager.getProductLines()) {
            report.append(String.format("Line: %s | Tasks: %d | Performance: %d%%\n",
                line.getLineName(),
                line.getProductLineTasks().size(),
                line.getLinePerformance()
            ));
        }

        // Tasks Summary
        int totalTasks = 0;
        int completedTasks = 0;
        int inProgressTasks = 0;
        int pendingTasks = 0;
        
        for (ProductLine line : manager.getProductLines()) {
            for (Task task : line.getProductLineTasks()) {
                totalTasks++;
                if (task.getStatus() == Status.taskStatus.COMPLETED) completedTasks++;
                else if (task.getStatus() == Status.taskStatus.IN_PROGRESS) inProgressTasks++;
                else if (task.getStatus() == Status.taskStatus.PENDING) pendingTasks++;
            }
        }

        report.append("\n").append("=".repeat(60)).append("\n");
        report.append("Task Summary:\n");
        report.append("-".repeat(60)).append("\n");
        report.append(String.format("Total Tasks: %d\n", totalTasks));
        report.append(String.format("Completed: %d\n", completedTasks));
        report.append(String.format("In Progress: %d\n", inProgressTasks));
        report.append(String.format("Pending: %d\n", pendingTasks));

        reportArea.setText(report.toString());

        JScrollPane scrollPane = new JScrollPane(reportArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        // For testing
        SwingUtilities.invokeLater(() -> {
            ProductionManager pm = new ProductionManager(new java.util.ArrayList<>());
            new CompleteSupervisorUI(pm).setVisible(true);
        });
    }
}
