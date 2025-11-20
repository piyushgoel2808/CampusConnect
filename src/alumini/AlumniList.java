/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumini;

/**
 *
 * @author piyush
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AlumniList extends JFrame {

    private JTable table;
    private String currentUser;

    public AlumniList(String myUser) {
        this.currentUser = myUser;
        setTitle("Alumni Directory - Find Your Seniors");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Layout
        setLayout(new BorderLayout());

        // Header
        JLabel title = new JLabel("🎓 Alumni Directory", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Department", "Batch", "Company", "Designation", "Username", "Email"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        
        // Hide technical columns (ID, Username, Email) from view but keep in model
        table.removeColumn(table.getColumnModel().getColumn(7)); // Hide Email
        table.removeColumn(table.getColumnModel().getColumn(6)); // Hide Username
        table.removeColumn(table.getColumnModel().getColumn(0)); // Hide ID

        loadAlumni(model);
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom Button Panel
        JPanel btnPanel = new JPanel();
        JButton btnView = new JButton("View Profile & Chat");
        btnView.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnView.setBackground(new Color(0, 153, 102));
        btnView.setForeground(Color.WHITE);

        btnView.addActionListener(e -> openProfile(model));
        
        btnPanel.add(btnView);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadAlumni(DefaultTableModel model) {
        try (Connection conn = dbconnection.getConnection()) {
            String sql = "SELECT * FROM alumni ORDER BY name ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("alumni_id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getInt("batch"),
                    rs.getString("company"),
                    rs.getString("designation"),
                    rs.getString("username"), // Hidden Col 6
                    rs.getString("email")     // Hidden Col 7
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openProfile(DefaultTableModel model) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a senior from the list first!");
            return;
        }

        // Get data from the hidden model columns (Convert view index to model index)
        // We access the model directly because we hid columns in the visual table
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