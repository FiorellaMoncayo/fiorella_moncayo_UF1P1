package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "products")
public class ProductList {

	private int total;
	
	private ArrayList<Product> products = new ArrayList<>();

	public ProductList() {
		
	}

	public ProductList(ArrayList<Product> productList) {
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
	
	@XmlAttribute(name = "total")
	public int getTotal() {
		return products.size();
	}
}
