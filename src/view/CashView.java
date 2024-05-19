package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

public class CashView extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldCash;
	private Shop shop;
	private int option;
	

	/**
	 * Create the dialog.
	 */
	public CashView(Shop shop) {
		setTitle("CashView");
		this.shop = shop;
		this.option = option;
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Total Cash");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(43, 53, 155, 29);
		contentPanel.add(lblNewLabel);
		
		textFieldCash = new JTextField();
		textFieldCash.setEditable(false);
		textFieldCash.setBounds(53, 93, 86, 20);
		contentPanel.add(textFieldCash);
		textFieldCash.setColumns(10);
		textFieldCash.setText(String.valueOf(shop.getCash().getValue()+"€"));
		
	}

	
}
