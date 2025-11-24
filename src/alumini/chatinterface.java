/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package alumini;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Modern Dashboard UI for Campus Connect
 * @author piyush
 */
public class chatinterface extends JFrame {

    // --- Logic Variables ---
    private String lastBotReply = "";
    private String username;
    private String role;

    // --- UI Components ---
    private JTextArea txtChatArea;
    private JTextField txtMessage;
    private JPanel pnlSideBar, pnlMain;
    
    // ✅ FIXED: Variable names now match exactly
    private JButton btnSend, btnDirectMessage, btnCommunity, btnWall, btnSeniors, btnExport, btnLogout,btnEditProfile;
    
    // Profile Labels
    private JLabel lblWelcome, lblEmail, lblBatch, lblDept, lblCompany, lblRole;
    // ... existing buttons ...
//private JButton btnSend, btnDirectMessage, btnCommunity, btnWall, btnSeniors, btnExport, btnLogout;
//private JButton btnEditProfile; // <--- ADD THIS

    // --- Query Helper Classes ---
    private class JobQueryParams { String department, role, location, company, jobType; }
    private class EventQueryParams { String venue, month, year, date, keyword; }
    public class AlumniParams { public String name, department, company, designation; public Integer batch; }

    /**
     * Default Constructor for IDE
     */
    public chatinterface() {
        initComponents();
    }

    /**
     * Main Constructor with Login Data
     */
    public chatinterface(String username, String role) {
        this.username = username;
        this.role = role;
        
        initComponents(); // Build the Modern UI
        
        // Load Data & Configure Buttons
        if ("alumni".equalsIgnoreCase(role)) {
            loadAlumniData(username);
            btnSeniors.setVisible(false); // Alumni don't need to find seniors
        } else {
            loadUserData(username);
            btnSeniors.setVisible(true); // Students need this
        }
        if ("alumni".equalsIgnoreCase(role)) {
    loadAlumniData(username);
    btnSeniors.setVisible(false); 
    btnEditProfile.setVisible(true); // ✅ Alumni CAN see it
} else {
    loadUserData(username);
    btnSeniors.setVisible(true);
    btnEditProfile.setVisible(false); // ❌ Students CANNOT see it
}
    }

    // --- GUI BUILDER (Modern Layout) ---
    private void initComponents() {
        setTitle("Campus Connect - Dashboard");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===========================================================================
        // 1. LEFT SIDEBAR (Profile & Menu)
        // ===========================================================================
        pnlSideBar = new JPanel();
        pnlSideBar.setPreferredSize(new Dimension(280, 0));
        pnlSideBar.setBackground(new Color(33, 43, 54)); // Dark Blue
        pnlSideBar.setLayout(new BoxLayout(pnlSideBar, BoxLayout.Y_AXIS));
        pnlSideBar.setBorder(new EmptyBorder(20, 20, 20, 20));

        // -- App Logo/Title --
        JLabel lblTitle = new JLabel("CAMPUS CONNECT");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JSeparator sep1 = new JSeparator();
        sep1.setMaximumSize(new Dimension(240, 10));
        sep1.setForeground(new Color(80, 80, 80));

        // -- Profile Section --
        JLabel lblProfileHeader = new JLabel("MY PROFILE");
        lblProfileHeader.setForeground(new Color(150, 150, 150));
        lblProfileHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        lblWelcome = createProfileLabel("Loading...");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblWelcome.setForeground(new Color(72, 201, 176)); // Teal Accent
        
        lblEmail = createProfileLabel("Email: ...");
        lblBatch = createProfileLabel("Batch: ...");
        lblDept = createProfileLabel("Dept: ...");
        lblCompany = createProfileLabel(""); 
        lblRole = createProfileLabel("");    

        // -- Menu Section --
        JLabel lblMenuHeader = new JLabel("NAVIGATION");
        lblMenuHeader.setForeground(new Color(150, 150, 150));
        lblMenuHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Creating Styled Buttons
        btnDirectMessage = createMenuButton("» Direct Message");
        btnCommunity = createMenuButton("● Community Chat");
        btnWall = createMenuButton("[+] Memory Wall");
        btnSeniors = createMenuButton("👨‍🎓 View Seniors");
        btnExport = createMenuButton("💾 Export Chat");
        btnLogout = createMenuButton("X Logout");
        btnEditProfile = createMenuButton("Edit Profile");

        btnLogout.setBackground(new Color(192, 57, 43)); // Red for logout

        // Adding Actions
        btnDirectMessage.addActionListener(e -> actionDirectMessage());
        btnCommunity.addActionListener(e -> actionCommunityChat());
        btnWall.addActionListener(e -> actionMemoryWall());
        btnSeniors.addActionListener(e -> actionViewSeniors());
        btnExport.addActionListener(e -> actionExportChat());
        btnLogout.addActionListener(e -> actionLogout());
        btnEditProfile.addActionListener(e -> new AlumniProfileEdit(this.username).setVisible(true));

        // -- Add Components to Sidebar --
        pnlSideBar.add(lblTitle);
        pnlSideBar.add(Box.createVerticalStrut(15));
        pnlSideBar.add(sep1);
        pnlSideBar.add(Box.createVerticalStrut(20));
        
        pnlSideBar.add(lblProfileHeader);
        pnlSideBar.add(Box.createVerticalStrut(10));
        pnlSideBar.add(lblWelcome);
        pnlSideBar.add(lblEmail);
        pnlSideBar.add(lblBatch);
        pnlSideBar.add(lblDept);
        pnlSideBar.add(lblCompany);
        pnlSideBar.add(lblRole);
        
        pnlSideBar.add(Box.createVerticalStrut(30));
        pnlSideBar.add(lblMenuHeader);
        pnlSideBar.add(Box.createVerticalStrut(10));
        pnlSideBar.add(btnDirectMessage);
        pnlSideBar.add(Box.createVerticalStrut(8));
        pnlSideBar.add(btnCommunity);
        pnlSideBar.add(Box.createVerticalStrut(8));
        pnlSideBar.add(btnWall);
        pnlSideBar.add(Box.createVerticalStrut(8));
        pnlSideBar.add(btnSeniors);
        pnlSideBar.add(Box.createVerticalStrut(8));
        pnlSideBar.add(btnExport);
        pnlSideBar.add(Box.createVerticalGlue()); // Push logout to bottom
        pnlSideBar.add(btnEditProfile); // <--- ADD THIS
        pnlSideBar.add(Box.createVerticalGlue());
        pnlSideBar.add(btnLogout);

        // ===========================================================================
        // 2. MAIN CONTENT (Chat Area)
        // ===========================================================================
        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);

