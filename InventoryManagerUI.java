import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryManagerUI extends JFrame {
    private final ProductionManager pm;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JComboBox<String> statusFilter;

    public InventoryManagerUI() {
        this(null);
    }

    public InventoryManagerUI(ProductionManager pm) {
        this.pm = pm;
        FileManager.loadInventory();

        setTitle("Inventory Management System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initTopPanel();
        initTable();
        initButtons();

        refreshTable();
    }

    private void initTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JButton btnBack = new JButton("X");
        btnBack.setForeground(Color.RED);
        btnBack.addActionListener(e -> {
            if (pm != null) {
                new SupervisorSelectionUI(pm).setVisible(true);
            }
            dispose();
        });

        searchField = new JTextField(12);
        searchField.addActionListener(e -> refreshTable());

        categoryFilter = new JComboBox<>();
        categoryFilter.addActionListener(e -> refreshTable());

        statusFilter = new JComboBox<>(new String[]{"All", "Available", "Low Stock", "Out of Stock"});
        statusFilter.addActionListener(e -> refreshTable());

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryFilter);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);

        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        updateCategoryCombo();
    }

    private void initTable() {
        String[] columns = {"ID", "Name", "Category", "Price", "Quantity", "Min Quantity"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    return c;
                }
                try {
                    int qty = Integer.parseInt(table.getValueAt(row, 4).toString());
                    int min = Integer.parseInt(table.getValueAt(row, 5).toString());
                    if (qty == 0) {
                        c.setBackground(new Color(255, 150, 150));
                    } else if (qty < min) {
                        c.setBackground(new Color(255, 230, 150));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } catch (Exception ex) {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnAdd = new JButton("Add Item");
        JButton btnUpdateQty = new JButton("Update Quantity");
        JButton btnDelete = new JButton("Delete Item");
        JButton btnSave = new JButton("Save");

        btnAdd.addActionListener(e -> showAddItemDialog());
        btnUpdateQty.addActionListener(e -> showEditQuantityDialog());
        btnDelete.addActionListener(e -> deleteSelectedItem());
        btnSave.addActionListener(e -> saveInventoryToFile());

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdateQty);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSave);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateCategoryCombo() {
        String current = categoryFilter.getSelectedItem() == null
                ? "All Categories"
                : categoryFilter.getSelectedItem().toString();
        categoryFilter.removeAllItems();
        categoryFilter.addItem("All Categories");
        getAllCategories().forEach(categoryFilter::addItem);
        categoryFilter.setSelectedItem(current);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        Map<Item, Integer> stock = Inventory.getStock();
        if (stock == null) {
            return;
        }

        List<Item> items = new ArrayList<>(stock.keySet());
        String searchText = searchField.getText().trim().toLowerCase();
        String selectedCategory = String.valueOf(categoryFilter.getSelectedItem());
        String selectedStatus = String.valueOf(statusFilter.getSelectedItem());

        for (Item item : items) {
            int quantity = Inventory.getItemQuantity(item);
            int min = item.getMinQuantity();

            boolean matchesSearch = item.getName().toLowerCase().contains(searchText);
            boolean matchesCategory = "All Categories".equals(selectedCategory)
                    || item.getCategory().name().equals(selectedCategory);
            boolean matchesStatus = switch (selectedStatus) {
                case "Available" -> quantity >= min;
                case "Low Stock" -> quantity > 0 && quantity < min;
                case "Out of Stock" -> quantity == 0;
                default -> true;
            };

            if (matchesSearch && matchesCategory && matchesStatus) {
                tableModel.addRow(new Object[]{
                        item.getId(),
                        item.getName(),
                        item.getCategory(),
                        item.getPrice(),
                        quantity,
                        min
                });
            }
        }
    }

    private List<String> getAllCategories() {
        return Inventory.getStock().keySet().stream()
                .map(i -> i.getCategory().name())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private void showAddItemDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JComboBox<Item.Categories> categoryCombo = new JComboBox<>(Item.Categories.values());
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField minField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryCombo);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Min Quantity:"));
        panel.add(minField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Item", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            int min = Integer.parseInt(minField.getText().trim());
            Item.Categories category = (Item.Categories) categoryCombo.getSelectedItem();

            if (name.isEmpty() || category == null) {
                throw new IllegalArgumentException("Name and category are required.");
            }

            Item existingById = Inventory.getItemById(id);
            Item existingByName = Inventory.getItemByName(name);
            if (existingById != null && !existingById.getName().equalsIgnoreCase(name)) {
                JOptionPane.showMessageDialog(this, "ID already exists for another item.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (existingByName != null && existingByName.getId() != id) {
                JOptionPane.showMessageDialog(this, "Name already exists with another ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (existingById != null) {
                Inventory.addItem(existingById, quantity);
            } else {
                Item newItem = new Item(id, name, category, price, quantity, min);
                Inventory.addItem(newItem, quantity);
            }
            updateCategoryCombo();
            refreshTable();
            JOptionPane.showMessageDialog(this, "Item saved.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showEditQuantityDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an item first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int itemId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
        Item item = Inventory.getItemById(itemId);
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Current quantity: " + Inventory.getItemQuantity(item) + "\nEnter delta (+/-):", "0");
        if (input == null) {
            return;
        }

        try {
            int delta = Integer.parseInt(input.trim());
            Inventory.addItem(item, delta);
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an item first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int itemId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
        Inventory.removeItem(itemId);
        updateCategoryCombo();
        refreshTable();
    }

    private void saveInventoryToFile() {
        FileManager.saveInventory(null);
        JOptionPane.showMessageDialog(this, "Inventory saved successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }
}
