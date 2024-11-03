package model;


import main.Logeable;

import java.sql.SQLException;

import dao.Dao;
import dao.DaoImplJDBC;

public class Employee extends Person implements Logeable{
	private int employeeId;
	private String password;
	// private final int USER = 123;
	// private final String PASSWORD = "test";
    
	// Data access object for interacting with the database
	private Dao dao = new DaoImplJDBC();
	
	public Employee(int id, String name, String password) {
		super(name);
		this.employeeId = id;
		this.password = password;
	}
   
	// Default constructor
	public Employee() {

	}

	public int getEmplyeeId() {
		return employeeId;
	}
	
	public void setEmplyeeId(int emplyeeId) {
		this.employeeId = emplyeeId;
	}
    
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
			}else {
				// Return false if login fails
				return false;
			} 
	}
}
