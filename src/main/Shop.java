package main;

import model.Product;
import model.Sale;
import model.Amount;
import model.Client;
import model.Employee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import dao.Dao;
import dao.DaoImplHibernate;
import dao.DaoImplJDBC;

public class Shop {
	private Amount cash = new Amount(100.00);
//	private Product[] inventory;
	private ArrayList<Product> inventory;
	private int numberProducts;
//	private Sale[] sales;
	private ArrayList<Sale> sales;
	private int numberSales;
	//private DaoImplJDBC dao;
	private DaoImplHibernate dao;

	final static double TAX_RATE = 1.04;

	public Shop() {
		this.dao = new DaoImplHibernate();
		inventory = new ArrayList<Product>();
		sales = new ArrayList<Sale>();
	}
	
	

	public boolean WriteInventory() {
		//return dao.writeInventory(inventory);
		//boolean result = dao.writeInventory(inventory);
		dao.connect();
		boolean result = false;
		
		try {
			result = dao.writeInventory(inventory);
			
			if (result) {
		        System.out.println("Inventario guardado correctamente.");
		    } else {
		        System.out.println("Hubo un error al guardar el inventario.");
		    }
		} catch (Exception e) {
			System.out.println("Ocurrió un error al intentar guardar el inventario: " + e.getMessage());
	        e.printStackTrace();
		}
		dao.disconnect();
	    return result;
	}
	
	
	

	public Amount getCash() {
		return cash;
	}



	public void setCash(Amount cash) {
		this.cash = cash;
	}



	public ArrayList<Product> getInventory() {
		return inventory;
	}



	public void setInventory(ArrayList<Product> inventory) {
		this.inventory = inventory;
	}



	public int getNumberProducts() {
		return numberProducts;
	}



	public void setNumberProducts(int numberProducts) {
		this.numberProducts = numberProducts;
	}



	public ArrayList<Sale> getSales() {
		return sales;
	}



	public void setSales(ArrayList<Sale> sales) {
		this.sales = sales;
	}



	public int getNumberSales() {
		return numberSales;
	}



	public void setNumberSales(int numberSales) {
		this.numberSales = numberSales;
	}



	public static void main(String[] args) {
		Shop shop = new Shop();

		// load inventory from external data
		shop.loadInventory();
		
		// init session as employee
		shop.initSession();

		Scanner scanner = new Scanner(System.in);
		int opcion = 0;
		boolean exit = false;

		do {
			System.out.println("\n");
			System.out.println("===========================");
			System.out.println("Menu principal miTienda.com");
			System.out.println("===========================");
			System.out.println("1) Contar caja");
			System.out.println("2) Añadir producto");
			System.out.println("3) Añadir stock");
			System.out.println("4) Marcar producto proxima caducidad");
			System.out.println("5) Ver inventario");
			System.out.println("6) Venta");
			System.out.println("7) Ver ventas");
			System.out.println("8) Ver venta total");
			System.out.println("9) Eliminar producto");
			System.out.println("10) Salir programa");
			System.out.print("Seleccione una opción: ");
			opcion = scanner.nextInt();

			switch (opcion) {
			case 1:
				shop.showCash();
				break;

			case 2:
				shop.addProduct(null, 0, new Amount(0));
				break;

			case 3:
				shop.addStock();
				break;

			case 4:
				shop.setExpired();
				break;

			case 5:
				shop.showInventory();
				break;

			case 6:
				shop.sale();
				break;

			case 7:
				shop.showSales();
				break;

			case 8:
				shop.showSalesAmount();
				break;

			case 9:
				shop.removeProduct();
				break;

			case 10:
				System.out.println("Cerrando programa ...");
				exit = true;
				break;
			}

		} while (!exit);

	}

	private void initSession() {
		// TODO Auto-generated method stub
		
		Employee employee = new Employee("test");
		boolean logged=false;
		
		do {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Introduzca numero de empleado: ");
			int employeeId = scanner.nextInt();
			
			System.out.println("Introduzca contraseña: ");
			String password = scanner.next();
			
			logged = employee.login(employeeId, password);
			if (logged) {
				System.out.println("Login correcto ");
			} else {
				System.out.println("Usuario o password incorrectos ");
			}
		} while (!logged);
				
	}

	/**
	 * load initial inventory to shop
	 */
	public void loadInventory() {
//		addProduct(new Product("Manzana", new Amount(10.00), true, 10));
//		addProduct(new Product("Pera", new Amount(20.00), true, 20));
//		addProduct(new Product("Hamburguesa", new Amount(30.00), true, 30));
//		addProduct(new Product("Fresa", new Amount(5.00), true, 20));
		// now read from file
		this.readInventory();
	}

	/**
	 * read inventory from file
	 */
	private void readInventory() {
		//this.inventory = (dao.getInventory());
		dao.connect();
		inventory = dao.getInventory();
		
		for(Product product : inventory) {
			System.out.println(product);
		}
		dao.disconnect();
	}

	/**
	 * show current total cash
	 */
	private void showCash() {
		System.out.println("Dinero actual: " + cash);
	}

	/**
	 * add a new product to inventory getting data from console
	 */
	public boolean addProduct(String name, int stock, Amount price) {
		
		Product productInInventory = findProduct(name);
	    if (productInInventory != null) {
	        System.out.println("El producto ya existe en el inventario.");
	        return false;
	    }
	    
	    dao.connect();
		
	    boolean operationResult = false;
		
		try {
			boolean available = stock > 0;
			addProduct(new Product(name, price, available, stock));
			operationResult = dao.addProduct(name, price, stock, available);
			
			 if (operationResult) {
		            System.out.println("Producto añadido correctamente al inventario.");
		        } else {
		            System.out.println("Error al añadir el producto a la base de datos.");
		        }
			
		} catch (Exception e) {
			System.out.println("Ocurrió un error al intentar añadir el producto: " + e.getMessage());
			e.printStackTrace();
		}
		dao.disconnect();

	    return operationResult;
	}

