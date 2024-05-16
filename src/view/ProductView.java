package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;
import model.Product;
import utils.Constants;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class ProductView extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldProductName;
	private JTextField textFieldProductStock;
	private JTextField textFieldProductPrice;

	public ProductView(Shop shop, int option) {
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
		textFieldProductPrice.addActionListener(this);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(this);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(this);
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == okButton) {
			Product product;
			switch (this.option) {
			case Constants.OPTION_ADD_PRODUCT: {
				product = shop.findProduct()
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + this.option);
			}
		}
	}
	

}
