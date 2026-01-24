import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

public class ManagerUI extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JComboBox<ProductLine.State> stateBox;
    private ProductionManager manager;
    private JTable linePerformanceTable;
    private DefaultTableModel linePerformanceModel;
    private JComboBox<ProductLine> lineBox;
    private JSpinner ratingSpinner;
    private JTextArea noteArea;

    public ManagerUI(ProductionManager manager) {
        this.manager = manager;

        setTitle("Manager Panel");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Font font = new Font("Arial", Font.BOLD, 40);

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

        JButton addButton = new JButton("Add Line");
        addButton.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(addButton, gbc);

        // state
        JButton changeStateButton = new JButton("Change State");
        changeStateButton.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(changeStateButton, gbc);
        // table
        String[] columns = { "Line Id", "Line Name", "Performance" };
        linePerformanceModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        linePerformanceTable = new JTable(linePerformanceModel);

        linePerformanceTable.setRowHeight(30);
        JScrollPane lineScroll = new JScrollPane(linePerformanceTable);
        GridBagConstraints gbcLine = new GridBagConstraints();
        lineScroll.setPreferredSize(new Dimension(200, 100));
        gbcLine.gridx = 0;
        gbcLine.gridy = 5;
        gbcLine.gridwidth = 2;
        gbcLine.weightx = 1;
        gbcLine.weighty = 1;
        gbcLine.fill = GridBagConstraints.BOTH;
        add(lineScroll, gbcLine);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Select Production Line:"), gbc);

        lineBox = new JComboBox<>();
        for (ProductLine line : manager.getProductLines()) {
            lineBox.addItem(line);
        }
        lineBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ProductLine) {
                    setText(((ProductLine) value).getLineName());
                }
                return this;
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 6;
        add(lineBox, gbc);

        // rat
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Rating (0-10):"), gbc);

        ratingSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        gbc.gridx = 1;
        gbc.gridy = 7;
        add(ratingSpinner, gbc);

        // note
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        add(new JLabel("Notes:"), gbc);

        noteArea = new JTextArea(5, 20);
        JScrollPane scroll = new JScrollPane(noteArea);
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(scroll, gbc);

        // save
        JButton saveButton = new JButton("Save Rating & Notes");
        saveButton.addActionListener(e -> saveNotesAndRating());
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbcLine.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(saveButton, gbc);

        Timer lineTimer = new Timer(1000, e -> refreshLinePerformance());
        lineTimer.start();
        addButton.addActionListener(e -> addLine());
        changeStateButton.addActionListener(e -> changeLineState());
    }

    private void addLine() {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Please enter the line name!");
            }
            ProductLine.State state = (ProductLine.State) stateBox.getSelectedItem();

            ProductLine line = new ProductLine(id, name, state);
            manager.addLine(line);

            lineBox.addItem(line);

            refreshLinePerformance();
            JOptionPane.showMessageDialog(this,
                    "Line added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid ID!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ManagerUI | Invalid Id!");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ManagerUI | " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ManagerUI | Invalid input!");
        }
    }

    private void changeLineState() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            ProductLine.State newState = (ProductLine.State) stateBox.getSelectedItem();

            ProductLine line = manager.getProductLines()
                    .stream()
                    .filter(l -> l.getLineId() == id)
                    .findFirst()
                    .orElse(null);

            if (line != null) {
                line.setState(newState);
                JOptionPane.showMessageDialog(this,
                        "State updated successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new NullPointerException("Line with ID " + id + " not found!");
            }

        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ManagerUI | " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID!", "Error", JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ManagerUI | Invalid ID!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating line state!", "Error", JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ManagerUI | Error updating line state: ");
        }
    }

   private void refreshLinePerformance() {
    linePerformanceModel.setRowCount(0);
    for (ProductLine line : manager.getProductLines()) {
        
        // حساب إجمالي المطلوب وإجمالي المنجز لمهام هذا الخط
        int totalRequired = 0;
        int totalProduced = 0;
        
        for (Task task : line.getProductLineTasks()) {
            totalRequired += task.getQuantity();
            totalProduced += task.getProductionProgress();
        }

        Object displayValue;
        
        // الشرط: إذا كان المنجز يساوي المطلوب (وكلاهما أكبر من صفر)
        if (totalProduced > 0 && totalProduced == totalRequired) {
            displayValue = totalProduced + " / " + totalRequired + " Done";
        } else {
            // عرض الرقم الحالي (مثلاً 2 / 10)
            displayValue = totalProduced + " / " + totalRequired;
        }

        linePerformanceModel.addRow(new Object[] { 
            line.getLineId(), 
            line.getLineName(), 
            displayValue 
        });
    }
}

    private void saveNotesAndRating() {

        ProductLine selectedLine = (ProductLine) lineBox.getSelectedItem();
        int rating = (int) ratingSpinner.getValue();
        String note = noteArea.getText();

        try {
            if (lineBox.getItemCount() == 0) {
                throw new IllegalStateException("No production lines available to save notes and rating.");
            }
            if (!note.isEmpty()) {
                try (FileWriter noteWriter = new FileWriter("notes.txt", true)) {
                    noteWriter.write("Line: " + selectedLine.getLineName() + " | Note: " + note + "\n");
                } catch (IOException e) {
                    FileManager.logError("ManagerUI | Error writing note to file: ");
                }
            }

            try (FileWriter ratingWriter = new FileWriter("rating.txt", true)) {
                ratingWriter.write("Line: " + selectedLine.getLineName() + " | Rating: " + rating + "\n");
            }

            JOptionPane.showMessageDialog(this, "Notes & Rating saved successfully!");
            noteArea.setText("");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving to file ", "Error", JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ManagerUI | Error saving to file");

        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ManagerUI | " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving notes and rating!", "Error", JOptionPane.ERROR_MESSAGE);
            FileManager.logError("ManagerUI | Error saving notes and rating!");
        }
    }
}