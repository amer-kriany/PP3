import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryManagerUI extends JFrame {

    private Inventory inventory;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField searchField;
    private JComboBox<String> categoryFilter;
    private JComboBox<String> statusFilter;

    public InventoryManagerUI() {
        // inventory = new Inventory();
        FileManager.loadInventory();

        setTitle("Production Supervisor/Inventory Manager");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // show schedule
        String[] columnNames = { "ID", "Name", "Category", "Price", "Quantity", "Min Quantity" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(28);

        // Coloring the rows below the minimum
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int quantity = Integer.parseInt(table.getValueAt(row, 4).toString());
                int min = Integer.parseInt(table.getValueAt(row, 5).toString());
                if (quantity < min) {
                    c.setBackground(new Color(255, 100, 100)); // Light red
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Search and filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchField = new JTextField(15);
        searchField.setToolTipText("Search by name...");
        searchField.addActionListener(e -> refreshTable());

        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("All Categories");
        getAllCategories().forEach(categoryFilter::addItem);
        categoryFilter.addActionListener(e -> refreshTable());

        statusFilter = new JComboBox<>(new String[] { "All", "Available", "Low Stock", "Out of Stock" });
        statusFilter.addActionListener(e -> refreshTable());

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryFilter);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);

        add(filterPanel, BorderLayout.NORTH);

        // buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Item");
        JButton editButton = new JButton("Edit Quantity");
        JButton deleteButton = new JButton("Delete Item");
        JButton saveButton = new JButton("Save");

        addButton.addActionListener(e -> showAddItemDialog());
        editButton.addActionListener(e -> showEditQuantityDialog());
        deleteButton.addActionListener(e -> deleteSelectedItem());
        saveButton.addActionListener(e -> saveInventoryToFile());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Filling in the table
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Item> items = new ArrayList<>(Inventory.getStock().keySet());

        String searchText = searchField.getText().trim().toLowerCase();
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        String selectedStatus = (String) statusFilter.getSelectedItem();

        for (Item item : items) {
            boolean matchesSearch = item.getName().toLowerCase().contains(searchText);
            boolean matchesCategory = selectedCategory.equals("All Categories")
                    || item.getCategory().name().equalsIgnoreCase(selectedCategory);
            boolean matchesStatus = switch (selectedStatus) {
                case "Available" -> item.getQuantity() >= item.getMinQuantity();
                case "Low Stock" -> item.getQuantity() > 0 && item.getQuantity() < item.getMinQuantity();
                case "Out of Stock" -> item.getQuantity() == 0;
                default -> true;
            };

            if (matchesSearch && matchesCategory && matchesStatus) {
                Object[] row = {
                        item.getId(),
                        item.getName(),
                        item.getCategory(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getMinQuantity()
                };
                tableModel.addRow(row);
            }
        }
    }

    private List<String> getAllCategories() {
        return Inventory.getStock().keySet().stream()
                .map(item -> item.getCategory().toString())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private void showAddItemDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField minField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Min Quantity:"));
        panel.add(minField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                String name = nameField.getText().trim();
                String categoryStr = categoryField.getText().trim();
                Item.Categories category;
                try {
                    category = Item.Categories.valueOf(categoryStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    category = null;
                    for (Item.Categories c : Item.Categories.values()) {
                        if (c.name().equalsIgnoreCase(categoryStr)) {
                            category = c;
                            break;
                        }
                    }
                    if (category == null) {
                        throw new IllegalArgumentException("Invalid category: " + categoryStr);
                    }
                }
                double price = Double.parseDouble(priceField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                int min = Integer.parseInt(minField.getText().trim());

                Item newItem = new Item(id, name, category, price, quantity, min);
                Inventory.addItem(newItem, newItem.getQuantity());
                refreshTable();
            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditQuantityDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int itemId = (int) table.getValueAt(selectedRow, 0);
        Item item = Inventory.getItemById(itemId);

        String newQuantityStr = JOptionPane.showInputDialog(this, "Enter new quantity for " + item.getName() + ":");
        if (newQuantityStr != null) {
            try {
                int newQuantity = Integer.parseInt(newQuantityStr.trim());
                item.setQuantity(newQuantity);
                refreshTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int itemId = (int) table.getValueAt(selectedRow, 0);
        inventory.removeItem(itemId);
        refreshTable();
    }

    private void saveInventoryToFile() {
        FileManager.saveInventory(inventory);
        JOptionPane.showMessageDialog(this, "Inventory saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> new InventoryManagerUI().setVisible(true));
    // }
}
