package nhom8;

import nhom8.controller.HomeController;
import nhom8.utils.DataSource;
import nhom8.view.home.Dashboard;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.formdev.flatlaf.FlatIntelliJLaf");
            System.out.println("Khoi tao look and feel thanh cong");

            HomeController homeController = new HomeController(new Dashboard());
        } catch (Exception e) {
            try {
                DataSource.getInstance().getConnection().close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Khoi tao look and feel that bai");
            e.printStackTrace();
            System.exit(0);
        }
    }
}

