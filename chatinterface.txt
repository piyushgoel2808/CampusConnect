/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package alumini;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author piyush
 */
public class chatinterface extends javax.swing.JFrame {

    private String lastBotReply = "";
    private String username;
    private String role;

    private class JobQueryParams { String department, role, location, company, jobType; }
    private class EventQueryParams { String venue, month, year, date, keyword; }
    public class AlumniParams { public String name, department, company, designation; public Integer batch; }

    public chatinterface() { initComponents(); }

    public chatinterface(String username, String role) {
        initComponents(); 
        this.username = username;
        this.role = role;

        if ("alumni".equalsIgnoreCase(role)) {
            loadAlumniData(username);
            btnViewSeniors.setVisible(false); // Alumni don't need to see "Seniors" button usually
        } else if ("user".equalsIgnoreCase(role)) {
            loadUserData(username);
            btnViewSeniors.setVisible(true); // Only show for students
        }
    }

    private void loadUserData(String username) {
        try (Connection con = dbconnection.getConnection()) {
            String query = "SELECT name, email FROM users WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lblwelcome.setText("Welcome, " + rs.getString("name"));
                lblemail.setText("Email: " + rs.getString("email"));
                lblbatch.setVisible(false); lbldepartment.setVisible(false); lblcompany.setVisible(false); lbldesignation.setVisible(false);
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadAlumniData(String username) {
        try (Connection con = dbconnection.getConnection()) {
            String query = "SELECT name, email, batch, department, company, designation FROM alumni WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lblwelcome.setText("Welcome, " + rs.getString("name"));
                lblemail.setText("Email: " + rs.getString("email"));
                lblbatch.setText("Batch: " + rs.getString("batch"));
                lbldepartment.setText("Department: " + rs.getString("department"));
                lblcompany.setText("Company: " + rs.getString("company"));
                lbldesignation.setText("Designation: " + rs.getString("designation"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TxtChatArea = new javax.swing.JTextArea();
        TxtMessage = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblwelcome = new javax.swing.JLabel();
        lblemail = new javax.swing.JLabel();
        lblbatch = new javax.swing.JLabel();
        lbldepartment = new javax.swing.JLabel();
        lblcompany = new javax.swing.JLabel();
        lbldesignation = new javax.swing.JLabel();
        
        jButton2 = new javax.swing.JButton();
        btnDirectMessage = new javax.swing.JButton();
        btnCommunity = new javax.swing.JButton(); 
        btnWall = new javax.swing.JButton(); 
        btnViewSeniors = new javax.swing.JButton(); // ✅ NEW BUTTON
        btnLogout = new javax.swing.JButton();
        
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 51));

        TxtChatArea.setColumns(20); TxtChatArea.setRows(5);
        jScrollPane1.setViewportView(TxtChatArea);

        jButton1.setText("Send");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup().addGap(23, 23, 23).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup().addGap(32, 32, 32).addComponent(TxtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(39, 39, 39).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup().addGap(14, 14, 14).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(26, 26, 26).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(TxtMessage).addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)).addContainerGap(35, Short.MAX_VALUE))
        );

        lblwelcome.setText("Welcome User"); lblemail.setText("Email"); lblbatch.setText("Batch"); lbldepartment.setText("Department"); lblcompany.setText("Company"); lbldesignation.setText("Designation");

        jButton2.setText("Export Chat");
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));

        btnDirectMessage.setText("Direct Message");
        btnDirectMessage.addActionListener(evt -> btnDirectMessageActionPerformed(evt));
        
        btnCommunity.setText("Community Chat");
        btnCommunity.addActionListener(evt -> btnCommunityActionPerformed(evt));

        btnWall.setText("📸 Memory Wall");
        btnWall.setBackground(new java.awt.Color(255, 153, 0)); 
        btnWall.setForeground(new java.awt.Color(0,0,0));
        btnWall.addActionListener(evt -> btnWallActionPerformed(evt));

        // ✅ SENIORS BUTTON UI
        btnViewSeniors.setText("👨‍🎓 View Seniors");
        btnViewSeniors.setBackground(new java.awt.Color(0, 102, 204));
        btnViewSeniors.setForeground(new java.awt.Color(255, 255, 255));
        btnViewSeniors.addActionListener(evt -> btnViewSeniorsActionPerformed(evt));

        btnLogout.setText("Logout");
        btnLogout.addActionListener(evt -> btnLogoutActionPerformed(evt));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(lblwelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblemail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblbatch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lbldepartment, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE).addComponent(lblcompany, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lbldesignation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDirectMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                    .addComponent(btnCommunity, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                    .addComponent(btnWall, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                    .addComponent(btnViewSeniors, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE) // ✅ Added
                    .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(0, 0, Short.MAX_VALUE))).addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup().addGap(23, 23, 23).addComponent(lblwelcome).addGap(18, 18, 18).addComponent(lblemail).addGap(18, 18, 18).addComponent(lblbatch).addGap(18, 18, 18).addComponent(lbldepartment).addGap(18, 18, 18).addComponent(lblcompany).addGap(18, 18, 18).addComponent(lbldesignation).addGap(20, 20, 20)
            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnDirectMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnCommunity, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnWall, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnViewSeniors, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED) // ✅ Added
            .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Nirmala Text", 1, 12)); jLabel1.setText("BVICAM CHATBOT");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(15, 15, 15)).addGroup(layout.createSequentialGroup().addGap(241, 241, 241).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(38, 38, 38).addComponent(jLabel1).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String userMessage = TxtMessage.getText().trim();
        if (userMessage.isEmpty()) return;
        TxtChatArea.append("You: " + userMessage + "\n");
        String botReply = getBotReplyFromDatabase(userMessage);
        TxtChatArea.append("Bot: " + botReply + "\n\n");
        lastBotReply = botReply;
        TxtMessage.setText("");
    }                                        

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) { 
        if (lastBotReply.isEmpty()) { JOptionPane.showMessageDialog(this, "No bot response to export!"); return; }
        try {
            String jsonContent = "{\n    \"chatbot_response\": \"" + lastBotReply + "\"\n}";
            javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
            chooser.setSelectedFile(new java.io.File("chatbot_output.json"));
            if (chooser.showSaveDialog(this) == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.FileWriter fw = new java.io.FileWriter(chooser.getSelectedFile());
                fw.write(jsonContent); fw.close();
                JOptionPane.showMessageDialog(this, "Saved!");
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }                                        

    private void btnDirectMessageActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        String[] availableUsers = fetchAvailableUsers(this.role);
        if (availableUsers.length == 0) { JOptionPane.showMessageDialog(this, "No users found."); return; }
        String targetUser = (String) JOptionPane.showInputDialog(this, "Select person:", "Direct Message", JOptionPane.QUESTION_MESSAGE, null, availableUsers, availableUsers[0]);
        if (targetUser != null) new PrivateChat(this.username, targetUser).setVisible(true);
    }       
    
    private void btnCommunityActionPerformed(java.awt.event.ActionEvent evt) { new GlobalChat(this.username, this.role).setVisible(true); }
    private void btnWallActionPerformed(java.awt.event.ActionEvent evt) { new AlumniWall(this.username).setVisible(true); }
    
    // ✅ ACTION FOR VIEW SENIORS BUTTON
    private void btnViewSeniorsActionPerformed(java.awt.event.ActionEvent evt) {
        new AlumniList(this.username).setVisible(true);
    }

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) { this.dispose(); new MainHome().setVisible(true); }                                         

    private String getBotReplyFromDatabase(String userMessage) {
        String reply = "I'm sorry, I didn’t understand that.";
        try (Connection conn = dbconnection.getConnection()) {
            String query = "SELECT reply FROM chatbot_data WHERE LOWER(keyword) = LOWER(?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, userMessage.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) reply = rs.getString("reply");
        } catch (SQLException e) { reply = "Database error: " + e.getMessage(); }
        return reply;
    }

    private String[] fetchAvailableUsers(String myRole) {
        ArrayList<String> list = new ArrayList<>();
        String query = "alumni".equalsIgnoreCase(myRole) ? "SELECT username FROM users" : "SELECT username FROM alumni";
        try (Connection conn = dbconnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString("username"));
        } catch (Exception e) {}
        return list.toArray(new String[0]);
    }
    
    public static void main(String args[]) { java.awt.EventQueue.invokeLater(() -> new chatinterface().setVisible(true)); }

    // Variables
    private javax.swing.JTextArea TxtChatArea;
    private javax.swing.JTextField TxtMessage;
    private javax.swing.JButton btnCommunity; 
    private javax.swing.JButton btnDirectMessage; 
    private javax.swing.JButton btnLogout; 
    private javax.swing.JButton btnWall; 
    private javax.swing.JButton btnViewSeniors; // ✅ Variable Added
    private javax.swing.JButton jButton1; 
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1; private javax.swing.JPanel jPanel1; private javax.swing.JPanel jPanel2; private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblbatch; private javax.swing.JLabel lblcompany; private javax.swing.JLabel lbldepartment; private javax.swing.JLabel lbldesignation; private javax.swing.JLabel lblemail; private javax.swing.JLabel lblwelcome;
}