package dao;

// Import necessary SQL and utility classes
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.Employee;
import model.Product;

// Implementation of the Data Access Object (DAO) interface using JDBC
public class DaoImplJDBC implements Dao {
    private Connection connection; // Variable to hold the database connection

    // Constructor that establishes the connection to the database
    public DaoImplJDBC() {
        connect();
    }

    // Connects to the database
    @Override
    public void connect() {
    	// Database URL
        String url = "jdbc:mysql://localhost:3306/shop"; 
        // Database username
        String user = "root"; 
        // Database password
        String pass = ""; 
        try {
            // Establish the connection
            this.connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            // Print error message if connection fails
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    // Retrieves an employee from the database using employeeId and password
    @Override
    public Employee getEmployee(int employeeId, String password) {
        Employee employee = null; 
        String select = "SELECT * FROM employee WHERE employeeId = ? AND password = ?"; 
        try (PreparedStatement ps = connection.prepareStatement(select)) {
            // Set parameters for the query
            ps.setInt(1, employeeId);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                // If a result is found, create an Employee object
                if (rs.next()) {
                    employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
                }
            }
        } catch (SQLException e) {
            // Print error message if retrieval fails
            System.err.println("Error retrieving employee: " + e.getMessage());
        }
        return employee; // Return the found employee or null
    }

    // Disconnects from the database
    @Override
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close(); 
            } catch (SQLException e) {
                // Print error message if disconnection fails
                System.err.println("Error disconnecting from the database: " + e.getMessage());
            }
        }
    }

    // Retrieves the inventory from the database
    @Override
    public ArrayList<Product> getInventory() throws SQLException {
    	// List to hold products
        ArrayList<Product> products = new ArrayList<>(); 
        if (connection == null) {
        	// Throw exception if connection is null
            throw new SQLException("Database connection is not initialized."); 
        }
        try (Statement ps = connection.createStatement(); 
             ResultSet rs = ps.executeQuery("SELECT * FROM inventory")) {
            // Iterate through the result set and add products to the list
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name"),
                        rs.getDouble("wholesalerPrice"), rs.getBoolean("available"),
                        rs.getInt("stock")));
            }
        } catch (SQLException e) {
            // Print error message if retrieval fails
            System.err.println("Error retrieving inventory: " + e.getMessage());
        }
        return products; // Return the list of products
    }

    // Writes the inventory to a historical table
    @Override
    public boolean writeInventory(ArrayList<Product> product) {
    	// Flag to check if any product was exported
        boolean isExported = false; 
        // Get the current time
        LocalDateTime now = LocalDateTime.now(); 
        // Date format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
        // Format the current date
        String formattedDate = now.format(formatter); 
        
        String exportInventory = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) VALUES (?, ?, ?, ?, ?, ?)"; // SQL query for export
        for (Product dataProduct : product) {
            try (PreparedStatement statement = this.connection.prepareStatement(exportInventory)) {
                // Set parameters for the export query
                statement.setInt(1, dataProduct.getId());
                statement.setString(2, dataProduct.getName());
                statement.setDouble(3, dataProduct.getWholesalerPrice().getValue());
                statement.setBoolean(4, dataProduct.isAvailable());
                statement.setInt(5, dataProduct.getStock());
                statement.setString(6, formattedDate);
                // Execute the update
                int rowsAffected = statement.executeUpdate(); 
                if (rowsAffected > 0) {
                	// Set flag if export was successful
                    isExported = true; 
                    System.out.println("Product exported: " + dataProduct.getName());
                }
            } catch (SQLException e) {
                // Print error message if writing fails
                System.err.println("Error writing to inventory: " + e.getMessage());
            }
        }
        
        // Print success or failure message based on export status
        if (isExported) {
            System.out.println("Inventory export completed successfully.");
        } else {
            System.out.println("No products were exported.");
        }
        // Return the export status
        return isExported; 
    }

    // Adds a product to the inventory
    @Override
    public void addProduct(Product product) {
    	// SQL query for adding product
        String addProduct = "INSERT INTO Inventory (name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?)"; 
        try (PreparedStatement statement = this.connection.prepareStatement(addProduct)) {
            // Set parameters for the add query
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getWholesalerPrice().getValue());
            statement.setBoolean(3, product.isAvailable());
            statement.setInt(4, product.getStock());
            // Execute the update
            statement.executeUpdate(); 
            System.out.println("Product added: " + product.getName());
        } catch (SQLException e) {
            // Print error message if adding fails
            System.err.println("Error adding product: " + e.getMessage());
        }
    }

    // Updates an existing product in the inventory
    @Override
    public void updateProduct(Product product) {
    	// SQL query for updating product
        String updateProduct = "UPDATE Inventory SET stock = ?, wholesalerPrice = ?, available = ? WHERE name = ?"; 
        try (PreparedStatement statement = this.connection.prepareStatement(updateProduct)) {
            // Set parameters for the update query
            statement.setInt(1, product.getStock());
            statement.setDouble(2, product.getWholesalerPrice().getValue());
            statement.setBoolean(3, product.isAvailable());
            statement.setString(4, product.getName());
            // Execute the update
            int rowsAffected = statement.executeUpdate(); 
            if (rowsAffected > 0) {
                System.out.println("Product updated: " + product.getName());
            } else {
                System.out.println("Product not found for update: " + product.getName());
            }
        } catch (SQLException e) {
            // Print error message if updating fails
            System.err.println("Error updating product: " + e.getMessage());
        }
    }

    // Deletes a product from the inventory
    @Override
    public void deleteProduct(Product product) {
    	// SQL query for deleting product
        String deleteProduct = "DELETE FROM Inventory WHERE name = ?"; 
        try (PreparedStatement deletePs = this.connection.prepareStatement(deleteProduct)) {
            // Set parameter for the delete query
            deletePs.setString(1, product.getName());
            // Execute the update
            int rowsAffected = deletePs.executeUpdate(); 
            if (rowsAffected > 0) {
                System.out.println("Product deleted: " + product.getName());
            } else {
                System.out.println("Product not found for deletion: " + product.getName());
            }
        } catch (SQLException e) {
            // Print error message if deletion fails
            System.err.println("Error deleting product: " + e.getMessage());
        }
    }
}
