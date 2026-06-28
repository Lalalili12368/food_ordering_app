import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();

    public LoginFrame() {

        setTitle("Food Ordering - Login");
        setSize(420, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        getContentPane().setBackground(new Color(240, 242, 245));

        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBounds(40, 40, 330, 420);
        card.setBackground(Color.WHITE);

        JLabel title = new JLabel("Welcome Back");
        title.setBounds(85, 30, 250, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 30, 30));

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(40, 100, 200, 20);
        userLabel.setForeground(new Color(120, 120, 120));

        username.setBounds(40, 125, 250, 40);
        username.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        username.setBackground(new Color(245, 245, 245));

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(40, 190, 200, 20);
        passLabel.setForeground(new Color(120, 120, 120));

        password.setBounds(40, 215, 250, 40);
        password.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        password.setBackground(new Color(245, 245, 245));

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(40, 290, 250, 45);
        loginBtn.setBackground(new Color(255, 90, 0));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);

        JButton signupBtn = new JButton("Create New Account");
        signupBtn.setBounds(40, 350, 250, 30);
        signupBtn.setContentAreaFilled(false);
        signupBtn.setBorderPainted(false);
        signupBtn.setForeground(new Color(120, 120, 120));

        loginBtn.addActionListener(e -> loginUser());

        signupBtn.addActionListener(e -> {
            new SignUpFrame();
            dispose();
        });

        card.add(title);
        card.add(userLabel);
        card.add(username);
        card.add(passLabel);
        card.add(password);
        card.add(loginBtn);
        card.add(signupBtn);

        add(card);

        setVisible(true);
    }

    void loginUser() {
        try {
            Connection c = DB.getConnection();
            if (c == null)
                return;

            String user = username.getText().trim();
            String pass = String.valueOf(password.getPassword()).trim();

            PreparedStatement ps = c.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?");

            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                new MenuFrame(rs.getInt("id"));
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Wrong credentials!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}