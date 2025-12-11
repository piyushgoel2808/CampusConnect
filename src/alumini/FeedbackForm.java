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
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.border.EmptyBorder;

public class FeedbackForm extends JFrame {

    private String username;
    private String role;
    private JComboBox<String> comboRating;
    private JTextArea txtComments;

    public FeedbackForm(String user, String role) {
        this.username = user;
        this.role = role;

        setTitle("Give Feedback");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("We Value Your Feedback! 📝", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBorder(new EmptyBorder(20, 0, 20, 0));
        lblTitle.setForeground(new Color(33, 43, 54));
        add(lblTitle, BorderLayout.NORTH);

        // Center Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(10, 40, 10, 40));
        formPanel.setBackground(Color.WHITE);

        JLabel lblRate = new JLabel("Rate your experience (1-5):");
        lblRate.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] stars = {"⭐⭐⭐⭐⭐ (Excellent)", "⭐⭐⭐⭐ (Good)", "⭐⭐⭐ (Average)", "⭐⭐ (Poor)", "⭐ (Very Poor)"};
        comboRating = new JComboBox<>(stars);
        comboRating.setMaximumSize(new Dimension(300, 35));
        comboRating.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblMsg = new JLabel("Your Suggestions:");
        lblMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtComments = new JTextArea(5, 20);
        txtComments.setLineWrap(true);
        txtComments.setWrapStyleWord(true);
        txtComments.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        formPanel.add(lblRate);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(comboRating);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(lblMsg);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(new JScrollPane(txtComments));

        add(formPanel, BorderLayout.CENTER);

        // Submit Button
        JButton btnSubmit = new JButton("Submit Feedback");
        btnSubmit.setBackground(new Color(0, 153, 102));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setFocusPainted(false);
        
        btnSubmit.addActionListener(e -> submitFeedback());
        
        JPanel btnPanel = new JPanel();
        btnPanel.setBorder(new EmptyBorder(10, 0, 20, 0));
        btnPanel.add(btnSubmit);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void submitFeedback() {
        String comments = txtComments.getText().trim();
        // Extract number from string "⭐⭐⭐⭐⭐ (Excellent)" -> 5
        int rating = 5 - comboRating.getSelectedIndex(); 

        if (comments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please write a comment.");
            return;
        }

        try (Connection conn = dbconnection.getConnection()) {
            String sql = "INSERT INTO feedback (username, role, rating, comments) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, role);
            ps.setInt(3, rating);
            ps.setString(4, comments);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thank you for your feedback!");
            this.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}