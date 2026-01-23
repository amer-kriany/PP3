import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddTaskButton extends JDialog {
    private static int nextId = 1;

    private JTextField nameField;
    private JTextField clientField;
    private JTextField productField;
    private JTextField quantityField;
    private JTextField deadlineField;
    private JComboBox<Status.taskStatus> statusBox;

    private Task createdTask;

    public AddTaskButton(JFrame parent) {
        super(parent, true);
        this.setTitle("Add New Task");
        this.setModal(true);
        this.setSize(700, 420);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(10, 5, 5, 5));


        nameField = new JTextField();
        clientField = new JTextField();
        productField = new JTextField();
        quantityField = new JTextField(10);
        statusBox = new JComboBox<>(Status.taskStatus.values());


        this.add(new JLabel("Task Name:"));
        this.add(nameField);

        this.add(new JLabel("Client Name:"));
        this.add(clientField);

        this.add(new JLabel("Product:"));
        this.add(productField);

        this.add(new JLabel("Quantity:"));
        this.add(quantityField);

        this.add(new JLabel("Deadline (dd-MM-yyyy HH:mm:ss):"));
        deadlineField = new JTextField(10);
        this.add(deadlineField);

        this.add(new JLabel("Status:"));
        this.add(statusBox);


        JButton okButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        this.add(okButton);
        this.add(cancelButton);

        okButton.addActionListener(e -> createTask());
        cancelButton.addActionListener(e -> dispose());
    }

    

    private void createTask() {

        if (nameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Task name is required");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Quantity must be a number");
            return;
        }


        String deadline = deadlineField.getText();
        LocalDateTime parsedDeadline = null;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            parsedDeadline = LocalDateTime.parse(deadline, formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Please use dd-MM-yyyy HH:mm:ss.");
            return;
        }


        createdTask = new Task(
               
                nameField.getText(),
                clientField.getText(),
                productField.getText(),
                quantity,
                
                parsedDeadline.toString()
        );

        dispose();
    }

    public Task getCreatedTask() {
        return createdTask;
    }
}