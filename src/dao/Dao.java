package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import model.Product;

public interface Dao {
	 
	public void connect();
	    
	public Employee getEmployee(int employeeId, String password);
	    
	public void disconnect() ;
	
	
	//method getIventory
	public ArrayList<Product> getInventory() throws SQLException;
	
	//method writeInventory
	public boolean writeInventory(ArrayList<Product> product);
	
}