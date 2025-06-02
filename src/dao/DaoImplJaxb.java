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
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		return null;
	}

	@Override
	public void disconnect() {
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

	@Override
	public void addProduct(Product product) {
	}

	@Override
	public void updateProduct(Product product) {
	}

	@Override
	public void deleteProduct(Product product) {
	}

}
