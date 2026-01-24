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

    public ProductionManagerGUI(ProductionManager pm) {
        this.pm = pm;
        initMainUI();
    }

    private void initMainUI() {
        setTitle("نظام إدارة الإنتاج - نسخة الذهب");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- 1. شريط التحكم العلوي ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("البحث والتصفية"));

        searchField = new JTextField(12);
        
        filterCategoryCombo = new JComboBox<>();
        filterCategoryCombo.addItem("كل التصنيفات");
        for (Item.Categories cat : Item.Categories.values()) filterCategoryCombo.addItem(cat.name());

        filterProductCombo = new JComboBox<>();
        filterProductCombo.addItem("كل المنتجات");
        String[] prods = {"Laptop", "Phone", "Tablet", "JACKET", "JEANSE", "HOODIE", "tuna", "sardines", "Lanchun"};
        for (String p : prods) filterProductCombo.addItem(p);

        topPanel.add(new JLabel("بحث:"));
        topPanel.add(searchField);
        topPanel.add(new JLabel("التصنيف:"));
        topPanel.add(filterCategoryCombo);
        topPanel.add(new JLabel("المنتج:"));
        topPanel.add(filterProductCombo);

        add(topPanel, BorderLayout.NORTH);

        // --- 2. الجدول ---
        String[] columns = {"ID", "المهمة", "العميل", "المنتج", "الكمية", "الحالة", "الخط"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30);

        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
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

        // --- 3. الأزرار السفلية (تمت إضافة زر الحذف هنا) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnOpenAdd = new JButton("إضافة مهمة +");
        JButton btnDelete = new JButton("حذف المهمة المحددة -"); // الزر الجديد
        JButton btnRefresh = new JButton("تحديث 🔄");
        
        bottomPanel.add(btnOpenAdd);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- 4. المستشعرات اللحظية ---
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshTable(); }
            public void removeUpdate(DocumentEvent e) { refreshTable(); }
            public void changedUpdate(DocumentEvent e) { refreshTable(); }
        });
        filterCategoryCombo.addActionListener(e -> refreshTable());
        filterProductCombo.addActionListener(e -> refreshTable());
        
        btnOpenAdd.addActionListener(e -> new AddTaskDialog(this).setVisible(true));
        
        // حدث زر الحذف
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int taskId = (int) model.getValueAt(selectedRow, 0);
                // البحث في كل الخطوط لحذف المهمة
                for (ProductLine line : pm.getProductLines()) {
                    line.productLineTasks.removeIf(t -> t.taskID == taskId);
                }
                refreshTable();
                JOptionPane.showMessageDialog(this, "تم حذف المهمة بنجاح!");
            } else {
                JOptionPane.showMessageDialog(this, "يرجى اختيار مهمة من الجدول لحذفها");
            }
        });

        btnRefresh.addActionListener(e -> refreshTable());

        refreshTable();
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
            boolean matchesCat = catFilter.equals("كل التصنيفات") || isProductInCategory(prod, catFilter);
            boolean matchesProd = prodFilter.equals("كل المنتجات") || prodFilter.equalsIgnoreCase(prod);

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

    // --- نافذة الإضافة المدمجة ---
    class AddTaskDialog extends JDialog {
        private JTextField txtName, txtClient, txtQty, txtDate;
        private JComboBox<Item.Categories> cbCategory;
        private JComboBox<String> cbProduct;
        private JComboBox<String> cbLine;

        public AddTaskDialog(JFrame parent) {
            super(parent, "إضافة مهمة جديدة", true);
            setSize(400, 480);
            setLocationRelativeTo(parent);
            setLayout(new GridLayout(8, 2, 10, 10));
            ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            txtName = new JTextField(); txtClient = new JTextField(); txtQty = new JTextField();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            txtDate = new JTextField(LocalDateTime.now().plusDays(1).format(dtf));
            cbCategory = new JComboBox<>(Item.Categories.values());
            cbProduct = new JComboBox<>();
            cbLine = new JComboBox<>();
            for (ProductLine pl : pm.getProductLines()) cbLine.addItem(pl.getLineName());

            add(new JLabel(" اسم المهمة:")); add(txtName);
            add(new JLabel(" اسم العميل:")); add(txtClient);
            add(new JLabel(" التصنيف:")); add(cbCategory);
            add(new JLabel(" المنتج:")); add(cbProduct);
            add(new JLabel(" الكمية:")); add(txtQty);
            add(new JLabel(" موعد التسليم:")); add(txtDate);
            add(new JLabel(" خط الإنتاج:")); add(cbLine);

            JButton btnSave = new JButton("إضافة");
            JButton btnExit = new JButton("إلغاء");
            add(btnSave); add(btnExit);

            cbCategory.addActionListener(e -> updateProducts());
            updateProducts();

            btnSave.addActionListener(e -> {
                try {
                    Task t = new Task(txtName.getText(), txtClient.getText(), (String)cbProduct.getSelectedItem(), Integer.parseInt(txtQty.getText()), txtDate.getText());
                    pm.addTask(t, (String) cbLine.getSelectedItem());
                    refreshTable();
                    dispose();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "خطأ: " + ex.getMessage()); }
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