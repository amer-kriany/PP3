import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Manager extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JComboBox<ProductLine.State> stateBox;
    private ProductionManager manager;

    public Manager(ProductionManager manager) {
        this.manager = manager;

        setTitle("Manager Panel");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ====== Layout ======
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // مسافات حول المكونات
        gbc.fill = GridBagConstraints.HORIZONTAL; // المكونات تملأ العرض

        Font font = new Font("Arial", Font.BOLD, 40);

        // ====== ID ======
        JLabel idLabel = new JLabel("Line ID:");
        idLabel.setFont(font);
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(idLabel, gbc);

        idField = new JTextField();
        idField.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(idField, gbc);

        // ====== Name ======
        JLabel nameLabel = new JLabel("Line Name:");
        nameLabel.setFont(font);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(nameLabel, gbc);

        nameField = new JTextField();
        nameField.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(nameField, gbc);

        // ====== State ======
        JLabel stateLabel = new JLabel("State:");
        stateLabel.setFont(font);
        stateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(stateLabel, gbc);

        stateBox = new JComboBox<>(ProductLine.State.values());
        stateBox.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(stateBox, gbc);

        // ====== Buttons ======
        JButton addButton = new JButton("Add Line");
        addButton.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // يأخذ عمودين
        gbc.anchor = GridBagConstraints.CENTER; // في المنتصف
        add(addButton, gbc);

        JButton changeStateButton = new JButton("Change State");
        changeStateButton.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // يأخذ عمودين
        gbc.anchor = GridBagConstraints.CENTER;
        add(changeStateButton, gbc);

        // ====== Actions ======
        addButton.addActionListener(e -> addLine());
        changeStateButton.addActionListener(e -> changeLineState());
    }

    // ================== METHODS ==================
    private void addLine() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Please enter the line name!");
            }
            ProductLine.State state = (ProductLine.State) stateBox.getSelectedItem();

            ProductLine line = ProductLine.addLine(id, name, state);
            manager.addLine(line);

            JOptionPane.showMessageDialog(this,
                    "Line added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid ID!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeLineState() {
        try {
            int id = Integer.parseInt(idField.getText());
            ProductLine.State newState = (ProductLine.State) stateBox.getSelectedItem();

            for (ProductLine line : manager.getProductLines()) {
                if (line.getLineId() == id) {
                    line.setState(newState);
                    JOptionPane.showMessageDialog(this,
                            "State updated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Line not found!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid ID!", 
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        ProductionManager pm = new ProductionManager(new ArrayList<>());
        // 🔹 خطوط ثابتة
        pm.addLine(ProductLine.addLine(1, "Line A", ProductLine.State.ACTIVE));
        pm.addLine(ProductLine.addLine(2, "Line B", ProductLine.State.STOP));
        pm.addLine(ProductLine.addLine(3, "Line C", ProductLine.State.MAINTENANCE));

        // 🔹 فتح الواجهة
        SwingUtilities.invokeLater(() -> new Manager(pm).setVisible(true));
    }
}