package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import model.Employee;
import model.Product;
import model.Sale;

public class DaoImplFile implements Dao{

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
		//addProduct(new Product("Apple", new Amount(10.00), new Amount(5.00), true, 10));
        //addProduct(new Product("Pear", new Amount(20.00), new Amount(10.00), true, 20));
        //addProduct(new Product("Hamburger", new Amount(30.00), new Amount(15.00), true, 30));
        //addProduct(new Product("Strawberry", new Amount(5.00), new Amount(2.50), true, 20));
    	
		ArrayList<Product> loadInventory = new ArrayList<Product>();
		//create a txt file to store data.
    	File file = new File("./files/inputInventory.txt");
    	//FileWriter filex = new FileWriter(file); 
    	//PrintWriter x = new PrintWriter(file)
    	//check if the file not exist, show the error message
    	if(!file.exists()) {
    		System.out.println("No file founded");
    	}
    	Scanner myReader;
		try {
			//read all the file
			myReader = new Scanner(file);
			
			// Loop through each line in the file
			while (myReader.hasNextLine()) {
			    // Read the next line from the file
			    String data = myReader.nextLine();
			    
			    // Separate the data between the semicolons
			    String[] text = data.split(";");
			    
			    // Declare the first part as the product name
			    String productName = text[0]; 
			    
			    // Declare the second part as the price
			    String price = text[1];
			    
			    // Declare the third part as the number of stock
			    String stock = text[2];
			    
			    // Further separate the product name, price, and stock using colons
			    // Extract the product name
			    String[] textProduct = productName.split(":");  
			    String nameProduct = textProduct[1];
			    
			    // Extract the price and parse it into a Double
			    String[] textPrice = price.split(":");  
			    Double namePrice = Double.parseDouble(textPrice[1]);
			    
			    // Extract the stock and parse it into an Integer
			    String[] textStock = stock.split(":");  
			    int nameStock = Integer.parseInt(textStock[1]);
			    
			    // Add a product with the extracted information to some data structure
			    loadInventory.add(new Product(nameProduct, nameStock,true, nameStock));
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return loadInventory;
		
	}
	


	@Override
	public boolean writeInventory(ArrayList<Product> products) {
	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String nameFile = "Inventory_" + LocalDateTime.now().format(timeFormatter) + ".txt";
	    String directory = "./files/";

	    try {
	        // Create directory if it does not exist
	        File file = new File(directory);
	        if (!file.exists()) {
	        	//show message that the directory not exist.
	        	JOptionPane.showMessageDialog(null, "The directory does not exist.", "Directory Not Found", JOptionPane.WARNING_MESSAGE);
	        	//creating automatically the new directory and show the messages. 
	        	file = new File("./" + directory);
	            file.mkdirs();
	            JOptionPane.showMessageDialog(null, "A new directory has been created automatically.", "Directory Created", JOptionPane.INFORMATION_MESSAGE);
	        }

	        // Create the file and write product data
	        File outputFile = new File(file, nameFile);
	        FileWriter writer = new FileWriter(outputFile);
	        PrintWriter printWriter = new PrintWriter(writer);

	        int count = 0;
	        for (Product product : products) {
	            String line = product.getId() + ";Product:" + product.getName() + ";Stock:" + product.getStock() + "\n";
	            printWriter.append(line);
	            count++;
	        }
	        // Write the total number of products
	        printWriter.append("Total number of products:" + count + ";");

	        // Close the PrintWriter
	        printWriter.close();
	    } catch (IOException e) {
	        return false;
	    }
	    return true;
	}




}
