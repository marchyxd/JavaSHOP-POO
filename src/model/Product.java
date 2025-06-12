package model;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@Entity
@Table(name = "inventory")
@XmlRootElement(name = "product")
@XmlType(propOrder = { "available", "wholesalerPrice", "publicPrice", "stock" })
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = true)
    private int id;

    @Column
    private String name;

    @Transient
    private Amount publicPrice;

    @Transient
    private Amount wholesalerPrice;

    @Column(name = "price")
    private double price;

    @Column(name = "available")
    private boolean available;

    @Column(name = "stock")
    private int stock;

    @Transient
    private static int totalProducts;

    @Transient
    static final double EXPIRATION_RATE = 0.60;

    public Product(String name, double wholesalerPrice, boolean available, int stock) {
        this.name = name;
        this.price = wholesalerPrice;
        this.publicPrice = new Amount(wholesalerPrice * 2);
        this.wholesalerPrice = new Amount(wholesalerPrice);
        this.available = available;
        this.stock = stock;
        totalProducts++;
    }

    public Product(int id, String name, double wholesalerPrice, boolean available, int stock) {
        this.id = id;
        this.name = name;
        this.price = wholesalerPrice;
        this.publicPrice = new Amount(wholesalerPrice * 2);
        this.wholesalerPrice = new Amount(wholesalerPrice);
        this.available = available;
        this.stock = stock;
        totalProducts++;
    }

    public Product(String name) {
        this.name = name;
    }

    public Product() {
        // Default constructor required by JPA
    }

    // Getters and setters remain the same, but with proper annotations
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        this.wholesalerPrice = new Amount(this.price);
        this.publicPrice = new Amount(this.price * 2);
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + this.publicPrice + "}";
    }
}