import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// ====== Main ======
public class FactoryUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FactoryFrame());
    }
}

// ====== JFrame ======
class FactoryFrame extends JFrame {
    private List<ProductLine> productLines;
    private JPanel taskPanelContainer;

    public FactoryFrame() {
        setTitle("Factory Production System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ====== Product Lines setup ======
        productLines = new ArrayList<>();
        ProductLine tabletLine = new ProductLine(0, "Tablet", ProductLine.State.ACTIVE);
        ProductLine syrupLine = new ProductLine(1, "Syrup", ProductLine.State.ACTIVE);

        productLines.add(tabletLine);
        productLines.add(syrupLine);

        tabletLine.start();
        syrupLine.start();

        // ====== Layout ======
        setLayout(new BorderLayout());

        // ====== Input Panel ======
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Task"));

        JTextField clientField = new JTextField();
        JTextField productField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField deadlineField = new JTextField("dd-MM-yyyy HH:mm:ss");
        JComboBox<String> lineBox = new JComboBox<>(new String[]{"Tablet", "Syrup"});

        JButton addTaskButton = new JButton("Add Task");

        inputPanel.add(new JLabel("Client Name:"));
        inputPanel.add(clientField);
        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(productField);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Deadline:"));
        inputPanel.add(deadlineField);
        inputPanel.add(new JLabel("Select Line:"));
        inputPanel.add(lineBox);
        inputPanel.add(new JLabel());
        inputPanel.add(addTaskButton);

        add(inputPanel, BorderLayout.NORTH);

        // ====== Task Panel Container ======
        taskPanelContainer = new JPanel();
        taskPanelContainer.setLayout(new BoxLayout(taskPanelContainer, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(taskPanelContainer);
        add(scrollPane, BorderLayout.CENTER);

        // ====== Add Task Action ======
        addTaskButton.addActionListener(e -> {
            try {
                String client = clientField.getText().trim();
                String product = productField.getText().trim();
                int qty = Integer.parseInt(quantityField.getText().trim());
                String deadline = deadlineField.getText().trim();
                String lineName = (String) lineBox.getSelectedItem();

                ProductLine selectedLine = null;
                for (ProductLine line : productLines) {
                    if (line.getLineName().equalsIgnoreCase(lineName)) {
                        selectedLine = line;
                        break;
                    }
                }

                if (selectedLine == null) {
                    JOptionPane.showMessageDialog(this, "Selected line not found!");
                    return;
                }

                Task newTask = new Task(client, product, qty, deadline, taskPanelContainer);
                selectedLine.addTask(newTask);
                taskPanelContainer.revalidate();
                taskPanelContainer.repaint();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}

// ====== ProductLine ======
class ProductLine extends Thread {
    private int lineId;
    private String lineName;
    enum State { ACTIVE, STOP, MAINTENANCE }
    private State state;
    private List<Task> productLineTasks;

    public ProductLine(int lineId, String lineName, State state) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.state = state;
        this.productLineTasks = new ArrayList<>();
    }

    public String getLineName() { return lineName; }

    public void addTask(Task task) {
        synchronized (productLineTasks) {
            productLineTasks.add(task);
        }
    }

    private void executeTask(Task task) throws Exception {
        task.start();
        for (int i = 1; i <= task.getQuantity(); i++) {
            int progress = (i * 100) / task.getQuantity();
            task.setProductionProgressPercentege(progress);
            Thread.sleep(200); // simulate time
        }
        task.complete();
    }

    @Override
    public void run() {
        while (state == State.ACTIVE) {
            synchronized (productLineTasks) {
                for (Task task : productLineTasks) {
                    if (task.getStatus() == Task.TaskStatus.PENDING && !task.isStarted()) {
                        new Thread(() -> {
                            try {
                                executeTask(task);
                            } catch (Exception e) {
                                System.err.println("Error in Task " + task.getTaskID() + ": " + e.getMessage());
                            }
                        }).start();
                    }
                }
            }

            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }
}

// ====== Task ======
class Task {
    private String clientName;
    private String desireProduct;
    private int quantity;
    static int counter = 0;
    int taskID;
    private int productionProgressPercentege = 0;
    private boolean started = false;

    enum TaskStatus { PENDING, IN_PROGRESS, COMPLETED, CANCELED }
    private TaskStatus status;

    private JProgressBar progressBar;

    public Task(String clientName, String desireProduct, int quantity, String endDate, JPanel parentPanel) {
        this.clientName = clientName;
        this.desireProduct = desireProduct;
        this.quantity = quantity;
        this.taskID = ++counter;
        this.status = TaskStatus.PENDING;

        // ====== Progress Bar ======
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("Task " + taskID + ": 0%");
        parentPanel.add(progressBar);
    }

    public boolean isStarted() { return started; }
    public int getTaskID() { return taskID; }
    public TaskStatus getStatus() { return status; }
    public int getQuantity() { return quantity; }
    public void setProductionProgressPercentege(int progress) {
        this.productionProgressPercentege = progress;
        progressBar.setValue(progress);
        progressBar.setString("Task " + taskID + ": " + progress + "%");
    }
    public void start() { this.status = TaskStatus.IN_PROGRESS; this.started = true; }
    public void complete() { this.status = TaskStatus.COMPLETED; this.productionProgressPercentege = 100; }
}
