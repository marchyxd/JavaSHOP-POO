package dao;

import org.hibernate.cfg.Configuration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import model.Employee;
import model.Product;
import model.ProductHistory;
	
public class DaoImplHibernate implements Dao {
	Configuration configuration = new Configuration();

	private Session session;
	private Transaction tx;

	@Override
	public void connect() {
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		org.hibernate.SessionFactory sessionFactory = configuration.buildSessionFactory();
		session = sessionFactory.openSession();

	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		session.close();
	}

	@Override
	public ArrayList<Product> getInventory() throws SQLException {
		ArrayList<Product> productsList = new ArrayList<Product>();
		connect();
		try {
			tx = session.beginTransaction();
			Query<Product> query = session.createQuery("FROM Product p", Product.class);
			List<Product> list = query.list();
			productsList.addAll(list);
			
			for (Product product : productsList) {
				product.setPrice(product.getPrice());
			}
			/*
			 * // Print out each product to verify
			 * System.out.println("Inventory Products:"); for (Product product :
			 * productsList) { System.out.println("Product ID: " + product.getId() +
			 * ", Name: " + product.getName() + ", Price: " + product.getPrice() ); }
			 */

			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return productsList;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> product) {
		Date today = new Date();
		boolean isExport = false;
		connect();
		try {
			tx = session.beginTransaction();
			for (Product inventoryProduct : product) {
				ProductHistory history = new ProductHistory();
				history.setIdProduct(inventoryProduct.getId());
				history.setName(inventoryProduct.getName());
				history.setPrice(inventoryProduct.getPrice());
				history.setStock(inventoryProduct.getStock());
				history.setCreatedAt(today);
				session.save(history);
			}
			tx.commit();
			isExport = true;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return isExport;
	}

	@Override
	public void addProduct(Product product) {
		connect();
		try {
			tx = session.beginTransaction();
			// Set 'price' from 'wholesalerPrice' to save data in the database
			product.setPrice(product.getWholesalerPrice().getValue());
			session.save(product);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	@Override
	public void updateProduct(Product product) {
		// TODO Auto-generated method stub
		connect();
		try {
			tx = session.beginTransaction();
			session.update(product);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
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
			session.remove(product);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			disconnect();
		}


	}

}
