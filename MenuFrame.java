import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class MenuFrame extends JFrame {

   JPanel mainPanel = new JPanel();
   ArrayList<CartItem> cart = new ArrayList<>();
   int userId;

   String selectedCategory = "All";
   JLabel cartBadge = new JLabel("0 items", SwingConstants.CENTER);

   public MenuFrame(int userId) {

      this.userId = userId;

      setTitle("CravingHub - Menu");
      setSize(960, 720);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setLayout(null);
      setResizable(false);

      getContentPane().setBackground(new Color(245, 246, 248));

      JPanel topBar = new JPanel(null);
      topBar.setBounds(0, 0, 960, 65);
      topBar.setBackground(new Color(255, 90, 0));

      JLabel brand = new JLabel("CravingHub");
      brand.setFont(new Font("Segoe UI", Font.BOLD, 24));
      brand.setForeground(Color.WHITE);
      brand.setBounds(25, 14, 220, 35);

      JPanel cartPanel = new JPanel(new BorderLayout());
      cartPanel.setBounds(720, 12, 200, 40);
      cartPanel.setBackground(new Color(255, 120, 40));

      cartBadge.setForeground(Color.WHITE);
      cartBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));

      JButton cartBtn = new JButton("Cart");
      cartBtn.setBackground(Color.WHITE);
      cartBtn.setForeground(new Color(255, 90, 0));
      cartBtn.setFocusPainted(false);
      cartBtn.setBorderPainted(false);

      cartBtn.addActionListener(e -> {
         if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
         } else {
            new CartFrame(userId, cart);
         }
      });

      cartPanel.add(cartBadge, BorderLayout.WEST);
      cartPanel.add(cartBtn, BorderLayout.CENTER);

      topBar.add(brand);
      topBar.add(cartPanel);
      add(topBar);

      JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
      filterPanel.setBounds(0, 65, 960, 45);
      filterPanel.setBackground(Color.WHITE);

      String[] categories = { "All", "Burger", "Pizza", "Lunch", "Snacks", "Drinks" };

      for (String cat : categories) {

         JButton b = new JButton(cat);
         b.setFocusPainted(false);
         b.setContentAreaFilled(false);
         b.setBorderPainted(false);
         b.setFont(new Font("Segoe UI", Font.BOLD, 12));
         b.setForeground(new Color(80, 80, 80));

         b.addActionListener(e -> {
            selectedCategory = cat;

            for (Component c : filterPanel.getComponents()) {
               c.setForeground(new Color(80, 80, 80));
            }

            b.setForeground(new Color(255, 90, 0));
            loadFood();
         });

         filterPanel.add(b);
      }

      add(filterPanel);

      mainPanel.setLayout(new GridLayout(0, 3, 18, 18));
      mainPanel.setBackground(new Color(245, 246, 248));
      mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

      JScrollPane scroll = new JScrollPane(mainPanel);
      scroll.setBounds(0, 110, 945, 570);
      scroll.setBorder(null);

      add(scroll);

      setVisible(true);
      loadFood();
   }

   void updateCartBadge() {
      int count = 0;
      for (CartItem c : cart)
         count += c.qty;
      cartBadge.setText(count + " items");
   }

   void loadFood() {

      try {

         Connection c = DB.getConnection();
         if (c == null)
            return;

         String sql = selectedCategory.equals("All")
               ? "SELECT * FROM food ORDER BY category"
               : "SELECT * FROM food WHERE category=?";

         PreparedStatement ps = c.prepareStatement(sql);

         if (!selectedCategory.equals("All")) {
            ps.setString(1, selectedCategory);
         }

         ResultSet rs = ps.executeQuery();

         mainPanel.removeAll();

         while (rs.next()) {

            String name = rs.getString("name");

            // ONLY REMOVED ITEMS
            if (name.equalsIgnoreCase("Margherita Pizza") ||
                  name.equalsIgnoreCase("Chicken Wings") ||
                  name.equalsIgnoreCase("Cold Coffee") ||
                  name.equalsIgnoreCase("Beef Curry Rice") ||
                  name.equalsIgnoreCase("Fish Curry Rice") ||
                  name.equalsIgnoreCase("Cheese Pizza") ||
                  name.equalsIgnoreCase("Spring Rolls")) {
               continue;
            }

            String cat = rs.getString("category");
            double price = rs.getDouble("price");
            String img = rs.getString("image");

            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);

            JLabel imgLabel = new JLabel("Loading...", SwingConstants.CENTER);
            imgLabel.setPreferredSize(new Dimension(260, 140));
            imgLabel.setOpaque(true);
            imgLabel.setBackground(new Color(235, 235, 235));

            new Thread(() -> {
               try {
                  File file = new File(img);

                  if (file.exists()) {
                     BufferedImage bi = ImageIO.read(file);

                     int w = bi.getWidth();
                     int h = bi.getHeight();

                     int targetW = 260;
                     int targetH = (h * targetW) / w;

                     Image scaled = bi.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);

                     SwingUtilities.invokeLater(() -> {
                        imgLabel.setIcon(new ImageIcon(scaled));
                        imgLabel.setText("");
                     });

                  } else {
                     SwingUtilities.invokeLater(() -> imgLabel.setText("No Image"));
                  }

               } catch (Exception ex) {
                  SwingUtilities.invokeLater(() -> imgLabel.setText("No Image"));
               }
            }).start();

            JPanel info = new JPanel(new GridLayout(3, 1));
            info.setBackground(Color.WHITE);

            JLabel n = new JLabel(name);
            n.setFont(new Font("Segoe UI", Font.BOLD, 13));

            JLabel c2 = new JLabel(cat);
            c2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            c2.setForeground(Color.GRAY);

            JLabel p = new JLabel("Tk " + (int) price);
            p.setForeground(new Color(0, 160, 80));
            p.setFont(new Font("Segoe UI", Font.BOLD, 13));

            info.add(c2);
            info.add(n);
            info.add(p);

            JButton add = new JButton("Add");
            add.setBackground(new Color(255, 90, 0));
            add.setForeground(Color.WHITE);
            add.setFocusPainted(false);
            add.setBorderPainted(false);

            add.addActionListener(e -> {

               boolean exists = false;

               for (CartItem ci : cart) {
                  if (ci.name.equals(name)) {
                     ci.qty++;
                     exists = true;
                     break;
                  }
               }

               if (!exists) {
                  cart.add(new CartItem(name, price, 1));
               }

               updateCartBadge();

               add.setText("Added");
               add.setBackground(new Color(0, 170, 80));

               Timer t = new Timer(700, ev -> {
                  add.setText("Add");
                  add.setBackground(new Color(255, 90, 0));
               });

               t.setRepeats(false);
               t.start();
            });

            JPanel bottom = new JPanel(new BorderLayout());
            bottom.setBackground(Color.WHITE);
            bottom.add(add, BorderLayout.CENTER);

            card.add(imgLabel, BorderLayout.NORTH);
            card.add(info, BorderLayout.CENTER);
            card.add(bottom, BorderLayout.SOUTH);

            mainPanel.add(card);
         }

         mainPanel.revalidate();
         mainPanel.repaint();

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}