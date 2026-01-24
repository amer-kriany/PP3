import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductionManagerGUI extends JFrame {
    private ProductionManager pm;
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> filterCategoryCombo;
    private JComboBox<String> filterProductCombo;

    private final Font strongFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 16);

    public ProductionManagerGUI(ProductionManager pm) {
        this.pm = pm;
        initUI();
    }

    private void initUI() {
        setTitle("Production Manager");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== TOP PANEL (BACK ARROW + FILTERS) =====
        JPanel topPanel = new JPanel(new BorderLayout());

        // Back arrow
        JLabel backLabel = new JLabel();
backLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon")); // مؤقت، نقدر نحط أي صورة سهم
backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
backLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent e) {
        dispose();
        new SupervisorSelectionUI(pm).setVisible(true);
    }
});
        backLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        backLabel.setForeground(Color.BLUE);
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new SupervisorSelectionUI(pm).setVisible(true);
            }
        });
        topPanel.add(backLabel, BorderLayout.WEST);

        // Search & Filters
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchField = new JTextField(10);
        searchField.setFont(strongFont);

        filterCategoryCombo = new JComboBox<>();
        filterCategoryCombo.setFont(strongFont);
        filterCategoryCombo.addItem("All Categories");
        for (Item.Categories cat : Item.Categories.values())
            filterCategoryCombo.addItem(cat.name());

        filterProductCombo = new JComboBox<>();
        filterProductCombo.setFont(strongFont);
        filterProductCombo.addItem("All Products");
        String[] prods = { "Laptop", "Phone", "Tablet", "JACKET", "JEANSE", "HOODIE", "tuna", "sardines", "Lanchun" };
        for (String p : prods) filterProductCombo.addItem(p);

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(filterCategoryCombo);
        filterPanel.add(new JLabel("Product:"));
        filterPanel.add(filterProductCombo);

        topPanel.add(filterPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        String[] columns = { "ID", "Task Name", "Client", "Product", "Quantity", "Status", "Production Line" };
        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(headerFont);
        table.setFont(strongFont);

        // Status column coloring
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    String s = value.toString();
                    if (s.equals("COMPLETED")) c.setForeground(new Color(0, 150, 0));
                    else if (s.equals("CANCELED")) c.setForeground(Color.RED);
                    else if (s.equals("IN_PROGRESS")) c.setForeground(Color.BLUE);
                    else c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== BOTTOM PANEL (BUTTONS) =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnAdd = createButton("Add Task", new Color(46, 204, 113));
        JButton btnDelete = createButton("Delete Task", new Color(231, 76, 60));
        JButton btnRefresh = createButton("Refresh", new Color(52, 152, 219));
        JButton btnReports = createButton("Reports", new Color(155, 89, 182));
        bottomPanel.add(btnAdd);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnReports);
        add(bottomPanel, BorderLayout.SOUTH);

        // ===== LISTENERS =====
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshTable(); }
            public void removeUpdate(DocumentEvent e) { refreshTable(); }
            public void changedUpdate(DocumentEvent e) { refreshTable(); }
        });
        filterCategoryCombo.addActionListener(e -> refreshTable());
        filterProductCombo.addActionListener(e -> refreshTable());

        btnRefresh.addActionListener(e -> refreshTable());
        btnAdd.addActionListener(e -> new AddTaskDialog(this).setVisible(true));
        btnDelete.addActionListener(e -> deleteSelectedTask());
        btnReports.addActionListener(e -> new ProductReportGUI(pm).setVisible(true));

        refreshTable();
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(strongFont);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void deleteSelectedTask() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int taskId = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete Task ID: " + taskId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                for (ProductLine pl : pm.getProductLines()) pl.productLineTasks.removeIf(t -> t.taskID == taskId);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a task first!");
        }
    }

    public void refreshTable() {
        model.setRowCount(0);
        List<Task> allTasks = new ArrayList<>();
        for (ProductLine pl : pm.getProductLines()) synchronized (pl.productLineTasks) { allTasks.addAll(pl.productLineTasks); }
        allTasks.sort(Comparator.comparingInt(t -> t.taskID));

        String search = searchField.getText().toLowerCase();
        String catFilter = (String) filterCategoryCombo.getSelectedItem();
        String prodFilter = (String) filterProductCombo.getSelectedItem();

        for (Task t : allTasks) {
            String name = t.getTaskName().toLowerCase();
            Product prod = t.getProduct();
            boolean matches = name.contains(search)
                    && (catFilter.equals("All Categories") || isProductInCategory(prod.toString(), catFilter))
                    && (prodFilter.equals("All Products") || prodFilter.equalsIgnoreCase(prod.toString()));
            if (matches) {
                String lineName = "N/A";
                for (ProductLine pl : pm.getProductLines()) if (pl.productLineTasks.contains(t)) { lineName = pl.getLineName(); break; }
                model.addRow(new Object[] { t.taskID, t.getTaskName(), t.getClientName(), prod, t.getQuantity(), t.getStatus(), lineName });
            }
        }
    }

    private boolean isProductInCategory(String productName, String category) {
        if (productName == null) return false;
        String p = productName.toUpperCase();
        if (category.equals("CLOTHES")) return p.matches("JACKET|JEANSE|HOODIE");
        if (category.equals("TECHNOLOGY")) return p.matches("LAPTOP|PHONE|TABLET");
        if (category.equals("CANNED_FOOD")) return p.matches("TUNA|SARDINES|LANCHUN");
        return false;
    }

    // ----- AddTaskDialog (مصغرة) -----
    class AddTaskDialog extends JDialog {
        public AddTaskDialog(JFrame parent) {
            super(parent, "Add Task", true);
            setSize(400, 300);
            setLocationRelativeTo(parent);
            setLayout(new GridLayout(5, 2, 10, 10));
            add(new JLabel("Task Name:")); add(new JTextField());
            add(new JLabel("Client:")); add(new JTextField());
            add(new JLabel("Product:")); add(new JComboBox<>(new String[]{"Laptop","Phone","Tablet"}));
            add(new JLabel("Quantity:")); add(new JTextField());
            JButton btnSave = new JButton("Save");
            add(btnSave); JButton btnCancel = new JButton("Cancel"); add(btnCancel);
            btnCancel.addActionListener(e -> dispose());
        }
    }
}
