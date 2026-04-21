import javax.swing.*;
import java.awt.*;

public class LoginPage {

    public LoginPage() {

        JFrame f = new JFrame("Inventory Login");

        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 🌈 GRADIENT BACKGROUND PANEL
        JPanel bgPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0,161,155),
                        0, getHeight(), new Color(0,102,153)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        bgPanel.setLayout(new GridBagLayout());

        // 🔥 CARD
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(500, 360));
        card.setBackground(new Color(255,255,255,230)); // slight transparency
        card.setLayout(null);

        card.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        // 🏷 TITLE
        JLabel title = new JLabel("INVENTORY SYSTEM", JLabel.CENTER);
        title.setBounds(100, 20, 300, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel sub = new JLabel("Smart stock management", JLabel.CENTER);
        sub.setBounds(100, 50, 300, 20);
        sub.setForeground(Color.GRAY);

        // 🔥 USERNAME
        JTextField user = new JTextField();
        user.setBounds(80, 110, 340, 35);
        user.setBorder(BorderFactory.createMatteBorder(0,0,2,0,new Color(0,161,155)));

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(80, 90, 100, 20);

        // 🔥 PASSWORD
        JPasswordField pass = new JPasswordField();
        pass.setBounds(80, 180, 340, 35);
        pass.setBorder(BorderFactory.createMatteBorder(0,0,2,0,new Color(0,161,155)));

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(80, 160, 100, 20);

        // 🔥 BUTTON
        JButton login = new JButton("LOGIN");
        login.setBounds(150, 250, 200, 45);
        login.setBackground(new Color(0,161,155));
        login.setForeground(Color.WHITE);
        login.setFont(new Font("Segoe UI", Font.BOLD, 15));
        login.setFocusPainted(false);

        // HOVER EFFECT 🔥
        login.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                login.setBackground(new Color(0,130,125));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                login.setBackground(new Color(0,161,155));
            }
        });

        // LOGIN ACTION
        login.addActionListener(e -> {
            if(user.getText().isEmpty() || pass.getPassword().length==0){
                JOptionPane.showMessageDialog(f, "Enter credentials");
            } else {
                f.dispose();
                new InventorySystem();
            }
        });

        pass.addActionListener(e -> login.doClick());

        // ADD COMPONENTS
        card.add(title);
        card.add(sub);
        card.add(userLabel);
        card.add(user);
        card.add(passLabel);
        card.add(pass);
        card.add(login);

        bgPanel.add(card);

        f.setContentPane(bgPanel);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}