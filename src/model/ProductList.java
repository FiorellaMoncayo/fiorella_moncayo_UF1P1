package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "inventory")
public class ProductList {

	private ArrayList<Product> products;

	public ProductList() {
		
	}

	public ProductList(ArrayList<Product> productList) {
		// TODO Auto-generated constructor stub
		super();
		this.products = productList;
		
	}

	@XmlElement(name = "product")
	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
}
