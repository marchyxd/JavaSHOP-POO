package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import model.Product;

public class DaoImplJDBC implements Dao{
	private Connection connection;
    
	// Connects to the database
	@Override
	public void connect()  {
		// TODO Auto-generated method stub
		// Database URL
		String url = "jdbc:mysql://localhost:3306/shop";
		// Database username
		String user = "root";
		// Database password (empty in this case)
		String pass = "";
		try {
			// Establishing the connection
			this.connection = DriverManager.getConnection(url, user, pass);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
	}	
	
	// Retrieves an employee from the database using employeeId and password
	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		// SQL query to fetch employee
		String select = "SELECT * FROM employee WHERE employeeId = ? AND password = ?";
		try {
			// Preparing the SQL statement
			PreparedStatement ps = connection.prepareStatement(select);
			// Setting the employeeId parameter
			ps.setInt(1, employeeId);
			// Setting the password parameter
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					// Creating an Employee object from the result set
					employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return employee;
	}
	
	// Disconnects from the database
	@Override
	public void disconnect() {
		// TODO Auto-generated method stub       
		// Database disconnection
        if (connection != null) {
            try {
				connection.close();
			} catch (SQLException e) {
				System.err.println("Error disconnecting the database.");
				e.getMessage();
			}
        }
		
	}

	@Override
	public ArrayList<Product> getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> product) {
		// TODO Auto-generated method stub
		return false;
	}

}
