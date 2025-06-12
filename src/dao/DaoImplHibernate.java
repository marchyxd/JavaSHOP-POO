package dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import utils.HibernateUtil;
import model.Employee;
import model.Product;
import model.ProductHistory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DaoImplHibernate implements Dao {
	private Session session;
	private Transaction tx;

	@Override
	public void connect() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	@Override
	public void disconnect() {
		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		connect();
		Employee employee = null;
		try {
			tx = session.beginTransaction();
			Query<Employee> query = session.createQuery(
					"FROM Employee e WHERE e.employeeId = :id AND e.password = :pwd", 
					Employee.class);
			query.setParameter("id", employeeId);
			query.setParameter("pwd", password);
			
			List<Employee> results = query.getResultList();
			if (!results.isEmpty()) {
				employee = results.get(0);
			}
			
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			System.err.println("Error authenticating employee: " + e.getMessage());
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return employee;
	}

	@Override
	public ArrayList<Product> getInventory() throws SQLException {
		connect();
		ArrayList<Product> productsList = new ArrayList<Product>();
		try {
			tx = session.beginTransaction();
			
			// Clear the session to ensure we get fresh data from the database
			session.clear();
			
			// Use SQL query to get the latest data
			Query<Product> query = session.createQuery("FROM Product p", Product.class);
			List<Product> list = query.list();
			productsList.addAll(list);

			// Ensure all transient fields are properly initialized
			for (Product product : productsList) {
				if (product.getWholesalerPrice() == null && product.getPrice() > 0) {
					product.setPrice(product.getPrice()); // This will initialize wholesalerPrice and publicPrice
				}
			}

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			System.err.println("Error loading inventory: " + e.getMessage());
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return productsList;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> productList) {
		boolean isExported = false;
		connect();
		try {
			tx = session.beginTransaction();
			Date today = new Date();
			for (Product product : productList) {
				// Create ProductHistory object with default constructor and set properties manually
				ProductHistory history = new ProductHistory();
				history.setIdProduct(product.getId());
				history.setName(product.getName());
				history.setPrice(product.getPrice());
				history.setStock(product.getStock());
				history.setAvailable(product.isAvailable() ? 1 : 0);
				history.setCreatedAt(today);
				session.save(history);
			}
			tx.commit();
			isExported = true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.err.println("Failed to export historical records: " + e.getMessage());
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return isExported;
	}

	@Override
	public void addProduct(Product product) {
		connect();
		try {
			tx = session.beginTransaction();
			
			// Important: Ensure price field contains the wholesalerPrice value
			// In the Product class, wholesalerPrice is @Transient and won't be persisted by Hibernate
			// But the price field will be persisted to the database, so we need to synchronize both values before saving
			if (product.getWholesalerPrice() != null) {
				product.setPrice(product.getWholesalerPrice().getValue());
			}
			
			// Save the product to the database
			session.save(product);
			
			// Refresh the entity to ensure it reflects the database state
			session.refresh(product);
			
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.err.println("Error adding product: " + e.getMessage());
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	@Override
	public void deleteProduct(Product product) {
		connect();
		try {
			tx = session.beginTransaction();
			
			// First get the current entity from the database to ensure we are deleting a managed object
			Product managedProduct = session.get(Product.class, product.getId());
			if (managedProduct == null) {
				System.err.println("Error: Cannot find product with ID " + product.getId() + " in the database");
				return;
			}
			
			// Delete the product
			session.delete(managedProduct);
			
			// Flush the session and commit the transaction
			session.flush();
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.err.println("Error deleting product: " + e.getMessage());
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	@Override
	public void updateProduct(Product product) {
		connect();
		try {
			tx = session.beginTransaction();
			
			// First get the current entity from the database to ensure we are updating a managed object
			Product managedProduct = session.get(Product.class, product.getId());
			if (managedProduct == null) {
				System.err.println("Error: Cannot find product with ID " + product.getId() + " in the database");
				return;
			}
			
			// Update the managed object properties
			managedProduct.setStock(product.getStock());
			managedProduct.setAvailable(product.isAvailable());
			
			// Ensure price field contains the latest wholesalerPrice value
			if (product.getWholesalerPrice() != null) {
				managedProduct.setPrice(product.getWholesalerPrice().getValue());
			}
			
			// Use merge to ensure object state is correctly synchronized to the database
			session.merge(managedProduct);
			
			// Commit the transaction
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			System.err.println("Error updating product: " + e.getMessage());
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

}
