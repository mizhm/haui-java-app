package nhom8.controller;

import lombok.Getter;
import lombok.Setter;
import nhom8.dao.HomeDAO;
import nhom8.model.User;
import nhom8.utils.DataSource;
import nhom8.view.bill.PnlBill;
import nhom8.view.category.PnlCategory;
import nhom8.view.home.Dashboard;
import nhom8.view.home.JDLogin;
import nhom8.view.home.PnlHome;
import nhom8.view.product.PnlProduct;
import nhom8.view.user.PnlUser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class HomeController {
    @Getter
    @Setter
    private Dashboard view;

    private final PnlHome pnlHome = new PnlHome(new HomeDAO().getStatistic());
    @Getter
    private final PnlCategory pnlCategory = new PnlCategory();
    @Getter
    private final PnlProduct pnlProduct = new PnlProduct();
    @Getter
    private final PnlBill pnlBill = new PnlBill();
    private final PnlUser pnlUser = new PnlUser();

    private final JDLogin jdLogin;

    private final LoginController loginController;
    private final CategoryController categoryController;
    private final ProductController productController;
    private final BillController billController;
    private final UserController userController;

    @Setter
    private User user;

    public HomeController(Dashboard view) {
        this.view = view;
        view.setPnlBody(pnlHome);
        this.jdLogin = new JDLogin(view, true);
        this.loginController = new LoginController(view, jdLogin, this);
        jdLogin.setVisible(true);

        this.categoryController = new CategoryController(view, pnlCategory);
        this.productController = new ProductController(view, pnlProduct);
        this.billController = new BillController(view, pnlBill);
        this.userController = new UserController(view, pnlUser);
        addEvent();
    }


    private void addEvent() {
        view.getBtnDashboard().addActionListener(e -> {
            view.setPnlBody(pnlHome);
        });

        view.getBtnCategory().addActionListener(e -> {
            view.setPnlBody(pnlCategory);
            categoryController.updateData();
        });

        view.getBtnProduct().addActionListener(e -> {
            view.setPnlBody(pnlProduct);
            productController.updateData();
        });

        view.getBtnBill().addActionListener(e -> {
            view.setPnlBody(pnlBill);
            billController.updateData();
        });

        view.getBtnUser().addActionListener(e -> {
            view.setPnlBody(pnlUser);
            userController.updateData();
        });

        view.getLblLogout().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                view.setVisible(false);
                jdLogin.setVisible(true);
            }
        });

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    DataSource.getInstance().getConnection().close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0);
            }
        });
    }

}
