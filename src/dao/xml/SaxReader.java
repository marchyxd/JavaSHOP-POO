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
	    // Switch statement to handle different XML elements
		switch (qName) {
		case "product":
            // Create a new Product object using the "name" attribute, defaulting to "empty" if not present
			this.product = new Product(attributes.getValue("name") != null ? attributes.getValue("name") : "empty");
            // Set the ID of the product using the current product ID
			this.product.setId(idProduct);
			break;
		case "wholesalerPrice":
            // Set the currency for the wholesaler price from the "currency" attribute
			this.product.setCurrency(attributes.getValue("currency"));
			break;
		}
		this.parsedElement = qName;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
	    // Convert character array to a string representing the current value
		value = new String(ch, start, length);
		
		switch (parsedElement) {
		case "product":
            // No action needed for "product" element, as its attributes are handled in startElement
			break;
		case "wholesalerPrice":
            // Set the wholesaler price and calculate the public price, using the parsed value
		    this.product.setWholesalerPrice(new Amount(Double.parseDouble(value)));
		    this.product.setPublicPrice(new Amount(Double.parseDouble(value) * 2));
			break;
		case "stock":
            // Set the stock quantity using the parsed value
			this.product.setStock(Integer.valueOf(value));
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
	    // Check if the end of a "product" element has been reached
		if (qName.equals("product")) {
	        // Add the parsed product to the products list
			this.products.add(product);
	        // Update the total number of products and increment the ID for the next product
			Product.setTotalProducts(idProduct);
			idProduct++;
		}
	    // Reset the parsedElement to indicate the end of the current element
		this.parsedElement = "";
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}


}

