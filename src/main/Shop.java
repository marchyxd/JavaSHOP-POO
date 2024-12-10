package main;


import model.Amount;

import model.Client;
import model.Employee;
import model.Product;
import model.Sale;

import java.util.ArrayList;
import java.util.Scanner;

import dao.DaoImplFile;
import dao.DaoImplJaxb;
import dao.DaoImplXml;
import dao.Dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.io.IOException; 


public class Shop {
    private Amount cash = new Amount(100.00);
    //private Product[] inventory;
    ArrayList<Product> inventory = new ArrayList<Product>();
    
    private int numberProducts;
    private int numberSale;
    //private Sale[] sales;
    ArrayList<Sale> sales = new ArrayList<Sale>();
    // Constants
    final static double TAX_RATE = 1.04;
    LocalDateTime Date = LocalDateTime.now();
    
    //connection to the file.
    //private DaoImplFile daoFile = new DaoImplFile();
    //connection to the XML.
	//private DaoImplXml daoXml = new DaoImplXml();
	//connection to the JAXB.
	private Dao dao = new DaoImplJaxb();
	
    // Constructor
    public Shop() {
        //inventory = new Product[10];
    	inventory = new ArrayList<Product>();
        //sales = new Sale[10];
    	sales = new ArrayList<Sale>();
    }
    
    

    public void setInventory(ArrayList<Product> inventory) {
		this.inventory = inventory;
	}

	// Main method
    public static void main(String[] args) throws SQLException {
        // Instance of Shop
        Shop shop = new Shop();
        // Load initial inventory
        shop.loadInventory();

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;
        boolean exit = false;
        
        
        shop.initSession();
        
        // Main menu loop
        do {
            // Display main menu options
            System.out.println("\n");
            System.out.println("===========================");
            System.out.println("Main Menu myStore.com");
            System.out.println("===========================");
            System.out.println("1) Count cash");
            System.out.println("2) Add product");
            System.out.println("3) Add stock");
            System.out.println("4) Set product expiration");
            System.out.println("5) View inventory");
            System.out.println("6) Sale");
            System.out.println("7) View sales");
            System.out.println("8) Delecte Product");
            System.out.println("9) Exit program");
            System.out.print("Select an option: ");
            opcion = scanner.nextInt();

            // Switch statement to handle user input
            switch (opcion) {
                case 1:
                    shop.showCash();
                    break;

                case 2:
                    shop.addProduct();
                    break;

                case 3:
                    shop.addStock();
                    break;

                case 4:
                    shop.setExpired();
                    break;

                case 5:
                    shop.showInventory();
                    break;

                case 6:
                    shop.sale();
                    break;

                case 7:
                    shop.showSales();
                    break;
                    
                case 8:
                	shop.remove();
                	break;

                case 9:
                    exit = true;
                    System.out.println("EXIT");
                    break;
            }

        } while (!exit);

    }
    
    public void initSession() {
	    	Scanner scanner = new Scanner(System.in);
			Boolean login = false;
    		Employee employee = new Employee();
    	do {
			System.out.println("Introduce the employee number: ");
			int user = scanner .nextInt();
			System.out.println("Introduce the password: ");
			String password = scanner .next();
			login = employee.login(user, password);
			if(!login) {
				
				System.err.println("Try again, ERROR");
			}
		} while (!login);
    	if(login) {
    		System.out.println("Welcome!");
    	}
    }

