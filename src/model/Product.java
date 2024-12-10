package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "product")
@XmlType(propOrder = { "available", "wholesalerPrice", "publicPrice", "stock" })
public class Product {
	private int id;
    private String name;
    private Amount publicPrice;
    private Amount wholesalerPrice;
    private boolean available;
    private int stock;
    
    private static int totalProducts;
    
    static double EXPIRATION_RATE=0.60;
    
    public Product(String name, double wholesalerPrice, boolean available, int stock) {
		super();
		this.id = totalProducts+1;
		this.name = name;
		this.publicPrice = new Amount(wholesalerPrice * 2);
        this.wholesalerPrice = new Amount(wholesalerPrice);
		this.available = available;
		this.stock = stock;
		totalProducts++;
	}
    
    
    public Product( int id, String name, double wholesalerPrice, boolean available, int stock) {
 		super();
 		this.id = id+1;
 		this.name = name;	
 		this.publicPrice = new Amount(wholesalerPrice * 2);
        this.wholesalerPrice = new Amount(wholesalerPrice);
 		this.available = available;
 		this.stock = stock;
 	}
    
    //constructor for XML
    public Product(String name) {
    	this.id = totalProducts + 1;
    	this.name = name;
    }
    
	// constructor for JAXB
	public Product() {
		this.id = totalProducts + 1;
		totalProducts++;
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
	}

	public static int getTotalProducts() {
		return totalProducts;
	}

	public static void setTotalProducts(int totalProducts) {
		Product.totalProducts = totalProducts;
	}

	
	public Amount expire() {
		double newPublicPriceValue = this.getPublicPrice().getValue() * EXPIRATION_RATE;
		this.publicPrice.setValue(newPublicPriceValue);
		return this.publicPrice;
	}

	@Override
	public String toString() {
		return "Product [name=" + this.name + ", stock=" + this.stock + "]";
	}


    
}
