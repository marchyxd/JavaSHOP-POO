package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.jaxb.JaxbMarshaller;
import dao.jaxb.JaxbUnMarshaller;
import model.Employee;
import model.Product;

public class DaoImplJaxb implements Dao  {

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Product> getInventory()throws SQLException {
		JaxbUnMarshaller jaxbUnMarshaller = new JaxbUnMarshaller();
		return jaxbUnMarshaller.read();
	}

	@Override
	public boolean writeInventory(ArrayList<Product> product) {
		JaxbMarshaller jaxbMarshall = new JaxbMarshaller();
		return jaxbMarshall.export(product);
	}

}
