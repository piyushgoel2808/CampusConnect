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

private class JobQueryParams {
    String department;
    String role;
    String location;
    String company;
    String jobType; // "internship" or "job"
}

private class EventQueryParams {
    String venue;
    String month;
    String year;
    String date;
    String keyword;
}

public class AlumniParameters {
    private String name;
    private String department;
    private String company;
    private String designation;
    private Integer batch;
}
public class AlumniParams {
    public String name;
    public String department;
    public String company;
    public String designation;
    public Integer batch;
}



public AlumniParams extractAlumniParams(String msg) {
    msg = msg.toLowerCase();
    AlumniParams p = new AlumniParams();

    // Departments
    String[] depts = {"computer science", "it", "electrical", "electronics", "mechanical", "civil", "biotechnology"};
    for (String d : depts) {
        if (msg.contains(d)) p.department = capitalize(d);
    }

    // Companies
    String[] companies = {"infosys", "tcs", "wipro", "intel", "reliance", "mahindra", "accenture", "biocon", "l&t"};
    for (String c : companies) {
        if (msg.contains(c.toLowerCase())) p.company = capitalize(c);
    }

    // Designations (partial match)
    String[] roles = {"engineer", "developer", "analyst", "associate", "lead", "research", "manager"};
    for (String r : roles) {
        if (msg.contains(r)) p.designation = capitalize(r);
    }

    // Batch year
    for (int year = 2000; year <= 2030; year++) {
        if (msg.contains(String.valueOf(year))) {
            p.batch = year;
        }
    }

    // Name extraction (if user types EXACT name)
    if (msg.contains("amit sharma")) p.name = "Amit Sharma";
    if (msg.contains("ananya das")) p.name = "Ananya Das";
    if (msg.contains("divya joshi")) p.name = "Divya Joshi";
    if (msg.contains("harshad patil")) p.name = "Harshad Patil";
    if (msg.contains("kavita reddy")) p.name = "Kavita Reddy";
    if (msg.contains("mohit singh")) p.name = "Mohit Singh";
    if (msg.contains("priya verma")) p.name = "Priya Verma";
    if (msg.contains("rohan gupta")) p.name = "Rohan Gupta";
    if (msg.contains("sneha iyer")) p.name = "Sneha Iyer";
    if (msg.contains("vikram nair")) p.name = "Vikram Nair";

    return p;
}

private String capitalize(String text) {
    return text.substring(0,1).toUpperCase() + text.substring(1);
}





private EventQueryParams extractEventParameters(String msg) {
    msg = msg.toLowerCase();
    EventQueryParams p = new EventQueryParams();

    // Detect venue
    if (msg.contains("auditorium")) p.venue = "Auditorium Hall";
    if (msg.contains("seminar")) p.venue = "Seminar Room 3";
    if (msg.contains("online")) p.venue = "Online";

    
    
    // Detect month
    if (msg.contains("january")) p.month = "01";
    if (msg.contains("february")) p.month = "02";
    if (msg.contains("march")) p.month = "03";
    if (msg.contains("april")) p.month = "04";
    if (msg.contains("may")) p.month = "05";
    if (msg.contains("june")) p.month = "06";
    if (msg.contains("july")) p.month = "07";
    if (msg.contains("august")) p.month = "08";
    if (msg.contains("september")) p.month = "09";
    if (msg.contains("october")) p.month = "10";
    if (msg.contains("november")) p.month = "11";
    if (msg.contains("december")) p.month = "12";

    // Detect year
    if (msg.contains("2025")) p.year = "2025";
    if (msg.contains("2026")) p.year = "2026";

    // Detect specific date (yyyy-mm-dd)
    if (msg.matches(".*\\d{4}-\\d{2}-\\d{2}.*")) {
        p.date = msg.replaceAll(".*(\\d{4}-\\d{2}-\\d{2}).*", "$1");
    }

    // Detect keywords inside event name
    if (msg.contains("alumni")) p.keyword = "Alumni";
    if (msg.contains("tech")) p.keyword = "Tech";
    if (msg.contains("mentor")) p.keyword = "Mentorship";

    return p;
}

