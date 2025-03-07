package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import dao.xml.DomWriter;
import dao.xml.SaxReader;
import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplXml implements Dao {

	@Override
	public void connect() {

	}

	@Override
	public void disconnect() {

	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
		SaxReader handler = null;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser;
			parser = factory.newSAXParser();
			File file = new File("files/inputInventory.xml");

			handler = new SaxReader();
			parser.parse(file, handler);
		} catch (ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return handler.getProducts();
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		DomWriter domWriter = new DomWriter(inventory);
		return domWriter.generateDocument();
	}

	@Override
	public boolean addProduct(String name, Amount price, int stock, boolean avaiblable) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addStockProduct(String name, int stock) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteProduct(String name) {
		// TODO Auto-generated method stub
		return false;
	}

}
