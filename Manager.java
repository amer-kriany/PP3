import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Manager extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JComboBox<ProductLine.State> stateBox;
    private static ArrayList<ProductLine> lines = new ArrayList<>();

    public Manager() {
        setTitle("Manager Panel");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        Font font = new Font("Arial", Font.BOLD, 20);

        
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

        JButton changeStateButton = new JButton("Change State");
        changeStateButton.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        add(changeStateButton, gbc);

        
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

            ProductLine line = ProductLine.addLine(id, name, state);
            lines.add(line);

            JOptionPane.showMessageDialog(this,
                    "Line added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid ID!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        }  catch (Exception e) {
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

            for (ProductLine line : lines) {
                if (line.lineId == id) {
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
        SwingUtilities.invokeLater(() -> new Manager().setVisible(true));
    }
}