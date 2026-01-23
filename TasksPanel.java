import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TasksPanel extends JPanel {

    private ArrayList<Task> tasks;
    private JTable table;
    private DefaultTableModel tableModel;

    public TasksPanel(ArrayList<Task> tasks) {

        this.tasks = (tasks != null) ? tasks : new ArrayList<>();

        initUI();
        loadTasks();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        String[] columns = {
                "Task Name",
                "Client Name",
                "Desired Product",
                "Status",
                "Start Appointment",
                "Deadline"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
    }
    public void addTask(Task task){
        if(task !=null){
            tasks.add(task);
            refreshTable();
        }

    }

    public void loadTasks() {
        tableModel.setRowCount(0);

        if (tasks.isEmpty()) {
            return;
        }

        for (Task task : tasks) {
            Object[] row = {
                    task.getTaskName(),
                    task.getClientName(),
                    task.getDesireProduct(),
                    task.getStatus().name(),
                    task.getStartAppointment(),
                    task.getDeadLine()
            };
            tableModel.addRow(row);
        }
    }

    public void refreshTable() {
        loadTasks();
    }

    public JTable getTable() {
        return table;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}