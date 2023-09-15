import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Statement;


public class PurchaseProducts {
    JFrame purchaseFrame;
    JLabel heading, subHeading, customerNameL, customerMobileL, subHeading2, productNameL, qtyL, price, priceF, totalLabel;
    JTextField customerNameF, customerMobileF, qtyF;
    JPanel customerPanel, tablePanel, navigationPanel, purchasePanel;
    JComboBox productName;
    JButton startBilling, clear, addToCart, clear1, save, back;
    JScrollPane scrollPane;
    JTable productsTable;
    JDBC jdbc;
    ResultSet resultSet, resultSet1;
    PreparedStatement preparedStatement;
    Statement statement;
    String products[];
    static String tableData[][] = new String[20][7];
    static int index;
    int customerId;


    public PurchaseProducts() {
        purchaseFrame = new JFrame();
        purchaseFrame.setSize(965, 625);
        purchaseFrame.setDefaultCloseOperation(purchaseFrame.EXIT_ON_CLOSE);
        purchaseFrame.setLocationRelativeTo(null);
        purchaseFrame.setLayout(null);

        heading = new JLabel("Purchase Products");
        heading.setBounds(10, 10, 930, 40);
        heading.setFont(new Font("monospaced", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(new LineBorder(Color.GRAY, 2));
        purchaseFrame.add(heading);

        customerPanel = new JPanel();
        customerPanel.setBounds(10, 60, 470, 200);
        customerPanel.setBorder(new LineBorder(Color.gray, 2));
        customerPanel.setLayout(null);
        purchaseFrame.add(customerPanel);

        tablePanel = new JPanel();
        tablePanel.setBounds(10, 270, 930, 250);
        tablePanel.setBorder(new LineBorder(Color.gray, 2));
        tablePanel.setLayout(null);
        purchaseFrame.add(tablePanel);

        navigationPanel = new JPanel();
        navigationPanel.setBounds(10, 530, 930, 50);
        navigationPanel.setBorder(new LineBorder(Color.gray, 2));
        navigationPanel.setLayout(null);
        purchaseFrame.add(navigationPanel);

        subHeading = new JLabel("Customer Details");
        subHeading.setBounds(10, 10, 470, 40);
        subHeading.setFont(new Font("monospaced", Font.BOLD, 22));
        customerPanel.add(subHeading);

        customerNameL = new JLabel("Customer Name");
        customerNameL.setBounds(10, 60, 200, 40);
        customerNameL.setFont(new Font("monospaced", Font.BOLD, 18));
        customerPanel.add(customerNameL);

        customerNameF = new JTextField();
        customerNameF.setBounds(240, 70, 220, 30);
        customerNameF.setFont(new Font("monospaced", Font.BOLD, 18));
        customerPanel.add(customerNameF);

        customerMobileL = new JLabel("Customer Mobile No");
        customerMobileL.setBounds(10, 100, 930, 40);
        customerMobileL.setFont(new Font("monospaced", Font.BOLD, 18));
        customerPanel.add(customerMobileL);

        customerMobileF = new JTextField();
        customerMobileF.setBounds(240, 110, 220, 30);
        customerMobileF.setFont(new Font("monospaced", Font.BOLD, 18));
        customerPanel.add(customerMobileF);

        clear = new JButton("Clear");
        clear.setBounds(10, 150, 180, 40);
        clear.setFont(new Font("monospaced", Font.BOLD, 16));
        clear.setFocusable(false);
        clear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customerPanel.add(clear);

        clear.addActionListener(e -> {
            customerNameF.setText("");
            customerMobileF.setText("");
        });

        startBilling = new JButton("Start Billing");
        startBilling.setBounds(240, 150, 220, 40);
        startBilling.setFont(new Font("monospaced", Font.BOLD, 16));
        startBilling.setFocusable(false);
        startBilling.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customerPanel.add(startBilling);

        startBilling.addActionListener(e -> {
            try {
                Integer.parseInt(customerMobileF.getText());

            } catch (Exception exception) {
                JOptionPane.showMessageDialog(purchaseFrame, "Number must be in Integer");
                return;
            }
            if (customerNameF.getText().isEmpty() && customerMobileF.getText().isEmpty()) {
                JOptionPane.showMessageDialog(purchaseFrame, "Enter Customer Details");

            } else {
                try {
                    preparedStatement = jdbc.connection.prepareStatement("SELECT Count(*) FROM Customers;");
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    System.out.println(resultSet.getInt(1));
                    if (resultSet.getInt(1) == 0) {
                        System.out.println("Enter inot result set 0");
                        jdbc.setConnection();
                        preparedStatement = jdbc.connection.prepareStatement("INSERT INTO Customers (Name, customerMobile) VALUES(?, ?)");

                        preparedStatement.setString(1, customerNameF.getText());
                        preparedStatement.setLong(2, Long.parseLong(customerMobileF.getText()));
                        preparedStatement.executeUpdate();
                        System.out.println("Data Inserted");
                        customerNameF.setEditable(false);
                        customerMobileF.setEditable(false);
                        startBilling.setEnabled(false);
                        clear.setEnabled(false);
                        productName.setEnabled(true);
                        qtyF.setEnabled(true);
                        clear1.setEnabled(true);
                        addToCart.setEnabled(true);
                    } else {
                        preparedStatement = jdbc.connection.prepareStatement("SELECT Id, Name, customerMobile FROM Customers;");
                        resultSet = preparedStatement.executeQuery();
                         while (resultSet.next()) {
                            System.out.println(resultSet.getString(2));
                            if (resultSet.getString(2).equalsIgnoreCase(customerNameF.getText())) {

                                int bolean = JOptionPane.showConfirmDialog(purchaseFrame, "Customer Name Already Present, Customer Id : " + resultSet.getInt(1), "  Do You Want to Continue", JOptionPane.YES_NO_OPTION);
                                if (bolean == JOptionPane.NO_OPTION || bolean == JOptionPane.CLOSED_OPTION) {
                                    customerNameF.setText("");
                                    customerMobileF.setText("");
                                    return;
                                } else {
                                    System.out.println(" User want to continue");
                                    customerNameF.setEditable(false);
                                    customerMobileF.setEditable(false);
                                    startBilling.setEnabled(false);
                                    clear.setEnabled(false);
                                    productName.setEnabled(true);
                                    qtyF.setEnabled(true);
                                    clear1.setEnabled(true);
                                    addToCart.setEnabled(true);
                                    return;
                                }
                            }


                        }
                        System.out.println(" customer doesnt exists in table Inserting");
                        customerNameF.setEditable(false);
                        customerMobileF.setEditable(false);
                        startBilling.setEnabled(false);
                        clear.setEnabled(false);
                        productName.setEnabled(true);
                        qtyF.setEnabled(true);
                        clear1.setEnabled(true);
                        addToCart.setEnabled(true);
                        try {
                            jdbc.setConnection();
                            preparedStatement = jdbc.connection.prepareStatement("INSERT INTO Customers (Name, customerMobile) VALUES(?, ?)");

                            preparedStatement.setString(1, customerNameF.getText());
                            preparedStatement.setLong(2, Long.parseLong(customerMobileF.getText()));
                            preparedStatement.executeUpdate();
                            System.out.println("Data Inserted");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            System.out.println("Error While Inserting Data");
                        }
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        purchasePanel = new JPanel();
        purchasePanel.setBounds(490, 60, 450, 200);
        purchasePanel.setBorder(new LineBorder(Color.gray, 2));
        purchasePanel.setLayout(null);
        purchaseFrame.add(purchasePanel);

        subHeading2 = new JLabel("Select Products");
        subHeading2.setBounds(10, 10, 470, 40);
        subHeading2.setFont(new Font("monospaced", Font.BOLD, 22));
        purchasePanel.add(subHeading2);

        productNameL = new JLabel("Product");
        productNameL.setBounds(10, 60, 80, 40);
        productNameL.setFont(new Font("monospaced", Font.BOLD, 18));
        purchasePanel.add(productNameL);

        int count = 0;
        int i = 1;

        try {
            jdbc = new JDBC();
            jdbc.setConnection();
            preparedStatement = jdbc.connection.prepareStatement("SELECT Count(*) From Products;");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);

            products = new String[count + 1];
            resultSet = null;

            statement = jdbc.connection.createStatement();
            resultSet = statement.executeQuery("SELECT Name FROM Products;");
            System.out.println("Working");
            while (resultSet.next()) {
                products[0] = "Select";
                products[i] = resultSet.getString("Name");
                i++;
            }
        } catch (Exception e) {
            System.out.println("Exception");
        }

        productName = new JComboBox(products);
        productName.setBounds(100, 70, 180, 30);
        productName.setFont(new Font("monospaced", Font.BOLD, 18));
        purchasePanel.add(productName);
        productName.setEnabled(false);

        qtyL = new JLabel("Qty");
        qtyL.setBounds(300, 60, 50, 40);
        qtyL.setFont(new Font("monospaced", Font.BOLD, 18));
        purchasePanel.add(qtyL);

        qtyF = new JTextField();
        qtyF.setBounds(360, 70, 80, 30);
        qtyF.setFont(new Font("monospaced", Font.BOLD, 18));
        purchasePanel.add(qtyF);
        qtyF.setEnabled(false);

        price = new JLabel("Price");
        price.setBounds(10, 100, 100, 40);
        price.setFont(new Font("monospaced", Font.BOLD, 18));
        purchasePanel.add(price);

        priceF = new JLabel();
        priceF.setBounds(100, 110, 180, 30);
        priceF.setFont(new Font("monospaced", Font.BOLD, 18));
        priceF.setBorder(new LineBorder(Color.gray));
        purchasePanel.add(priceF);

        clear1 = new JButton("Clear");
        clear1.setBounds(10, 150, 180, 40);
        clear1.setFont(new Font("monospaced", Font.BOLD, 16));
        clear1.setFocusable(false);
        clear1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        purchasePanel.add(clear1);
        clear1.setEnabled(false);

        clear1.addActionListener(e -> {
            qtyF.setText("");
            priceF.setText("");
            productName.setSelectedIndex(0);
        });

        addToCart = new JButton("Add To Cart");
        addToCart.setBounds(220, 150, 220, 40);
        addToCart.setFont(new Font("monospaced", Font.BOLD, 16));
        addToCart.setFocusable(false);
        addToCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        purchasePanel.add(addToCart);
        addToCart.setEnabled(false);

        addToCart.addActionListener(e -> {
            if (productName.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(purchaseFrame, "Fill Details Properly");
                priceF.setText("");
                qtyF.setText("");
            } else {
                if (qtyF.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(purchaseFrame, "Fill Details Properly");
                } else {
                    int productId = 0;
                    try {
                        jdbc.setConnection();
                        String customerName = customerNameF.getText();
                        long quantity = 0L;
                        String productNameS = productName.getSelectedItem().toString();
                        resultSet = statement
                                .executeQuery("SELECT * FROM Products WHERE Name = '" + productNameS + "';");
                        while (resultSet.next()) {
                            quantity = (resultSet.getLong("Quantity"));
                            productId = resultSet.getInt("Id");
                        }
                        if (Long.parseLong(qtyF.getText()) > quantity) {
                            JOptionPane.showMessageDialog(purchaseFrame,
                                    "Out Of Quantity Available Quantity : " + quantity);
                        } else {
                            resultSet = statement
                                    .executeQuery("SELECT Id FROM Customers WHERE Name = '" + customerName + "';");
                            while (resultSet.next()) {
                                customerId = resultSet.getInt("Id");
                            }
                            long millis = System.currentTimeMillis();
                            java.sql.Date date = new java.sql.Date(millis);
                            System.out.println(date);
                            long qty = Long.parseLong(qtyF.getText());
                            long price = Long.parseLong(priceF.getText());
                            System.out.println(qtyF.getText());
                            long temp = qty * price;

                            System.out.println("Working");

                            /*      storing data into temporders according to orders id    */
                            int auto_Increament = 1;
                            ResultSet rst = jdbc.connection.prepareStatement("Select id from Orders ;").executeQuery();
                            while (rst.next()) {
                                auto_Increament = Integer.parseInt(rst.getString(1));
                            }
                            auto_Increament++;

                            jdbc.connection.prepareStatement("ALTER TABLE tempOrders  Auto_Increment = " + auto_Increament + " ; ").executeUpdate();


                            preparedStatement = jdbc.connection.prepareStatement(
                                    "INSERT INTO tempOrders (CustomerId, ProductId, Quantity, ProductPrice, Date, Amount, ProductName) VALUES(?, ?, ?, ?, ?, ?, ?);");
                            preparedStatement.setInt(1, customerId);
                            preparedStatement.setInt(2, productId);
                            preparedStatement.setLong(3, Long.parseLong(qtyF.getText()));
                            preparedStatement.setLong(4, Long.parseLong(priceF.getText()));
                            preparedStatement.setDate(5, date);
                            preparedStatement.setLong(6, temp);
                            preparedStatement.setString(7, productName.getSelectedItem().toString());
                            preparedStatement.executeUpdate();
                            System.out.println("Working");


                            showTable();
                        }

                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                }
            }
        });

        productName.addActionListener(e -> {
            if (productName.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(purchaseFrame, "Select An Product First");
            } else {
                // int productId = 0;
                String productNameS = productName.getSelectedItem().toString();
                System.out.println(productNameS);

                jdbc.setConnection();
                try {
                    resultSet = statement.executeQuery("SELECT * FROM Products WHERE Name = '" + productNameS + "';");
                    priceF.setText("");
                    while (resultSet.next()) {
                        priceF.setText(String.valueOf(resultSet.getLong("Price")));
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        save = new JButton("Save");
        save.setBounds(250, 5, 180, 40);
        save.setFont(new Font("monospaced", Font.BOLD, 25));
        save.setFocusable(false);
        save.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navigationPanel.add(save);

        save.addActionListener(e -> {
            if (customerMobileF.getText().isEmpty() && customerNameF.getText().isEmpty() && qtyF.getText().isEmpty()) {
                JOptionPane.showMessageDialog(purchaseFrame, "Please Fill all Details or Select Product");
                return;
            } else if (productsTable == null) {
                JOptionPane.showMessageDialog(purchaseFrame, "Please select Product");
                return;
            } else {
                int rowCount = productsTable.getRowCount();
                System.out.println("Row Count " + rowCount);
                try {
                    jdbc.setConnection();
                    for (int j = 0; j < rowCount; j++) {
                        long productId = Long.parseLong((String) productsTable.getValueAt(j, 0));
                        long productQuantity = Long.parseLong((String) productsTable.getValueAt(j, 3));

                        preparedStatement = jdbc.connection
                                .prepareStatement("UPDATE Products SET Quantity = Quantity - " + productQuantity
                                        + " WHERE Id = " + productId);
                        preparedStatement.executeUpdate();
                        System.out.println("Done");
                        long millis = System.currentTimeMillis();
                        java.sql.Date date = new java.sql.Date(millis);
                        System.out.println(date);
                        preparedStatement = jdbc.connection.prepareStatement(
                                "INSERT INTO Sales (CustomerId, Date, Amount) VALUES(?, ?, ?);");
                        preparedStatement.setInt(1, customerId);
                        // preparedStatement.setString(2, customerNameF.getText());
                        // preparedStatement.setLong(3, Long.parseLong(customerMobileF.getText()));
                        preparedStatement.setDate(2, date);
                    }
                    resultSet = jdbc.connection
                            .prepareStatement("select sum(Amount)  from tempOrders where CustomerId = " + customerId + ";")
                            .executeQuery();
                    resultSet.next();

                    Long totalAmount = resultSet.getLong(1);
                    System.out.println(totalAmount);
                    preparedStatement.setLong(3, totalAmount);
                    preparedStatement.executeUpdate();
                    System.out.println("Sales Done");
                    System.out.println("inserting  into orders");
                    jdbc.connection.prepareStatement("insert orders select * from tempOrders ;").executeUpdate();
                    System.out.println("truncating");
                    jdbc.connection.prepareStatement("truncate tempOrders ; ").executeUpdate();


                } catch (Exception e3) {
                    System.out.println("Sales Exception ");
                    e3.printStackTrace();
                }
                new PurchaseProducts();
                purchaseFrame.dispose();
            }
        });
        back = new JButton("Back");
        back.setBounds(500, 5, 180, 40);
        back.setFont(new Font("monospaced", Font.BOLD, 25));
        back.setFocusable(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navigationPanel.add(back);
        back.addActionListener(e -> {
            try {
                System.out.println("truncating");
                jdbc.connection.prepareStatement("truncate temporders ; ").executeUpdate();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(" error when Truncate temporders ");
            }

            new Dashboard();
            purchaseFrame.dispose();

        });

        purchaseFrame.setVisible(true);
    }

    public void showTable() {
        String tableColumns[] = {"ProductId", "ProductName", "ProductPrice", "Quantity", "Amount"};
        String tableData[][] = new String[index][5];
        tableData = PurchaseProducts.tableData;

        int count = 0;
        try {
            jdbc = new JDBC();
            jdbc.setConnection();
            preparedStatement = jdbc.connection
                    .prepareStatement("SELECT Count(*) From tempOrders WHERE CustomerId = " + customerId + ";");
            System.out.println(customerId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
            System.out.println(" count " + count);
            tableData = new String[count][5];

            resultSet = statement.executeQuery(
                    /*
                     * "SELECT ProductId,ProductName, ProductPrice, Quantity, Amount from Orders WHERE CustomerId = "
                     *
                     */
                    "SELECT ProductId,ProductName, ProductPrice, Quantity, Amount from tempOrders WHERE CustomerId = "
                            + customerId + ";");

            for (int i = 0; i < count; i++) {
                resultSet.next();
                tableData[i][0] = resultSet.getString(1);
                tableData[i][1] = resultSet.getString(2);
                tableData[i][2] = resultSet.getString(3);
                tableData[i][3] = resultSet.getString(4);
                tableData[i][4] = resultSet.getString(5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        productsTable = new JTable(tableData, tableColumns);
        productsTable.setRowHeight(25);
        productsTable.setFont(new Font("monospaced", Font.BOLD, 18));
        scrollPane = new JScrollPane(productsTable);
        scrollPane.setBounds(5, 5, 920, 240);
        tablePanel.add(scrollPane);
        tablePanel.setVisible(true);
    }
}
