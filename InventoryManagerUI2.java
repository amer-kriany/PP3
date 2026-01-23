import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InventoryManagerUI2 extends JFrame {
    private ArrayList<Task> tasks;

    public InventoryManagerUI2() {

        tasks = new ArrayList<>();

        this.setTitle("Production Management System");
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        TasksPanel tasksPanel = new TasksPanel(tasks);
        TopPanel topPanel = new TopPanel();
        ButtonsPanel buttonsPanel = new ButtonsPanel(this, tasksPanel);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(tasksPanel, BorderLayout.CENTER);
        this. add(buttonsPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }
}