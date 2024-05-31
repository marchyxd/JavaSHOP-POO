package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Employee;

public class DaoImplJDBC implements Dao{
	private Connection connection;

	@Override
	public void connect() throws SQLException {
		// TODO Auto-generated method stub
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		
		this.connection = DriverManager.getConnection(url, user, pass);
	}	
	

	@Override
	public Employee getEmployee(int user, String pw) {
		Employee employee = null;
		String select = "SELECT * FROM employee WHERE employeeId = ? AND password = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(select);
			ps.setInt(1, user);
			ps.setString(2, pw);
			// run query
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return employee;
	}

	@Override
	public void disconnect() throws SQLException {
		// TODO Auto-generated method stub
			if (connection != null) {
				connection.close();
			}
		
	}

}
