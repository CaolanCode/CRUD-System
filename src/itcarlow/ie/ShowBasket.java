package itcarlow.ie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.*;

public class ShowBasket extends JFrame {
    // variables
    // panels
    private JPanel tableJPanel;
    private JPanel buttonJPanel;
    // text pane
    DefaultTableModel tableModel = new DefaultTableModel();
    private JTable invoiceJTable;
    String[] columnNames = {"Invoice ID", "Quantity", "Price", "Product ID"};
    // button
    private JButton returnJButton;

    // database variables
    final String DATABASE_URL = "jdbc:mysql://localhost/C.I.M.S";
    Connection connection = null;
    PreparedStatement pstat = null;
    ResultSet resultSet;
    int invoiceId;
    BigDecimal cost;
    int quantity;
    int productId;
    int i = 0;

    // constructor
    public ShowBasket(String title){
        super(title);
        // set layout
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // table panel
        tableJPanel = new JPanel();
        tableModel.setColumnIdentifiers(columnNames);
        invoiceJTable = new JTable();
        invoiceJTable.setPreferredSize(new Dimension(400,350));
        invoiceJTable.setModel(tableModel);
        invoiceJTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        invoiceJTable.setFillsViewportHeight(true);
        tableJPanel.add(invoiceJTable);

        // button panel
        buttonJPanel = new JPanel();
        returnJButton = new JButton("Return to Order");
        buttonJPanel.add(returnJButton);

        // add panels to jframe
        add(tableJPanel);
        add(new JScrollPane(invoiceJTable));
        add(buttonJPanel);

        // try catch block to show table
        try{
            // establish connection to database
            connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
            // create prepared statement to select all data from product table
            pstat = connection.prepareStatement("SELECT * FROM invoice WHERE customerFk=?");
            pstat.setInt(1,Login.customerID);
            resultSet = pstat.executeQuery();
            while(resultSet.next()){
                invoiceId = resultSet.getInt("idInvoice");
                quantity = resultSet.getInt("quantity");
                cost = resultSet.getBigDecimal("cost");
                productId = resultSet.getInt("productFk");
                tableModel.addRow(new Object[]{invoiceId,quantity, cost, productId});
                i++;
            }
            if(i==1){
                System.out.println(i + " record found in the invoice table");
            } else{
                System.out.println(i + " records found in the invoice table");
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

        // return button listener
        // create instance of CreateOrder class
        returnJButton.addMouseListener(new MouseAdapter() {
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
    }// end constructor

    public static void main(String args[]){
        ShowBasket showBasket = new ShowBasket("Account Basket");
        showBasket.setVisible(true);
        showBasket.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showBasket.setSize(550,400);
        showBasket.setLocation(500,400);
    }// end main
}// end class

