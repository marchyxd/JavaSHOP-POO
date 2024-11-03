package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import model.Product;

public interface Dao {
	 
	void connect();
	    
	Employee getEmployee(int employeeId, String password);
	    
	void disconnect() ;
	
	
	//method getIventory
	ArrayList<Product> getInventory();
	
	//method writeInventory
	boolean writeInventory(ArrayList<Product> inventory);
	
}