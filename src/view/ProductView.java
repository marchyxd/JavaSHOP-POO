	package view;
	
	import java.awt.BorderLayout;
	import java.awt.EventQueue;
	import java.awt.FlowLayout;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.KeyEvent;
	import java.awt.event.KeyListener;
	
	import javax.swing.JButton;
	import javax.swing.JDialog;
	import javax.swing.JPanel;
	import javax.swing.border.EmptyBorder;
	
	import main.Shop;
	import model.Amount;
	import model.Product;
	import utils.Constants;
	
	import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JTextField;
	
	public class ProductView extends JDialog implements ActionListener {
	
		private static final long serialVersionUID = 1L;
		private Shop shop;
		private int option;
		private JButton btnOK;
		private JButton btnCancel;
		private final JPanel contentPanel = new JPanel();
		private JTextField textFieldProductName;
		private JTextField textFieldProductStock;
		private JTextField textFieldProductPrice;
	
		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
	
				public void run() {
				}
			});
		}
		
		public ProductView(Shop shop, int option) {
			this.shop = shop;
			this.option = option;
			
		
			setTitle("Add Product");
			setBounds(100, 100, 450, 300);
			getContentPane().setLayout(new BorderLayout());
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(null);
			
			JLabel lblNProductName = new JLabel("Product Name:");
			lblNProductName.setBounds(44, 70, 121, 14);
			contentPanel.add(lblNProductName);
			
			JLabel lblProductStock = new JLabel("Product Stock");
			lblProductStock.setBounds(44, 120, 121, 14);
			contentPanel.add(lblProductStock);
			
			JLabel lblProductPrice = new JLabel("Product Price");
			lblProductPrice.setBounds(44, 164, 121, 14);
			contentPanel.add(lblProductPrice);
			
			textFieldProductName = new JTextField();
			textFieldProductName.setBounds(175, 67, 86, 20);
			contentPanel.add(textFieldProductName);
			textFieldProductName.setColumns(10);
			textFieldProductName.addActionListener(this);
			
			textFieldProductStock = new JTextField();
			textFieldProductStock.setBounds(175, 117, 86, 20);
			contentPanel.add(textFieldProductStock);
			textFieldProductStock.setColumns(10);
			textFieldProductStock.addActionListener(this);
			
			textFieldProductPrice = new JTextField();
			textFieldProductPrice.setBounds(175, 161, 86, 20);
			contentPanel.add(textFieldProductPrice);
			textFieldProductPrice.setColumns(10);
			
			btnOK = new JButton("OK");
			btnOK.setBounds(44, 208, 89, 23);
			contentPanel.add(btnOK);
			btnOK.addActionListener(this);
			
			btnCancel = new JButton("Cancel");
			btnCancel.setBounds(172, 208, 89, 23);
			contentPanel.add(btnCancel);
			btnCancel.addActionListener(this);	
			
			// Adjust UI based on the option
			switch (this.option) {
			case Constants.OPTION_ADD_PRODUCT: 
				setTitle("Add Product");
				break;
			case Constants.OPTION_ADD_STOCK:
				setTitle("Add Stock");
				textFieldProductPrice.setVisible(false);
				lblProductPrice.setVisible(false);
				break;
			case Constants.OPTION_REMOVE_PRODUCT:
				setTitle("Remove Product");
				textFieldProductPrice.setVisible(false);
				lblProductPrice.setVisible(false);
				textFieldProductStock.setVisible(false);
				lblProductStock.setVisible(false);
				break;
			}
		}
	
		@Override
		public void actionPerformed(ActionEvent e) {
			Shop shop = new Shop();
			// TODO Auto-generated method stub
			// Handle button clicks
			if (e.getSource() == btnOK) {
				// Check the option and call the corresponding method
				switch (this.option) {
				case Constants.OPTION_ADD_PRODUCT: 
					addProduct();
					break;
				case Constants.OPTION_ADD_STOCK:
					addStock();
					break;
				case Constants.OPTION_REMOVE_PRODUCT:
					removeProduct();
					break;
				}
			}
			// Close the dialog if the cancel button is pressed
			if (e.getSource() == btnCancel) {
				dispose();
			}
		}
	
		public void addProduct() {
			// Add a new product to the shop
			Product product;
			product = shop.findProduct(textFieldProductName.getText());
			String productName = textFieldProductName.getText();
			
			if (productName.isEmpty()) {
				// Find if the product already exists
	            JOptionPane.showMessageDialog(null, 
	            		"Product name is empty ", 
	            		"Error", 
	            		JOptionPane.ERROR_MESSAGE);
	        } 
	            
			try {
				// If product doesn't exist, create a new product
				if(product == null) {
	            double wholesalerPrice = Double.parseDouble(textFieldProductPrice.getText());
	            // Create a new product
	            product = new Product(productName, wholesalerPrice, true, Integer.parseInt(textFieldProductStock.getText()));
	            // Add the product to the shop
	            shop.addProduct(product);
	            JOptionPane.showMessageDialog(null, 
	            		"New product Added ", 
	            		"INFO", 
	            		JOptionPane.INFORMATION_MESSAGE);
	            dispose();
				}else {
					 // If product already exists, show an error message
					JOptionPane.showMessageDialog(null, 
							"This prouct " + productName + " Already exist",
							"ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
	        } catch (NumberFormatException ex) {
	        	// Handle invalid input for price or stock
	            JOptionPane.showMessageDialog(null, 
	            		"Invalid product", 
	            		"ERROR", 
	            		JOptionPane.ERROR_MESSAGE);
	        }
	        
		}
		
		public void addStock() {
			// Add stock to an existing product
			String productName = textFieldProductName.getText();
			String stock = textFieldProductStock.getText();
			
			// Check if the product name or stock fields are empty
			if (productName.isEmpty() || stock.isEmpty()) {
				JOptionPane.showMessageDialog(this, 
						"the product name/stock is empty", 
						"ERROR",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
	
			try {
				// Pass the parameter to the correct type of data
				int productStock = Integer.parseInt(stock);
				Product product = shop.findProduct(productName);
				
				// Check if the product exists
				if (product != null) {
					int totalStock = product.getStock() + productStock;
					// Update the stock
					product.setStock(totalStock);
					JOptionPane.showMessageDialog(null, 
							"Product stock updated", 
							"UPDATE",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} else {
					// If product is not found, show an error message
					JOptionPane.showMessageDialog(null, 
							"Product not found", 
							"ERROR",
							JOptionPane.ERROR_MESSAGE);
				}
	
			} catch (NumberFormatException number) {
				// Handle invalid input for stock
				JOptionPane.showMessageDialog(this, 
						"ERROR",
						"Incorret product", 
						JOptionPane.WARNING_MESSAGE);
	
			}
			
		}
		
		public void removeProduct() {
			// Remove an existing product from the shop
			String productName = textFieldProductName.getText();
		   
			// Check if the product name is empty
			if (productName.isEmpty()) {
				JOptionPane.showMessageDialog(null, 
						"The product name is empty",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
			//find the product from shop.
			Product product;
			product = shop.findProduct(textFieldProductName.getText());
		    
			// Check if the product exists
			if(product != null) {
				shop.getInventory().remove(product);
				JOptionPane.showMessageDialog(null, 
						"Product delected",
						"INFO",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}else {
				// If product is not found, show an error message
				JOptionPane.showMessageDialog(null, 
						"Product not found",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		
	
	}
