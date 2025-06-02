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
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() throws SQLException {
		connect();
		ArrayList<Product> productsList = new ArrayList<Product>();
		try {
			tx = session.beginTransaction();
			Query<Product> query = session.createQuery("FROM Product p", Product.class);
			List<Product> list = query.list();
			productsList.addAll(list);

			for (Product product : productsList) {
				product.setPrice(product.getPrice());
			}

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
	public boolean writeInventory(ArrayList<Product> productList) {
		boolean isExported = false;
		connect();
		try {
			tx = session.beginTransaction();
			Date today = new Date();
			for (Product inventoryProduct : productList) {
				ProductHistory history = new ProductHistory();
				history.setIdProduct(inventoryProduct.getId());
				history.setName(inventoryProduct.getName());
				history.setPrice(inventoryProduct.getPrice());
				history.setStock(inventoryProduct.getStock());
				history.setAvailable(inventoryProduct.isAvailable() ? 1 : 0);
				history.setCreatedAt(today);
				
				session.save(history);
			}
			tx.commit();
			isExported = true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
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
			if (product.getWholesalerPrice() != null) {
				product.setPrice(product.getWholesalerPrice().getValue());
			}
			session.save(product);
			tx.commit();
		} catch (Exception e) {
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
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
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
			session.update(product);
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

}