        // -- Chat Header --
        JPanel pnlChatHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlChatHeader.setBackground(new Color(245, 246, 250));
        pnlChatHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        JLabel lblChatIcon = new JLabel("🤖");
        lblChatIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel lblChatTitle = new JLabel("  BVICAM AI Assistant");
        lblChatTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblChatTitle.setForeground(new Color(44, 62, 80));
        
        JPanel headerContent = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerContent.setOpaque(false);
        headerContent.setBorder(new EmptyBorder(10, 20, 10, 20));
        headerContent.add(lblChatIcon);
        headerContent.add(lblChatTitle);
        
        pnlChatHeader.add(headerContent);

        // -- Chat Area --
        txtChatArea = new JTextArea();
        txtChatArea.setEditable(false);
        txtChatArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtChatArea.setLineWrap(true);
        txtChatArea.setWrapStyleWord(true);
        txtChatArea.setMargin(new Insets(20, 20, 20, 20));
        txtChatArea.setText("Hello! I am your Campus Assistant.\n\nTry asking about:\n• Job Opportunities\n• Upcoming Events\n• Alumni Details\n\n");
        
        JScrollPane scrollPane = new JScrollPane(txtChatArea);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scroll

        // -- Input Area --
        JPanel pnlInput = new JPanel(new BorderLayout());
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(new EmptyBorder(15, 20, 15, 20));

        txtMessage = new JTextField();
        txtMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMessage.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        btnSend = new JButton("Send Message");
        btnSend.setBackground(new Color(0, 102, 204)); // Brand Blue
        btnSend.setForeground(Color.WHITE);
        btnSend.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSend.setFocusPainted(false);
        btnSend.setBorder(new EmptyBorder(10, 25, 10, 25));
        btnSend.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Actions
        btnSend.addActionListener(e -> actionSendMessage());
        txtMessage.addActionListener(e -> actionSendMessage()); // Enter key works too

        pnlInput.add(txtMessage, BorderLayout.CENTER);
        pnlInput.add(btnSend, BorderLayout.EAST);

        // -- Assemble Main Panel --
        pnlMain.add(pnlChatHeader, BorderLayout.NORTH);
        pnlMain.add(scrollPane, BorderLayout.CENTER);
        pnlMain.add(pnlInput, BorderLayout.SOUTH);

