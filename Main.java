import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main() {
        setTitle("Food Ordering System");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Food Ordering System", SwingConstants.CENTER);
        title.setBounds(50, 60, 400, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 30, 30));

        JLabel sub = new JLabel("Order your favorite food easily", SwingConstants.CENTER);
        sub.setBounds(50, 100, 400, 30);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(new Color(120, 120, 120));

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 170, 180, 40);
        loginBtn.setBackground(new Color(255, 102, 0));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(150, 230, 180, 40);
        signupBtn.setBackground(new Color(60, 60, 60));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFocusPainted(false);
        signupBtn.setOpaque(true);
        signupBtn.setBorderPainted(false);
        signupBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        loginBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });
        signupBtn.addActionListener(e -> {
            new SignUpFrame();
            dispose();
        });

        add(title);
        add(sub);
        add(loginBtn);
        add(signupBtn);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
