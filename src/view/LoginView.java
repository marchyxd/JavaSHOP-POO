package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import exception.LimitLoginException;
import model.Employee;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Color;

public class LoginView extends JFrame implements ActionListener  {
	
	private Employee employee;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldUsername;
	private JButton btnNewButton;
	private JTextField textFieldPassword;
	
	private int loginAttempts = 0;
	private static final int MAX_LOGIN_ATTEMPTS = 3;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView frame = new LoginView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginView() {
		setBackground(new Color(220, 200, 129));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(206, 242, 224));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("LOGIN");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblNewLabel.setBounds(174, 25, 111, 31);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("User");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(91, 70, 75, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Password");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(91, 141, 169, 14);
		contentPane.add(lblNewLabel_2);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setBackground(new Color(234, 234, 234));
		textFieldUsername.setBounds(91, 95, 222, 31);
		contentPane.add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		btnNewButton = new JButton("Log in");
		btnNewButton.setBackground(new Color(255, 255, 255));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton.setBounds(91, 209, 222, 41);
		contentPane.add(btnNewButton);
		
		textFieldPassword = new JTextField();
		textFieldPassword.setBackground(new Color(234, 234, 234));
		textFieldPassword.setBounds(91, 166, 222, 31);
		contentPane.add(textFieldPassword);
		textFieldPassword.setColumns(10);
		btnNewButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnNewButton) {
			String username = textFieldUsername.getText();
	        String password = textFieldPassword.getText();

	        if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please, introduce the user and password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
	        try {
                int userId = Integer.parseInt(username);
                
                Employee employee = new Employee(password);
                boolean loginSuccessful = employee.login(userId, password); 

                
                if (loginSuccessful) {
                    JOptionPane.showMessageDialog(this, "Log in correct.","Correct", JOptionPane.INFORMATION_MESSAGE);
                    ShopView shopView = new ShopView();
                    shopView.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect, try again", "Error", JOptionPane.ERROR_MESSAGE);
                    textFieldUsername.setText("");
                    textFieldPassword.setText("");
                    
                    loginAttempts++;
                    if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                    	try {
                    		throw new LimitLoginException();
                    	} catch (LimitLoginException ex) {
                    		JOptionPane.showMessageDialog(this,ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    		dispose(); 
                    	}
                    } else {
                    	JOptionPane.showMessageDialog(this, "Error Log in. Intento " + loginAttempts + " of5555 " + MAX_LOGIN_ATTEMPTS, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "The user must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                textFieldUsername.setText("");
                textFieldPassword.setText("");
            }
        }
    }
}