        // Add Side and Main to Frame
        add(pnlSideBar, BorderLayout.WEST);
        add(pnlMain, BorderLayout.CENTER);
    }

    // --- UI Helper Methods ---
    private JLabel createProfileLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(new Color(200, 200, 200));
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(240, 45));
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }

    // --- Database Loading Logic ---
    private void loadUserData(String username) {
        try (Connection con = dbconnection.getConnection()) {
            String query = "SELECT name, email FROM users WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                lblWelcome.setText("Hi, " + rs.getString("name"));
                lblEmail.setText(rs.getString("email"));
                lblBatch.setVisible(false);
                lblDept.setVisible(false);
                lblCompany.setVisible(false);
                lblRole.setVisible(false);
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
                lblWelcome.setText("Hi, " + rs.getString("name"));
                lblEmail.setText(rs.getString("email"));
                lblBatch.setText("Batch: " + rs.getString("batch"));
                lblDept.setText("Dept: " + rs.getString("department"));
                lblCompany.setText("Company: " + rs.getString("company"));
                lblRole.setText("Role: " + rs.getString("designation"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- Button Actions ---

    private void actionSendMessage() {
        String userMessage = txtMessage.getText().trim();
        if (userMessage.isEmpty()) return;

        txtChatArea.append("You: " + userMessage + "\n");

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
        txtChatArea.append("Bot: " + botReply + "\n\n");
        lastBotReply = botReply;
        txtMessage.setText("");
    }

    private void actionDirectMessage() {
        String[] availableUsers = fetchAvailableUsers(this.role);
        if (availableUsers.length == 0) {
            JOptionPane.showMessageDialog(this, "No users found to chat with.");
            return;
        }
        String targetUser = (String) JOptionPane.showInputDialog(this, "Select person:", "Direct Message", JOptionPane.QUESTION_MESSAGE, null, availableUsers, availableUsers[0]);
        if (targetUser != null) new PrivateChat(this.username, targetUser).setVisible(true);
    }

    private void actionCommunityChat() {
        new GlobalChat(this.username, this.role).setVisible(true);
    }

    private void actionMemoryWall() {
        new AlumniWall(this.username).setVisible(true);
    }

    private void actionViewSeniors() {
        new AlumniList(this.username).setVisible(true);
    }

    private void actionExportChat() {
        if (lastBotReply.isEmpty()) { JOptionPane.showMessageDialog(this, "No bot response to export!"); return; }
        try {
            String jsonContent = "{\n    \"chatbot_response\": \"" + lastBotReply + "\"\n}";
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("chatbot_output.json"));
            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                java.io.FileWriter fw = new java.io.FileWriter(chooser.getSelectedFile());
                fw.write(jsonContent); fw.close();
                JOptionPane.showMessageDialog(this, "Saved!");
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    private void actionLogout() {
        this.dispose();
        new MainHome().setVisible(true);
    }

    // --- Logic Helpers (Same as before) ---
    
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

    // Parsing Logic
    private AlumniParams extractAlumniParams(String msg) {
        msg = msg.toLowerCase(); AlumniParams p = new AlumniParams();
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
    private String capitalize(String text) { return text.substring(0, 1).toUpperCase() + text.substring(1); }
    private EventQueryParams extractEventParameters(String msg) {
        msg = msg.toLowerCase(); EventQueryParams p = new EventQueryParams();
        if (msg.contains("auditorium")) p.venue = "Auditorium Hall";
        if (msg.contains("seminar")) p.venue = "Seminar Room 3";
        if (msg.contains("online")) p.venue = "Online";
        if (msg.contains("2025")) p.year = "2025";
        if (msg.contains("2026")) p.year = "2026";
        return p;
    }
    private JobQueryParams extractJobParameters(String msg) {
        msg = msg.toLowerCase(); JobQueryParams p = new JobQueryParams();
        if (msg.contains("intern")) p.jobType = "Internship"; if (msg.contains("job")) p.jobType = "Job";
        if (msg.contains("it")) p.department = "IT"; if (msg.contains("computer")) p.department = "Computer Science";
        if (msg.contains("pune")) p.location = "Pune"; if (msg.contains("bangalore")) p.location = "Bangalore";
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
            StringBuilder reply = new StringBuilder("Events:\n"); boolean found = false;
            while (rs.next()) { found = true; reply.append("📅 ").append(rs.getString("event_name")).append("\n"); }
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
            StringBuilder reply = new StringBuilder("Alumni found:\n"); boolean found = false;
            while (rs.next()) { found = true; reply.append("👤 ").append(rs.getString("name")).append(" - ").append(rs.getString("company")).append("\n"); }
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
            StringBuilder reply = new StringBuilder("Jobs:\n"); boolean found = false;
            while (rs.next()) { found = true; reply.append("💼 ").append(rs.getString("title")).append(" at ").append(rs.getString("company")).append("\n"); }
            return found ? reply.toString() : "No jobs found.";
        } catch (Exception e) { return "Error: " + e.getMessage(); }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new chatinterface().setVisible(true));
    }
}