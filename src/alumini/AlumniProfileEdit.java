/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumini;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author piyus
 */
public class AlumniProfileEdit extends JFrame{
    private JTextField txtCompany, txtRole;
    private String currentUser;

    public AlumniProfileEdit(String username) {
        this.currentUser = username;
        setTitle("Update Professional Details");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("Update Your Job Info", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(33, 43, 54));
        lblTitle.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 40, 20, 40));
        formPanel.setBackground(Color.WHITE);

        JLabel lblComp = new JLabel("Current Company:");
        lblComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtCompany = new JTextField();
        txtCompany.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblRole = new JLabel("Current Designation/Role:");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtRole = new JTextField();
        txtRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        formPanel.add(lblComp);
        formPanel.add(txtCompany);
        formPanel.add(lblRole);
        formPanel.add(txtRole);

        add(formPanel, BorderLayout.CENTER);

        // Save Button
        JButton btnSave = new JButton("Save Updates");
        btnSave.setBackground(new Color(0, 102, 204));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        
        btnSave.addActionListener(e -> saveChanges());
        
        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
        btnPanel.add(btnSave);
        add(btnPanel, BorderLayout.SOUTH);
        
        // Load current data
        loadCurrentData();
    }

    private void loadCurrentData() {
        try (Connection conn = dbconnection.getConnection()) {
            String sql = "SELECT company, designation FROM alumni WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, currentUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtCompany.setText(rs.getString("company"));
                txtRole.setText(rs.getString("designation"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveChanges() {
        String newComp = txtCompany.getText().trim();
        String newRole = txtRole.getText().trim();

        if(newComp.isEmpty() || newRole.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
            return;
        }

        try (Connection conn = dbconnection.getConnection()) {
            // ✅ ONLY UPDATES COMPANY AND DESIGNATION
            String sql = "UPDATE alumni SET company = ?, designation = ? WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newComp);
            ps.setString(2, newRole);
            ps.setString(3, currentUser);

            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Profile Updated! Please re-login to see changes.");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
}
