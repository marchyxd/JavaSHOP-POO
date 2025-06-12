package model;

import main.Logeable;

import javax.persistence.*;

import dao.Dao;
import dao.DaoImplHibernate;

@Entity
@Table(name = "employee")
public class Employee extends Person implements Logeable {
	@Id
	@Column(name = "employeeId")
	private int employeeId;
	
	@Column(name = "password")
	private String password;
	
	@Transient
	private Dao dao = new DaoImplHibernate(); 

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
