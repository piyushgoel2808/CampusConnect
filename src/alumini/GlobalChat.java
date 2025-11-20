/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumini;

/**
 *
 * @author piyush
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class GlobalChat extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private String currentUser;
    private String currentRole;
    private int lastMsgId = 0; // To keep track of new messages

    public GlobalChat(String user, String role) {
        this.currentUser = user;
        this.currentRole = role;

        setTitle("Campus Connect - Community Chat (" + user + ")");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window

        // --- UI LAYOUT ---
        
        // 1. The Chat Display Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new java.awt.Font("Segoe UI", 0, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        
        // 2. The Input Area
        JPanel bottomPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setFont(new java.awt.Font("Segoe UI", 0, 14));
        
        JButton btnSend = new JButton("Send");
        btnSend.setBackground(new Color(0, 102, 204));
        btnSend.setForeground(Color.WHITE);
        
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(btnSend, BorderLayout.EAST);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- EVENTS ---

        // Send Button Click
        btnSend.addActionListener(e -> sendMessage());
        
        // Allow pressing "Enter" key to send
        inputField.addActionListener(e -> sendMessage());

        // --- AUTO REFRESH TIMER ---
        // Checks database every 2000 milliseconds (2 seconds)
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadNewMessages();
            }
        });
        timer.start();
        
        // Load initial history
        loadNewMessages();
    }

    // --- LOGIC METHODS ---

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (msg.isEmpty()) return;

        try (Connection conn = dbconnection.getConnection()) {
            String sql = "INSERT INTO community_chat (sender_name, role, message) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, currentUser);
            ps.setString(2, currentRole);
            ps.setString(3, msg);
            
            ps.executeUpdate();
            
            inputField.setText(""); // Clear input box
            loadNewMessages(); // Refresh immediately
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadNewMessages() {
        try (Connection conn = dbconnection.getConnection()) {
            // Only get messages we haven't seen yet (ID > lastMsgId)
            String sql = "SELECT * FROM community_chat WHERE chat_id > ? ORDER BY chat_id ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, lastMsgId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("chat_id");
                String sender = rs.getString("sender_name");
                String role = rs.getString("role");
                String msg = rs.getString("message");
                String time = rs.getString("timestamp");

                // Format: [Student] Rahul: Hello guys! (10:00 AM)
                chatArea.append("[" + role + "] " + sender + ": " + msg + "\n\n");
                
                // Update ID so we don't load this again
                lastMsgId = id; 
                
                // Auto-scroll to bottom
                chatArea.setCaretPosition(chatArea.getDocument().getLength());
            }
            
        } catch (Exception e) {
            System.out.println("Chat error: " + e.getMessage());
        }
    }
}
