import java.sql.*;

public class DB_Base {
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/pokat";
                conn = DriverManager.getConnection(url, "root", "");
                System.out.println("Database Connected!");
            } catch (SQLException e) {
                System.out.println("Connect Failed: " + e.getMessage());
            }
        }
        return conn;
    }
}