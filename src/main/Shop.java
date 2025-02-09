package main;

import model.Amount;
import model.Client;
import model.Employee;
import model.Product;
import model.Sale;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JDialog;

import dao.Dao;
import dao.DaoImplHibernate;

import java.time.LocalDateTime;
import java.sql.SQLException;

public class Shop {
    private static final double TAX_RATE = 0;
	private Amount cash = new Amount(100.00);
    private ArrayList<Product> inventory;
    private ArrayList<Sale> sales;
    private Dao dao;

    // Constructor
    public Shop() {
        this.inventory = new ArrayList<>();
        this.sales = new ArrayList<>();
        this.dao = new DaoImplHibernate();
    }

    public static void main(String[] args) {
        Shop shop = new Shop();
        shop.loadInventory();

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        shop.initSession();

        do {
            System.out.println("\n===========================");
            System.out.println("Main Menu myStore.com");
            System.out.println("===========================");
            System.out.println("1) Count cash");
            System.out.println("2) Add product");
            System.out.println("3) Add stock");
            System.out.println("4) Set product expiration");
            System.out.println("5) View inventory");
            System.out.println("6) Sale");
            System.out.println("7) View sales");
            System.out.println("8) Delete Product");
            System.out.println("9) Exit program");
            System.out.print("Select an option: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> shop.showCash();
                case 2 -> shop.addProduct(null);
                case 3 -> shop.addStock();
                case 4 -> shop.setExpired();
                case 5 -> shop.showInventory();
                case 6 -> shop.sale();
                case 7 -> shop.showSales();
                case 8 -> shop.removeProduct();
                case 9 -> {
                    exit = true;
                    System.out.println("Exiting program...");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        } while (!exit);
    }

    public void initSession() {
        Scanner scanner = new Scanner(System.in);
        boolean login = false;
        Employee employee = new Employee();

        while (!login) {
            System.out.print("Introduce the employee number: ");
            int user = scanner.nextInt();
            System.out.print("Introduce the password: ");
            String password = scanner.next();

            login = employee.login(user, password);
            if (!login) {
                System.err.println("Try again, ERROR");
            }
        }
        System.out.println("Welcome!");
    }

    public void loadInventory() {
        try {
            this.inventory = dao.getInventory();
        } catch (SQLException e) {
            System.err.println("Error loading inventory from database.");
            e.printStackTrace();
        }
    }

    public void showCash() {
        System.out.println("Current cash: " + cash);
    }

    public void addProduct(Product newProduct) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Product name: ");
        String name = scanner.nextLine();
        System.out.print("Wholesale price: ");
        double wholesalePrice = scanner.nextDouble();
        double publicPrice = wholesalePrice * 2;
        System.out.print("Stock: ");
        int stock = scanner.nextInt();

        Product product = new Product(name, wholesalePrice, true, stock);
        dao.addProduct(product);
        inventory.add(product);
        System.out.println("Product added successfully.");
    }

    public void addStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product name: ");
        String name = scanner.next();
        Product product = findProduct(name);

        if (product != null) {
            System.out.print("Enter quantity to add: ");
            int stockToAdd = scanner.nextInt();
            product.setStock(product.getStock() + stockToAdd);
            dao.updateProduct(product);
            System.out.println("Stock updated.");
        } else {
            System.out.println("Product not found.");
        }
    }

    public void setExpired() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product name: ");
        String name = scanner.next();

        Product product = findProduct(name);
        if (product != null) {
            product.expire();
            dao.updateProduct(product);
            System.out.println("Product marked as expired.");
        } else {
            System.out.println("Product not found.");
        }
    }

    public void showInventory() {
        System.out.println("Store inventory:");
        for (Product product : inventory) {
            System.out.println(product);
        }
    }

    public void sale() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter customer name: ");
        String clientName = scanner.nextLine();
        ArrayList<Product> saleProducts = new ArrayList<>();
        double totalAmount = 0;

        while (true) {
            System.out.print("Enter product name (or type '0' to finish): ");
            String name = scanner.nextLine();

            if (name.equals("0")) break;

            Product product = findProduct(name);
            if (product != null && product.isAvailable() && product.getStock() > 0) {
                saleProducts.add(product);
                totalAmount += product.getPublicPrice().getValue();
                product.setStock(product.getStock() - 1);
                if (product.getStock() == 0) product.setAvailable(false);
                dao.updateProduct(product);
                System.out.println("Product added to sale.");
            } else {
                System.out.println("Product not found or out of stock.");
            }
        }

        totalAmount *= TAX_RATE;
        LocalDateTime saleDate = LocalDateTime.now();

        if (totalAmount <= cash.getValue()) {
            sales.add(new Sale(clientName, saleProducts, totalAmount, saleDate));
            cash.setValue(cash.getValue() - totalAmount);
            System.out.println("Sale completed. Total: " + totalAmount);
        } else {
            System.out.println("Insufficient cash.");
        }
    }

    public void showSales() {
        System.out.println("Sales history:");
        for (Sale sale : sales) {
            System.out.println("Client: " + sale.getClient() + " | Amount: " + sale.getAmount() + " | Date: " + sale.getDate());
        }
    }

    public void removeProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product name to remove: ");
        String name = scanner.next();
        Product product = findProduct(name);

        if (product != null) {
            dao.deleteProduct(product);
            inventory.remove(product);
            System.out.println("Product removed.");
        } else {
            System.out.println("Product not found.");
        }
    }

    public Product findProduct(String name) {
        return inventory.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public ArrayList<Product> getInventory() {
        return inventory;
    }
    
    public boolean writeInventory() {
        if (inventory == null || inventory.isEmpty()) {
            System.out.println("No products in inventory to write.");
            return false; // Exit early if no data
        }

        boolean success = dao.writeInventory(inventory);
        if (success) {
            System.out.println("Inventory successfully written.");
        } else {
            System.err.println("Failed to write inventory.");
        }
        return success;
    }
    
    public Amount getCash() {
        return this.cash;
    }



}
