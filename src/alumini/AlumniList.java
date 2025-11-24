/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumini;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author piyush
 */
public class AlumniList extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private String currentUser;
    
    // Search Components
    private JComboBox<String> comboFilter;
    private JTextField txtSearch;

    public AlumniList(String myUser) {
        this.currentUser = myUser;
        setTitle("Alumni Directory - Find Your Seniors");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // =================================================================
        // 1. TOP PANEL (Search Bar)
        // =================================================================
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Title
        JLabel title = new JLabel("🎓 Alumni Directory", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(33, 43, 54));
        
        // Search Controls Container
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Search By: ");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));

        String[] filters = {"Name", "Company", "Batch", "Designation"};
        comboFilter = new JComboBox<>(filters);
        comboFilter.setBackground(Color.WHITE);

        txtSearch = new JTextField(15);
        
        JButton btnSearch = new JButton("🔍 Search");
        btnSearch.setBackground(new Color(0, 102, 204));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(e -> searchAlumni());

        JButton btnReset = new JButton("❌ Reset");
        btnReset.setBackground(new Color(200, 50, 50));
        btnReset.setForeground(Color.WHITE);
        btnReset.addActionListener(e -> resetTable());

        searchPanel.add(lblSearch);
        searchPanel.add(comboFilter);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnReset);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // =================================================================
        // 2. CENTER PANEL (Table)
        // =================================================================
        String[] columns = {"ID", "Name", "Department", "Batch", "Company", "Designation", "Username", "Email"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        
        // Hide technical columns (ID, Username, Email) from view but keep in model
        table.removeColumn(table.getColumnModel().getColumn(7)); // Hide Email
        table.removeColumn(table.getColumnModel().getColumn(6)); // Hide Username
        table.removeColumn(table.getColumnModel().getColumn(0)); // Hide ID

        loadAlumni("SELECT * FROM alumni ORDER BY name ASC"); // Initial Load
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        // =================================================================
        // 3. BOTTOM PANEL (Action Button)
        // =================================================================
        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton btnView = new JButton("View Full Profile & Chat");
        btnView.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnView.setBackground(new Color(0, 153, 102));
        btnView.setForeground(Color.WHITE);
        btnView.setPreferredSize(new Dimension(250, 40));

        btnView.addActionListener(e -> openProfile());
        
        btnPanel.add(btnView);
        add(btnPanel, BorderLayout.SOUTH);
    }

    // --- LOGIC ---

    private void loadAlumni(String query) {
        model.setRowCount(0); // Clear existing data
        try (Connection conn = dbconnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("alumni_id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getInt("batch"),
                    rs.getString("company"),
                    rs.getString("designation"),
                    rs.getString("username"), // Hidden Col
                    rs.getString("email")     // Hidden Col
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    private void searchAlumni() {
        String keyword = txtSearch.getText().trim();
        String category = (String) comboFilter.getSelectedItem();
        String dbColumn = "";

        // Map Dropdown selection to Database Column names
        switch (category) {
            case "Name": dbColumn = "name"; break;
            case "Company": dbColumn = "company"; break;
            case "Batch": dbColumn = "batch"; break;
            case "Designation": dbColumn = "designation"; break;
        }

        if (keyword.isEmpty()) {
            resetTable();
            return;
        }

        // Use prepared statement logic manually here for flexibility
        model.setRowCount(0);
        try (Connection conn = dbconnection.getConnection()) {
            String sql = "SELECT * FROM alumni WHERE " + dbColumn + " LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%"); // Add wildcards for partial match
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                model.addRow(new Object[]{
                    rs.getInt("alumni_id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getInt("batch"),
                    rs.getString("company"),
                    rs.getString("designation"),
                    rs.getString("username"),
                    rs.getString("email")
                });
            }
            
            if (!found) {
                JOptionPane.showMessageDialog(this, "No seniors found matching '" + keyword + "'");
                resetTable(); // Show all again
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetTable() {
        txtSearch.setText("");
        loadAlumni("SELECT * FROM alumni ORDER BY name ASC");
    }

    private void openProfile() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please click on a senior's name in the table first!");
            return;
        }

        // Convert view index to model index (important if columns are hidden)
        String name = (String) model.getValueAt(row, 1);
        String dept = (String) model.getValueAt(row, 2);
        int batch = (int) model.getValueAt(row, 3);
        String company = (String) model.getValueAt(row, 4);
        String designation = (String) model.getValueAt(row, 5);
        String alumUsername = (String) model.getValueAt(row, 6);
        String email = (String) model.getValueAt(row, 7);

        // Open Profile Window
        new AlumniProfile(currentUser, name, alumUsername, dept, batch, company, designation, email).setVisible(true);
    }
}