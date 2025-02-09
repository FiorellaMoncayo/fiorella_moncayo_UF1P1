package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "product")
@XmlType(propOrder = { "name", "available", "wholesalerPrice", "publicPrice", "stock" })

@Entity
@Table(name = "inventory")
public class Product implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Transient
	private Amount publicPrice;
	
	@Transient
	private Amount wholesalerPrice;
	
	@Column(name = "available")
	private boolean available;
	
	@Column(name = "price")
	private Double price;
	
	@Column(name = "stock")
	private int stock;
	
	@Transient
	private static int totalProducts = 0;

	public final static double EXPIRATION_RATE = 0.60;

	public Product() {
		this.id = ++totalProducts;
	}

	public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
		super();
		this.id = totalProducts + 1;
		this.name = name;
		this.wholesalerPrice = wholesalerPrice;
	    this.price = this.wholesalerPrice.getValue() * 2; 
		this.available = available;
		this.stock = stock;
		totalProducts++;
		calculatePrices();
	}
	
	@PostLoad //cuando se haga una query, esto lo carga
	private void calculatePrices() {
	        this.publicPrice = new Amount(this.price * 2);
	        this.wholesalerPrice = new Amount(this.price);
	}



	@XmlAttribute(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "publicPrice")
	public Amount getPublicPrice() {
		return publicPrice;
	}

	public void setPublicPrice(Amount publicPrice) {
		this.publicPrice = publicPrice;
	}

	@XmlElement(name = "wholesalerPrice")
	public Amount getWholesalerPrice() {
		return wholesalerPrice;
	}

	public void setWholesalerPrice(Amount wholesalerPrice) {
		this.wholesalerPrice = wholesalerPrice;
		this.publicPrice = new Amount(wholesalerPrice.getValue() * 2);
	}

	@XmlElement(name = "available")
	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@XmlElement(name = "stock")
	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
		this.available = stock > 0;
	}

	public static int getTotalProducts() {
		return totalProducts;
	}

	public static void setTotalProducts(int totalProducts) {
		Product.totalProducts = totalProducts;
	}

	public void expire() {
		this.publicPrice.setValue(this.getPublicPrice().getValue() * EXPIRATION_RATE);
		;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	    calculatePrices();
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", publicPrice=" + publicPrice + ", wholesalerPrice=" + wholesalerPrice
				+ ", available=" + available + ", stock=" + stock + "]";
	}

}