    // Method to load initial inventory
    public void loadInventory() {
    	try {
			this.setInventory(dao.getInventory());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public boolean writeInventory(){
    	return dao.writeInventory(inventory);
    }

    // Method to display current cash
    private void showCash() {
        System.out.println("Current cash: " + cash );
    }
    
    
    public Amount getCash() {
        return this.cash;
    }
    
//    // Method to update cash amount
//    private void updateCash(double amount) {
//        cash.setValue(cash.getValue() + amount);
//        System.out.println("Cash updated: " + cash);
//    }
    // Method to add a product
    public void addProduct() {
        // Check if inventory is full
        if (isInventoryFull()) {
            System.out.println("No more products can be added");
            return;
        }
        // Scanner to read user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Price: ");
        double wholesalerPrice = scanner.nextDouble();
        // Calculate public price based on wholesale price
        double publicPrice = wholesalerPrice * 2;
        System.out.print("Stock: ");
        int stock = scanner.nextInt();

        // Add product to inventory
        addProduct(new Product(name, wholesalerPrice, true, stock));
        
    }

    // Method to add stock for a product
    public void addStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a product name: ");
        String name = scanner.next();
        Product product = findProduct(name);
        //check if the product is null, them add it. 
        if (product != null) {
            System.out.print("Select the quantity to add: ");
            int stockToAdd = scanner.nextInt();
            product.setStock(product.getStock() + stockToAdd);
            System.out.println("The stock of product " + name + " has been updated to " + product.getStock());
          //else not exist the product. 
        } else {
            System.out.println("Product with name " + name + " not found");
        }
    }

    // Method to set a product as expired
    private void setExpired() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a product name: ");
        String name = scanner.next();

        Product product = findProduct(name);

        if (product != null) {
            product.expire();
            System.out.println("The price of product " + name + " has been updated to " + product.getPublicPrice() + "€");
        } else {
            System.out.println("Product with name " + name + " not found");
        }
    }

