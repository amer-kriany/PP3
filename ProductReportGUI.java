import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.Timer;

public class ProductReportGUI extends JFrame {

    private ProductionManager manager;

    private JComboBox<ProductLine> lineComboBox;
    private JTable productTable;
    private DefaultTableModel productTableModel;

    private JButton showByLineBtn;
    private JButton showAllBtn;
    private JButton showMostRequestedBtn;

    private JTextField fromField;
    private JTextField toField;

    public ProductReportGUI(ProductionManager manager) {
        this.manager = manager;

        setTitle("Production Report Panel");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font font = new Font("Arial", Font.PLAIN, 40);

        // ===== Line Selection =====
        JLabel lineLabel = new JLabel("Select Production Line:");
        lineLabel.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lineLabel, gbc);

        lineComboBox = new JComboBox<>();
        for (ProductLine line : manager.getProductLines()) {
            lineComboBox.addItem(line);
        }
        lineComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ProductLine) {
                    setText(((ProductLine) value).getLineName());
                }
                return this;
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(lineComboBox, gbc);

        // ===== Buttons =====
        showByLineBtn = new JButton("Show Products by Line");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(showByLineBtn, gbc);

        showAllBtn = new JButton("Show All Manufactured Products");
        gbc.gridy = 2;
        add(showAllBtn, gbc);

        // ===== Most Requested Product =====
        JPanel periodPanel = new JPanel(new FlowLayout());
        periodPanel.add(new JLabel("From (dd-MM-yyyy HH:mm:ss):"));
        fromField = new JTextField(12);
        periodPanel.add(fromField);

        periodPanel.add(new JLabel("To (dd-MM-yyyy HH:mm:ss):"));
        toField = new JTextField(12);
        periodPanel.add(toField);

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(periodPanel, gbc);

        showMostRequestedBtn = new JButton("Show Most Requested Product");
        gbc.gridy = 4;
        add(showMostRequestedBtn, gbc);

        // ===== Product Table =====
        String[] columns = { "Product Name", "Quantity" };
        productTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(productTableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);

        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(scrollPane, gbc);

        // ===== Button Actions =====
        showByLineBtn.addActionListener(e -> showProductsByLine());
        showAllBtn.addActionListener(e -> showAllProducts());
        showMostRequestedBtn.addActionListener(e -> showMostRequestedProduct());

        // تحديث الجدول كل ثانية (1000 ms)
        Timer refreshTimer = new Timer(1000, e -> refreshTable());
        refreshTimer.start();
    }

    // ================== METHODS ==================
    private void showProductsByLine() {
        ProductLine line = (ProductLine) lineComboBox.getSelectedItem();
        if (line == null)
            return;
        productTableModel.setRowCount(0);
        try {
            for (Task task : line.getProductLineTasks()) {
                String name = task.getDesireProduct();
                int Product = task.getProductionProgress();
                productTableModel.addRow(new Object[] { name, Product });
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void refreshTable() {
        ProductLine selectedLine = (ProductLine) lineComboBox.getSelectedItem();
        if (selectedLine == null)
            return;

        productTableModel.setRowCount(0); // مسح القديم

        for (Task task : selectedLine.getProductLineTasks()) {
            String name = task.getDesireProduct();
            int produced = task.getProductionProgress(); // عدد المنتجات المصنوعة حتى الآن
            int total = task.getQuantity(); // العدد المطلوب
            productTableModel.addRow(new Object[] { name, produced + " / " + total });
        }
    }

    private void showAllProducts() {
        productTableModel.setRowCount(0);
        try {
            List<String> products = manager.showAllManufacturedProducts();
            for (String p : products) {
                productTableModel.addRow(new Object[] { p });
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void showMostRequestedProduct() {
        String fromText = fromField.getText().trim();
        String toText = toField.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        try {
            LocalDateTime from = LocalDateTime.parse(fromText, formatter);
            LocalDateTime to = LocalDateTime.parse(toText, formatter);

            String mostRequested = manager.getMostRequestedProduct(from, to);
            productTableModel.setRowCount(0);
            productTableModel.addRow(new Object[] { mostRequested });

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use dd-MM-yyyy HH:mm:ss");
        }
    }

    // ================== MAIN ==================
    public static void main(String[] args) {

    }
}