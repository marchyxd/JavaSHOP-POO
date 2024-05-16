package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Sale {
	 private Client client;
	 //private Product[] products;
	 private ArrayList<Product> products = new ArrayList<Product>();
	 private Amount amount;
	 private LocalDateTime date;
	
	
	    public Sale(String client, ArrayList<Product> products, Double amount,LocalDateTime date) {
		super();
		this.client =  new Client(client);
        this.products = products;
        this.amount = new Amount(amount);
        this.date = date;
	}
	
	    public Client getClient() {
			return client;
		}

		public void setClient(Client client) {
			this.client = client;
		}

		public ArrayList<Product> getProducts() {
			return products;
		}

		public void setProducts(ArrayList<Product> products) {
			this.products = products;
		}

		public Amount getAmount() {
			return amount;
		}

		public void setAmount(Amount amount) {
			this.amount = amount;
		}


	@Override
	public String toString() {
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formattedDate = date.format(myFormatObj);
		return "Sale [client=" + client.getName().toUpperCase() + ", products=" + products.toString() + ", amount=" + amount + "]" + formattedDate ;
	}
}
