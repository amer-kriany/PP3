import javax.swing.*;
import java.awt.*;

public class SupervisorSelectionUI extends JFrame {
    private ProductionManager pm;

    public SupervisorSelectionUI(ProductionManager pm) {
        this.pm = pm;
        
        setTitle("Access Control - Supervisor");
        setSize(550, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Panel setup
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(new Color(230, 235, 240));

        // Inventory Button
        JButton btnInventory = createStyledButton("Inventory System", new Color(41, 128, 185));
        btnInventory.addActionListener(e -> {
            new InventoryManagerUI(pm).setVisible(true);
            this.dispose();
           
        });

        // Production Button
        JButton btnProduction = createStyledButton("Production Lines", new Color(39, 174, 96));
        btnProduction.addActionListener(e -> {
            new ProductionManagerGUI(this.pm).setVisible(true);
            this.dispose();
        });

        mainPanel.add(btnInventory);
        mainPanel.add(btnProduction);
        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(color.darker(), 2));
        return btn;
    }
}