private String getDynamicEvents(EventQueryParams p) {

    StringBuilder sql = new StringBuilder(
        "SELECT event_name, event_date, event_venue, description FROM events WHERE 1=1 "
    );

    ArrayList<String> filters = new ArrayList<>();

    if (p.venue != null) {
        sql.append(" AND event_venue = ?");
        filters.add(p.venue);
    }

    if (p.year != null) {
        sql.append(" AND YEAR(event_date) = ?");
        filters.add(p.year);
    }

    if (p.month != null) {
        sql.append(" AND MONTH(event_date) = ?");
        filters.add(p.month);
    }

    if (p.date != null) {
        sql.append(" AND event_date = ?");
        filters.add(p.date);
    }

    if (p.keyword != null) {
        sql.append(" AND event_name LIKE ?");
        filters.add("%" + p.keyword + "%");
    }

    sql.append(" ORDER BY event_date ASC");

    StringBuilder reply = new StringBuilder("Here are the event results:\n\n");

    try (Connection conn = dbconnection.getConnection()) {

        PreparedStatement ps = conn.prepareStatement(sql.toString());

        for (int i = 0; i < filters.size(); i++) {
            ps.setString(i + 1, filters.get(i));
        }

        ResultSet rs = ps.executeQuery();
        boolean found = false;

        while (rs.next()) {
            found = true;
            reply.append("📅 ").append(rs.getString("event_name"))
                 .append("\n   Date: ").append(rs.getString("event_date"))
                 .append("\n   Venue: ").append(rs.getString("event_venue"))
                 .append("\n   Details: ").append(rs.getString("description"))
                 .append("\n\n");
        }

        if (!found) {
            return "No events found matching your filters.";
        }

    } catch (Exception e) {
        return "⚠ Error fetching events: " + e.getMessage();
    }

    return reply.toString();
}


