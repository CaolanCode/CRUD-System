package itcarlow.ie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class UpdateAccount extends JFrame {
    // variables
    // panels
    private JPanel nameJPanel;
    private JPanel addressJPanel;
    private JPanel emailJPanel;
    private JPanel confirmEmailJPanel;
    private JPanel telephoneJPanel;
    private JPanel passwordJPanel;
    private JPanel confirmPasswordJPanel;
    private JPanel updateCancelJPanel;
    // labels
    private JLabel nameJLabel;
    private JLabel addressJLabel;
    private JLabel emailJLabel;
    private JLabel confirmEmailJLabel;
    private JLabel telephoneJLabel;
    private JLabel passwordJLabel;
    private JLabel confirmPasswordJLabel;
    // text fields
    private JTextField nameTextField;
    private JTextField addressTextField;
    private JTextField emailTextField;
    private JTextField confirmEmailTextField;
    private JTextField telephoneTextField;
    private JPasswordField passwordTextField;
    private JPasswordField confirmPasswordTextField;
    // buttons
    private JButton updateJButton;
    private JButton cancelJButton;
    private JButton deleteJButton;

    // database variables
    final String DATABASE_URL = "jdbc:mysql://localhost/C.I.M.S";
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet;
    String name;
    String address;
    String email;
    String confirmEmail;
    String password;
    String DBPassword;
    String confirmPassword;
    String telephone;
    int i = 0;

    // constructor
    public UpdateAccount(String title){
        super(title);
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // name panel
        nameJPanel = new JPanel(new FlowLayout());
        nameJLabel = new JLabel("Name");
        nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(350,30));
        nameJPanel.add(nameJLabel);
        nameJPanel.add(nameTextField);

        // address panel
        addressJPanel = new JPanel(new FlowLayout());
        addressJLabel = new JLabel("Address");
        addressTextField = new JTextField();
        addressTextField.setPreferredSize(new Dimension(350,30));
        addressJPanel.add(addressJLabel);
        addressJPanel.add(addressTextField);

        // email panel
        emailJPanel = new JPanel(new FlowLayout());
        emailJLabel = new JLabel("Email");
        emailTextField = new JTextField();
        emailTextField.setPreferredSize(new Dimension(350,30));
        emailJPanel.add(emailJLabel);
        emailJPanel.add(emailTextField);

        // confirm email panel
        confirmEmailJPanel = new JPanel(new FlowLayout());
        confirmEmailJLabel = new JLabel("Confirm Email");
        confirmEmailTextField = new JTextField();
        confirmEmailTextField.setPreferredSize(new Dimension(350,30));
        confirmEmailJPanel.add(confirmEmailJLabel);
        confirmEmailJPanel.add(confirmEmailTextField);

        // telephone panel
        telephoneJPanel = new JPanel(new FlowLayout());
        telephoneJLabel = new JLabel("Telephone");
        telephoneTextField = new JTextField();
        telephoneTextField.setPreferredSize(new Dimension(350,30));
        telephoneJPanel.add(telephoneJLabel);
        telephoneJPanel.add(telephoneTextField);

        // password panel
        passwordJPanel = new JPanel(new FlowLayout());
        passwordJLabel = new JLabel("Password");
        passwordTextField = new JPasswordField();
        passwordTextField.setPreferredSize(new Dimension(350,30));
        passwordJPanel.add(passwordJLabel);
        passwordJPanel.add(passwordTextField);

        // confirm password
        confirmPasswordJPanel = new JPanel(new FlowLayout());
        confirmPasswordJLabel = new JLabel("Confirm Password");
        confirmPasswordTextField = new JPasswordField();
        confirmPasswordTextField.setPreferredSize(new Dimension(350,30));
        confirmPasswordJPanel.add(confirmPasswordJLabel);
        confirmPasswordJPanel.add(confirmPasswordTextField);

        // button panel
        updateCancelJPanel = new JPanel(new FlowLayout());
        updateJButton = new JButton("Update");
        cancelJButton = new JButton("Cancel");
        deleteJButton = new JButton("Delete");
        updateCancelJPanel.add(updateJButton);
        updateCancelJPanel.add(cancelJButton);
        updateCancelJPanel.add(deleteJButton);

        // add panels to jframe
        add(nameJPanel);
        add(emailJPanel);
        add(confirmEmailJPanel);
        add(passwordJPanel);
        add(confirmPasswordJPanel);
        add(addressJPanel);
        add(telephoneJPanel);
        add(updateCancelJPanel);

        // retrieve details from database
        try{
            // establish connection to database
            connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
            // create prepared statement to select all data from product table
            pstat = connection.prepareStatement("SELECT * FROM customer WHERE idCust=?");
            pstat.setInt(1,Login.customerID);
            resultSet = pstat.executeQuery();
            if(resultSet.next()){
                nameTextField.setText(resultSet.getString("name"));
                emailTextField.setText(resultSet.getString("email"));
                DBPassword = resultSet.getString("password");
                addressTextField.setText(resultSet.getString("address"));
                telephoneTextField.setText(resultSet.getString("telephone"));
            }
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        } finally {
            try {
                connection.close();
                pstat.close();
                resultSet.close();
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }

        // update button listener
        // display details for update
        // create instance of CreateOrder class
        updateJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    // establish connection to database
                    connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
                    name = nameTextField.getText();
                    email = emailTextField.getText();
                    confirmEmail = confirmEmailTextField.getText();
                    address = addressTextField.getText();
                    telephone = telephoneTextField.getText();
                    // if password textfield is empty
                    // make prepared statement without password
                    if(passwordTextField.getPassword().length == 0){
                        // email vaildation
                        // check if email is enter in confirmEmail
                        if (confirmEmailTextField.getText().length() != 0 && !email.equals(confirmEmail)) {
                            JOptionPane.showMessageDialog(null, "Emails do not match", "Error", JOptionPane.ERROR_MESSAGE);
                            updateAccount();
                            dispose();
                            // validate email
                        } else if (!Validate.validEmail(email)) {
                            JOptionPane.showMessageDialog(null, "Not a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
                            updateAccount();
                            dispose();
                            // details are valid
                        } else{
                            // create prepared statement for inserting data into table
                            pstat = connection.prepareStatement("UPDATE customer SET name=?, email=?, address=?, telephone=? WHERE idCust=?");
                            pstat.setString(1, name);
                            pstat.setString(2, email);
                            pstat.setString(3, address);
                            pstat.setString(4, telephone);
                            pstat.setInt(5, Login.customerID);
                            // insert data into table
                            i = pstat.executeUpdate();
                            System.out.println(i + " record successfully updated in the customer table");

                            // create instance of CreateOrder when listener complete
                            CreateOrder createOrder = new CreateOrder("Order a Product");
                            createOrder.setVisible(true);
                            createOrder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            createOrder.setSize(550,400);
                            createOrder.setLocation(500,400);
                            dispose();
                        }
                        // password enter in password field
                    } else{
                        // password textfields have inputs
                        // assign password to password textfield
                        password = new String(passwordTextField.getPassword());
                        // assign confirmPassword to confirm password textfield
                        confirmPassword = new String(confirmPasswordTextField.getPassword());
                        // check if email is entered in confirmEmail
                         if (confirmEmailTextField.getText().length() != 0 && !email.equals(confirmEmail)) {
                            JOptionPane.showMessageDialog(null, "Emails do not match", "Error", JOptionPane.ERROR_MESSAGE);
                            updateAccount();
                            dispose();
                            // validate emails
                        } else if (!Validate.validEmail(email)) {
                            JOptionPane.showMessageDialog(null, "Not a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
                            updateAccount();
                            dispose();
                            // password validation
                            // compare passwords
                        } else if (!password.equals(confirmPassword)) {
                            JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                            updateAccount();
                            dispose();
                            // validate password
                        } else if (!Validate.validPassword(password)) {
                            JOptionPane.showMessageDialog(null, "Password must have 1 number, 1 lowercase character, 1 capital character, 1 special character and length between 8 and 20", "Error", JOptionPane.ERROR_MESSAGE);
                            updateAccount();
                            dispose();
                        } else {
                            // hash password
                            password = HashPassword.hashPassword(password);

                            // create prepared statement for inserting data into table
                            pstat = connection.prepareStatement("UPDATE customer SET name=?, email=?, password=?, address=?, telephone=? WHERE idCust=?");
                            pstat.setString(1, name);
                            pstat.setString(2, email);
                            pstat.setString(3, password);
                            pstat.setString(4, address);
                            pstat.setString(5, telephone);
                            pstat.setInt(6, Login.customerID);
                            // insert data into table
                            i = pstat.executeUpdate();
                            System.out.println(i + " record successfully updated in the customer table");

                            // create instance of CreateOrder when listener complete
                            CreateOrder createOrder = new CreateOrder("Order a Product");
                            createOrder.setVisible(true);
                            createOrder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            createOrder.setSize(550,400);
                            createOrder.setLocation(500,400);
                            dispose();
                        }
                    }
            } catch (SQLException sqlException){
                sqlException.printStackTrace();
            } finally {
                try {
                    connection.close();
                    pstat.close();
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }//end finally
            }
        });// end listener

        // cancel button listener
        // create instance of CreateOrder class
        cancelJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                CreateOrder createOrder = new CreateOrder("Order a Product");
                createOrder.setVisible(true);
                createOrder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                createOrder.setSize(550,400);
                createOrder.setLocation(500,400);
                dispose();
            }
        });

        // delete button listener
        // create instance of DeleteAccount class
        deleteJButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DeleteAccount deleteAccount = new DeleteAccount("Delete Account");
                deleteAccount.setVisible(true);
                deleteAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                deleteAccount.setSize(550,400);
                deleteAccount.setLocation(500,400);
                dispose();
            }
        });
    }// end constructor

    // create instance of UpdateAccount class
    private static void updateAccount(){
        UpdateAccount updateAccount = new UpdateAccount("Update Account Details");
        updateAccount.setVisible(true);
        updateAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateAccount.setSize(550,400);
        updateAccount.setLocation(500,400);
    }

    // main
    public static void main(String args[]){
        UpdateAccount updateAccount = new UpdateAccount("Update Account Details");
        updateAccount.setVisible(true);
        updateAccount.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateAccount.setSize(550,400);
        updateAccount.setLocation(500,400);
    }// end main
}// end class
