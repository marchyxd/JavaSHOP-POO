package dao.jaxb;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.Product;
import model.ProductList;

public class JaxbMarshaller {
	
	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	String formattedDateTime = now.format(formatter);

	
	public boolean export(ArrayList<Product> productList) {
		try {
			JAXBContext context = JAXBContext.newInstance(ProductList.class);
			Marshaller marshaller = context.createMarshaller();
			
			ProductList products = new ProductList(productList);
			
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(products, new File("jaxb/inventory_" + formattedDateTime + ".xml"));
			return true;
		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		}
	}
}