private JobQueryParams extractJobParameters(String msg) {
    msg = msg.toLowerCase();
    JobQueryParams p = new JobQueryParams();

    // Detect job type
    if (msg.contains("intern")) p.jobType = "Internship";
    if (msg.contains("job")) p.jobType = "Job";

    // Detect department
    if (msg.contains("mechanical")) p.department = "Mechanical";
    if (msg.contains("civil")) p.department = "Civil";
    if (msg.contains("electrical")) p.department = "Electrical";
    if (msg.contains("computer")) p.department = "Computer";
    if (msg.contains("it")) p.department = "IT";
    if (msg.contains("electronics")) p.department = "Electronics";

    // Detect role / skill
    if (msg.contains("full stack")) p.role = "Full Stack";
    if (msg.contains("developer")) p.role = "Developer";
    if (msg.contains("data science")) p.role = "Data Science";
    if (msg.contains("python")) p.role = "Python";

    // Detect location (add more cities anytime)
    if (msg.contains("pune")) p.location = "Pune";
    if (msg.contains("delhi")) p.location = "Delhi";
    if (msg.contains("mumbai")) p.location = "Mumbai";
    if (msg.contains("bangalore")) p.location = "Bangalore";

    // Detect company name
    if (msg.contains("infosys")) p.company = "Infosys";
    if (msg.contains("tcs")) p.company = "TCS";
    if (msg.contains("wipro")) p.company = "Wipro";
    if (msg.contains("tech mahindra")) p.company = "Tech Mahindra";

    return p;
}




    
     private String username; // received from login page
     private String role;
    public chatinterface(String username, String role) {

       initComponents();
       this.username = username;
       this.role = role;

       if ("alumni".equalsIgnoreCase(role)) {
        loadAlumniData(username);
       } else if ("user".equalsIgnoreCase(role)) {
        loadUserData(username);
    }}
    
    private void loadUserData(String username) {
    try {
        Connection con = dbconnection.getConnection();
        String query = "SELECT name, email FROM users WHERE username = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, username);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            lblwelcome.setText("Welcome, " + rs.getString("name"));
            lblemail.setText("Email: " + rs.getString("email"));

            // Hide extra labels for users
            lblbatch.setVisible(false);
            lbldepartment.setVisible(false);
            lblcompany.setVisible(false);
            lbldesignation.setVisible(false);
        } else {
            lblwelcome.setText("User not found!");
        }

        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    private void loadAlumniData(String username) {
        try {
            Connection con = dbconnection.getConnection(); // assuming you have this method
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

            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(chatinterface.class.getName());

    /**
     */
    public chatinterface() {
        initComponents();
    }
    
    
    public String getDynamicAlumni(AlumniParams p) {

    StringBuilder sql = new StringBuilder(
        "SELECT name, department, company, designation, batch FROM alumni WHERE 1=1 "
    );

    ArrayList<String> filters = new ArrayList<>();

    if (p.name != null) {
        sql.append(" AND name LIKE ?");
        filters.add("%" + p.name + "%");
    }

    if (p.department != null) {
        sql.append(" AND department = ?");
        filters.add(p.department);
    }

    if (p.company != null) {
        sql.append(" AND company = ?");
        filters.add(p.company);
    }

    if (p.designation != null) {
        sql.append(" AND designation LIKE ?");
        filters.add("%" + p.designation + "%");
    }

    if (p.batch != null) {
        sql.append(" AND batch = ?");
        filters.add(String.valueOf(p.batch));
    }

    sql.append(" ORDER BY name ASC");

    StringBuilder reply = new StringBuilder("Here are the alumni results:\n\n");

    try (Connection conn = dbconnection.getConnection()) {

        PreparedStatement ps = conn.prepareStatement(sql.toString());

        for (int i = 0; i < filters.size(); i++) {
            ps.setString(i + 1, filters.get(i));
        }

        ResultSet rs = ps.executeQuery();
        boolean found = false;

        while (rs.next()) {
            found = true;
            reply.append("👤 ").append(rs.getString("name"))
                 .append("\n   Dept: ").append(rs.getString("department"))
                 .append("\n   Company: ").append(rs.getString("company"))
                 .append("\n   Role: ").append(rs.getString("designation"))
                 .append("\n   Batch: ").append(rs.getInt("batch"))
                 .append("\n\n");
        }

        if (!found) return "No matching alumni found.";

    } catch (Exception e) {
        return "⚠ Error fetching alumni: " + e.getMessage();
    }

    return reply.toString();
}

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TxtChatArea = new javax.swing.JTextArea();
        TxtMessage = new java.awt.TextField();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblwelcome = new javax.swing.JLabel();
        lblemail = new javax.swing.JLabel();
        lblbatch = new javax.swing.JLabel();
        lbldepartment = new javax.swing.JLabel();
        lblcompany = new javax.swing.JLabel();
        lbldesignation = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 51));

        jLabel1.setFont(new java.awt.Font("Nirmala Text", 1, 12)); // NOI18N
        jLabel1.setText("BVICAM CHATBOT");

        TxtChatArea.setColumns(20);
        TxtChatArea.setRows(5);
        jScrollPane1.setViewportView(TxtChatArea);

        TxtMessage.setText("textField1");

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
                    .addComponent(TxtMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        lblwelcome.setText("jLabel2");

        lblemail.setText("jLabel3");

        lblbatch.setText("jLabel4");

        lbldepartment.setText("jLabel5");

        lblcompany.setText("jLabel6");

        lbldesignation.setText("jLabel7");

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblbatch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lbldepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblcompany, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbldesignation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblemail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblwelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblwelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblemail, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblbatch, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbldepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblcompany, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbldesignation, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

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
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        // TODO add your handling code here:
         String userMessage = TxtMessage.getText().trim();
    if (userMessage.isEmpty()) return;

    TxtChatArea.append("You: " + userMessage + "\n");

    String botReply;
if (userMessage.toLowerCase().contains("event")) {

    EventQueryParams p = extractEventParameters(userMessage);
    botReply = getDynamicEvents(p);
}else if (userMessage.toLowerCase().contains("alumni")) {

    AlumniParams p = extractAlumniParams(userMessage);
    botReply = getDynamicAlumni(p);
}

else if (userMessage.toLowerCase().contains("job") ||
         userMessage.toLowerCase().contains("intern")) {

    JobQueryParams p = extractJobParameters(userMessage);
    botReply = getDynamicJobResults(p);
}
else {
        botReply = getBotReplyFromDatabase(userMessage); // your earlier keyword table logic
    }
    TxtChatArea.append("Bot: " + botReply + "\n\n");
    lastBotReply = botReply;  // store latest chatbot answer

    TxtMessage.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    
    
    
    
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         
        if (lastBotReply == null || lastBotReply.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "No bot response to export!");
        return;
    }

    try {
        // Create JSON object
        org.json.JSONObject json = new org.json.JSONObject();
        json.put("chatbot_response", lastBotReply);

        // Choose location to save
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.setDialogTitle("Save Chatbot JSON");
        chooser.setSelectedFile(new java.io.File("chatbot_output.json"));

        int result = chooser.showSaveDialog(this);

        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            java.io.FileWriter fw = new java.io.FileWriter(file);
            fw.write(json.toString(4)); // pretty print with indentation
            fw.close();

            JOptionPane.showMessageDialog(this, 
                "JSON exported successfully:\n" + file.getAbsolutePath());
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
        
        
    }//GEN-LAST:event_jButton2ActionPerformed
private String getDynamicJobResults(JobQueryParams p) {

    StringBuilder sql = new StringBuilder("SELECT title, company, location, description FROM job_opportunities WHERE 1=1 ");
    ArrayList<String> filters = new ArrayList<>();
    
    if (p.department != null) {
        sql.append(" AND department = ?");
        filters.add(p.department);
    }
    if (p.role != null) {
        sql.append(" AND role LIKE ?");
        filters.add("%" + p.role + "%");
    }
    if (p.location != null) {
        sql.append(" AND location = ?");
        filters.add(p.location);
    }
    if (p.company != null) {
        sql.append(" AND company = ?");
        filters.add(p.company);
    }
    if (p.jobType != null) {
        sql.append(" AND type = ?");
        filters.add(p.jobType);
    }

    sql.append(" ORDER BY job_id DESC");

    StringBuilder reply = new StringBuilder("Here are the job results:\n\n");

    try (Connection conn = dbconnection.getConnection()) {

        PreparedStatement ps = conn.prepareStatement(sql.toString());

        // Fill dynamic parameters
        for (int i = 0; i < filters.size(); i++) {
            ps.setString(i + 1, filters.get(i));
        }

        ResultSet rs = ps.executeQuery();
        boolean found = false;

        while (rs.next()) {
            found = true;
            reply.append("💼 ").append(rs.getString("title"))
                 .append("\n   Company: ").append(rs.getString("company"))
                 .append("\n   Location: ").append(rs.getString("location"))
                 .append("\n   Details: ").append(rs.getString("description"))
                 .append("\n\n");
        }

        if (!found) {
            return "No matching job found for your filters.";
        }

    } catch (Exception e) {
        return "⚠ Error: " + e.getMessage();
    }

    return reply.toString();
}

    private String getJobOpportunities() {
    StringBuilder reply = new StringBuilder("Here are the latest job opportunities for alumni:\n\n");

    try (Connection conn = dbconnection.getConnection()) {

        String query = "SELECT title, company, location, description "
                     + "FROM job_opportunities ORDER BY job_id DESC";

        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        boolean found = false;

        while (rs.next()) {
            found = true;

            String title = rs.getString("title");
            String company = rs.getString("company");
            String location = rs.getString("location");
            String description = rs.getString("description");

            reply.append("💼 ").append(title)
                 .append("\n   Company: ").append(company)
                 .append("\n   Location: ").append(location)
                 .append("\n   Details: ").append(description)
                 .append("\n\n");
        }

        if (!found) {
            reply = new StringBuilder("There are no job opportunities available right now.");
        }

    } catch (Exception e) {
        reply = new StringBuilder("⚠️ Error fetching job opportunities: " + e.getMessage());
    }

    return reply.toString();
}

    
    
    private String getUpcomingEvents() {
    StringBuilder reply = new StringBuilder("Here are the upcoming alumni events:\n\n");
    try (Connection conn = dbconnection.getConnection()) {
        String query = "SELECT event_name, event_date, event_venue FROM events WHERE event_date >= CURDATE() ORDER BY event_date ASC";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        boolean found = false;
        while (rs.next()) {
            found = true;
            String name = rs.getString("event_name");
            String date = rs.getString("event_date");
            String venue = rs.getString("event_venue");
            reply.append("📅 ").append(name)
                 .append("\n   Date: ").append(date)
                 .append("\n   Venue: ").append(venue)
                 .append("\n\n");
        }

        if (!found)
            reply = new StringBuilder("There are no upcoming events at the moment.");

    } catch (Exception e) {
        reply = new StringBuilder("⚠️ Error fetching events: " + e.getMessage());
    }
    return reply.toString();
}
    
    
    
private String getBotReplyFromDatabase(String userMessage) {
    String reply = "I'm sorry, I didn’t understand that.";

    try (Connection conn = dbconnection.getConnection()) {
        String query = "SELECT reply FROM chatbot_data WHERE LOWER(keyword) = LOWER(?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, userMessage.trim());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            reply = rs.getString("reply");
        }

    } catch (SQLException e) {
        reply = "⚠️ Database error: " + e.getMessage();
    }

    return reply;
}

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new chatinterface().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea TxtChatArea;
    private java.awt.TextField TxtMessage;
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
    // End of variables declaration//GEN-END:variables
}
