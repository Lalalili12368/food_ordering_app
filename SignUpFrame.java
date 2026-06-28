import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignUpFrame extends JFrame {

    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField();

    public SignUpFrame() {

        setTitle("Food Ordering - Sign Up");
        setSize(420, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        getContentPane().setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Create Account");
        title.setBounds(120, 40, 250, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 30, 30));

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(70, 120, 200, 20);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        username.setBounds(70, 145, 270, 40);
        username.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(70, 210, 200, 20);
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        password.setBounds(70, 235, 270, 40);
        password.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton signupBtn = new JButton("Create Account");
        signupBtn.setBounds(70, 310, 270, 45);
        signupBtn.setBackground(new Color(255, 102, 0));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFocusPainted(false);
        signupBtn.setBorderPainted(false);
        signupBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton loginBtn = new JButton("Already have account? Login");
        loginBtn.setBounds(70, 370, 270, 30);
        loginBtn.setBorderPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setForeground(new Color(80, 80, 80));
        loginBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        add(title);
        add(userLabel);
        add(username);
        add(passLabel);
        add(password);
        add(signupBtn);
        add(loginBtn);

        signupBtn.addActionListener(e -> registerUser());

        loginBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    void registerUser() {

        try {
            Connection con = DB.getConnection();

            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!");
                return;
            }

            String user = username.getText().trim();
            String pass = String.valueOf(password.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(username, password, role) VALUES (?, ?, 'user')");

            ps.setString(1, user);
            ps.setString(2, pass);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account created successfully!");

            new LoginFrame();
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}