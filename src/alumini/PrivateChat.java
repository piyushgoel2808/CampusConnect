/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alumini;

/**
 *
 * @author piyus
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

public class PrivateChat extends JFrame {

    private JTextArea chatArea;
    private JTextField inputField;
    private String myUsername;
    private String otherUsername; // The person we are chatting with
    private int lastMsgId = 0; 

    public PrivateChat(String myUser, String otherUser) {
        this.myUsername = myUser;
        this.otherUsername = otherUser;

        setTitle("Chat with " + otherUsername);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // --- UI SETUP ---
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new java.awt.Font("Segoe UI", 0, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setFont(new java.awt.Font("Segoe UI", 0, 14));
        
        JButton btnSend = new JButton("Send");
        btnSend.setBackground(new Color(0, 153, 102)); // Different color for Private chat
        btnSend.setForeground(Color.WHITE);
        
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(btnSend, BorderLayout.EAST);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- EVENTS ---
        btnSend.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());

        // --- TIMER ---
        Timer timer = new Timer(2000, e -> loadPrivateMessages());
        timer.start();
        
        loadPrivateMessages(); // Initial load
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (msg.isEmpty()) return;

        try (Connection conn = dbconnection.getConnection()) {
            // Store SENDER and RECEIVER
            String sql = "INSERT INTO private_chats (sender, receiver, message) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, myUsername);
            ps.setString(2, otherUsername);
            ps.setString(3, msg);
            
            ps.executeUpdate();
            inputField.setText("");
            loadPrivateMessages();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPrivateMessages() {
        try (Connection conn = dbconnection.getConnection()) {
            // LOGIC: Get messages where (Sender is ME and Receiver is HIM) OR (Sender is HIM and Receiver is ME)
            // Only load messages with ID > lastMsgId to avoid duplicates
            String sql = "SELECT * FROM private_chats WHERE " +
                         "((sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?)) " +
                         "AND id > ? ORDER BY id ASC";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, myUsername);
            ps.setString(2, otherUsername);
            ps.setString(3, otherUsername);
            ps.setString(4, myUsername);
            ps.setInt(5, lastMsgId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String sender = rs.getString("sender");
                String msg = rs.getString("message");

                if (sender.equals(myUsername)) {
                    chatArea.append("Me: " + msg + "\n");
                } else {
                    chatArea.append(otherUsername + ": " + msg + "\n");
                }
                
                lastMsgId = id;
                chatArea.setCaretPosition(chatArea.getDocument().getLength());
            }
            
        } catch (Exception e) {
            System.out.println("Private chat error: " + e.getMessage());
        }
    }
}