package nhom8;

import nhom8.controller.HomeController;
import nhom8.view.home.Dashboard;

public class Main {
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.formdev.flatlaf.FlatIntelliJLaf");
            System.out.println("Khoi tao look and feel thanh cong");

            HomeController homeController = new HomeController(new Dashboard());
        } catch (Exception e) {
            System.out.println("Khoi tao look and feel that bai");
            System.exit(0);
        }
    }
}

