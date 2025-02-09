package model;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "products")
public class ProductList {
	private int total;

	private ArrayList<Product> listProducts = new ArrayList<>();

	public ProductList() {

	}

	public ProductList(ArrayList<Product> product) {
		this.listProducts = product;
	}

	@XmlAttribute(name = "total")
	public int getTotal() {
		// return total;
		return listProducts.size();
	}

	@XmlElement(name = "product")
	public ArrayList<Product> getProducts() {
		return listProducts;
	}

}