    // Method to display current inventory
    public void showInventory() {
        System.out.println("Current content of the store:");
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) != null) {
                System.out.println(inventory.get(i));
            }
        }
    }
    
 // Method to perform a sale
    public void sale() {
        // Create a Scanner object to read user input
        Scanner sc = new Scanner(System.in);
        // Prompt message to ask the user to enter the customer's name
        System.out.println("Make sale, enter customer name");
        // Read input for customer name
        String client = sc.nextLine();
        // Create an array to store the products for the sale
        ArrayList<Product> fixedProduct = new ArrayList<Product>();
        // Initialize a counter for the products
        int productCounter = 0;
        // Initialize the total amount of the sale
        double totalAmount = 0.0;
        String name = "";
        Client clientSale = new Client(client);
        // Loop until the user enters "0" to finish adding products
        while (!name.equals("0")) {
            // Prompt the user to enter the product name or "0" to finish
            System.out.println("Enter product name, write 0 to finish:");
            // Read the product name input
            name = sc.nextLine();

            // Check if the user entered "0" to finish
            if (name.equals("0")) {
                break;
            }

            // Find the product with the entered name
            Product product = findProduct(name);
            // Store the found product in the array of fixed products
            fixedProduct.add(product);

            // Flag to indicate if the product is available
            boolean productAvailable = false;

            // Check if the product is not null, available, and has stock
            if (product != null && product.isAvailable() && product.getStock() > 0) {
                // Set the flag to true indicating the product is available
                productAvailable = true;
                // Add the public price of the product to the total amount
                totalAmount += product.getPublicPrice().getValue();
                // Decrement the stock of the product by 1
                product.setStock(product.getStock() - 1);
                // Check if the product's stock has become zero
                if (product.getStock() == 0) {
                    product.setAvailable(false);
                }
                // Print a success message indicating the product was added successfully
                System.out.println("Product added successfully");
            }

            // Check if the product is not available
            if (!productAvailable) {
                System.out.println("Product not found or out of stock");
            }
            // Increment the product counter
            productCounter++;
            
        }

        // Calculate the total amount after applying the tax rate
        totalAmount *= TAX_RATE;
        //create a local date time. 
        //LocalDateTime date = LocalDateTime.now();
        // Check if the total amount exceeds the cash available
        if (totalAmount <= cash.getValue()) {
            // Create a new sale object with the customer name, products, and total amount
            sales.add(new Sale(client.trim(), fixedProduct, totalAmount, Date));
            // Update the cash amount by subtracting the total amount of the sale
            cash.setValue(cash.getValue() - totalAmount);
            // Print a success message indicating the sale was completed and display the total amount
            Amount totalSale = new Amount(totalAmount);
            System.out.println("Sale successful, total: " + totalSale);
        } else {
            // If the client doesn't have enough money, calculate the amount they owe
            double difference = totalAmount - cash.getValue();
            Amount owe = new Amount(difference);
            Amount totalSale = new Amount(totalAmount);
            System.out.println("Sale successful, total: " + totalSale);
            System.out.println("client owes: -" + owe.getValue());
        }
    }

    // Method to display all sales
    private void showSales() {
    	//show the sales list
        System.out.println("Sales list:");
        for (Sale sale : sales) {
        	//check if not null
            if (sale != null) {
                String clientUpperCase = sale.getClient().toString(); 
                //get the client names.
                String saleInfo = "Client: " + clientUpperCase  + "\n Products List: ";
                //get the product list with only the product name. 
                for(int i = 0; i < sale.getProducts().size(); i++) {
                	saleInfo += sale.getProducts().get(i).getName() + ", ";
                }
                //concat the saleInfo. 
                saleInfo += "\n Price: " + sale.getAmount() + "\n Time: " + Date;
                //print all the Sales list.
                System.out.println(saleInfo);
            }
        }
        //show the total sales. 
        showTotalSales();
       
        //ask user if want to safe the data into a file. 
        Scanner sc = new Scanner(System.in);
        System.out.println("Save data into the file?(Y / N)");
        String option = sc.next();
        if(option.equalsIgnoreCase("N")) {
        	System.out.println("Operation finished");
        }
        if(option.equalsIgnoreCase("Y")) {
        	writeInventory();
        }
    }
    
    
    // Method to display total sales amount
    public void showTotalSales() {
        Amount totalSalesAmount = new Amount(0.0);
        for (Sale sale : sales) {
            if (sale != null) {
                totalSalesAmount.setValue(totalSalesAmount.getValue() + sale.getAmount().getValue());
            }
        }
        System.out.println("Total sales made: " + totalSalesAmount);
    }

    // Method to add a product to inventory
    public void addProduct(Product product) {
        if (isInventoryFull()) {
            System.out.println("No more products can be added, maximum reached: " + inventory.size());
            return;
        }
        //inventory[numberProducts] = product; 
        inventory.add(product);
        //numberProducts++;
    }

    // Method to check if inventory is full
    public boolean isInventoryFull() {
        return numberProducts == 10;
    }

    // Method to add a sale
    public void addSale(Sale sale) {
        if (isSaleFull()) {
            System.out.println("No more sales can be added, maximum reached: " + sales.size());
            return;
        }
        //sales[numberSale] = sale;
        
        //numberSale++;
        sales.add(sale);
    }

    // Method to check if sales array is full
    public boolean isSaleFull() {
        return numberSale == 10;
    }

    // Method to find a product by name
    public Product findProduct(String name) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) != null && inventory.get(i).getName().equalsIgnoreCase(name)) {
                return inventory.get(i);
            }
        }
        return null;
    }
    
    public ArrayList<Product> getInventory() {
		return inventory;
	}
    

    
    public void remove() {
    	// Create a Scanner object to read user input
        Scanner sc = new Scanner(System.in);
        //ask the product name
        System.out.println("Product name: ");
        String name = sc.next();
        //find the product name. 
        Product product = findProduct(name);
        
        //check if the product exist
        if (product != null) {
        	//remove the product from the list. 
           if(inventory.remove(product)) {
        	 System.out.println("Element Eliminated."); 
        	 //error message cant eliminate
           } else {
        	System.out.println("Error eliminate product");
           }
           //If the product not exist. 
        }else {
        	System.out.println("No product founded");
        }
	}  
}