	/**
	 * remove a new product to inventory getting data from console
	 */
	public void removeProduct() {
		if (inventory.size() == 0) {
			System.out.println("No se pueden eliminar productos, inventario vacio");
			return;
		}
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();
		Product product = findProduct(name);

		if (product != null) {
	        dao.connect(); // Conectar al DAO

	        try {
	            if (dao.deleteProduct(name)) {
	                // Si la eliminación en la base de datos fue exitosa, eliminar de la lista
	                inventory.remove(product);
	                System.out.println("El producto " + name + " ha sido eliminado del inventario.");
	            } else {
	                System.out.println("Error: No se pudo eliminar el producto de la base de datos.");
	            }
	        } catch (Exception e) {
	            System.out.println("Ocurrió un error al intentar eliminar el producto: " + e.getMessage());
	            e.printStackTrace();
	        }

	        dao.disconnect();

	    } else {
	        System.out.println("No se ha encontrado el producto con nombre " + name);
	    }
	}

	/**
	 * add stock for a specific product
	 */
	public void addStock() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();
		Product product = findProduct(name);
		
		dao.connect();

		if (product != null) {
			// ask for stock
			System.out.print("Seleccione la cantidad a añadir: ");
			int stock = scanner.nextInt();
			// update stock product
			dao.addStockProduct(name, stock);
			product.setStock(product.getStock() + stock);
			System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());

		} else {
			System.out.println("No se ha encontrado el producto con nombre " + name);
		}
		
		dao.disconnect();
	}

	/**
	 * set a product as expired
	 */
	private void setExpired() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();

		Product product = findProduct(name);

		if (product != null) {
			product.expire();
			System.out.println("El precio del producto " + name + " ha sido actualizado a " + product.getPublicPrice());
		}
	}

	/**
	 * show all inventory
	 */
	public void showInventory() {
		System.out.println("Contenido actual de la tienda:");
		try {
			for (Product product : inventory) {
				if (product != null) {
					System.out.println(product);
				}
			}
		}catch (Exception e) {
			System.out.println(e);
		}
		
	}

	/**
	 * make a sale of products to a client
	 */
	public void sale() {
		// ask for client name
		Scanner sc = new Scanner(System.in);
		System.out.println("Realizar venta, escribir nombre cliente");
		String nameClient = sc.nextLine();
		Client client = new Client(nameClient);

		// sale product until input name is not 0
		// Product[] shoppingCart = new Product[10];
		ArrayList<Product> shoppingCart = new ArrayList<Product>();
		int numberShopping = 0;

		Amount totalAmount = new Amount(0.0);
		String name = "";
		while (!name.equals("0")) {
			System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
			name = sc.nextLine();

			if (name.equals("0")) {
				break;
			}
			Product product = findProduct(name);
			boolean productAvailable = false;

			if (product != null && product.isAvailable()) {
				productAvailable = true;
				totalAmount.setValue(totalAmount.getValue() + product.getPublicPrice().getValue());
				product.setStock(product.getStock() - 1);
				shoppingCart.add(product);
				numberShopping++;
				// if no more stock, set as not available to sale
				if (product.getStock() == 0) {
					product.setAvailable(false);
				}
				System.out.println("Producto añadido con éxito");
			}

			if (!productAvailable) {
				System.out.println("Producto no encontrado o sin stock");
			}
		}

		totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
		// show cost total
		System.out.println("Venta realizada con éxito, total: " + totalAmount);
		
		// make payment
		if(!client.pay(totalAmount)) {
			System.out.println("Cliente debe: " + client.getBalance());;
		}

		// create sale
		Sale sale = new Sale(client, shoppingCart, totalAmount);

		// add to shop
		sales.add(sale);
//		numberSales++;

		// add to cash
		cash.setValue(cash.getValue() + totalAmount.getValue());
	}

	/**
	 * show all sales
	 */
	private void showSales() {
		System.out.println("Lista de ventas:");
		for (Sale sale : sales) {
			if (sale != null) {
				System.out.println(sale);
			}
		}
		
		// ask for client name
		Scanner sc = new Scanner(System.in);
		System.out.println("Exportar fichero ventas? S / N");
		String option = sc.nextLine();
		if ("S".equalsIgnoreCase(option)) {
			this.writeSales();
		} 
		
	}

	/**
	 * write in file the sales done
	 */
	private void writeSales() {
		
	}

	/**
	 * show total amount all sales
	 */
	private void showSalesAmount() {
		Amount totalAmount = new Amount(0.0);
		for (Sale sale : sales) {
			if (sale != null) {
				totalAmount.setValue(totalAmount.getValue() + sale.getAmount().getValue());
			}
		}
		System.out.println("Total cantidad ventas:");
		System.out.println(totalAmount);
	}

	/**
	 * add a product to inventory
	 * 
	 * @param product
	 */
	public void addProduct(Product product) {
		if (isInventoryFull()) {
			System.out.println("No se pueden añadir más productos, se ha alcanzado el máximo de " + inventory.size());
			return;
		}
		inventory.add(product);
		numberProducts++;
		
		dao.writeInventory(inventory);
	}
	
	

	/**
	 * check if inventory is full or not
	 */
	public boolean isInventoryFull() {
		if (numberProducts == 10) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * find product by name
	 * 
	 * @param product name
	 */
	public Product findProduct(String name) {
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i) != null && inventory.get(i).getName().equalsIgnoreCase(name)) {
				return inventory.get(i);
			}
		}
		return null;

	}
	

}