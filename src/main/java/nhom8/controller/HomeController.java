package nhom8.controller;

import lombok.Getter;
import lombok.Setter;
import nhom8.dao.HomeDAO;
import nhom8.view.bill.PnlBill;
import nhom8.view.category.PnlCategory;
import nhom8.view.home.Dashboard;
import nhom8.view.home.PnlHome;
import nhom8.view.product.PnlProduct;

public class HomeController {
    @Getter
    @Setter
    private Dashboard view;

    private final PnlHome pnlHome = new PnlHome(new HomeDAO().getStatistic());
    private final PnlCategory pnlCategory = new PnlCategory();
    private final PnlProduct pnlProduct = new PnlProduct();
    private final PnlBill pnlBill = new PnlBill();

    private final CategoryController categoryController = new CategoryController(view, pnlCategory);
    private final ProductController productController = new ProductController(view, pnlProduct);
    private final BillController billController = new BillController(view, pnlBill);

    public HomeController(Dashboard view) {
        this.view = view;
        view.setPnlBody(pnlHome);
        view.setVisible(true);
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
    }
}
