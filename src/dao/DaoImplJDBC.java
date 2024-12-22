package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplJDBC implements Dao {
	Connection connection;

	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (connection != null) {
			try {
				if(!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		String query = "select * from employee where employeeId= ? and password = ? ";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setInt(1,employeeId);
    	  	ps.setString(2,password);
    	  	//System.out.println(ps.toString());
            try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
            		employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
            	}
            }
        } catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
    	return employee;
	}

	@Override
	public ArrayList<Product> getInventory() {
		
		ArrayList<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Inventory"; //???
        
        try (PreparedStatement ps = connection.prepareStatement(query)) { 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("name");
                    boolean available = rs.getBoolean("available");
                    double wholesalerPrice = rs.getDouble("wholesalerPrice");
                    int stock = rs.getInt("stock");
                    Product product = new Product(name, new Amount(wholesalerPrice), available, stock);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

	
	
	
	// Método para cargar los nombres que existen en la tabla historical_inventory
	private Set<String> loadExistingProductNames() throws SQLException {
	    Set<String> productNames = new HashSet<>();
	    String query = "SELECT name FROM historical_inventory";
	    
	    try (PreparedStatement ps = connection.prepareStatement(query);
	         ResultSet rs = ps.executeQuery()) {
	        while (rs.next()) {
	            productNames.add(rs.getString("name"));
	        }
	    }
	    return productNames;
	}
	
	
	// Método para procesar actualizaciones e inserciones
	private void processBatchUpdates(ArrayList<Product> inventory, Set<String> productNamesInHisInv, PreparedStatement insertPs, PreparedStatement updatePs) throws SQLException {
	    // Configura el formato de fecha y hora actual
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String currentTime = LocalDateTime.now().format(formatter);

	    // Recorre el inventario y decide si actualizar o insertar cada producto
	    for (Product product : inventory) {
	        if (productNamesInHisInv.contains(product.getName())) {
	            // Si el producto ya existe, prepara una actualización
	            updatePs.setInt(1, product.getId());
	            updatePs.setDouble(2, product.getWholesalerPrice().getValue());
	            updatePs.setBoolean(3, product.isAvailable());
	            updatePs.setInt(4, product.getStock());
	            updatePs.setString(5, currentTime);
	            updatePs.setString(6, product.getName());
	            updatePs.addBatch(); // Agrega a lote de actualizaciones
	        } else {
	            // Si el producto no existe, prepara una inserción
	            insertPs.setInt(1, product.getId());
	            insertPs.setString(2, product.getName());
	            insertPs.setDouble(3, product.getWholesalerPrice().getValue());
	            insertPs.setBoolean(4, product.isAvailable());
	            insertPs.setInt(5, product.getStock());
	            insertPs.setString(6, currentTime);
	            insertPs.addBatch(); // Agrega a lote de inserciones
	        }
	    }
	}
	
	
	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		 // Query para insertar nuevos productos
	    String insertQuery = "INSERT INTO historical_inventory (id_producto, name, wholesalerPrice, available, stock, created_at) VALUES (?, ?, ?, ?, ?, ?)";
	    // Query para actualizar productos existentes
	    String updateQuery = "UPDATE historical_inventory SET id_producto = ?, wholesalerPrice = ?, available = ?, stock = ?, created_at = ? WHERE name = ?";

	    try {
	        // Carga los nombres de productos existentes en la base de datos para evitar duplicados
	        Set<String> productNamesInHisInv = loadExistingProductNames();

	        // Usa try-with-resources para manejar automáticamente el cierre de recursos
	        try (PreparedStatement insertPs = connection.prepareStatement(insertQuery);
	             PreparedStatement updatePs = connection.prepareStatement(updateQuery)) {

	            // Inicia una transacción para asegurar consistencia en las operaciones
	            connection.setAutoCommit(false);

	            // Procesa las operaciones de actualización o inserción en la base de datos
	            processBatchUpdates(inventory, productNamesInHisInv, insertPs, updatePs);

	            // Ejecuta los lotes de actualizaciones e inserciones
	            updatePs.executeBatch();
	            insertPs.executeBatch();

	            // Confirma la transacción si todo salió bien
	            connection.commit();
	            return true;

	        } catch (SQLException e) {
	            // En caso de error, revierte todos los cambios realizados en esta transacción
	            connection.rollback();
	            e.printStackTrace();
	            return false;
	        }
	    } catch (SQLException e) {
	        // Maneja excepciones durante la carga de datos inicial
	        e.printStackTrace();
	        return false;
	    }
	}

	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean addProduct(String name, Amount price, int stock, boolean avaiblable) {
		String insertQuery = "INSERT INTO Inventory (name, available, wholesalerPrice, publicPrice, stock) "
                + "VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
	        
		    ps.setString(1, name);
		    ps.setBoolean(2, avaiblable);
		    ps.setDouble(3, price.getValue());
		    ps.setDouble(4, price.getValue()*2);
		    ps.setInt(5, stock);
		    
		    ps.addBatch();
		
		ps.executeBatch();
		return true;
		} catch (SQLException e) {
		e.printStackTrace();
		return false;
		}
	}

	@Override
	public boolean addStockProduct(String name, int stock) {
		String updateQuery = "UPDATE Inventory SET stock = stock + ? WHERE name = ?";
		try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
	        
		    ps.setInt(1, stock);
		    ps.setString(2, name);
		    
		    ps.addBatch();
		
		ps.executeBatch();
		return true;
		} catch (SQLException e) {
		e.printStackTrace();
		return false;
		}
	}

	@Override
	public boolean deleteProduct(String name) {
		String deleteQuery = "DELETE FROM Inventory WHERE name=?";
		try (PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
			ps.setString(1, name);
	        int rowsAffected = ps.executeUpdate();
	        System.out.println("Archivos eliminados: " + rowsAffected);
	        return rowsAffected > 0;
		}catch (Exception e) {
			e.printStackTrace();
			return false;		
		}
	}

}