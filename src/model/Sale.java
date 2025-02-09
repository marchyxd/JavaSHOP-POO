package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Sale {
    private Client client;
    private List<Product> products;
    private Amount amount;
    private LocalDateTime date;

    // Constructor principal (cuando se proporciona la fecha)
    public Sale(String client, List<Product> products, Double amount, LocalDateTime date) {
        this.client = new Client(client);
        this.products = (products != null) ? products : new ArrayList<>(); // Evita null
        this.amount = new Amount(amount);
        this.date = (date != null) ? date : LocalDateTime.now(); // Si la fecha es null, usa la actual
    }

    // Constructor sin fecha (asigna la fecha actual automáticamente)
    public Sale(String client, List<Product> products, Double amount) {
        this(client, products, amount, LocalDateTime.now());
    }

    // Getters y Setters
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // Método para formatear la lista de productos
    private String formatProductList() {
        if (products.isEmpty()) {
            return "No products";
        }
        StringBuilder productList = new StringBuilder();
        for (Product product : products) {
            productList.append(product.getName()).append(", ");
        }
        return productList.substring(0, productList.length() - 2); // Elimina la última coma y espacio
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = (date != null) ? date.format(formatter) : "No date";
        return "Sale [Client=" + client.getName().toUpperCase() +
               ", Products=" + formatProductList() +
               ", Amount=" + amount +
               ", Date=" + formattedDate + "]";
    }
}
