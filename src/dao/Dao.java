package dao;

import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;


public interface Dao {
	
	public void connect();

	public void disconnect();

	public Employee getEmployee(int employeeId, String password);
	
	public ArrayList<Product> getInventory();
	
	public boolean writeInventory(ArrayList<Product> inventory);

	public boolean addProduct(String name, Amount price, int stock, boolean avaiblable);

	public boolean addStockProduct(String name, int stock);

	public boolean deleteProduct(String name);
}