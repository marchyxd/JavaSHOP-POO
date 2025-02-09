package view;

import java.awt.EventQueue;
import javax.swing.*;

import main.Shop;
import utils.Constants;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.*;
import java.sql.SQLException;

public class ShopView extends JFrame implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Shop shop;  
    private JButton btnExport,btnCount, btnAddProduct, btnAddStock, btnDeleteProduct;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ShopView frame = new ShopView();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ShopView() {
    	// Add a key listener to the frame
        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
        
        // Initialize the shop and load inventory
        shop = new Shop();
        shop.loadInventory();
        shop.writeInventory();
        
        setTitle("MyShop.com - Principal Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 590, 432);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(206, 242, 224));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Choose or select an option");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        lblNewLabel.setBounds(94, 20, 381, 50);
        contentPane.add(lblNewLabel);
        
        btnExport = new JButton("0. Export");
        btnExport.setBounds(169, 90, 210, 50);
        contentPane.add(btnExport);
        btnExport.addActionListener(this);
        
        btnCount = new JButton("1. Count");
        btnCount.setBounds(169, 150, 210, 50);
        contentPane.add(btnCount);
        btnCount.addActionListener(this);
        
        btnAddProduct = new JButton("2. Add Product");
        btnAddProduct.setBounds(169, 210, 210, 50);
        contentPane.add(btnAddProduct);
        btnAddProduct.addActionListener(this);
        
        btnAddStock = new JButton("3. Add Stock");
        btnAddStock.setBounds(169, 270, 210, 50);
        contentPane.add(btnAddStock);
        btnAddStock.addActionListener(this);
        
        btnDeleteProduct = new JButton("4. Delete Product");
        btnDeleteProduct.setBackground(new Color(255, 132, 135));
        btnDeleteProduct.setBounds(169, 330, 210, 50);
        contentPane.add(btnDeleteProduct);
        btnDeleteProduct.addActionListener(this);
       
    }

    public void actionPerformed(ActionEvent e) {
    	// Handle button clicks
        Object source = e.getSource();
        if (e.getSource() == btnCount) {
        	// Open when button is clicked
            this.openCashView();
        }else if (e.getSource() == btnExport) {
        	exportInventory();
        } else if (e.getSource() == btnAddProduct) {
            this.openProductView(Constants.OPTION_ADD_PRODUCT);
        } else if (e.getSource() == btnAddStock) {
        	this.openProductView(Constants.OPTION_ADD_STOCK);
        } else if (e.getSource() == btnDeleteProduct) {
        	this.openProductView(Constants.OPTION_REMOVE_PRODUCT);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	// Handle key press events
    	int key = e.getKeyCode();
		switch (key) {
		// Open when keyboard is clicked
		case KeyEvent.VK_0:
			exportInventory();
			break;
		case KeyEvent.VK_1:
			openCashView();
			break;
		case KeyEvent.VK_2:
			openProductView(Constants.OPTION_ADD_PRODUCT);
			break;
		case KeyEvent.VK_3:
			openProductView(Constants.OPTION_ADD_STOCK);
			break;
		case KeyEvent.VK_9:
			openProductView(Constants.OPTION_REMOVE_PRODUCT);
			
		}
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    private void openCashView() {
    	// Create and display the cash view
        CashView cashView = new CashView(shop);
        cashView.setVisible(true);
    }

    private void openProductView(int option) {
    	// Create and display the product view with the specified option
        ProductView productView = new ProductView(shop, option);
        productView.setSize(400,400);
        productView.setVisible(true);
    }
    
    private void exportInventory() {
        boolean success = shop.writeInventory();
        if (success) {
            JOptionPane.showMessageDialog(this, "Inventory exported successfully" , "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error exporting inventory.", "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}

