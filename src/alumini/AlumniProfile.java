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
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class AlumniProfile extends JFrame {

    public AlumniProfile(String myUser, String alumName, String alumUser, String dept, int batch, String company, String role, String email) {
        setTitle("Profile: " + alumName);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main Container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(255, 255, 255));

        // 1. Avatar / Icon
        JLabel lblIcon = new JLabel("👤", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. Name
        JLabel lblName = new JLabel(alumName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Company & Role
        JLabel lblJob = new JLabel(role + " at " + company);
        lblJob.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblJob.setForeground(Color.GRAY);
        lblJob.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 4. Details Panel
        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        detailsPanel.setBackground(new Color(245, 245, 245));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Academic Details"));
        detailsPanel.setMaximumSize(new Dimension(350, 100));
        
        detailsPanel.add(new JLabel("  🎓 Department: " + dept));
        detailsPanel.add(new JLabel("  📅 Batch: " + batch));
        detailsPanel.add(new JLabel("  📧 Email: " + email));

        // 5. CHAT BUTTON (The Logic)
        JButton btnChat = new JButton("💬 Chat with " + alumName);
        btnChat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnChat.setBackground(new Color(0, 102, 204));
        btnChat.setForeground(Color.WHITE);
        btnChat.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChat.setMaximumSize(new Dimension(350, 40));
        
        // Action: Open Private Chat
        btnChat.addActionListener(e -> {
            new PrivateChat(myUser, alumUser).setVisible(true);
            this.dispose(); // Close profile after opening chat
        });

        // Add Spacer Logic
        mainPanel.add(lblIcon);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(lblName);
        mainPanel.add(lblJob);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(btnChat);

        add(mainPanel);
    }
}
