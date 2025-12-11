/*
 * Modern Admin Dashboard for Campus Connect
 */
package alumini;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * @author piyush
 */
public class Admin extends javax.swing.JFrame {

    // Logic Variables
    private String adminUser;
    private CardLayout cardLayout;
    
    // UI Components
    private JPanel pnlSideBar, pnlMainContent, pnlHeader;
    private JLabel lblTitle, lblWelcome;
    
    // Menu Buttons
    private JButton btnAnnouncements, btnAlumni, btnEvents, btnJobs, btnFAQ, btnUsers, btnEditProfile, btnFeedback, btnLogout;

    // Content Panels (Lazy Loading)
    private announcementpanel pnlAnnouncements;
    private AlumniPanel pnlAlumni;
    private eventpanel pnlEvents;
    private jobpanel pnlJobs;
    private faqpanel pnlFAQ;
    private userpanel pnlUsers;

    /**
     * Default Constructor
     */
    public Admin() {
        initComponents();
    }

    /**
     * Main Constructor with Username
     */
    public Admin(String username) {
        this.adminUser = username;
        initComponents();
        lblWelcome.setText("Welcome, " + username);
    }

    private void initComponents() {
        setTitle("Campus Connect - Admin Control Center");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===================================================================
        // 1. LEFT SIDEBAR (Navigation)
        // ===================================================================
        pnlSideBar = new JPanel();
        pnlSideBar.setPreferredSize(new Dimension(260, 0));
        pnlSideBar.setBackground(new Color(33, 43, 54)); // Dark Slate Blue
        pnlSideBar.setLayout(new BoxLayout(pnlSideBar, BoxLayout.Y_AXIS));
        pnlSideBar.setBorder(new EmptyBorder(20, 0, 20, 0));

        // -- Logo Section --
        JPanel pnlLogo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlLogo.setBackground(new Color(33, 43, 54));
        pnlLogo.setMaximumSize(new Dimension(260, 60));
        
        lblTitle = new JLabel("ADMIN PANEL");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        pnlLogo.add(lblTitle);
        
        pnlSideBar.add(pnlLogo);
        pnlSideBar.add(Box.createVerticalStrut(30)); // Spacer

        // -- Menu Buttons --
        btnAnnouncements = createMenuButton("📢 Announcements");
        btnAlumni = createMenuButton("🎓 Alumni Records");
        btnEvents = createMenuButton("📅 Events Manager");
        btnJobs = createMenuButton("💼 Job Portal");
        btnFAQ = createMenuButton("❓ FAQs");
        btnUsers = createMenuButton("👥 User Management");
        btnFeedback = createMenuButton("📝 View Feedback"); // New Feature!
        
        // Add Buttons to Sidebar
        pnlSideBar.add(btnAnnouncements);
        pnlSideBar.add(btnAlumni);
        pnlSideBar.add(btnEvents);
        pnlSideBar.add(btnJobs);
        pnlSideBar.add(btnFAQ);
        pnlSideBar.add(btnUsers);
        pnlSideBar.add(btnFeedback);
        
        pnlSideBar.add(Box.createVerticalGlue()); // Push bottom buttons down

        // -- Bottom Buttons (Profile & Logout) --
        btnEditProfile = createMenuButton("⚙️ Settings");
        btnEditProfile.setBackground(new Color(44, 62, 80)); 
        
        btnLogout = createMenuButton("🚪 Logout");
        btnLogout.setBackground(new Color(192, 57, 43)); // Red
        
        pnlSideBar.add(btnEditProfile);
        pnlSideBar.add(btnLogout);

        // ===================================================================
        // 2. MAIN CONTENT AREA
        // ===================================================================
        JPanel pnlRight = new JPanel(new BorderLayout());
        
        // -- Header Bar --
        pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setPreferredSize(new Dimension(0, 60));
        pnlHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        lblWelcome = new JLabel("Welcome, Admin");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblWelcome.setForeground(new Color(50, 50, 50));
        lblWelcome.setBorder(new EmptyBorder(0, 20, 0, 0));
        
        pnlHeader.add(lblWelcome, BorderLayout.WEST);
        pnlRight.add(pnlHeader, BorderLayout.NORTH);

        // -- Content Panel (CardLayout) --
        cardLayout = new CardLayout();
        pnlMainContent = new JPanel(cardLayout);
        pnlMainContent.setBackground(new Color(245, 246, 250)); // Light Grey bg

        // Initialize Panels (Lazy loading happens here basically)
        pnlAnnouncements = new announcementpanel();
        pnlAlumni = new AlumniPanel();
        pnlEvents = new eventpanel();
        pnlJobs = new jobpanel();
        pnlFAQ = new faqpanel();
        pnlUsers = new userpanel();

        // Add Panels to CardLayout
        pnlMainContent.add(pnlAnnouncements, "announcements");
        pnlMainContent.add(pnlAlumni, "alumni");
        pnlMainContent.add(pnlEvents, "events");
        pnlMainContent.add(pnlJobs, "jobs");
        pnlMainContent.add(pnlFAQ, "faq");
        pnlMainContent.add(pnlUsers, "users");
        
        // Default View
        cardLayout.show(pnlMainContent, "announcements");

        pnlRight.add(pnlMainContent, BorderLayout.CENTER);

        // Add to Frame
        add(pnlSideBar, BorderLayout.WEST);
        add(pnlRight, BorderLayout.CENTER);

        // ===================================================================
        // 3. ACTION LISTENERS
        // ===================================================================
        btnAnnouncements.addActionListener(e -> switchPanel("announcements", btnAnnouncements));
        btnAlumni.addActionListener(e -> switchPanel("alumni", btnAlumni));
        btnEvents.addActionListener(e -> switchPanel("events", btnEvents));
        btnJobs.addActionListener(e -> switchPanel("jobs", btnJobs));
        btnFAQ.addActionListener(e -> switchPanel("faq", btnFAQ));
        btnUsers.addActionListener(e -> switchPanel("users", btnUsers));
        
        btnFeedback.addActionListener(e -> new ViewFeedback().setVisible(true)); // Pop-up window
        
        btnEditProfile.addActionListener(e -> {
            if (this.adminUser != null) new AdminProfileEdit(this.adminUser).setVisible(true);
            else JOptionPane.showMessageDialog(this, "Admin user not set!");
        });

        btnLogout.addActionListener(e -> {
            this.dispose();
            new MainHome().setVisible(true);
        });
    }

