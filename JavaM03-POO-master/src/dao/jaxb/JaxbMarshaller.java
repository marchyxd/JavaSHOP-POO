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

    // Current date and time captured when the class is instantiated.
    LocalDateTime now = LocalDateTime.now();

    // Define a formatter to format the date in "yyyy-MM-dd" format.
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Format the current date using the formatter.
    String formattedDateTime = now.format(formatter);


    public boolean export(ArrayList<Product> productList) {
        try {
            // Create a new JAXBContext for the ProductList class.
            JAXBContext context = JAXBContext.newInstance(ProductList.class);

            // Create a Marshaller to convert Java objects to XML.
            Marshaller marshaller = context.createMarshaller();

            // Create a ProductList object from the provided product list.
            ProductList products = new ProductList(productList);

            // Set the Marshaller property to format the output XML for readability.
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Marshal the ProductList object to an XML file with a date-stamped filename.
            marshaller.marshal(products, new File("jaxb/inventory_" + formattedDateTime + ".xml"));

            // Return true if the export operation succeeds.
            return true;

        } catch (JAXBException e) {
            // Print the stack trace if an exception occurs during the marshalling process.
            e.printStackTrace();

            // Return false if the export operation fails.
            return false;
        }
    }
}
