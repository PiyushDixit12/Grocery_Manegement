import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SalesReport {
    JFrame salesFrame, salesFrame2;
    JFrame viewFrame;
    JLabel heading;
    JPanel tablePanel, navigationPanel;
    JButton back;
    JScrollPane scrollPane;
    JTable productsTable;
    JDBC jdbc;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public SalesReport() {
        salesFrame = new JFrame();
        salesFrame.setSize(965, 625);
        salesFrame.setDefaultCloseOperation(viewFrame.EXIT_ON_CLOSE);
        salesFrame.setLocationRelativeTo(null);
        salesFrame.setLayout(null);

        heading = new JLabel("Sales Report");
        heading.setBounds(10, 10, 930, 40);
        heading.setFont(new Font("monospaced", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(new LineBorder(Color.GRAY, 2));
        salesFrame.add(heading);

        tablePanel = new JPanel();
        tablePanel.setBounds(10, 60, 930, 390);
        tablePanel.setBorder(new LineBorder(Color.gray, 2));
        tablePanel.setLayout(null);
        salesFrame.add(tablePanel);

        JPanel totalpanel = new JPanel();
        totalpanel.setBounds(10, 470, 930, 50);
        totalpanel.setBorder(new LineBorder(Color.gray, 2));
        totalpanel.setLayout(null);
        salesFrame.add(totalpanel);
        int total = 0;
        try {
            JDBC jdbc = new JDBC();
            jdbc.setConnection();
            PreparedStatement psmtotal = jdbc.connection.prepareStatement("select sum(Amount) from sales ;");
            ResultSet rstotal = psmtotal.executeQuery();
            rstotal.next();

            total = rstotal.getInt(1);
            System.out.println(" total amount " + total);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel totalAmount = new JLabel("Total-Amount : " + total);
        totalAmount.setBounds(10, 5, 270, 40);
        totalAmount.setFont(new Font("monospaced", Font.BOLD, 20));
        totalAmount.setHorizontalAlignment(SwingConstants.CENTER);
        totalAmount.setBorder(new LineBorder(Color.GRAY, 2));
        totalpanel.add(totalAmount);

        int profitAmount = (total* 30) / 100 ;
        System.out.println(" profit Amount : "+profitAmount);
          System.out.println(" profit Amount : "+total);
        JLabel profit = new JLabel("Profit(30%) : " + profitAmount);
        profit.setBounds(320, 5, 270, 40);
        profit.setFont(new Font("monospaced", Font.BOLD, 20));
        profit.setHorizontalAlignment(SwingConstants.CENTER);
        profit.setBorder(new LineBorder(Color.GRAY, 2));
        totalpanel.add(profit);

        JLabel Gst = new JLabel("GST (10%) : " + (total - profitAmount)   / 10);
        Gst.setBounds(630, 5, 270, 40);
        Gst.setFont(new Font("monospaced", Font.BOLD, 20));
        Gst.setHorizontalAlignment(SwingConstants.CENTER);
        Gst.setBorder(new LineBorder(Color.GRAY, 2));
        totalpanel.add(Gst);

        navigationPanel = new JPanel();
        navigationPanel.setBounds(10, 530, 930, 50);
        navigationPanel.setBorder(new LineBorder(Color.gray, 2));
        navigationPanel.setLayout(null);
        salesFrame.add(navigationPanel);

        back = new JButton("Back");
        back.setBounds(375, 5, 180, 40);
        back.setFont(new Font("monospaced", Font.BOLD, 25));
        back.setFocusable(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navigationPanel.add(back);

        back.addActionListener(e -> {
            new Dashboard();
            salesFrame.dispose();
        });
        showTable();
        salesFrame.setVisible(true);
    }

    public void showTable() {
        String tableColumns[] = {"CUSTOMER ID", "CUSTOMER NAME", "CUSTOMER MOBILE No", "DATE", "TOTAL AMOUNT"};
        String tableData[][] = new String[0][0];
        int count = 0;
        try {
            jdbc = new JDBC();
            jdbc.setConnection();
            preparedStatement = jdbc.connection.prepareStatement("SELECT Count(*) From Sales;");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
            System.out.println(count);

            tableData = new String[count][5];

            preparedStatement = jdbc.connection.prepareStatement("SELECT CustomerId,Name,CustomerMobile,Date,Amount from Sales inner join customers where customers.id= sales.customerid;");
            ResultSet resultSet1 = preparedStatement.executeQuery();
            for (int i = 0; i < count; i++) {
                resultSet1.next();
                System.out.println("result set" + i);
                tableData[i][0] = resultSet1.getString(1);
                tableData[i][1] = resultSet1.getString(2);
                tableData[i][2] = resultSet1.getString(3);
                tableData[i][3] = resultSet1.getString(4);
                tableData[i][4] = resultSet1.getString(5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        productsTable = new JTable(tableData, tableColumns);
        productsTable.setRowHeight(30);
        productsTable.setFont(new Font("monospaced", Font.BOLD, 18));

        productsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (productsTable.getValueAt(productsTable.getSelectedRow(), 1) != null) {
                    createCustomerSellsTable(productsTable.getSelectedRow(), productsTable);
                    salesFrame.dispose();
                }
            }
        });
        scrollPane = new JScrollPane(productsTable);
        scrollPane.setBounds(5, 5, 920, 450);
        tablePanel.add(scrollPane);
        tablePanel.setVisible(true);
    }

    public void createCustomerSellsTable(int index, JTable salesTable) {
        salesFrame2 = new JFrame();
        salesFrame2.setSize(965, 625);
        salesFrame2.setDefaultCloseOperation(salesFrame2.EXIT_ON_CLOSE);
        salesFrame2.setLocationRelativeTo(null);
        salesFrame2.setLayout(null);

        heading = new JLabel(" Customer Sales Report  ");
        heading.setBounds(10, 10, 930, 40);
        heading.setFont(new Font("monospaced", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(new LineBorder(Color.GRAY, 2));
        salesFrame2.add(heading);

        JPanel detailPanel = new JPanel();
        detailPanel.setBounds(10, 60, 930, 100);
        detailPanel.setBorder(new LineBorder(Color.gray, 2));
        detailPanel.setLayout(null);
        salesFrame2.add(detailPanel);

        JLabel customerName = new JLabel("  Name :   " + salesTable.getValueAt(salesTable.getSelectedRow(), 1));
        customerName.setBounds(30, 5, 300, 40);
        customerName.setFont(new Font("Arials", Font.PLAIN, 20));
        customerName.setBorder(new LineBorder(Color.gray, 2));
        customerName.setLayout(null);
        detailPanel.add(customerName);

        JLabel customerMobileNo = new JLabel(" MobileNo : " + salesTable.getValueAt(salesTable.getSelectedRow(), 2));
        customerMobileNo.setBounds(30, 50, 300, 40);
        customerMobileNo.setFont(new Font("Arials", Font.PLAIN, 20));
        customerMobileNo.setBorder(new LineBorder(Color.gray, 2));
        customerMobileNo.setLayout(null);
        detailPanel.add(customerMobileNo);


        tablePanel = new JPanel();
        tablePanel.setBounds(10, 170, 930, 350);
        tablePanel.setBorder(new LineBorder(Color.gray, 2));
        tablePanel.setLayout(null);
        salesFrame2.add(tablePanel);

        navigationPanel = new JPanel();
        navigationPanel.setBounds(10, 530, 930, 50);
        navigationPanel.setBorder(new LineBorder(Color.gray, 2));
        navigationPanel.setLayout(null);
        salesFrame2.add(navigationPanel);

        back = new JButton("Back");
        back.setBounds(375, 5, 180, 40);
        back.setFont(new Font("monospaced", Font.BOLD, 25));
        back.setFocusable(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navigationPanel.add(back);

        back.addActionListener(e -> {
            new SalesReport();
            salesFrame2.dispose();
        });

        String Columns[] = {"PRODUCT ID", "PRODUCT NAME", " PRICE", "QUANTITY", "DATE", "BILL"};

        int id = Integer.parseInt((String) productsTable.getValueAt(index, 0));
        System.out.println(id);
        int count = 0;
        String Data[][] = new String[1][1];
        try {
            jdbc = new JDBC();
            jdbc.setConnection();
            ResultSet rs = (ResultSet) jdbc.connection.prepareStatement("SELECT SUM(Amount) from orders where Customerid = " + id + ";").executeQuery();
            long totalAmount = 0;
            while (rs.next()) {
                totalAmount = rs.getLong(1);
            }

            JLabel total = new JLabel("  Total Bill Amount  :  " + totalAmount);
            total.setBounds(550, 50, 300, 40);
            total.setFont(new Font("Arials", Font.PLAIN, 20));
            total.setBorder(new LineBorder(Color.gray, 2));
            total.setLayout(null);
            detailPanel.add(total);

            preparedStatement = jdbc.connection
                    .prepareStatement("SELECT Count(*) From orders where Customerid = " + id + ";");
            System.out.println(" customer id " + id);
            ResultSet resultSet1 = preparedStatement.executeQuery();
            resultSet1.next();
            count = resultSet1.getInt(1);
            System.out.println(count);

            Data = new String[count][6];

            PreparedStatement preparedStatement = jdbc.connection.prepareStatement(
                    "select productId,productName,productPrice,quantity,date ,Amount from orders  where customerid="
                            + id + ";");
            ResultSet resultSet2 = preparedStatement.executeQuery();

            for (int i = 0; i < count; i++) {
                resultSet2.next();
                Data[i][0] = resultSet2.getString(1);
                Data[i][1] = resultSet2.getString(2);
                Data[i][2] = resultSet2.getString(3);
                Data[i][3] = resultSet2.getString(4);
                Data[i][4] = resultSet2.getString(5);
                Data[i][5] = resultSet2.getString(6);
            }

        } catch (Exception e4) {
            e4.printStackTrace();
            System.out.println(" exception in row");
        }
        productsTable = new JTable(Data, Columns);
        productsTable.setRowHeight(30);
        productsTable.setFont(new Font("monospaced", Font.BOLD, 18));
        scrollPane = new JScrollPane(productsTable);
        scrollPane.setBounds(5, 5, 920, 300);
        tablePanel.add(scrollPane);
        tablePanel.setVisible(true);

        salesFrame2.setVisible(true);
    }
}
