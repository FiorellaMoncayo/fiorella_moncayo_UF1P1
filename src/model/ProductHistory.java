package model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "historical_inventory")
public class ProductHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "available")
	private boolean available;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@OneToOne
	@JoinColumn(name = "id_product")
	private Product idProduct;

	@Column(name = "name")
	private String name;

	@Column(name = "price")
	private double price;

	@Column(name = "stock")
	private int stock;
	

	public ProductHistory() {
	}

	// Getters and setters
	public int getId() {
		return id;
	}

	public Product getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(Product idProduct) {
		this.idProduct = idProduct;
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime localDateTime) {
		this.createdAt = localDateTime;
	}


}
