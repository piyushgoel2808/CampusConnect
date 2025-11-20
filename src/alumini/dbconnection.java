package alumini;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconnection {
    private static final String URL = "jdbc:mysql://localhost:3306/chatbot"; // your DB name
    private static final String USER = "root"; // default XAMPP username
    private static final String PASSWORD = "root"; // empty if no password set

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // ✅ Load MySQL JDBC Driver (important for older NetBeans setups)
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }
        return conn;
    }
}
