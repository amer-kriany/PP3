import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ButtonsPanel extends JPanel {

    private TasksPanel tasksPanel;
    private JFrame parentFrame;
    private ArrayList<Task> tasks;

    public ButtonsPanel(JFrame parentFrame,TasksPanel tasksPanel) {
        this.parentFrame=parentFrame;
        this.tasksPanel = tasksPanel;

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton addButton = new JButton("Add Task");
        JButton deleteButton = new JButton("Delete Task");

        this.add(addButton);
        this.add(deleteButton);

        // actions
        addButton.addActionListener(e -> openAddDialog());
        deleteButton.addActionListener(e -> deleteTask());
    }

    private void openAddDialog() {

        AddTaskButton dialog =
                new AddTaskButton(parentFrame);

        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);

        Task newTask = dialog.getCreatedTask();

        if (newTask != null) {
            tasksPanel.addTask(newTask);
        }
        else {
            JOptionPane.showMessageDialog(parentFrame,"All fields are required","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }


    private void deleteTask() {
        int selectedRow = tasksPanel.getTable().getSelectedRow();

        if (selectedRow != -1) {
            tasks.remove(selectedRow);
            tasksPanel.loadTasks();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a task first",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}

