import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class ConnectionDB {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Step 1: Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Step 2: Establish a connection to the database
            String os = System.getProperty("os.name").toLowerCase();
            if (os.indexOf("win") >= 0) {
                // Windows OS
                connection = DriverManager.getConnection("jdbc:sqlite:database.db");
                System.out.println("windows");
            } else if (os.indexOf("mac") >= 0) {
                // macOS
                connection = DriverManager.getConnection("jdbc:sqlite:database.db");
                System.out.println("Mac");
            } else {
                // Unsupported OS
                throw new IllegalStateException("Unsupported operating system: " + os);
            }

            System.out.println("Connection to SQLite has been established.");

            // Step 3: Check the user and password
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "admin");
            statement.setString(2, "admin");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Login successful.");
            } else {
                System.out.println("Login failed.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to load the JDBC driver.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Unable to connect to the database.");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            // Step 3: Close the connection
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Connection to SQLite has been closed.");
                }
            } catch (SQLException e) {
                System.out.println("Unable to close the connection to the database.");
                e.printStackTrace();
            }
        }
    }
}
