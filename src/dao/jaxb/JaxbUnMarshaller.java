package dao.jaxb;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.ProductList;

import model.*;

public class JaxbUnMarshaller {

		public ArrayList<Product> read() {
			ArrayList<Product> listProduct = new ArrayList<Product>();
			ProductList products = null;
			try {
				JAXBContext context = JAXBContext.newInstance(ProductList.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				System.out.println("unmarshalling...");
				products = (ProductList) unmarshaller.unmarshal(new File("./jaxb/inputInventory.xml"));

			} catch (JAXBException e) {
				e.printStackTrace();
			}
			
			for (Product prodct : products.getProducts()) {
				if (prodct.getWholesalerPrice() != null) {
					prodct.setPublicPrice(new Amount(prodct.getWholesalerPrice().getValue() * 2));
				}
				if (prodct.getStock() >= 1) {
					prodct.setAvailable(true);
				}
			}
			
			listProduct.addAll(products.getProducts());
			
			return listProduct;
			
		}
}