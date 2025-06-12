package model;

import main.Logeable;

import dao.Dao;
import dao.DaoImplHibernate;
import dao.DaoImplJDBC;
import dao.DaoImplMongoDB;

public class Employee extends Person implements Logeable {
	private int employeeId;
	private String password;
	
	// Inicializar con la implementación de DAO deseada
	private Dao dao = new DaoImplJDBC(); 

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
		dao.connect();
		Employee employee = dao.getEmployee(user, pw);
		
		if (employee != null) {
			dao.disconnect();
			return true;
		} else {
			dao.disconnect();
			return false;
		}
	}

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
		return super.getName();
	}

	@Override
	public void setName(String name) {
		super.setName(name);
	}

	@Override
	public String toString() {
		return "Employee{" + "employeeId=" + employeeId + ", name='" + super.getName() + '\'' + '}';
	}
}
