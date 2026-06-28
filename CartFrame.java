import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class CartFrame extends JFrame {

    ArrayList<CartItem> cart;
    JLabel totalLabel = new JLabel("Total: Tk 0");
    JPanel listPanel = new JPanel();
    int userId;

    public CartFrame(int userId, ArrayList<CartItem> cart) {

        this.userId = userId;
        this.cart = cart;

        setTitle("Your Cart");
        setSize(520, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(new Color(248, 249, 250));

        JLabel title = new JLabel("Review Your Order", SwingConstants.CENTER);
        title.setBounds(110, 20, 280, 32);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(40, 40, 40));

        listPanel.setLayout(new GridLayout(0, 1, 0, 10));
        listPanel.setBackground(new Color(248, 249, 250));

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBounds(30, 70, 445, 350);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(248, 249, 250));

        totalLabel.setBounds(30, 440, 300, 30);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(new Color(40, 40, 40));

        JButton orderBtn = new JButton("Place Order");
        orderBtn.setBounds(155, 490, 185, 45);
        orderBtn.setBackground(new Color(255, 90, 0));
        orderBtn.setForeground(Color.WHITE);
        orderBtn.setFocusPainted(false);
        orderBtn.setBorderPainted(false);
        orderBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        orderBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        orderBtn.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!");
                return;
            }
            placeOrder(calculateTotal());
        });

        add(title);
        add(scroll);
        add(totalLabel);
        add(orderBtn);

        renderCartItems();
        setVisible(true);
    }

    private double calculateTotal() {
        double total = 0;
        for (CartItem item : cart) {
            total += item.price * item.qty;
        }
        return total;
    }

    private void renderCartItems() {

        listPanel.removeAll();

        double total = 0;

        if (cart == null || cart.isEmpty()) {

            JLabel empty = new JLabel("Your cart is empty!", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            empty.setForeground(new Color(130, 130, 130));
            listPanel.add(empty);

        } else {

            for (CartItem item : cart) {

                JPanel row = new JPanel(new BorderLayout(10, 0));
                row.setBackground(Color.WHITE);
                row.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

                JPanel left = new JPanel(new GridLayout(2, 1));
                left.setBackground(Color.WHITE);

                JLabel name = new JLabel(item.name);
                name.setFont(new Font("Segoe UI", Font.BOLD, 14));

                JLabel details = new JLabel("Qty: " + item.qty + " | Tk " + (int) item.price);
                details.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                details.setForeground(new Color(120, 120, 120));

                left.add(name);
                left.add(details);

                JLabel price = new JLabel("Tk " + (int) (item.price * item.qty), SwingConstants.RIGHT);
                price.setFont(new Font("Segoe UI", Font.BOLD, 14));
                price.setForeground(new Color(0, 160, 80));

                JButton minusBtn = new JButton("-");
                JButton plusBtn = new JButton("+");
                JButton removeBtn = new JButton("X");

                styleBtn(minusBtn);
                styleBtn(plusBtn);

                removeBtn.setForeground(new Color(210, 50, 50));
                removeBtn.setBackground(new Color(255, 235, 235));
                removeBtn.setFocusPainted(false);

                plusBtn.addActionListener(e -> {
                    item.qty++;
                    updateItem(item);
                    renderCartItems();
                });

                minusBtn.addActionListener(e -> {
                    if (item.qty > 1) {
                        item.qty--;
                        updateItem(item);
                    } else {
                        deleteItem(item);
                        cart.remove(item);
                    }
                    renderCartItems();
                });

                removeBtn.addActionListener(e -> {
                    deleteItem(item);
                    cart.remove(item);
                    renderCartItems();
                });

                JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                right.setBackground(Color.WHITE);
                right.add(price);
                right.add(minusBtn);
                right.add(plusBtn);
                right.add(removeBtn);

                row.add(left, BorderLayout.WEST);
                row.add(right, BorderLayout.EAST);

                listPanel.add(row);

                total += item.price * item.qty;
            }
        }

        totalLabel.setText("Total: Tk " + (int) total);

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void styleBtn(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    void updateItem(CartItem item) {
        try {
            Connection c = DB.getConnection();

            PreparedStatement ps = c.prepareStatement(
                    "UPDATE order_items SET quantity=? WHERE food_name=? " +
                            "AND order_id IN (SELECT MAX(id) FROM orders WHERE user_id=?)");

            ps.setInt(1, item.qty);
            ps.setString(2, item.name);
            ps.setInt(3, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void deleteItem(CartItem item) {
        try {
            Connection c = DB.getConnection();

            PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM order_items WHERE food_name=? " +
                            "AND order_id IN (SELECT MAX(id) FROM orders WHERE user_id=?)");

            ps.setString(1, item.name);
            ps.setInt(2, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void placeOrder(double total) {
        try {
            Connection c = DB.getConnection();

            if (c == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!");
                return;
            }

            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO orders(user_id, total) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, userId);
            ps.setDouble(2, total);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int orderId = 0;
            if (rs.next())
                orderId = rs.getInt(1);

            for (CartItem item : cart) {
                PreparedStatement ps2 = c.prepareStatement(
                        "INSERT INTO order_items(order_id, food_name, quantity, price) VALUES (?, ?, ?, ?)");

                ps2.setInt(1, orderId);
                ps2.setString(2, item.name);
                ps2.setInt(3, item.qty);
                ps2.setDouble(4, item.price);

                ps2.executeUpdate();
            }

            cart.clear();
            JOptionPane.showMessageDialog(this, "Order placed successfully!");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Order failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}