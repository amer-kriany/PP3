import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ProductionLinesByProductUI extends JFrame {

    private ProductionManager manager;
    private JTable linesTable;
    private DefaultTableModel linesTableModel;
    private JTable tasksTable;
    private DefaultTableModel tasksTableModel;
    
    private JComboBox<String> productCombo;
    private JTextField taskIdsField;
    private JCheckBox specificTasksCheckbox;
    private JLabel resultLabel;

    public ProductionLinesByProductUI(ProductionManager manager) {
        this.manager = manager;

        setTitle("Production Lines by Product - Production Supervisor");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== Top Panel - Selection =====
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Product and Task Selection"));
        selectionPanel.setBackground(new Color(245, 245, 245));

        // Product selection
        JPanel productPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        productPanel.setBackground(new Color(245, 245, 245));
        
        productCombo = new JComboBox<>();
        productCombo.addItem("-- Select Product --");
        
        // Get all unique products from tasks
        Set<String> uniqueProducts = new TreeSet<>();
        for (ProductLine line : manager.getProductLines()) {
            for (Task task : line.getProductLineTasks()) {
                uniqueProducts.add(task.getDesireProduct());
            }
        }
        for (String product : uniqueProducts) {
            productCombo.addItem(product);
        }

        productPanel.add(new JLabel("Product:"));
        productPanel.add(productCombo);

        // Task IDs selection
        JPanel tasksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tasksPanel.setBackground(new Color(245, 245, 245));
        
        specificTasksCheckbox = new JCheckBox("Filter by specific tasks only");
        taskIdsField = new JTextField(30);
        taskIdsField.setEnabled(false);
        taskIdsField.setToolTipText("Enter task IDs separated by commas (e.g., 1,3,5)");

        tasksPanel.add(specificTasksCheckbox);
        tasksPanel.add(new JLabel("Task IDs:"));
        tasksPanel.add(taskIdsField);

        selectionPanel.add(productPanel);
        selectionPanel.add(tasksPanel);

        add(selectionPanel, BorderLayout.NORTH);

        // ===== Center Panel - Split View =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(8);

        // Top part - Production Lines Table
        JPanel linesPanel = new JPanel(new BorderLayout());
        linesPanel.setBorder(BorderFactory.createTitledBorder("Production Lines"));

        String[] linesColumns = {
            "Line ID",
            "Line Name",
            "State",
            "Tasks for Product",
            "Total Quantity",
            "Completed Tasks",
            "Performance %"
        };
        
        linesTableModel = new DefaultTableModel(linesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        linesTable = new JTable(linesTableModel);
        linesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        linesTable.setRowHeight(28);
        linesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Highlight active/inactive lines
        linesTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String state = value.toString();
                    if (state.equals("ACTIVE")) {
                        c.setForeground(new Color(0, 150, 0));
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else if (state.equals("STOP")) {
                        c.setForeground(Color.RED);
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setForeground(new Color(200, 100, 0));
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    }
                }
                
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }
                
                return c;
            }
        });

        JScrollPane linesScroll = new JScrollPane(linesTable);
        linesPanel.add(linesScroll, BorderLayout.CENTER);

        resultLabel = new JLabel("No search performed yet");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setForeground(new Color(0, 100, 200));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        linesPanel.add(resultLabel, BorderLayout.SOUTH);

        // Bottom part - Tasks Details Table
        JPanel tasksDetailsPanel = new JPanel(new BorderLayout());
        tasksDetailsPanel.setBorder(BorderFactory.createTitledBorder("Task Details"));

        String[] tasksColumns = {
            "Task ID",
            "Task Name",
            "Client",
            "Quantity",
            "Progress %",
            "Status",
            "Production Line",
            "Start Date"
        };
        
        tasksTableModel = new DefaultTableModel(tasksColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tasksTable = new JTable(tasksTableModel);
        tasksTable.setFont(new Font("Arial", Font.PLAIN, 13));
        tasksTable.setRowHeight(26);

        // Color code task status
        tasksTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
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
                    } else if (status.equals("CANCELED")) {
                        c.setForeground(Color.RED);
                    } else {
                        c.setForeground(new Color(200, 150, 0));
                    }
                }
                
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }
                
                return c;
            }
        });

        JScrollPane tasksScroll = new JScrollPane(tasksTable);
        tasksDetailsPanel.add(tasksScroll, BorderLayout.CENTER);

        splitPane.setTopComponent(linesPanel);
        splitPane.setBottomComponent(tasksDetailsPanel);

        add(splitPane, BorderLayout.CENTER);

        // ===== Bottom Panel - Buttons =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton searchButton = new JButton("Search");
        JButton clearButton = new JButton("Clear");
        JButton exportButton = new JButton("Export");
        JButton refreshButton = new JButton("Refresh");

        searchButton.setPreferredSize(new Dimension(120, 35));
        clearButton.setPreferredSize(new Dimension(120, 35));
        exportButton.setPreferredSize(new Dimension(120, 35));
        refreshButton.setPreferredSize(new Dimension(120, 35));

        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // ===== Event Listeners =====
        specificTasksCheckbox.addActionListener(e -> {
            taskIdsField.setEnabled(specificTasksCheckbox.isSelected());
            if (!specificTasksCheckbox.isSelected()) {
                taskIdsField.setText("");
            }
        });

        searchButton.addActionListener(e -> performSearch());
        
        clearButton.addActionListener(e -> {
            productCombo.setSelectedIndex(0);
            specificTasksCheckbox.setSelected(false);
            taskIdsField.setEnabled(false);
            taskIdsField.setText("");
            linesTableModel.setRowCount(0);
            tasksTableModel.setRowCount(0);
            resultLabel.setText("No search performed yet");
        });

        exportButton.addActionListener(e -> exportResults());
        
        refreshButton.addActionListener(e -> {
            if (productCombo.getSelectedIndex() > 0) {
                performSearch();
            }
        });

        linesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showTasksForSelectedLine();
            }
        });
    }

    private void performSearch() {
        linesTableModel.setRowCount(0);
        tasksTableModel.setRowCount(0);

        String selectedProduct = (String) productCombo.getSelectedItem();
        
        if (selectedProduct == null || selectedProduct.equals("-- Select Product --")) {
            JOptionPane.showMessageDialog(this, 
                "Please select a product first!",
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Integer> taskIds = new ArrayList<>();
        
        // Parse task IDs if specified
        if (specificTasksCheckbox.isSelected()) {
            String taskIdsText = taskIdsField.getText().trim();
            if (taskIdsText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter task IDs!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String[] ids = taskIdsText.split(",");
                for (String id : ids) {
                    taskIds.add(Integer.parseInt(id.trim()));
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Invalid task ID format! Use commas to separate IDs.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int totalLinesFound = 0;
        int totalTasksFound = 0;

        for (ProductLine line : manager.getProductLines()) {
            List<Task> matchingTasks = new ArrayList<>();
            
            for (Task task : line.getProductLineTasks()) {
                if (!task.getDesireProduct().equalsIgnoreCase(selectedProduct)) {
                    continue;
                }

                // If specific tasks are selected, check if this task is in the list
                if (specificTasksCheckbox.isSelected()) {
                    if (!taskIds.contains(task.getTaskID())) {
                        continue;
                    }
                }

                matchingTasks.add(task);
            }

            if (!matchingTasks.isEmpty()) {
                totalLinesFound++;
                totalTasksFound += matchingTasks.size();

                int totalQuantity = 0;
                int completedTasks = 0;
                
                for (Task task : matchingTasks) {
                    totalQuantity += task.getQuantity();
                    if (task.getStatus() == Status.taskStatus.COMPLETED) {
                        completedTasks++;
                    }
                }

                int performance = line.getLinePerformance();

                Object[] row = {
                    line.getLineId(),
                    line.getLineName(),
                    getLineState(line),
                    matchingTasks.size(),
                    totalQuantity,
                    completedTasks,
                    performance + "%"
                };
                
                linesTableModel.addRow(row);
            }
        }

        if (totalLinesFound == 0) {
            resultLabel.setText("No production lines found for the selected product");
            resultLabel.setForeground(Color.RED);
        } else {
            resultLabel.setText(String.format(
                "Found %d production line(s) | Total tasks: %d",
                totalLinesFound, totalTasksFound
            ));
            resultLabel.setForeground(new Color(0, 150, 0));
        }
    }

    private String getLineState(ProductLine line) {
        try {
            java.lang.reflect.Field stateField = ProductLine.class.getDeclaredField("state");
            stateField.setAccessible(true);
            return stateField.get(line).toString();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    private void showTasksForSelectedLine() {
        tasksTableModel.setRowCount(0);

        int selectedRow = linesTable.getSelectedRow();
        if (selectedRow == -1) return;

        int lineId = (int) linesTableModel.getValueAt(selectedRow, 0);
        String selectedProduct = (String) productCombo.getSelectedItem();

        ProductLine selectedLine = null;
        for (ProductLine line : manager.getProductLines()) {
            if (line.getLineId() == lineId) {
                selectedLine = line;
                break;
            }
        }

        if (selectedLine == null) return;

        List<Integer> taskIds = new ArrayList<>();
        if (specificTasksCheckbox.isSelected()) {
            String taskIdsText = taskIdsField.getText().trim();
            if (!taskIdsText.isEmpty()) {
                String[] ids = taskIdsText.split(",");
                for (String id : ids) {
                    try {
                        taskIds.add(Integer.parseInt(id.trim()));
                    } catch (NumberFormatException e) {
                        // Skip invalid IDs
                    }
                }
            }
        }

        for (Task task : selectedLine.getProductLineTasks()) {
            if (!task.getDesireProduct().equalsIgnoreCase(selectedProduct)) {
                continue;
            }

            if (specificTasksCheckbox.isSelected() && !taskIds.contains(task.getTaskID())) {
                continue;
            }

            Object[] row = {
                task.getTaskID(),
                task.getTaskName(),
                task.getClientName(),
                task.getQuantity(),
                task.getProductionProgress() + "%",
                task.getStatus(),
                selectedLine.getLineName(),
                task.getStartAppointment() != null ? task.getStartAppointment().toString() : "N/A"
            };
            
            tasksTableModel.addRow(row);
        }
    }

    private void exportResults() {
        if (linesTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No results to export! Search first.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            java.io.FileWriter writer = new java.io.FileWriter("production_lines_by_product.txt");
            
            String selectedProduct = (String) productCombo.getSelectedItem();
            writer.write("Production Lines by Product Report\n");
            writer.write("Product: " + selectedProduct + "\n");
            writer.write("=".repeat(100) + "\n\n");

            writer.write("Production Lines:\n");
            writer.write("-".repeat(100) + "\n");
            for (int i = 0; i < linesTableModel.getRowCount(); i++) {
                for (int j = 0; j < linesTableModel.getColumnCount(); j++) {
                    writer.write(linesTableModel.getColumnName(j) + ": " + 
                               linesTableModel.getValueAt(i, j) + " | ");
                }
                writer.write("\n");
            }

            writer.write("\n" + "=".repeat(100) + "\n");
            writer.write("Task Details:\n");
            writer.write("-".repeat(100) + "\n");
            
            for (ProductLine line : manager.getProductLines()) {
                for (Task task : line.getProductLineTasks()) {
                    if (task.getDesireProduct().equalsIgnoreCase(selectedProduct)) {
                        writer.write(String.format(
                            "Task #%d | %s | Client: %s | Quantity: %d | Status: %s | Line: %s\n",
                            task.getTaskID(),
                            task.getTaskName(),
                            task.getClientName(),
                            task.getQuantity(),
                            task.getStatus(),
                            line.getLineName()
                        ));
                    }
                }
            }

            writer.close();
            
            JOptionPane.showMessageDialog(this,
                "Results exported successfully to:\nproduction_lines_by_product.txt",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Export error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ProductionLinesByProductUI | Error exporting: " + e.getMessage());
        }
    }
}