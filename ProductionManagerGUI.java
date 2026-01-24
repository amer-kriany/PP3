import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class ProductionManagerGUI extends JFrame {
    private ProductionManager pm;
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> filterCategoryCombo;
    private JComboBox<String> filterProductCombo;
    private JComboBox<String> filterStatusCombo;

    private final Font strongFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 16);

    public ProductionManagerGUI(ProductionManager pm) {
        this.pm = pm;
        initUI();
    }

    private void initUI() {
        setTitle("Production Manager");
        setSize(1100, 600); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel backLabel = new JLabel();
        backLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon")); 
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                new SupervisorSelectionUI(pm).setVisible(true);
            }
        });
        topPanel.add(backLabel, BorderLayout.WEST);

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

        filterStatusCombo = new JComboBox<>();
        filterStatusCombo.setFont(strongFont);
        filterStatusCombo.addItem("All Statuses");
        for (Status.taskStatus s : Status.taskStatus.values())
            filterStatusCombo.addItem(s.name());

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(filterCategoryCombo);
        filterPanel.add(new JLabel("Product:"));
        filterPanel.add(filterProductCombo);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(filterStatusCombo);

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
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== BOTTOM PANEL (تعديل الطلب الثاني هنا) =====
        JPanel bottomPanel = new JPanel(new BorderLayout()); // تغيير الـ Layout لإضافة زر في أقصى اليسار
        
        // زر الطلب الثاني (أقصى اليسار)
        JButton btnProductHistory = createButton("Product Lines History", new Color(52, 73, 94));
        btnProductHistory.addActionListener(e -> new ProductHistoryFrame().setVisible(true));
        
        // الأزرار الأصلية (في الوسط)
        JPanel centerButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnAdd = createButton("Add Task", new Color(46, 204, 113));
        JButton btnDelete = createButton("Delete Task", new Color(231, 76, 60));
        JButton btnRefresh = createButton("Refresh", new Color(52, 152, 219));
        JButton btnReports = createButton("Reports", new Color(155, 89, 182));
        centerButtons.add(btnAdd);
        centerButtons.add(btnDelete);
        centerButtons.add(btnRefresh);
        centerButtons.add(btnReports);

        bottomPanel.add(btnProductHistory, BorderLayout.WEST); // الزر الجديد في اليسار
        bottomPanel.add(centerButtons, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // ===== LISTENERS =====
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshTable(); }
            public void removeUpdate(DocumentEvent e) { refreshTable(); }
            public void changedUpdate(DocumentEvent e) { refreshTable(); }
        });
        filterCategoryCombo.addActionListener(e -> refreshTable());
        filterProductCombo.addActionListener(e -> refreshTable());
        filterStatusCombo.addActionListener(e -> refreshTable());
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

    // --- نافذة الطلب الثاني (Product Lines History) ---
    class ProductHistoryFrame extends JFrame {
        private JComboBox<String> productBox;
        private DefaultListModel<String> listModel;

        public ProductHistoryFrame() {
            setTitle("Lines by Product");
            setSize(400, 400);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));

            JPanel top = new JPanel(new FlowLayout());
            productBox = new JComboBox<>(new String[]{"Laptop", "Phone", "Tablet", "JACKET", "JEANSE", "HOODIE", "tuna", "sardines", "Lanchun"});
            top.add(new JLabel("Select Product:"));
            top.add(productBox);

            listModel = new DefaultListModel<>();
            JList<String> lineList = new JList<>(listModel);
            lineList.setFont(strongFont);

            productBox.addActionListener(e -> updateLines());
            
            add(top, BorderLayout.NORTH);
            add(new JScrollPane(lineList), BorderLayout.CENTER);
            
            updateLines(); // التحديث الأولي
        }

        private void updateLines() {
            listModel.clear();
            String selectedProd = (String) productBox.getSelectedItem();
            Set<String> foundLines = new HashSet<>();

            for (ProductLine pl : pm.getProductLines()) {
                for (Task t : pl.productLineTasks) {
                    if (t.getProduct().toString().equalsIgnoreCase(selectedProd)) {
                        foundLines.add(pl.getLineName() + " (ID: " + pl.getLineId() + ")");
                        break; 
                    }
                }
            }

            if (foundLines.isEmpty()) {
                listModel.addElement("No lines have produced this product.");
            } else {
                for (String line : foundLines) listModel.addElement(line);
            }
        }
    }

    // ميثود الحذف والـ Refresh كما هي في كودك الأصلي
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
        String statusFilter = (String) filterStatusCombo.getSelectedItem();

        for (Task t : allTasks) {
            String name = t.getTaskName().toLowerCase();
            Product prod = t.getProduct();
            String currentStatus = t.getStatus().toString();

            boolean matches = name.contains(search)
                    && (catFilter.equals("All Categories") || isProductInCategory(prod.toString(), catFilter))
                    && (prodFilter.equals("All Products") || prodFilter.equalsIgnoreCase(prod.toString()))
                    && (statusFilter.equals("All Statuses") || statusFilter.equals(currentStatus));

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