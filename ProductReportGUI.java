import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    Timer refreshTimer;

    private enum ViewMode {
        BY_LINE,
        ALL_PRODUCTS,
        MOST_REQUESTED
    }

    private ViewMode Mode = ViewMode.BY_LINE;

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

        showByLineBtn = new JButton("Show Products by Line");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(showByLineBtn, gbc);

        showAllBtn = new JButton("Show All Manufactured Products");
        gbc.gridy = 2;
        add(showAllBtn, gbc);

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

        showByLineBtn.addActionListener(e -> {
            Mode = ViewMode.BY_LINE;
            refreshTable();
        });
        showAllBtn.addActionListener(e -> {
            Mode = ViewMode.ALL_PRODUCTS;
            refreshTable();
        });
        showMostRequestedBtn.addActionListener(e -> {
            Mode = ViewMode.MOST_REQUESTED;
            refreshTable();
        });

        refreshTimer = new Timer(1000, e -> refreshTable());
        refreshTimer.start();
    }

    private void showProductsByLine() {
        ProductLine line = (ProductLine) lineComboBox.getSelectedItem();
        if (line == null)
            return;
        productTableModel.setRowCount(0);
        try {
            for (Task task : line.getProductLineTasks()) {
                String name = task.getDesireProduct();
                int Product = task.getProductionProgress();
                int total = task.getQuantity();
                productTableModel.addRow(new Object[] { name, Product, "/" + total });
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ProductReportGUI | " + e.getMessage());
            stopRefreshAndShowError();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error showing products by line!", "Error", JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ProductReportGUI | Error showing products by line!");
            stopRefreshAndShowError();
        }
    }

    private void showAllProducts() {
        productTableModel.setRowCount(0);

        try {
            for (ProductLine line : manager.getProductLines()) {
                for (Task task : line.getProductLineTasks()) {

                    int produced = task.getProductionProgress();

                    if (produced > 0) {
                        String name = task.getDesireProduct();
                        int total = task.getQuantity();

                        productTableModel.addRow(new Object[] {
                                name,
                                produced + " / " + total
                        });
                    }
                }
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "No manufactured products found!", "Error", JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ProductReportGUI | No manufactured products found!");
            stopRefreshAndShowError();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error not found any Product!", "Error", JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ProductReportGUI | Error not found any Product!");
            stopRefreshAndShowError();

        }
    }

    private void showMostRequestedProduct() {
        String fromText = fromField.getText().trim();
        String toText = toField.getText().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        try {
            LocalDateTime from = LocalDateTime.parse(fromText, formatter);
            LocalDateTime to = LocalDateTime.parse(toText, formatter);

            ProductSale mostRequested = manager.getMostRequestedProduct(from, to);
            productTableModel.setRowCount(0);
            productTableModel.addRow(new Object[] { mostRequested.productName, mostRequested.quantity });

        } catch (IllegalArgumentException e) {
            productTableModel.setRowCount(0);
            productTableModel.addRow(new Object[] { "Error: " + e.getMessage(), "0" });
            FileManager.logError("ProductReportGUI | " + e.getMessage());
        } catch (DateTimeParseException e) {
            productTableModel.setRowCount(0);
            productTableModel.addRow(new Object[] { "Invalid date format", "0" });
            FileManager.logError("ProductReportGUI | Invalid date format! Use dd-MM-yyyy HH:mm:ss");
        } catch (Exception e) {
            productTableModel.setRowCount(0);
            productTableModel.addRow(new Object[] { "System error", "0" });
            FileManager.logError("ProductReportGUI | Invalid date format! Use dd-MM-yyyy HH:mm:ss");
        }
    }

    private void refreshTable() {
        productTableModel.setRowCount(0);

        switch (Mode) {

            case BY_LINE -> showProductsByLine();

            case ALL_PRODUCTS -> showAllProducts();
            case MOST_REQUESTED -> showMostRequestedProduct();

        }
    }

    private void stopRefreshAndShowError() {
        try {
            if (refreshTimer != null && refreshTimer.isRunning()) {
                refreshTimer.stop();
            }
        } catch (Exception e) {
            FileManager.logError("ProductReportGUI | " + e.getMessage());
        }
    }

}