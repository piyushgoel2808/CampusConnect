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
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.border.EmptyBorder;

public class AlumniWall extends JFrame {

    private JPanel wallPanel;
    private JButton uploadBtn;
    private String currentUser; // To track who is uploading

    public AlumniWall(String username) {
        this.currentUser = username;

        setTitle("Campus Connect - Memory Wall");
        setSize(800, 600);
        setLocationRelativeTo(null);
        // Use DISPOSE so it doesn't close the whole app
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        // Create Folder to store images locally
        File folder = new File("wallImages");
        if (!folder.exists()) folder.mkdir();

        // Main Layout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 51, 102)); // Campus Connect Blue
        
        JLabel title = new JLabel("📸 Alumni Memory Wall", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(15, 0, 15, 0));

        uploadBtn = new JButton("➕ Add Your Memory");
        uploadBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        uploadBtn.setFocusable(false);

        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(uploadBtn, BorderLayout.SOUTH);

        // Wall Panel (Grid of Images)
        // 0 rows, 4 columns, 10px gap
        wallPanel = new JPanel(new GridLayout(0, 4, 10, 10)); 
        wallPanel.setBackground(Color.WHITE);
        wallPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        loadSavedImages();  // Load images from DB on startup

        JScrollPane scrollPane = new JScrollPane(wallPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling

        // Events
        uploadBtn.addActionListener(e -> uploadImage());

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Upload and Save
    private void uploadImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Image to Upload");
        // Filter for images only
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "gif", "jpeg"));
        
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                // Create unique name to avoid overwrites (timestamp + filename)
                String uniqueName = System.currentTimeMillis() + "_" + file.getName();
                File destFile = new File("wallImages/" + uniqueName);
                
                // Copy file to project folder
                Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                // Save details to DB
                saveToDatabase(uniqueName);
                
                // Update UI
                addImageToWall(destFile, "Just Now");
                
                JOptionPane.showMessageDialog(this, "Memory uploaded successfully!");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error uploading image: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    // Insert File Name in DB
    private void saveToDatabase(String fileName) {
        try (Connection con = dbconnection.getConnection()) {
            String query = "INSERT INTO wall_images(image_name, uploaded_by) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, fileName);
            ps.setString(2, currentUser); // Uses the logged-in username
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add Image Component to GUI
    private void addImageToWall(File file, String uploader) {
        try {
            // Scale Image
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            
            // Create Panel for Image + Label
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            JLabel textLabel = new JLabel("By: " + uploader, SwingConstants.CENTER);
            textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            
            itemPanel.add(imgLabel, BorderLayout.CENTER);
            itemPanel.add(textLabel, BorderLayout.SOUTH);
            
            wallPanel.add(itemPanel);
            wallPanel.revalidate();
            wallPanel.repaint();
            
        } catch (Exception e) {
            System.out.println("Could not load image: " + file.getName());
        }
    }

    // Load Saved Images from DB
    private void loadSavedImages() {
        try (Connection con = dbconnection.getConnection()) {
            String query = "SELECT image_name, uploaded_by FROM wall_images ORDER BY id DESC";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String fileName = rs.getString("image_name");
                String uploader = rs.getString("uploaded_by");
                
                File file = new File("wallImages/" + fileName);
                if (file.exists()) {
                    addImageToWall(file, uploader);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}