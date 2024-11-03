package dao.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Amount;
import model.Product;

public class SaxReader extends DefaultHandler {
	ArrayList<Product> products;
	Product product;
	String value;
	String parsedElement;
	
	private int idProduct = 1;
	/**
	 * @return the products
	 */
	public ArrayList<Product> getProducts() {
		return products;
	}

	/**
	 * @param products the products to set
	 */
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	@Override
	public void startDocument() throws SAXException {
		this.products = new ArrayList<>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException    {
		switch (qName) {
		case "product":
			this.product = new Product(attributes.getValue("name") != null ? attributes.getValue("name") : "empty");
			this.product.setId(idProduct);
			break;
		case "wholesalerPrice":
			this.product.setCurrency(attributes.getValue("currency"));
			break;
		}
		this.parsedElement = qName;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		value = new String(ch, start, length);
		switch (parsedElement) {
		case "product":
			break;
		case "wholesalerPrice":
		        this.product.setWholesalerPrice(new Amount(Double.parseDouble(value)));
		        this.product.setPublicPrice(new Amount(Double.parseDouble(value) * 2));
			break;
		case "stock":
			this.product.setStock(Integer.valueOf(value));
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("product")) {
			this.products.add(product);
			Product.setTotalProducts(idProduct);
			idProduct++;
		}
		this.parsedElement = "";
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}


}

