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

    // --- HELPERS FOR PARSING ---
    private class JobQueryParams {
        String department;
        String role;
        String location;
        String company;
        String jobType;
    }

    private class EventQueryParams {
        String venue;
        String month;
        String year;
        String date;
        String keyword;
    }

    public class AlumniParams {
        public String name;
        public String department;
        public String company;
        public String designation;
        public Integer batch;
    }

    /**
     * Default Constructor (Required for IDE Design View)
     */
    public chatinterface() {
        initComponents();
    }

    /**
     * ✅ CORRECT CONSTRUCTOR (Accepts Username & Role)
     */
    public chatinterface(String username, String role) {
        initComponents(); // Initialize UI components first
        this.username = username;
        this.role = role;

        // Load specific data based on who logged in
        if ("alumni".equalsIgnoreCase(role)) {
            loadAlumniData(username);
        } else if ("user".equalsIgnoreCase(role)) {
            loadUserData(username);
        }
    }

    // --- DATA LOADING METHODS ---
    private void loadUserData(String username) {
        try (Connection con = dbconnection.getConnection()) {
            String query = "SELECT name, email FROM users WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                lblwelcome.setText("Welcome, " + rs.getString("name"));
                lblemail.setText("Email: " + rs.getString("email"));

                // Hide extra labels for normal users
                lblbatch.setVisible(false);
                lbldepartment.setVisible(false);
                lblcompany.setVisible(false);
                lbldesignation.setVisible(false);
            } else {
                lblwelcome.setText("User not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            } else {
                lblwelcome.setText("User not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- COMPONENT INITIALIZATION (Auto-Generated) ---
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
        btnCommunity = new javax.swing.JButton(); // ✅ Added Community Button
        btnLogout = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 51));

        TxtChatArea.setColumns(20);
        TxtChatArea.setRows(5);
        jScrollPane1.setViewportView(TxtChatArea);

        jButton1.setText("Send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(TxtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TxtMessage)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        lblwelcome.setText("Welcome User");
        lblemail.setText("Email");
        lblbatch.setText("Batch");
        lbldepartment.setText("Department");
        lblcompany.setText("Company");
        lbldesignation.setText("Designation");

        jButton2.setText("Export Chat");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnDirectMessage.setText("Direct Message");
        btnDirectMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDirectMessageActionPerformed(evt);
            }
        });
        
        // ✅ COMMUNITY BUTTON SETUP
        btnCommunity.setText("Community Chat");
        btnCommunity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommunityActionPerformed(evt);
            }
        });

        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblwelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblemail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblbatch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbldepartment, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                    .addComponent(lblcompany, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbldesignation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnDirectMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                            .addComponent(btnCommunity, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE) // ✅ Added to Layout
                            .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblwelcome)
                .addGap(18, 18, 18)
                .addComponent(lblemail)
                .addGap(18, 18, 18)
                .addComponent(lblbatch)
                .addGap(18, 18, 18)
                .addComponent(lbldepartment)
                .addGap(18, 18, 18)
                .addComponent(lblcompany)
                .addGap(18, 18, 18)
                .addComponent(lbldesignation)
                .addGap(30, 30, 30)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDirectMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCommunity, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE) // ✅ Added to Layout
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Nirmala Text", 1, 12)); 
        jLabel1.setText("BVICAM CHATBOT");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
            .addGroup(layout.createSequentialGroup()
                .addGap(241, 241, 241)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String userMessage = TxtMessage.getText().trim();
        if (userMessage.isEmpty()) return;

        TxtChatArea.append("You: " + userMessage + "\n");

        String botReply;
        if (userMessage.toLowerCase().contains("event")) {
            EventQueryParams p = extractEventParameters(userMessage);
            botReply = getDynamicEvents(p);
        } else if (userMessage.toLowerCase().contains("alumni")) {
            AlumniParams p = extractAlumniParams(userMessage);
            botReply = getDynamicAlumni(p);
        } else if (userMessage.toLowerCase().contains("job") || userMessage.toLowerCase().contains("intern")) {
            JobQueryParams p = extractJobParameters(userMessage);
            botReply = getDynamicJobResults(p);
        } else {
            botReply = getBotReplyFromDatabase(userMessage);
        }
        TxtChatArea.append("Bot: " + botReply + "\n\n");
        lastBotReply = botReply;
        TxtMessage.setText("");
    }                                        

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        if (lastBotReply == null || lastBotReply.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No bot response to export!");
            return;
        }
        try {
            String jsonContent = "{\n    \"chatbot_response\": \"" + lastBotReply + "\"\n}";
            javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
            chooser.setDialogTitle("Save Chatbot JSON");
            chooser.setSelectedFile(new java.io.File("chatbot_output.json"));
            int result = chooser.showSaveDialog(this);

            if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File file = chooser.getSelectedFile();
                java.io.FileWriter fw = new java.io.FileWriter(file);
                fw.write(jsonContent);
                fw.close();
                JOptionPane.showMessageDialog(this, "JSON exported successfully:\n" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }                                        

    private void btnDirectMessageActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        String[] availableUsers = fetchAvailableUsers(this.role);

        if (availableUsers.length == 0) {
            JOptionPane.showMessageDialog(this, "No users found to chat with.");
            return;
        }

        String targetUser = (String) JOptionPane.showInputDialog(
                this, 
                "Select a person to chat with:", 
                "Direct Message", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                availableUsers, 
                availableUsers[0] 
        );

        if (targetUser != null && !targetUser.trim().isEmpty()) {
            PrivateChat pc = new PrivateChat(this.username, targetUser.trim());
            pc.setVisible(true);
        }
    }       
    
    // ✅ COMMUNITY BUTTON ACTION
    private void btnCommunityActionPerformed(java.awt.event.ActionEvent evt) {                                             
        GlobalChat gChat = new GlobalChat(this.username, this.role);
        gChat.setVisible(true);
    }

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {                                          
        this.dispose();
        new MainHome().setVisible(true);
    }                                         

    // --- LOGIC FOR PARSING AND DB QUERIES ---
    
    private AlumniParams extractAlumniParams(String msg) {
        msg = msg.toLowerCase();
        AlumniParams p = new AlumniParams();
        String[] depts = {"computer science", "it", "electrical", "electronics", "mechanical", "civil", "biotechnology"};
        for (String d : depts) if (msg.contains(d)) p.department = capitalize(d);
        String[] companies = {"infosys", "tcs", "wipro", "intel", "reliance", "mahindra", "accenture", "biocon", "l&t"};
        for (String c : companies) if (msg.contains(c.toLowerCase())) p.company = capitalize(c);
        String[] roles = {"engineer", "developer", "analyst", "associate", "lead", "research", "manager"};
        for (String r : roles) if (msg.contains(r)) p.designation = capitalize(r);
        for (int year = 2000; year <= 2030; year++) if (msg.contains(String.valueOf(year))) p.batch = year;
        if (msg.contains("amit sharma")) p.name = "Amit Sharma"; 
        return p;
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private EventQueryParams extractEventParameters(String msg) {
        msg = msg.toLowerCase();
        EventQueryParams p = new EventQueryParams();
        if (msg.contains("auditorium")) p.venue = "Auditorium Hall";
        if (msg.contains("seminar")) p.venue = "Seminar Room 3";
        if (msg.contains("online")) p.venue = "Online";
        if (msg.contains("2025")) p.year = "2025";
        if (msg.contains("2026")) p.year = "2026";
        return p;
    }

    private JobQueryParams extractJobParameters(String msg) {
        msg = msg.toLowerCase();
        JobQueryParams p = new JobQueryParams();
        if (msg.contains("intern")) p.jobType = "Internship";
        if (msg.contains("job")) p.jobType = "Job";
        if (msg.contains("it")) p.department = "IT";
        if (msg.contains("computer")) p.department = "Computer Science";
        if (msg.contains("pune")) p.location = "Pune";
        if (msg.contains("bangalore")) p.location = "Bangalore";
        return p;
    }

    private String getDynamicEvents(EventQueryParams p) {
        StringBuilder sql = new StringBuilder("SELECT event_name, event_date, event_venue, description FROM events WHERE 1=1 ");
        ArrayList<String> filters = new ArrayList<>();
        if (p.venue != null) { sql.append(" AND event_venue = ?"); filters.add(p.venue); }
        if (p.year != null) { sql.append(" AND YEAR(event_date) = ?"); filters.add(p.year); }
        
        try (Connection conn = dbconnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < filters.size(); i++) ps.setString(i + 1, filters.get(i));
            ResultSet rs = ps.executeQuery();
            StringBuilder reply = new StringBuilder("Events:\n");
            boolean found = false;
            while (rs.next()) {
                found = true;
                reply.append("📅 ").append(rs.getString("event_name")).append("\n");
            }
            return found ? reply.toString() : "No events found.";
        } catch (Exception e) { return "Error: " + e.getMessage(); }
    }

    private String getDynamicAlumni(AlumniParams p) {
        StringBuilder sql = new StringBuilder("SELECT name, company FROM alumni WHERE 1=1 ");
        ArrayList<String> filters = new ArrayList<>();
        if (p.department != null) { sql.append(" AND department = ?"); filters.add(p.department); }
        if (p.company != null) { sql.append(" AND company = ?"); filters.add(p.company); }
        
        try (Connection conn = dbconnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < filters.size(); i++) ps.setString(i + 1, filters.get(i));
            ResultSet rs = ps.executeQuery();
            StringBuilder reply = new StringBuilder("Alumni found:\n");
            boolean found = false;
            while (rs.next()) {
                found = true;
                reply.append("👤 ").append(rs.getString("name")).append(" - ").append(rs.getString("company")).append("\n");
            }
            return found ? reply.toString() : "No alumni found.";
        } catch (Exception e) { return "Error: " + e.getMessage(); }
    }

    private String getDynamicJobResults(JobQueryParams p) {
        StringBuilder sql = new StringBuilder("SELECT title, company FROM job_opportunities WHERE 1=1 ");
        ArrayList<String> filters = new ArrayList<>();
        if (p.location != null) { sql.append(" AND location = ?"); filters.add(p.location); }
        
        try (Connection conn = dbconnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < filters.size(); i++) ps.setString(i + 1, filters.get(i));
            ResultSet rs = ps.executeQuery();
            StringBuilder reply = new StringBuilder("Jobs:\n");
            boolean found = false;
            while (rs.next()) {
                found = true;
                reply.append("💼 ").append(rs.getString("title")).append(" at ").append(rs.getString("company")).append("\n");
            }
            return found ? reply.toString() : "No jobs found.";
        } catch (Exception e) { return "Error: " + e.getMessage(); }
    }

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

    // ✅ METHOD TO FETCH USERS FOR DROPDOWN
    private String[] fetchAvailableUsers(String myRole) {
        ArrayList<String> list = new ArrayList<>();
        String query;

        if ("alumni".equalsIgnoreCase(myRole)) {
            query = "SELECT username FROM users ORDER BY username ASC";
        } else {
            query = "SELECT username FROM alumni ORDER BY username ASC";
        }

        try (Connection conn = dbconnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{};
        }
        return list.toArray(new String[0]);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new chatinterface().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTextArea TxtChatArea;
    private javax.swing.JTextField TxtMessage;
    private javax.swing.JButton btnCommunity; // ✅ Variable added
    private javax.swing.JButton btnDirectMessage;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblbatch;
    private javax.swing.JLabel lblcompany;
    private javax.swing.JLabel lbldepartment;
    private javax.swing.JLabel lbldesignation;
    private javax.swing.JLabel lblemail;
    private javax.swing.JLabel lblwelcome;
    // End of variables declaration                   
}