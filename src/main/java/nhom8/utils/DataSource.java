package nhom8.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DataSource {
    private static DataSource instance;
    private static Connection connection;

    public DataSource() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("db-config");
            Class.forName(bundle.getString("DRIVER_NAME"));
            connection = DriverManager.getConnection(bundle.getString("URL"), bundle.getString("USER"), bundle.getString("PASSWORD"));
        }
        catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public DataSource getInstance(){
        try {
            if(instance == null || instance.getConnection().isClosed()){
                instance = new DataSource();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return instance;
    }
}
