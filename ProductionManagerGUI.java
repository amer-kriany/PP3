import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class ProductionManagerGUI extends JFrame {
    private ProductionManager pm;
    private JTable table;
    private DefaultTableModel model;
    
    private JTextField searchField;
    private JComboBox<String> filterCategoryCombo; 
    private JComboBox<String> filterProductCombo;  

    // Define Strong Fonts
    private final Font strongFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 16);

    public ProductionManagerGUI(ProductionManager pm) {
        this.pm = pm;
        initMainUI();
    }

    private void initMainUI() {
        setTitle("Production Management System - Gold Edition");
        setSize(1150, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- 1. TOP CONTROL PANEL ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        topPanel.setBorder(BorderFactory.createTitledBorder(null, "Search & Advanced Filtering", 0, 0, headerFont));

        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        filterCategoryCombo = new JComboBox<>();
        filterCategoryCombo.setFont(strongFont);
        filterCategoryCombo.addItem("All Categories");
        for (Item.Categories cat : Item.Categories.values()) filterCategoryCombo.addItem(cat.name());

        filterProductCombo = new JComboBox<>();
        filterProductCombo.setFont(strongFont);
        filterProductCombo.addItem("All Products");
        String[] prods = {"Laptop", "Phone", "Tablet", "JACKET", "JEANSE", "HOODIE", "tuna", "sardines", "Lanchun"};
        for (String p : prods) filterProductCombo.addItem(p);

        JLabel lblSearch = new JLabel("Search Task:"); lblSearch.setFont(strongFont);
        JLabel lblCat = new JLabel("Category:"); lblCat.setFont(strongFont);
        JLabel lblProd = new JLabel("Product:"); lblProd.setFont(strongFont);

        topPanel.add(lblSearch);
        topPanel.add(searchField);
        topPanel.add(lblCat);
        topPanel.add(filterCategoryCombo);
        topPanel.add(lblProd);
        topPanel.add(filterProductCombo);

        add(topPanel, BorderLayout.NORTH);

        // --- 2. DATA TABLE ---
        String[] columns = {"ID", "Task Name", "Client", "Product", "Quantity", "Status", "Production Line"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setFont(headerFont);
        table.getTableHeader().setBackground(new Color(230, 230, 230));

        // Status Column Coloring & Styling
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(strongFont);
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

        // --- 3. BOTTOM ACTION PANEL ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        
        JButton btnOpenAdd = createStyledButton("Add New Task +", new Color(46, 204, 113));
        JButton btnDelete = createStyledButton("Delete Selected Task -", new Color(231, 76, 60));
        JButton btnRefresh = createStyledButton("Refresh Data 🔄", new Color(52, 152, 219));
        
        bottomPanel.add(btnOpenAdd);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- 4. LISTENERS ---
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshTable(); }
            public void removeUpdate(DocumentEvent e) { refreshTable(); }
            public void changedUpdate(DocumentEvent e) { refreshTable(); }
        });
        filterCategoryCombo.addActionListener(e -> refreshTable());
        filterProductCombo.addActionListener(e -> refreshTable());
        
        btnOpenAdd.addActionListener(e -> new AddTaskDialog(this).setVisible(true));
        
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int taskId = (int) model.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Task ID: " + taskId + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    for (ProductLine line : pm.getProductLines()) {
                        line.productLineTasks.removeIf(t -> t.taskID == taskId);
                    }
                    refreshTable();
                    JOptionPane.showMessageDialog(this, "Task deleted successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task from the table to delete.");
            }
        });

        btnRefresh.addActionListener(e -> refreshTable());

        refreshTable();
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(strongFont);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));
        return btn;
    }

    public void refreshTable() {
        if (model == null) return;
        model.setRowCount(0);

        String text = searchField.getText().toLowerCase();
        String catFilter = (String) filterCategoryCombo.getSelectedItem();
        String prodFilter = (String) filterProductCombo.getSelectedItem();

        List<Task> allTasks = new ArrayList<>();
        for (ProductLine line : pm.getProductLines()) {
            synchronized (line.productLineTasks) {
                allTasks.addAll(line.productLineTasks);
            }
        }
        allTasks.sort(Comparator.comparingInt(t -> t.taskID));

        for (Task t : allTasks) {
            String name = t.getTaskName().toLowerCase();
            String prod = t.getDesireProduct();

            boolean matchesSearch = name.contains(text);
            boolean matchesCat = catFilter.equals("All Categories") || isProductInCategory(prod, catFilter);
            boolean matchesProd = prodFilter.equals("All Products") || prodFilter.equalsIgnoreCase(prod);

            if (matchesSearch && matchesCat && matchesProd) {
                String lineName = "N/A";
                for (ProductLine pl : pm.getProductLines()) {
                    if (pl.productLineTasks.contains(t)) { lineName = pl.getLineName(); break; }
                }
                model.addRow(new Object[]{t.taskID, t.getTaskName(), t.getClientName(), prod, t.getQuantity(), t.getStatus(), lineName});
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

    // --- DIALOG: ADD TASK ---
    class AddTaskDialog extends JDialog {
        private JTextField txtName, txtClient, txtQty, txtDate;
        private JComboBox<Item.Categories> cbCategory;
        private JComboBox<String> cbProduct;
        private JComboBox<String> cbLine;

        public AddTaskDialog(JFrame parent) {
            super(parent, "Create New Production Task", true);
            setSize(450, 500);
            setLocationRelativeTo(parent);
            setLayout(new GridLayout(8, 2, 15, 15));
            ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

            txtName = new JTextField(); txtClient = new JTextField(); txtQty = new JTextField();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            txtDate = new JTextField(LocalDateTime.now().plusDays(1).format(dtf));
            
            cbCategory = new JComboBox<>(Item.Categories.values());
            cbProduct = new JComboBox<>();
            cbLine = new JComboBox<>();
            for (ProductLine pl : pm.getProductLines()) cbLine.addItem(pl.getLineName());

            // Styling labels
            JLabel[] labels = {
                new JLabel("Task Name:"), new JLabel("Client Name:"), 
                new JLabel("Category:"), new JLabel("Product:"), 
                new JLabel("Quantity:"), new JLabel("Deadline:"), 
                new JLabel("Production Line:")
            };
            
            for(JLabel l : labels) l.setFont(strongFont);

            add(labels[0]); add(txtName);
            add(labels[1]); add(txtClient);
            add(labels[2]); add(cbCategory);
            add(labels[3]); add(cbProduct);
            add(labels[4]); add(txtQty);
            add(labels[5]); add(txtDate);
            add(labels[6]); add(cbLine);

            JButton btnSave = new JButton("Save Task");
            btnSave.setFont(strongFont);
            btnSave.setBackground(new Color(46, 204, 113));
            btnSave.setForeground(Color.WHITE);

            JButton btnExit = new JButton("Cancel");
            btnExit.setFont(strongFont);

            add(btnSave); add(btnExit);

            cbCategory.addActionListener(e -> updateProducts());
            updateProducts();

            btnSave.addActionListener(e -> {
                try {
                    Task t = new Task(txtName.getText(), txtClient.getText(), (String)cbProduct.getSelectedItem(), Integer.parseInt(txtQty.getText()), txtDate.getText());
                    pm.addTask(t, (String) cbLine.getSelectedItem());
                    refreshTable();
                    dispose();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            });
            btnExit.addActionListener(e -> dispose());
        }

        private void updateProducts() {
            cbProduct.removeAllItems();
            Item.Categories cat = (Item.Categories) cbCategory.getSelectedItem();
            if (cat == Item.Categories.CLOTHES) { cbProduct.addItem("JACKET"); cbProduct.addItem("JEANSE"); cbProduct.addItem("HOODIE"); }
            else if (cat == Item.Categories.TECHNOLOGY) { cbProduct.addItem("Laptop"); cbProduct.addItem("Phone"); cbProduct.addItem("Tablet"); }
            else if (cat == Item.Categories.CANNED_FOOD) { cbProduct.addItem("tuna"); cbProduct.addItem("sardines"); cbProduct.addItem("Lanchun"); }
        }
    }
}