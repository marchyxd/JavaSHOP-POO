package view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import main.Shop;
import model.Product;
import utils.Constants;

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

    // Constructor
    public ProductView(Shop shop, int option) {
        this.shop = shop;
        this.option = option;

        setTitle("Product Manager");
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblProductName = new JLabel("Product Name:");
        lblProductName.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblProductName.setBounds(58, 50, 130, 28);
        contentPanel.add(lblProductName);

        JLabel lblProductStock = new JLabel("Product Stock:");
        lblProductStock.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblProductStock.setBounds(58, 89, 130, 33);
        contentPanel.add(lblProductStock);

        JLabel lblProductPrice = new JLabel("Product Price:");
        lblProductPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblProductPrice.setBounds(58, 128, 130, 33);
        contentPanel.add(lblProductPrice);

        textFieldProductName = new JTextField();
        textFieldProductName.setBounds(198, 56, 150, 20);
        contentPanel.add(textFieldProductName);
        textFieldProductName.setColumns(10);

        textFieldProductStock = new JTextField();
        textFieldProductStock.setBounds(198, 97, 150, 20);
        contentPanel.add(textFieldProductStock);
        textFieldProductStock.setColumns(10);

        textFieldProductPrice = new JTextField();
        textFieldProductPrice.setBounds(198, 136, 150, 20);
        contentPanel.add(textFieldProductPrice);
        textFieldProductPrice.setColumns(10);

        btnOK = new JButton("OK");
        btnOK.setBounds(87, 190, 100, 30);
        contentPanel.add(btnOK);
        btnOK.addActionListener(this);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(220, 190, 100, 30);
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
        if (e.getSource() == btnOK) {
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
        } else if (e.getSource() == btnCancel) {
            dispose();
        }
    }

    private void addProduct() {
        String productName = textFieldProductName.getText().trim();
        String priceText = textFieldProductPrice.getText().trim();
        String stockText = textFieldProductStock.getText().trim();

        if (productName.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);

            Product existingProduct = shop.findProduct(productName);
            if (existingProduct != null) {
                JOptionPane.showMessageDialog(this, "Product already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product newProduct = new Product(productName, price, true, stock);
            shop.addProduct(newProduct);
            
            JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price or stock format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addStock() {
        String productName = textFieldProductName.getText().trim();
        String stockText = textFieldProductStock.getText().trim();

        if (productName.isEmpty() || stockText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product name and stock must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int stockToAdd = Integer.parseInt(stockText);
            Product product = shop.findProduct(productName);

            if (product != null) {
                product.setStock(product.getStock() + stockToAdd);
                JOptionPane.showMessageDialog(this, "Stock updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid stock format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeProduct() {
        String productName = textFieldProductName.getText().trim();

        if (productName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product name must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Product product = shop.findProduct(productName);

        if (product != null) {
            shop.getInventory().remove(product);
            JOptionPane.showMessageDialog(this, "Product removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
