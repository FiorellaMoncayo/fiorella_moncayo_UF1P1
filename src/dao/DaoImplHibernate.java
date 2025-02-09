package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import model.Amount;
import model.Employee;
import model.Product;
import model.ProductHistory;

public class DaoImplHibernate implements Dao {

	private SessionFactory sessionFactory;
	private Connection connection;

	@Override
	public void connect() {
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
			sessionFactory = new Configuration().configure("hibernate.cfg.xml")
					.addAnnotatedClass(Product.class).buildSessionFactory();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void disconnect() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> productList = new ArrayList<>();
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();

			TypedQuery<Product> productQuery = session.createQuery("FROM Product", Product.class);
			productList = new ArrayList<>(productQuery.getResultList());

			session.getTransaction().commit();
			
			System.out.println("Inventario cargado correctamente");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error al cargar el inventario");
		}
		return productList;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {

		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();

			for (Product product : inventory) {
				ProductHistory history = new ProductHistory();
				history.setIdProduct(product);
				history.setAvailable(product.isAvailable());
				history.setCreatedAt(LocalDateTime.now());
				history.setName(product.getName());
				history.setPrice(product.getPublicPrice().getValue());
				history.setStock(product.getStock());
				
				System.out.println(history.getId() + " " + history.getName() + " " + history.getPrice()+ " " + history.getStock() + " " + history.isAvailable());

				session.save(history);
			}

			session.getTransaction().commit();
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addProduct(String name, Amount price, int stock, boolean avaiblable) {
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();

			Product product = new Product();
			product.setName(name);
			//product.setWholesalerPrice(price);
			product.setPrice(price.getValue());
			product.setStock(stock);
			product.setAvailable(avaiblable);


			session.save(product);
			session.getTransaction().commit();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addStockProduct(String name, int stock) {
		try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            
            String query = "FROM Product WHERE lower(name) = lower(:name)";
            Product foundProduct = session.createQuery(query, Product.class)
                                          .setParameter("name", name)
                                          .uniqueResult();
            
            if (foundProduct != null) {
                foundProduct.setStock(foundProduct.getStock() + stock);
                session.getTransaction().commit();
                return true;
            } else {
                System.out.println("Product not found: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

	@Override
	public boolean deleteProduct(String name) {
		try (Session session = sessionFactory.getCurrentSession()) {
			session.beginTransaction();

			String query = "FROM Product p WHERE lower(p.name) = lower(:name)";
			Product foundProduct = session.createQuery(query, Product.class)
					.setParameter("productName", name.toLowerCase()).uniqueResult();

			if (foundProduct != null) {
				session.delete(foundProduct);
				session.getTransaction().commit();
				return true;
			} else {
				System.out.println("No se encontró el producto: " + name);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

}
