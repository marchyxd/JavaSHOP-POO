package dao.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.Amount;
import model.Product;

public class DomWriter {
	private Document document;
	private String fileName;
	private File file;
	
	
	public DomWriter(String name) {
	    // Assign the provided name to the fileName variable.
		this.fileName = name;
		try {
			// Create a new instance of DocumentBuilderFactory, which is used to configure and obtain a document builder.
			DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
	        // Declare a DocumentBuilder variable.
			DocumentBuilder builder;
	        // Create a new DocumentBuilder from the factory.
			builder = factory.newDocumentBuilder();
	        // Create a new XML Document using the DocumentBuilder.
			document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			System.out.println("ERROR generating document");
		}
	}
	
	public boolean generateReport(ArrayList<Product> products) {
		// Create the root element "products" and set its "total" attribute to the number of products.
		Element parentNode = document.createElement("products");
		parentNode.setAttribute("total", String.valueOf(products.size()));
	    // Append the parent node to the document.
		document.appendChild(parentNode);
		
	    // Iterate through each product in the provided list.
		for (Product product : products) {
	        // Create a new "product" element and set its "id" attribute.
			Element newProduct = document.createElement("product");
			newProduct.setAttribute("id", String.valueOf(product.getId()));
	        // Append the new product element to the parent node.
			parentNode.appendChild(newProduct);

	        // Create and append the "name" element with the product's name.
			Element productName = document.createElement("name");
			productName.setTextContent(String.valueOf(product.getName()));
			newProduct.appendChild(productName);

	        // Create and append the "price" element with the product's price and currency.
			Element productPrice = document.createElement("price");
			productPrice.setAttribute("currency", product.getWholesalerPrice().getCurrency());
			productPrice.setTextContent(String.valueOf(product.getWholesalerPrice().getValue()));
			newProduct.appendChild(productPrice);

	        // Create and append the "stock" element with the product's stock quantity.
			Element productStock = document.createElement("stock");
			productStock.setTextContent(String.valueOf(product.getStock()));
			newProduct.appendChild(productStock);
		}
	    // Generate the XML file from the document and return the success status.
		return generateXml();
	}
	
	private boolean generateXml() {
	    // Flag to track if the file was successfully generated.
		boolean isFileGenerate = false;
		try {
	        // Create a transformer factory and transformer for converting the document to XML.
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			
	        // Set the source to be the current document.
			Source source = new DOMSource(document);
			this.file = new File("./xml/" + this.fileName);

	        // Prepare to write to the file using FileWriter and PrintWriter.
			FileWriter fw = new FileWriter(this.file);
			PrintWriter pw = new PrintWriter(fw);
			Result result = new StreamResult(pw);

	        // Transform the source document into the result output (the XML file).
			transformer.transform(source, result);

	        // Set the flag to true indicating the file was successfully generated.
			isFileGenerate = true;
	        
		} catch (IOException e) {
			System.out.println("Error when creating writter file");
		} catch (TransformerException e) {
			System.out.println("Error transforming the document");
		}
		return isFileGenerate;
	}


}
