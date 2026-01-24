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
    private ProductionManager pm;

    public InventoryManagerUI(ProductionManager pm) {
        this.pm = pm;
        FileManager.loadInventory();

        setTitle("Inventory Management System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 1. Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JButton btnBack = new JButton(" X ");
        btnBack.setForeground(Color.RED);
        btnBack.addActionListener(e -> {
            this.dispose();
            new SupervisorSelectionUI(this.pm).setVisible(true);
        });

        searchField = new JTextField(12);
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("All Categories");
        updateCategoryCombo();

        statusFilter = new JComboBox<>(new String[]{"All", "Available", "Low Stock", "Out of Stock"});

        filterPanel.add(new JLabel("Search:")); filterPanel.add(searchField);
        filterPanel.add(new JLabel("Category:")); filterPanel.add(categoryFilter);
        filterPanel.add(new JLabel("Status:")); filterPanel.add(statusFilter);
        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // 2. Table
        String[] columnNames = {"ID", "Name", "Category", "Price", "Quantity", "Min Quantity"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try {
                    int qty = Integer.parseInt(table.getValueAt(row, 4).toString());
                    int min = Integer.parseInt(table.getValueAt(row, 5).toString());
                    if (!isSelected) {
                        if (qty == 0) c.setBackground(new Color(255, 150, 150));
                        else if (qty < min) c.setBackground(new Color(255, 230, 150));
                        else c.setBackground(Color.WHITE);
                    }
                } catch (Exception e) { c.setBackground(Color.WHITE); }
                return c;
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Add Item");
        JButton btnUpdateQty = new JButton("Update Quantity");
        JButton btnSave = new JButton("Save Changes");

        btnAdd.addActionListener(e -> {
            JTextField idF = new JTextField(), nameF = new JTextField(), priceF = new JTextField(), qtyF = new JTextField(), minF = new JTextField();
            JComboBox<Item.Categories> catCombo = new JComboBox<>(Item.Categories.values());
            Object[] fields = {"ID:", idF, "Name:", nameF, "Category:", catCombo, "Price:", priceF, "Quantity:", qtyF, "Min Quantity:", minF};
            if (JOptionPane.showConfirmDialog(this, fields, "Add New Item", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try {
                    Item newItem = new Item(Integer.parseInt(idF.getText()), nameF.getText(), (Item.Categories)catCombo.getSelectedItem(), Double.parseDouble(priceF.getText()), Integer.parseInt(qtyF.getText()), Integer.parseInt(minF.getText()));
                    Inventory.addItem(newItem, Integer.parseInt(qtyF.getText()));
                    refreshTable(); updateCategoryCombo();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error in input."); }
            }
        });

        btnUpdateQty.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                Item item = Inventory.getItemById(id);
                if (item != null) {
                    String input = JOptionPane.showInputDialog(this, "Current: " + item.getQuantity() + "\nAdd/Subtract Quantity:", "0");
                    if (input != null) {
                        try {
                            Inventory.addItem(item, Integer.parseInt(input.trim()));
                            refreshTable();
                        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid number."); }
                    }
                }
            } else { JOptionPane.showMessageDialog(this, "Select a row first."); }
        });

        btnSave.addActionListener(e -> {
            FileManager.saveInventory(null);
            JOptionPane.showMessageDialog(this, "Saved!");
        });

        buttonPanel.add(btnAdd); buttonPanel.add(btnUpdateQty); buttonPanel.add(btnSave);
        add(buttonPanel, BorderLayout.SOUTH);

        searchField.addActionListener(e -> refreshTable());
        categoryFilter.addActionListener(e -> refreshTable());
        statusFilter.addActionListener(e -> refreshTable());
        refreshTable();
    }

    private void updateCategoryCombo() {
        String current = (String) categoryFilter.getSelectedItem();
        categoryFilter.removeAllItems();
        categoryFilter.addItem("All Categories");
        getAllCategories().forEach(categoryFilter::addItem);
        categoryFilter.setSelectedItem(current != null ? current : "All Categories");
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        Map<Item, Integer> stock = Inventory.getStock();
        if (stock == null) return;
        String search = searchField.getText().toLowerCase();
        String cat = categoryFilter.getSelectedItem().toString();
        String stat = statusFilter.getSelectedItem().toString();

        for (Item item : stock.keySet()) {
            boolean matchesSearch = item.getName().toLowerCase().contains(search);
            boolean matchesCat = cat.equals("All Categories") || item.getCategory().name().equals(cat);
            int q = Inventory.getItemQuantity(item); // استخدام دالة Inventory لضمان الدقة
            int m = item.getMinQuantity();
            boolean matchesStat = switch (stat) {
                case "Available" -> q >= m;
                case "Low Stock" -> q > 0 && q < m;
                case "Out of Stock" -> q == 0;
                default -> true;
            };
            if (matchesSearch && matchesCat && matchesStat) {
                tableModel.addRow(new Object[]{item.getId(), item.getName(), item.getCategory(), item.getPrice(), q, m});
            }
        }
    }

    private List<String> getAllCategories() {
        return Inventory.getStock().keySet().stream()
                .map(i -> i.getCategory().name()).distinct().sorted().collect(Collectors.toList());
    }
}