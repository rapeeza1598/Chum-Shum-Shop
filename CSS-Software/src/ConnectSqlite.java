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
    //table Customer
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
            System.out.println("First_Name: " + resultSet.getString("first_name"));
            System.out.println("Last_Name: " + resultSet.getString("last_name"));
        } else {
            System.out.println("User not found.");
        }
    }
    public void updateCustomer(String first_name,String last_name, String Email,String phone,String address,String city,boolean state,String zip, String id) throws SQLException {
        String sql = "UPDATE customer SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?, city = ?, state = ?, zip = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, first_name);
        statement.setString(2, last_name);
        statement.setString(3, Email);
        statement.setString(4, phone);
        statement.setString(5, address);
        statement.setString(6, city);
        statement.setBoolean(7, state);
        statement.setString(8, zip);
        statement.setString(9, id);
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("The user's password has been updated.");
        } else {
            System.out.println("User not found na ja.");
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
    //table Product
    public void createProduct(String name, String description, String price, String category) throws SQLException{
        String sql = "INSERT INTO product (name, description, price, category) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, description);
        statement.setString(3, price);
        statement.setString(4, category);
        int rowsInserted = statement.executeUpdate();
        if(rowsInserted > 0){
            System.out.println("A new user has been created.");
        }
    }
    public void readProduct(String name) throws SQLException{
        String sql = "SELECT * FROM product WHERE name = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            System.out.println("User ID: " + resultSet.getInt("id"));
            System.out.println("nameProduct: " + resultSet.getString("name"));
            System.out.println("price: " + resultSet.getString("price"));
            System.out.println("category: " + resultSet.getString("category"));
        } else {
            System.out.println("User not found.");
        }
    }
    public void updateProduct(String name, String description, String price, String category, String id) throws SQLException{
        String sql = "UPDATE product SET name = ?, description = ?, price = ?, category = ? where id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, description);
        statement.setString(3, price);
        statement.setString(4, category);
        statement.setString(5, id);
        int rowsInserted = statement.executeUpdate();
        if(rowsInserted > 0 ){
            System.out.println("The user's password has been updated.");
        } else {
            System.out.println("User not found.");
        }
    }
    public void deleteProduct(String name) throws SQLException {
        String sql = "DELETE FROM product WHERE name = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("The Product has been deleted.");
        } else {
            System.out.println("Product not found.");
        }
    }
    //table Order_by
    public void createOrder(int customerId, String orderDate) throws SQLException {
        String sql = "INSERT INTO order_by (customer_id, order_date) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, customerId);
        statement.setString(2, orderDate);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new user has been created.");
        }else{
            System.out.println("not found order_by");
        }
    }
    public void updateOrder(int orderId, int customerId, String orderDate) throws SQLException {
        String sql = "UPDATE order_by SET customer_id = ?, order_date = ? WHERE order_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, customerId);
        statement.setString(2, orderDate);
        statement.setInt(3,orderId);
        int rowsUpdated = statement.executeUpdate();
        if(rowsUpdated > 0){
            System.out.println("Update already done.");
        }else{
            System.out.println("Error Please check.");
        }

    }
    public void readOrder(int orderId) throws SQLException{
        String sql = "SELECT * FROM order_by WHERE order_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, orderId);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            System.out.println("Order_ID: " + resultSet.getInt("order_id"));
            System.out.println("Customer_id: " + resultSet.getString("customer_id"));
            System.out.println("Date :" + resultSet.getString("order_date"));
        } else {
            System.out.println("User not found.");
        }
    }
    public void deleteOrder(int orderId) throws SQLException{
        String sql = "DELETE FROM Order_by WHERE order_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, orderId);
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("The order has been deleted.");
        } else {
            System.out.println("order not found.");
        }
    }

    //table OrderItem
    public void createOrderItem(int orderId, String productName, int quantity, double price) throws SQLException{
        String sql = "INSERT INTO OrderItem (order_id, product_name, quantity, price) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, orderId);
        statement.setString(2, productName);
        statement.setInt(3, quantity);
        statement.setDouble(4, price);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new user has been created.");
        }else{
            System.out.println("can't Create orderItem");
        }
    }
    public void updateOrderItem(int orderItemId, int orderId, String productName, int quantity, double price) throws SQLException {
        String sql = "UPDATE OrderItem SET order_id = ?, product_name = ?, quantity = ?, price = ? WHERE order_item_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, orderId);
        statement.setString(2, productName);
        statement.setInt(3, quantity);
        statement.setDouble(4, price);
        statement.setInt(5, orderItemId);
        int rowsUpdated = statement.executeUpdate();
        if(rowsUpdated > 0){
            System.out.println("Update already done.");
        }else{
            System.out.println("Error Please check.");
        }
    }
    public void readOrderItem(int orderId) throws SQLException{
        String sql = "SELECT * FROM OrderItem WHERE order_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, orderId);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){
            System.out.println("OrderItem_id: " + resultSet.getInt("order_id"));
            System.out.println("Product_name: " + resultSet.getString("product_name"));
            System.out.println("Quantity: " + resultSet.getInt("quantity"));
            System.out.println("Price: " + resultSet.getDouble("price"));
        } else {
            System.out.println("User not found.");
        }
    }
    public void deleteOrderItem(int orderItemId) throws SQLException{
        String sql = "DELETE FROM OrderItem WHERE order_item_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, orderItemId);
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("The order has been deleted.");
        } else {
            System.out.println("order not found.");
        }
    }

    public static void main(String[] args) {
        try {
            ConnectSqlite cn = new ConnectSqlite("database.db");
            cn.createProduct("sukird", "surayoot", "10.5", "1");
            cn.readProduct("sukird");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}