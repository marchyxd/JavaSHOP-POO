package dao;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import dao.xml.DomWriter;
import dao.xml.SaxReader;
import model.Employee;
import model.Product;

public class DaoImplXml implements Dao{

	@Override
	public void connect() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disconnect() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> products = new ArrayList<>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		
		File file = new File("./xml/inputInventory.xml");

		try {
			parser = factory.newSAXParser();
			
			SaxReader saxReader = new SaxReader();
			parser.parse(file, saxReader);
			products = saxReader.getProducts();

		} catch (ParserConfigurationException | SAXException e) {
			System.out.println("ERROR creating the parser" + e.getMessage());
		} catch (IOException e) {
			System.out.println("ERROR file not found" + e.getMessage());
		}catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
        }
		
		return products;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> product) {
		//boolan for check if the xml is created
		boolean generateReport = false;
		//setting the file output formatter
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime date = LocalDateTime.now();
		String nameFile = "inventory_" + date.format(formatter) + ".xml";
		//create and use a DomWriter
		DomWriter domWriter = new DomWriter(nameFile);
		//check the file xml if created correctly
		generateReport = domWriter.generateReport(product);
		
	    // Display confirmation message if the file was generated successfully
	    if (generateReport) {
	        System.out.println("File successfully exported to: ./xml/" + nameFile);
	    } else {
	        System.out.println("Error exporting the file.");
	    }
		//return true.
		return generateReport;
	}

}
