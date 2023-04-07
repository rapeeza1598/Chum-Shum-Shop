import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectSqlite {
    private Connection connection;

    public ConnectSqlite(String dbName) throws ClassNotFoundException, SQLException {
        // Step 1: Load the SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
            // Step 2: Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:sqlite:"+dbName);
            System.out.println("Connection to SQLite has been established.");
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to load the JDBC driver.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Unable to connect to the database.");
        }
    }

    public void closeConnection() throws SQLException {
        // Step 5: Close the connection
        if (connection != null) {
            connection.close();
            System.out.println("Connection to SQLite has been closed.");
        }
    }

    public void createUser(String username, String password, String first_name,String last_name, String Email) throws SQLException {
        // Step 3: Execute a CREATE operation
        String sql = "INSERT INTO users (username, password, first_name, last_name, email) VALUES (?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, sha256(password));
        statement.setString(3, first_name);
        statement.setString(4, last_name);
        statement.setString(5, Email);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new user has been created.");
        }
    }

    public void readUser(String username) throws SQLException {
        // Step 3: Execute a READ operation
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            System.out.println("User ID: " + resultSet.getInt("id"));
            System.out.println("Username: " + resultSet.getString("username"));
            System.out.println("Password: " + resultSet.getString("password"));
        } else {
            System.out.println("User not found.");
        }
    }

    public void updateUser(String username, String password) throws SQLException {
        // Step 3: Execute an UPDATE operation
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, sha256(password));
        statement.setString(2, username);
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("The user's password has been updated.");
        } else {
            System.out.println("User not found.");
        }
    }

    public void deleteUser(String username) throws SQLException {
        // Step 3: Execute a DELETE operation
        String sql = "DELETE FROM users WHERE username = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("The user has been deleted.");
        } else {
            System.out.println("User not found.");
        }
    }

    private static String sha256(String str) {
        try {
            // Create a new instance of the SHA-256 message digest algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert the input string to a byte array and compute the digest
            byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));

            // Convert the resulting byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void createCustomer(String first_name,String last_name, String Email,String phone,String address,String city,boolean state,String zip) throws SQLException {
        // Step 3: Execute a CREATE operation
        String sql = "INSERT INTO customer (first_name, last_name, email, phone, address, city, state, zip) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, first_name);
        statement.setString(2, last_name);
        statement.setString(3, Email);
        statement.setString(4, phone);
        statement.setString(5, address);
        statement.setString(6, city);
        statement.setBoolean(7, state);
        statement.setString(8, zip);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new user has been created.");
        }
    }
    public void readCustomer(String first_name) throws SQLException {
        // Step 3: Execute a READ operation
        String sql = "SELECT * FROM customer WHERE first_name = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, first_name);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            System.out.println("User ID: " + resultSet.getInt("id"));
            System.out.println("Username: " + resultSet.getString("first_name"));
            System.out.println("Password: " + resultSet.getString("last_name"));
        } else {
            System.out.println("User not found.");
        }
    }
    public void updateCustomer(String first_name,String last_name, String Email,String phone,String address,String city,boolean state,String zip) throws SQLException {
        // Step 3: Execute an UPDATE operation
        String sql = "UPDATE customer SET last_name = ?, email = ?, phone = ?, address = ?, city = ?, state = ?, zip = ? WHERE first_name = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, last_name);
        statement.setString(2, Email);
        statement.setString(3, phone);
        statement.setString(4, address);
        statement.setString(5, city);
        statement.setBoolean(6, state);
        statement.setString(7, zip);
        statement.setString(8, first_name);
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("The user's password has been updated.");
        } else {
            System.out.println("User not found.");
        }
    }
    public void deleteCustomer(String first_name) throws SQLException {
        // Step 3: Execute a DELETE operation
        String sql = "DELETE FROM customer WHERE first_name = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, first_name);
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("The user has been deleted.");
        } else {
            System.out.println("User not found.");
        }
    }
    public static void main(String[] args) {
        try {
            ConnectSqlite cn = new ConnectSqlite("database.db");
//            cn.createUser("admin", "admin", "surayoot","setpom", "surayoot@sukird.com");
            cn.readUser("admin");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
