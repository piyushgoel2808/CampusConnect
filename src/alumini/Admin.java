/*
 * Modern & Fixed Admin Dashboard
 * Features: Add New Functionality, Auto-Resizing Table, Auto-Refresh
 */
package alumini;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Admin extends javax.swing.JFrame {

    // Logic
    private String adminUser;
    
    // UI Components
    private JPanel pnlSideBar;
    private JLabel lblTitle, lblWelcome;
    private JTable dynamicTable;
    private DefaultTableModel tableModel;
    
    // Buttons
    private JButton btnAnnouncements, btnAlumni, btnEvents, btnJobs, btnFAQ, btnUsers, btnFeedback, btnLogout, btnEditProfile;

    // Current Mode (To know which table is active for Edit/Delete)
    private String currentTable = "announcements"; 

    public Admin() { initComponents(); }

    // ✅ FIXED CONSTRUCTOR: Accepts username from Login
    public Admin(String username) {
        this.adminUser = username;
        initComponents();
        lblWelcome.setText("Welcome, " + username);
        loadData("announcements"); // Load default table
    }

    private void initComponents() {
        setTitle("Campus Connect - Admin Dashboard");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===================================================================
        // 1. LEFT SIDEBAR
        // ===================================================================
        pnlSideBar = new JPanel();
        pnlSideBar.setPreferredSize(new Dimension(260, 0));
        pnlSideBar.setBackground(new Color(33, 43, 54));
        pnlSideBar.setLayout(new BoxLayout(pnlSideBar, BoxLayout.Y_AXIS));
        pnlSideBar.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Header
        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlLogo.setBackground(new Color(33, 43, 54));
        lblTitle = new JLabel("ADMIN PANEL");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        pnlLogo.add(lblTitle);
        
        pnlSideBar.add(pnlLogo);
        pnlSideBar.add(Box.createVerticalStrut(30));

        // Navigation Buttons
        btnAnnouncements = createMenuButton("📢 Announcements");
        btnAlumni = createMenuButton("🎓 Alumni Records");
        btnEvents = createMenuButton("📅 Events Manager");
        btnJobs = createMenuButton("💼 Job Portal");
        btnFAQ = createMenuButton("❓ FAQs");
        btnUsers = createMenuButton("👥 User Management");
        btnFeedback = createMenuButton("📝 View Feedback");

        // Add Actions
        btnAnnouncements.addActionListener(e -> loadData("announcements"));
        btnAlumni.addActionListener(e -> loadData("alumni"));
        btnEvents.addActionListener(e -> loadData("events"));
        btnJobs.addActionListener(e -> loadData("job_opportunities"));
        btnFAQ.addActionListener(e -> loadData("faqs"));
        btnUsers.addActionListener(e -> loadData("users"));
        btnFeedback.addActionListener(e -> loadData("feedback"));

        pnlSideBar.add(btnAnnouncements);
        pnlSideBar.add(btnAlumni);
        pnlSideBar.add(btnEvents);
        pnlSideBar.add(btnJobs);
        pnlSideBar.add(btnFAQ);
        pnlSideBar.add(btnUsers);
        pnlSideBar.add(btnFeedback);
        
        pnlSideBar.add(Box.createVerticalGlue());

        // Settings Buttons
        btnEditProfile = createMenuButton("⚙️ Settings");
        btnEditProfile.addActionListener(e -> {
            if(adminUser != null) new AdminProfileEdit(adminUser).setVisible(true);
        });
        
        btnLogout = createMenuButton("🚪 Logout");
        btnLogout.setBackground(new Color(192, 57, 43));
        btnLogout.addActionListener(e -> { this.dispose(); new MainHome().setVisible(true); });

        pnlSideBar.add(btnEditProfile);
        pnlSideBar.add(btnLogout);

        // ===================================================================
        // 2. MAIN CONTENT (Header + Table)
        // ===================================================================
        JPanel pnlRight = new JPanel(new BorderLayout());
        
        // Header Bar
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        lblWelcome = new JLabel("Welcome, Admin");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblWelcome.setBorder(new EmptyBorder(0, 20, 0, 0));
        pnlHeader.add(lblWelcome, BorderLayout.WEST);
        
        // Action Toolbar
        JPanel pnlToolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlToolbar.setBackground(Color.WHITE);
        
        JButton btnAdd = new JButton("➕ Add New");
        JButton btnDelete = new JButton("🗑 Delete Selected");
        JButton btnRefresh = new JButton("🔄 Refresh");
        
        styleToolbarButton(btnAdd, new Color(0, 153, 76));
        styleToolbarButton(btnDelete, new Color(204, 0, 0));
        styleToolbarButton(btnRefresh, new Color(0, 102, 204));
        
        btnRefresh.addActionListener(e -> loadData(currentTable));
        btnDelete.addActionListener(e -> deleteSelectedRow());
        
        // ✅ NEW: Add Button Logic
        btnAdd.addActionListener(e -> performAdd());

        pnlToolbar.add(btnAdd);
        pnlToolbar.add(btnDelete);
        pnlToolbar.add(btnRefresh);
        pnlHeader.add(pnlToolbar, BorderLayout.EAST);

        pnlRight.add(pnlHeader, BorderLayout.NORTH);

        // Dynamic Table Area
        dynamicTable = new JTable();
        dynamicTable.setRowHeight(30);
        dynamicTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dynamicTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        dynamicTable.getTableHeader().setBackground(new Color(230, 230, 230));
        
        JScrollPane scrollPane = new JScrollPane(dynamicTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        pnlRight.add(scrollPane, BorderLayout.CENTER);

        add(pnlSideBar, BorderLayout.WEST);
        add(pnlRight, BorderLayout.CENTER);
    }

    // --- LOGIC: Load Data Dynamically ---
    private void loadData(String tableName) {
        this.currentTable = tableName;
        
        try (Connection conn = dbconnection.getConnection()) {
            String query = "SELECT * FROM " + tableName;
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            
            // 1. Get Column Names
            int colCount = meta.getColumnCount();
            String[] columns = new String[colCount];
            for (int i = 1; i <= colCount; i++) {
                columns[i-1] = meta.getColumnName(i);
            }
            
            // 2. Get Data Rows
            tableModel = new DefaultTableModel(columns, 0);
            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    row[i-1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }
            
            dynamicTable.setModel(tableModel);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    // ✅ LOGIC: ADD NEW RECORD (Dynamic Inputs)
    private void performAdd() {
        // 1. Announcements
        if (currentTable.equals("announcements")) {
            JTextField title = new JTextField();
            JTextArea desc = new JTextArea(5, 20);
            Object[] message = {"Title:", title, "Description:", new JScrollPane(desc)};

            int option = JOptionPane.showConfirmDialog(this, message, "Add Announcement", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                runInsert("INSERT INTO announcements (title, description, posted_by, date_posted) VALUES (?, ?, ?, NOW())", 
                        title.getText(), desc.getText(), adminUser);
            }
        
        // 2. Events
        } else if (currentTable.equals("events")) {
            JTextField name = new JTextField();
            JTextField date = new JTextField("YYYY-MM-DD");
            JTextField venue = new JTextField();
            JTextArea desc = new JTextArea(3, 20);
            Object[] message = {"Event Name:", name, "Date (YYYY-MM-DD):", date, "Venue:", venue, "Description:", new JScrollPane(desc)};

            int option = JOptionPane.showConfirmDialog(this, message, "Add Event", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                runInsert("INSERT INTO events (event_name, event_date, event_venue, description) VALUES (?, ?, ?, ?)", 
                        name.getText(), date.getText(), venue.getText(), desc.getText());
            }

        // 3. Job Opportunities
        } else if (currentTable.equals("job_opportunities")) {
            JTextField title = new JTextField();
            JTextField company = new JTextField();
            JTextField loc = new JTextField();
            Object[] message = {"Job Title:", title, "Company:", company, "Location:", loc};

            int option = JOptionPane.showConfirmDialog(this, message, "Add Job", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                runInsert("INSERT INTO job_opportunities (title, company, location, posted_by) VALUES (?, ?, ?, ?)", 
                        title.getText(), company.getText(), loc.getText(), adminUser);
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Adding items to '" + currentTable + "' is handled via Sign Up or Automatic processes.");
        }
    }

    // Helper for Inserts
    private void runInsert(String sql, String... params) {
        try (Connection conn = dbconnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setString(i + 1, params[i]);
            }
            ps.executeUpdate();
            loadData(currentTable); // Refresh table
            JOptionPane.showMessageDialog(this, "Success!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding record: " + e.getMessage());
        }
    }

    // --- LOGIC: Delete Row ---
    private void deleteSelectedRow() {
        int row = dynamicTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }
        
        String id = dynamicTable.getValueAt(row, 0).toString();
        String primaryKeyCol = dynamicTable.getColumnName(0);
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete ID: " + id + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = dbconnection.getConnection()) {
                String sql = "DELETE FROM " + currentTable + " WHERE " + primaryKeyCol + " = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, id);
                ps.executeUpdate();
                loadData(currentTable); // Refresh
                JOptionPane.showMessageDialog(this, "Deleted Successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    // --- UI Helpers ---
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(260, 50));
        btn.setBackground(new Color(33, 43, 54));
        btn.setForeground(new Color(189, 195, 199));
        
        Font font = new Font("Segoe UI Emoji", Font.PLAIN, 14);
        if (font.canDisplayUpTo("📢") != -1) font = new Font("Dialog", Font.PLAIN, 14);
        btn.setFont(font);
        
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn.getBackground().getRed() != 0) btn.setBackground(new Color(44, 62, 80));
            }
            public void mouseExited(MouseEvent e) {
                if (btn.getBackground().getRed() != 0) btn.setBackground(new Color(33, 43, 54)); 
            }
        });
        return btn;
    }
    
    private void styleToolbarButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Admin("admin").setVisible(true));
    }
}