package model;

import main.Logeable;

import dao.Dao;
import dao.DaoImplJDBC;
import dao.DaoImplMongoDB;

public class Employee extends Person implements Logeable {
	private int employeeId;
	private String password;
	private String name;

	public Employee() {
	}

	public Employee(int employeeId, String password) {
		this.employeeId = employeeId;
		this.password = password;
	}
	
	public Employee(int id, String name, String password) {
		super(name);
		this.employeeId = id;
		this.password = password;
	}
	
	@Override
	public boolean login(int user, String pw) {
		try {
			Dao dao = new DaoImplMongoDB();
			Employee authEmployee = dao.getEmployee(user, pw);
			return authEmployee != null;
		} catch (Exception e) {
			System.err.println("Error during login: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
/** 
	// Implementation of the login method from the Logeable interface
	@Override
	public boolean login(int employeeId, String password) {
		// Connect to the database
		dao.connect();
		// Fetch the employee
		Employee employee = dao.getEmployee(employeeId, password);

		if (employee != null) {
			// Disconnect from the database
			dao.disconnect();
			// Return true if employee exists
			return true;
		} else {
			// Return false if login fails
			return false;
		}
	}
**/
	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Employee{" + "employeeId=" + employeeId + ", name='" + name + '\'' + '}';
	}
}
