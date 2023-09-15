import java.sql.*;

public class JDBC {
    Connection connection;

    public void setConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String userName = "root";
            String password = "2020";
            String url = "jdbc:mysql://localhost/Kirana_store";
            connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Connection Done");
        } catch (Exception e) {
            System.out.println("Error While Connecting Database...");
            e.printStackTrace();
        }
    }
}