    // --- Helper: Create Styled Menu Button ---
    // --- Helper: Create Styled Menu Button ---
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(260, 50));
        btn.setBackground(new Color(33, 43, 54));
        btn.setForeground(new Color(189, 195, 199)); // Greyish Text
        
        // Font logic for emojis
        Font font = new Font("Segoe UI Emoji", Font.PLAIN, 14);
        
        // ✅ FIX: Use "canDisplayUpTo" with Double Quotes ("") 
        // This checks if the font supports the emoji. If NOT (-1 means it supports it), fallback to Dialog.
        if (font.canDisplayUpTo("📢") != -1) {
            font = new Font("Dialog", Font.PLAIN, 14);
        }
        btn.setFont(font);
        
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Hover Effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn.getBackground().getRed() != 0) // Don't change if it's the active blue one
                    btn.setBackground(new Color(44, 62, 80));
            }
            public void mouseExited(MouseEvent e) {
                if (btn.getBackground().getRed() != 0) // Don't reset if active
                    btn.setBackground(new Color(33, 43, 54)); 
            }
        });
        
        return btn;
    }
       
    // --- Helper: Switch Tabs ---
    private void switchPanel(String cardName, JButton activeButton) {
        cardLayout.show(pnlMainContent, cardName);
        
        // Reset all buttons to default dark color
        resetButtonColor(btnAnnouncements);
        resetButtonColor(btnAlumni);
        resetButtonColor(btnEvents);
        resetButtonColor(btnJobs);
        resetButtonColor(btnFAQ);
        resetButtonColor(btnUsers);
        
        // Highlight active button
        activeButton.setBackground(new Color(0, 102, 204)); // Active Blue
        activeButton.setForeground(Color.WHITE);
    }

    private void resetButtonColor(JButton btn) {
        btn.setBackground(new Color(33, 43, 54));
        btn.setForeground(new Color(189, 195, 199));
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Admin("admin").setVisible(true));
    }
}