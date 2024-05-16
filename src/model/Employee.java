package model;


import main.Logeable;

public class Employee extends Person implements Logeable{
	private int employeeId;
	private final int USER = 123;
	private final String PASSWORD = "test";
	
	
	
	public Employee(String name) {
		super(name);
		this.employeeId = USER;
	}


	public int getUSER() {
		return USER;
	}


	public String getPASSWORD() {
		return PASSWORD;
	}


	@Override
	public boolean login(int user, String password) {
		if(user == USER && password.equals(PASSWORD)) {
			return true;
		}
		return false;
	}
}
