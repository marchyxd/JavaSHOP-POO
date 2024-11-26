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
        // Initialize an empty list to store Product objects.
        ArrayList<Product> listProduct = new ArrayList<Product>();

        // Variable to hold the unmarshalled ProductList object.
        ProductList products = null;

        try {
            // Create a new JAXBContext for the ProductList class.
            JAXBContext context = JAXBContext.newInstance(ProductList.class);

            // Create an Unmarshaller to convert XML into Java objects.
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Log the unmarshalling process.
            System.out.println("unmarshalling...");

            // Unmarshal the XML file into a ProductList object.
            products = (ProductList) unmarshaller.unmarshal(new File("./jaxb/inputInventory.xml"));

        } catch (JAXBException e) {
            // Print the stack trace if an error occurs during unmarshalling.
            e.printStackTrace();
        }

        // Iterate over the list of products obtained from the ProductList object.
        for (Product prodct : products.getProducts()) {
            // Set the public price as twice the wholesaler price, if the latter is not null.
            if (prodct.getWholesalerPrice() != null) {
                prodct.setPublicPrice(new Amount(prodct.getWholesalerPrice().getValue() * 2));
            }

            // Mark the product as available if its stock is greater than or equal to 1.
            if (prodct.getStock() >= 1) {
                prodct.setAvailable(true);
            }
        }

        // Add all processed products to the listProduct ArrayList.
        listProduct.addAll(products.getProducts());

        // Return the list of products.
        return listProduct;
    }
}