import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

public class InventoryManagerUI extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JComboBox<String> statusFilter;
    private ProductionManager pm; // لإرجاعه عند الضغط على X

    public InventoryManagerUI(ProductionManager pm) {
        this.pm = pm;
        
        // تحميل البيانات من الملفات
        FileManager.loadInventory();

        setTitle("Inventory Management System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== 1. لوحة التحكم العلوية (الفلاتر + زر الرجوع) =====
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        // زر الرجوع (علامة X)
        JButton btnBack = new JButton(" X ");
        btnBack.setFont(new Font("Arial", Font.BOLD, 20));
        btnBack.setForeground(Color.RED);
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setToolTipText("الرجوع للقائمة الرئيسية");
        btnBack.addActionListener(e -> {
            this.dispose();
            new SupervisorSelectionUI(this.pm).setVisible(true);
        });

        searchField = new JTextField(12);
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("All Categories");
        List<String> cats = getAllCategories();
        if (cats != null) cats.forEach(categoryFilter::addItem);

        statusFilter = new JComboBox<>(new String[] { "All", "Available", "Low Stock", "Out of Stock" });

        filterPanel.add(new JLabel("Search:")); filterPanel.add(searchField);
        filterPanel.add(new JLabel("Category:")); filterPanel.add(categoryFilter);
        filterPanel.add(new JLabel("Status:")); filterPanel.add(statusFilter);

        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // ===== 2. إعداد الجدول =====
        String[] columnNames = { "ID", "Name", "Category", "Price", "Quantity", "Min Quantity" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // تلوين الأصناف المنتهية أو القليلة
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try {
                    int qty = Integer.parseInt(table.getValueAt(row, 4).toString());
                    int min = Integer.parseInt(table.getValueAt(row, 5).toString());
                    if (!isSelected) {
                        if (qty == 0) c.setBackground(new Color(255, 150, 150)); // أحمر غامق للمنتهي
                        else if (qty < min) c.setBackground(new Color(255, 230, 150)); // أصفر للتحذير
                        else c.setBackground(Color.WHITE);
                    }
                } catch (Exception e) { c.setBackground(Color.WHITE); }
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== 3. لوحة الأزرار السفلية =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add Item");
        JButton btnSave = new JButton("Save Changes");
        
        btnSave.addActionListener(e -> {
            FileManager.saveInventory(null);
            JOptionPane.showMessageDialog(this, "Inventory Saved Successfully!");
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnSave);
        add(buttonPanel, BorderLayout.SOUTH);

        // ===== 4. Listeners =====
        searchField.addActionListener(e -> refreshTable());
        categoryFilter.addActionListener(e -> refreshTable());
        statusFilter.addActionListener(e -> refreshTable());

        refreshTable();
    }

    private void refreshTable() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);

        Map<Item, Integer> stock = Inventory.getStock();
        if (stock == null) return;

        String search = searchField.getText().toLowerCase();
        String cat = categoryFilter.getSelectedItem().toString();
        String stat = statusFilter.getSelectedItem().toString();

        for (Item item : stock.keySet()) {
            if (item == null) continue;

            boolean matchesSearch = item.getName().toLowerCase().contains(search);
            boolean matchesCat = cat.equals("All Categories") || item.getCategory().name().equals(cat);
            
            int q = item.getQuantity();
            int m = item.getMinQuantity();
            boolean matchesStat = switch (stat) {
                case "Available" -> q >= m;
                case "Low Stock" -> q > 0 && q < m;
                case "Out of Stock" -> q == 0;
                default -> true;
            };

            if (matchesSearch && matchesCat && matchesStat) {
                tableModel.addRow(new Object[]{ item.getId(), item.getName(), item.getCategory(), item.getPrice(), q, m });
            }
        }
    }

    private List<String> getAllCategories() {
        Map<Item, Integer> stock = Inventory.getStock();
        if (stock == null) return new ArrayList<>();
        return stock.keySet().stream()
                .filter(i -> i != null && i.getCategory() != null)
                .map(i -> i.getCategory().name())
                .distinct().sorted().collect(Collectors.toList());
    }
}