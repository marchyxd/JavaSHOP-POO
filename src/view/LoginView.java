package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import exception.LimitLoginException;
import model.Employee;
import utils.Constants;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Color;

public class LoginView extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldUsername;
    private JButton btnLogin;
    private JTextField textFieldPassword;
    private int loginAttempts = 0;

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

    public LoginView() {
        setBackground(new Color(220, 200, 129));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(206, 242, 224));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("LOGIN");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblTitle.setBounds(174, 25, 111, 31);
        contentPane.add(lblTitle);

        JLabel lblUser = new JLabel("User");
        lblUser.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblUser.setBounds(91, 70, 75, 14);
        contentPane.add(lblUser);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPassword.setBounds(91, 141, 169, 14);
        contentPane.add(lblPassword);

        textFieldUsername = new JTextField();
        textFieldUsername.setBackground(new Color(234, 234, 234));
        textFieldUsername.setBounds(91, 95, 222, 31);
        contentPane.add(textFieldUsername);
        textFieldUsername.setColumns(10);

        btnLogin = new JButton("Log in");
        btnLogin.setBackground(new Color(255, 255, 255));
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnLogin.setBounds(91, 209, 222, 41);
        contentPane.add(btnLogin);

        textFieldPassword = new JTextField();
        textFieldPassword.setBackground(new Color(234, 234, 234));
        textFieldPassword.setBounds(91, 166, 222, 31);
        contentPane.add(textFieldPassword);
        textFieldPassword.setColumns(10);

        btnLogin.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            handleLogin();
        }
    }

    private void handleLogin() {
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Please, introduce the user and password");
            decrementLoginAttempts();
            return;
        }

        try {
            int userId = Integer.parseInt(username);
            Employee employee = new Employee(password);
            boolean loginSuccessful = employee.login(userId, password);

            if (loginSuccessful) {
                JOptionPane.showMessageDialog(this, "Log in correct.", "Correct", JOptionPane.INFORMATION_MESSAGE);
                ShopView shopView = new ShopView();
                shopView.setVisible(true);
                dispose();
            } else {
                showErrorMessage("Incorrect, try again");
                resetInputFields();
                decrementLoginAttempts();
            }
        } catch (NumberFormatException ex) {
            showErrorMessage("The user must be numbers.");
            resetInputFields();
            decrementLoginAttempts();
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void resetInputFields() {
        textFieldUsername.setText("");
        textFieldPassword.setText("");
    }

    private void decrementLoginAttempts() {
        loginAttempts++;
        if (loginAttempts >= Constants.MAX_LOGIN_TIMES) {
            try {
                throw new LimitLoginException();
            } catch (LimitLoginException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error Log in. Attempt " + loginAttempts + " of " + Constants.MAX_LOGIN_TIMES, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
