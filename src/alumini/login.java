/*
 * Modern Login UI for Campus Connect
 * Features: Gradient Background, Material Design Inputs, Hover Effects
 */
package alumini;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class login extends javax.swing.JFrame {

    // Logic Variables
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public login() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Login - Campus Connect");
        setSize(900, 550);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null); // Absolute Layout for precise control

        // ===================================================================
        // 1. LEFT PANEL (Gradient Branding)
        // ===================================================================
        GradientPanel pnlLeft = new GradientPanel(); 
        pnlLeft.setBounds(0, 0, 400, 550);
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
        pnlLeft.setBorder(new EmptyBorder(140, 40, 0, 40)); // Padding

        // Icon
        JLabel lblIcon = new JLabel("🎓");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 90));
        lblIcon.setForeground(Color.WHITE);
        lblIcon.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Title
        JLabel lblTitle = new JLabel("CAMPUS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle2 = new JLabel("CONNECT");
        lblTitle2.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle2.setForeground(new Color(204, 255, 255)); // Light Cyan
        lblTitle2.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Tagline
        JLabel lblTag = new JLabel("<html>Bridging the gap between<br>Students & Alumni</html>");
        lblTag.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTag.setForeground(new Color(230, 230, 230));
        lblTag.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlLeft.add(lblIcon);
        pnlLeft.add(Box.createVerticalStrut(10));
        pnlLeft.add(lblTitle);
        pnlLeft.add(lblTitle2);
        pnlLeft.add(Box.createVerticalStrut(15));
        pnlLeft.add(lblTag);

        add(pnlLeft);

        // ===================================================================
        // 2. RIGHT PANEL (Login Form)
        // ===================================================================
        JPanel pnlRight = new JPanel();
        pnlRight.setLayout(null);
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBounds(400, 0, 500, 550);

        // Close Button (X)
        JLabel lblClose = new JLabel("X");
        lblClose.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblClose.setForeground(new Color(150, 150, 150));
        lblClose.setBounds(460, 10, 30, 30);
        lblClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblClose.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { dispose(); }
        });
        pnlRight.add(lblClose);

        // Welcome Text
        JLabel lblWelcome = new JLabel("Welcome Back");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblWelcome.setForeground(new Color(51, 51, 51));
        lblWelcome.setBounds(60, 80, 300, 40);
        pnlRight.add(lblWelcome);

        JLabel lblSignIn = new JLabel("Sign in to continue");
        lblSignIn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSignIn.setForeground(Color.GRAY);
        lblSignIn.setBounds(60, 125, 200, 20);
        pnlRight.add(lblSignIn);

        // -- Username Input --
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(new Color(100, 100, 100));
        lblUser.setBounds(60, 180, 100, 20);
        pnlRight.add(lblUser);

        txtUsername = new JTextField();
        setupMaterialInput(txtUsername);
        txtUsername.setBounds(60, 200, 350, 35);
        pnlRight.add(txtUsername);

        // -- Password Input --
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(new Color(100, 100, 100));
        lblPass.setBounds(60, 260, 100, 20);
        pnlRight.add(lblPass);

        txtPassword = new JPasswordField();
        setupMaterialInput(txtPassword);
        txtPassword.setBounds(60, 280, 350, 35);
        pnlRight.add(txtPassword);

        // -- Login Button --
        btnLogin = new RoundedButton("LOGIN"); 
        btnLogin.setBounds(60, 360, 350, 45);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.addActionListener(e -> performLogin());
        pnlRight.add(btnLogin);

        // -- Home Link --
        JLabel lblHome = new JLabel("← Back to Home");
        lblHome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHome.setForeground(new Color(0, 102, 204));
        lblHome.setBounds(185, 430, 150, 20);
        lblHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblHome.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new MainHome().setVisible(true);
                dispose();
            }
        });
        pnlRight.add(lblHome);

        add(pnlRight);
    }

    // --- LOGIC: Authentication ---
    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try (Connection conn = dbconnection.getConnection()) {
            boolean loggedIn = false;

            // 1. Check Admin
            if (checkLogin(conn, "administrators", username, password)) {
                JOptionPane.showMessageDialog(this, "✅ Welcome Admin!");
                new Admin(username).setVisible(true);
                this.dispose();
                return;
            }
            // 2. Check Alumni
            if (checkLogin(conn, "alumni", username, password)) {
                JOptionPane.showMessageDialog(this, "✅ Welcome Alumni!");
                new chatinterface(username, "alumni").setVisible(true);
                this.dispose();
                return;
            }
            // 3. Check User
            if (checkLogin(conn, "users", username, password)) {
                JOptionPane.showMessageDialog(this, "✅ Welcome Student!");
                new chatinterface(username, "user").setVisible(true);
                this.dispose();
                return;
            }

            JOptionPane.showMessageDialog(this, "❌ Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    // Helper to avoid writing the same query 3 times
    private boolean checkLogin(Connection conn, String table, String user, String pass) throws Exception {
        String query = "SELECT * FROM " + table + " WHERE username = ? AND password = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, user);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    // --- STYLING METHODS ---
    private void setupMaterialInput(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));
        field.setBackground(Color.WHITE);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 102, 204)));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));
            }
        });
    }

    // ===================================================================
    // CUSTOM UI CLASSES
    // ===================================================================

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            int w = getWidth(), h = getHeight();
            GradientPaint gp = new GradientPaint(0, 0, new Color(0, 51, 102), w, h, new Color(0, 153, 255));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setForeground(Color.WHITE);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) {
                g2.setColor(new Color(0, 82, 164)); 
            } else {
                g2.setColor(new Color(0, 102, 204)); 
            }
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new login().setVisible(true));
    }
}