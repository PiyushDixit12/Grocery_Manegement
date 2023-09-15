import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewProducts {
    JFrame viewFrame;
    JLabel heading;
    JPanel tablePanel, navigationPanel;
    JButton back;
    JScrollPane scrollPane;
    JTable productsTable;
    JDBC jdbc;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public ViewProducts() {
        viewFrame = new JFrame();
        viewFrame.setSize(965, 625);
        viewFrame.setDefaultCloseOperation(viewFrame.EXIT_ON_CLOSE);
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setLayout(null);

        heading = new JLabel("All Products From Store");
        heading.setBounds(10, 10, 930, 40);
        heading.setFont(new Font("monospaced", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(new LineBorder(Color.GRAY, 2));
        viewFrame.add(heading);

        tablePanel = new JPanel();
        tablePanel.setBounds(10, 60, 930, 460);
        tablePanel.setBorder(new LineBorder(Color.gray, 2));
        tablePanel.setLayout(null);
        viewFrame.add(tablePanel);

        navigationPanel = new JPanel();
        navigationPanel.setBounds(10, 530, 930, 50);
        navigationPanel.setBorder(new LineBorder(Color.gray, 2));
        navigationPanel.setLayout(null);
        viewFrame.add(navigationPanel);

        back = new JButton("Back");
        back.setBounds(375, 5, 180, 40);
        back.setFont(new Font("monospaced", Font.BOLD, 25));
        back.setFocusable(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navigationPanel.add(back);

        back.addActionListener(e -> {
            new Dashboard();
            viewFrame.dispose();
        });

        showTable();

        viewFrame.setVisible(true);
    }

    public void showTable() {
        String tableColumns[] = {"PRODUCT ID", "PRODUCT NAME", "COST", "PRICE", "COMPANY NAME", "QUANTITY"};
        String tableData[][] = new String[0][0];
        int count = 0;
        try {
            jdbc = new JDBC();
            jdbc.setConnection();
            preparedStatement = jdbc.connection.prepareStatement("SELECT Count(*) From Products;");
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
            System.out.println(count);

            tableData = new String[count][6];

            preparedStatement = jdbc.connection.prepareStatement("SELECT * from Products;");
            resultSet = preparedStatement.executeQuery();

            for (int i = 0; i < count; i++) {
                resultSet.next();
                tableData[i][0] = resultSet.getString(1);
                tableData[i][1] = resultSet.getString(2);
                tableData[i][2] = resultSet.getString(4);
                tableData[i][3] = resultSet.getString(5);
                tableData[i][4] = resultSet.getString(3);
                tableData[i][5] = resultSet.getString(6);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        productsTable = new JTable(tableData, tableColumns);
        productsTable.setRowHeight(35);
        productsTable.setFont(new Font("monospaced", Font.BOLD, 18));
        scrollPane = new JScrollPane(productsTable);
        scrollPane.setBounds(5, 5, 920, 450);
        tablePanel.add(scrollPane);
        productsTable.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        int index = productsTable.getSelectedRow();

                        createProductSellsTable(index,
                                (String) productsTable.getValueAt(index, 1));
                        viewFrame.dispose();
                    }
                });

        tablePanel.setVisible(true);
    }

    public void createProductSellsTable(int index, String productName) {
        JFrame Frame = new JFrame();
        Frame.setSize(965, 625);
        Frame.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
        Frame.setLocationRelativeTo(null);
        Frame.setLayout(null);

        heading = new JLabel("SALES RECORD OF PRODUCT " + productName.toUpperCase());
        heading.setBounds(10, 10, 930, 40);
        heading.setFont(new Font("monospaced", Font.BOLD, 25));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(new LineBorder(Color.GRAY, 2));
        Frame.add(heading);

        tablePanel = new JPanel();
        tablePanel.setBounds(10, 60, 930, 460);
        tablePanel.setBorder(new LineBorder(Color.gray, 2));
        tablePanel.setLayout(null);
        Frame.add(tablePanel);

        navigationPanel = new JPanel();
        navigationPanel.setBounds(10, 530, 930, 50);
        navigationPanel.setBorder(new LineBorder(Color.gray, 2));
        navigationPanel.setLayout(null);
        Frame.add(navigationPanel);

        back = new JButton("Back");
        back.setBounds(375, 5, 180, 40);
        back.setFont(new Font("monospaced", Font.BOLD, 25));
        back.setFocusable(false);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navigationPanel.add(back);

        back.addActionListener(e -> {
            new ViewProducts();
            Frame.dispose();
        });
        String Columns[] = {"ORDER ID", "CUSTOMER ID", " CUSTOMER NAME", "QUANTITY", "DATE", "BILL"};

        int id = Integer.parseInt((String) productsTable.getValueAt(index, 0));
        System.out.println(id);
        int count = 0;
        String Data[][] = new String[1][1];
        try {
            jdbc = new JDBC();
            jdbc.setConnection();
            preparedStatement = jdbc.connection
                    .prepareStatement("SELECT Count(*) From orders where productid = " + id + " and customerid != 0 ;");
            ResultSet resultSet1 = preparedStatement.executeQuery();
            resultSet1.next();
            count = resultSet1.getInt(1);
            System.out.println(count);

            Data = new String[count][6];

            PreparedStatement preparedStatement = jdbc.connection.prepareStatement(
                    "select orders.id,customerid,Name,quantity,date ,Amount from orders inner join customers on orders.customerid= customers.id where productid="
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
        productsTable.setRowHeight(35);
        productsTable.setFont(new Font("monospaced", Font.BOLD, 18));
        scrollPane = new JScrollPane(productsTable);
        scrollPane.setBounds(5, 5, 920, 450);
        tablePanel.add(scrollPane);
        tablePanel.setVisible(true);

        Frame.setVisible(true);
    }

}
