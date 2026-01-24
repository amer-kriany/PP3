import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TaskSearchAndFilterUI extends JFrame {

    private ProductionManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JComboBox<String> statusFilter;
    private JComboBox<String> lineFilter;
    private JComboBox<String> productFilter;
    private JTextField searchField;

    public TaskSearchAndFilterUI(ProductionManager manager) {
        this.manager = manager;

        setTitle("Task Search and Filter - Production Supervisor");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search and Filter Options"));
        filterPanel.setBackground(new Color(245, 245, 245));

        // Search field
        searchField = new JTextField(15);
        searchField.setToolTipText("Search by task name or client...");
        
        // Status filter
        statusFilter = new JComboBox<>(new String[] { 
            "All Tasks", 
            "In Progress", 
            "Completed",
            "Pending",
            "Canceled"
        });

        // Line filter
        lineFilter = new JComboBox<>();
        lineFilter.addItem("All Lines");
        for (ProductLine line : manager.getProductLines()) {
            lineFilter.addItem(line.getLineName());
        }

        // Product filter
        productFilter = new JComboBox<>();
        productFilter.addItem("All Products");
        String[] products = {"Laptop", "Phone", "Tablet", "HOODIE", "JEANSE", "JACKET", "tuna", "sardines", "Lanchun"};
        for (String prod : products) {
            productFilter.addItem(prod);
        }

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("| Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(new JLabel("| Production Line:"));
        filterPanel.add(lineFilter);
        filterPanel.add(new JLabel("| Product:"));
        filterPanel.add(productFilter);

        add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {
            "Task ID", 
            "Task Name", 
            "Client", 
            "Product", 
            "Quantity", 
            "Progress %", 
            "Status", 
            "Production Line",
            "Start Date",
            "Deadline"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Color coding for status
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String status = value.toString();
                    if (status.equals("COMPLETED")) {
                        c.setForeground(new Color(0, 150, 0));
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else if (status.equals("IN_PROGRESS")) {
                        c.setForeground(new Color(0, 100, 200));
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else if (status.equals("CANCELED")) {
                        c.setForeground(Color.RED);
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else if (status.equals("PENDING")) {
                        c.setForeground(new Color(200, 150, 0));
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    }
                }
                
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }
                
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel - Buttons and Statistics
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Statistics Panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        JLabel statsLabel = new JLabel();
        statsPanel.add(statsLabel);
        bottomPanel.add(statsPanel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton refreshButton = new JButton("Refresh");
        JButton clearFiltersButton = new JButton("Clear Filters");
        JButton exportButton = new JButton("Export Results");

        refreshButton.setPreferredSize(new Dimension(120, 35));
        clearFiltersButton.setPreferredSize(new Dimension(140, 35));
        exportButton.setPreferredSize(new Dimension(140, 35));

        buttonPanel.add(refreshButton);
        buttonPanel.add(clearFiltersButton);
        buttonPanel.add(exportButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Event Listeners
        searchField.addActionListener(e -> refreshTable(statsLabel));
        statusFilter.addActionListener(e -> refreshTable(statsLabel));
        lineFilter.addActionListener(e -> refreshTable(statsLabel));
        productFilter.addActionListener(e -> refreshTable(statsLabel));
        
        refreshButton.addActionListener(e -> refreshTable(statsLabel));
        
        clearFiltersButton.addActionListener(e -> {
            searchField.setText("");
            statusFilter.setSelectedIndex(0);
            lineFilter.setSelectedIndex(0);
            productFilter.setSelectedIndex(0);
            refreshTable(statsLabel);
        });

        exportButton.addActionListener(e -> exportResults());

        // Timer for auto-refresh every 2 seconds
        Timer timer = new Timer(2000, e -> refreshTable(statsLabel));
        timer.start();

        // Initial load
        refreshTable(statsLabel);
    }

    private void refreshTable(JLabel statsLabel) {
        tableModel.setRowCount(0);

        String searchText = searchField.getText().trim().toLowerCase();
        String selectedStatus = (String) statusFilter.getSelectedItem();
        String selectedLine = (String) lineFilter.getSelectedItem();
        String selectedProduct = (String) productFilter.getSelectedItem();

        List<Task> allTasks = new ArrayList<>();
        
        // Collect all tasks from all production lines
        for (ProductLine line : manager.getProductLines()) {
            synchronized (line.getProductLineTasks()) {
                allTasks.addAll(line.getProductLineTasks());
            }
        }

        int totalTasks = 0;
        int inProgressCount = 0;
        int completedCount = 0;
        int pendingCount = 0;
        int canceledCount = 0;

        for (Task task : allTasks) {
            // Apply filters
            boolean matchesSearch = searchText.isEmpty() || 
                task.getTaskName().toLowerCase().contains(searchText) ||
                task.getClientName().toLowerCase().contains(searchText);

            boolean matchesStatus = selectedStatus.equals("All Tasks") ||
                (selectedStatus.equals("In Progress") && task.getStatus() == Status.taskStatus.IN_PROGRESS) ||
                (selectedStatus.equals("Completed") && task.getStatus() == Status.taskStatus.COMPLETED) ||
                (selectedStatus.equals("Pending") && task.getStatus() == Status.taskStatus.PENDING) ||
                (selectedStatus.equals("Canceled") && task.getStatus() == Status.taskStatus.CANCELED);

            String taskLineName = getLineNameForTask(task);
            boolean matchesLine = selectedLine.equals("All Lines") || 
                taskLineName.equals(selectedLine);

            boolean matchesProduct = selectedProduct.equals("All Products") ||
                task.getDesireProduct().equalsIgnoreCase(selectedProduct);

            if (matchesSearch && matchesStatus && matchesLine && matchesProduct) {
                Object[] row = {
                    task.getTaskID(),
                    task.getTaskName(),
                    task.getClientName(),
                    task.getDesireProduct(),
                    task.getQuantity(),
                    task.getProductionProgress() + "%",
                    task.getStatus(),
                    taskLineName,
                    task.getStartAppointment() != null ? task.getStartAppointment().toString() : "N/A",
                    task.getDeadLine() != null ? task.getDeadLine().toString() : "N/A"
                };
                tableModel.addRow(row);
                totalTasks++;

                // Count statistics
                switch (task.getStatus()) {
                    case IN_PROGRESS: inProgressCount++; break;
                    case COMPLETED: completedCount++; break;
                    case PENDING: pendingCount++; break;
                    case CANCELED: canceledCount++; break;
                }
            }
        }

        // Update statistics
        statsLabel.setText(String.format(
            "Total Tasks: %d | In Progress: %d | Completed: %d | Pending: %d | Canceled: %d",
            totalTasks, inProgressCount, completedCount, pendingCount, canceledCount
        ));
    }

    private String getLineNameForTask(Task task) {
        for (ProductLine line : manager.getProductLines()) {
            if (line.getProductLineTasks().contains(task)) {
                return line.getLineName();
            }
        }
        return "N/A";
    }

    private void exportResults() {
        try {
            java.io.FileWriter writer = new java.io.FileWriter("task_search_results.txt");
            writer.write("Task Search and Filter Results\n");
            writer.write("=".repeat(80) + "\n\n");

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.write(tableModel.getColumnName(j) + ": " + tableModel.getValueAt(i, j) + "\n");
                }
                writer.write("-".repeat(80) + "\n");
            }

            writer.close();
            JOptionPane.showMessageDialog(this, 
                "Results exported successfully to:\ntask_search_results.txt",
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error exporting results: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            FileManager.logError("TaskSearchUI | Error exporting results: " + e.getMessage());
        }
    }
}