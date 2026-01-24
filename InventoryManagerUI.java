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

      
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                
                // استدعاء الـ parent أولاً
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // التأكد من وجود القيم قبل القراءة
                if (table.getValueAt(row, 4) != null && table.getValueAt(row, 5) != null) {
                    try {
                        int quantity = Integer.parseInt(table.getValueAt(row, 4).toString());
                        int min = Integer.parseInt(table.getValueAt(row, 5).toString());
                        
                        // أولوية للتحديد - إذا السطر محدد، استخدم ألوان التحديد
                        if (isSelected) {
                            c.setBackground(table.getSelectionBackground());
                            c.setForeground(table.getSelectionForeground());
                        } else {
                            // إذا لم يكن محدد، طبق منطق الألوان المخصص
                            if (quantity < min) {
                                c.setBackground(new Color(255, 100, 100)); // Light red
                                c.setForeground(Color.BLACK);
                            } else {
                                c.setBackground(Color.WHITE);
                                c.setForeground(Color.BLACK);
                            }
                        }
                    } catch (NumberFormatException e) {
                        // في حال كانت القيمة مش رقم، استخدم الألوان الافتراضية
                        if (isSelected) {
                            c.setBackground(table.getSelectionBackground());
                            c.setForeground(table.getSelectionForeground());
                        } else {
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                        }
                    }
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
        panel.add(categoryCombo);  // استخدام ComboBox بدل TextField
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Min Quantity:"));
        panel.add(minField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Item", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                // التحقق من الحقول الفارغة
                String idText = idField.getText().trim();
                String name = nameField.getText().trim();

                if (idText.isEmpty() || name.isEmpty()) {
                    throw new IllegalArgumentException("ID and Name cannot be empty!");
                }

                int id = Integer.parseInt(idText);
                double price = Double.parseDouble(priceField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                int min = Integer.parseInt(minField.getText().trim());
                
                // الحصول على الـ Category من ComboBox
                Item.Categories category = (Item.Categories) categoryCombo.getSelectedItem();
                if (category == null) {
                    throw new IllegalArgumentException("Please select a Category!");
                }

                //  فحص الـ ID المكرر + جمع الكميات للعناصر الموجودة
                Item existingItemById = Inventory.getItemById(id);
                Item existingItemByName = Inventory.getItemByName(name);

                // حالة 1: الـ ID موجود
                if (existingItemById != null) {
                    // التحقق: هل نفس العنصر (نفس الاسم والفئة)؟
                    if (existingItemById.getName().equalsIgnoreCase(name) && 
                        existingItemById.getCategory() == category) {
                        
                        // نفس العنصر → نجمع الكميات
                        int currentQty = Inventory.getItemQuantity(existingItemById);
                        Inventory.addItem(existingItemById, quantity);
                        existingItemById.setQuantity(currentQty + quantity);
                        
                        refreshTable();
                        JOptionPane.showMessageDialog(this, 
                            "Added " + quantity + " to existing item!\nNew Quantity: " + (currentQty + quantity),
                            "Item Updated", 
                            JOptionPane.INFORMATION_MESSAGE);
                        return;
                        
                    } else {
                        
                        JOptionPane.showMessageDialog(this, 
                            "Error: ID (" + id + ") already exists for another item!", 
                            "Duplicate ID", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

         
                if (existingItemByName != null) {
                    JOptionPane.showMessageDialog(this, 
                        "Error: Name '" + name + "' already exists with ID: " + existingItemByName.getId(), 
                        "Duplicate Name", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                
                Item newItem = new Item(id, name, category, price, quantity, min);
                Inventory.addItem(newItem, quantity);
                
                refreshTable();
                JOptionPane.showMessageDialog(this, "Item added successfully!");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter valid numbers for ID, Price, Quantity, and Min Quantity.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "An unexpected error occurred: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
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
        Inventory.removeItem(itemId); 
        refreshTable();
    }

    private void saveInventoryToFile() {
        FileManager.saveInventory(inventory);
        JOptionPane.showMessageDialog(this, "Inventory saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }
}