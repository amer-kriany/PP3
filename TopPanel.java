import javax.swing.*;
import java.awt.*;

public class TopPanel extends JPanel {

    private JLabel titleLabel;

    public TopPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(40, 44, 52));
        this.setPreferredSize(new Dimension(1000, 60));

        titleLabel = new JLabel("Production Line Management", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));

        this.add(titleLabel);
    }
}