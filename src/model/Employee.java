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
	
	private Dao dao = new DaoImplJDBC();
	
	public Employee(int id, String name, String pw) {
		super(name);
		this.employeeId = id;
		this.password = pw;
	}
	
	public Employee() {

	}

	public int getEmplyeeId() {
		return employeeId;
	}
	
	public void setEmplyeeId(int emplyeeId) {
		this.employeeId = emplyeeId;
	}

	@Override
	public boolean login(int user, String password) {
		try {
			dao.connect();
			Employee employee = dao.getEmployee(user, password);

			if (employee != null) {
				dao.disconnect();
				return true;
			}

		} catch (SQLException e) {
			System.out.println(e);
		}

		return false;
	}
}
