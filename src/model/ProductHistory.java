package model;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "historical_inventory")
public class ProductHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "id_product")
	private int idProduct;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "wholesalerPrice")
	private double price;
	
	@Column(name = "stock")
	private int stock;
	
	@Column(name = "available")
	private int available;
	
	public ProductHistory() {
		super();
	}
	
	public ProductHistory(int id, Date createdAt, int idProduct) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.idProduct = idProduct;
	}
	
	// Getters and setters
	public int getId() {
		return id;
	}
	
	public int getIdProduct() {
		return idProduct;
	}
	
	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
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
	
	public int getAvailable() {
		return available;
	}
	
	public void setAvailable(int available) {
		this.available = available;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}