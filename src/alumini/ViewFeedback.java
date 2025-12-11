/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumini;

/**
 *
 * @author piyus
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewFeedback extends JFrame {

    public ViewFeedback() {
        setTitle("User Feedback Report");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table Setup
        String[] columns = {"ID", "User", "Role", "Rating", "Comments", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(300); // Wider comment column

        // Load Data
        try (Connection conn = dbconnection.getConnection()) {
            String sql = "SELECT * FROM feedback ORDER BY submitted_at DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String stars = "⭐".repeat(rs.getInt("rating"));
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("role"),
                    stars, // Show visual stars
                    rs.getString("comments"),
                    rs.getString("submitted_at")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(new JScrollPane(table));
    }
}