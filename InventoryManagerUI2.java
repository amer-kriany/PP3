import javax.swing.*;
import java.awt.*;

public class InventoryManagerUI2 extends JFrame {
    public InventoryManagerUI2() {
        setTitle("Inventory Manager UI 2");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Use InventoryManagerUI instead.", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
