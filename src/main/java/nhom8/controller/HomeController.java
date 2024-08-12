package nhom8.controller;

import lombok.Getter;
import lombok.Setter;
import nhom8.view.category.PnlCategory;
import nhom8.view.home.Dashboard;
import nhom8.view.product.PnlProduct;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeController {
    @Getter
    @Setter
    private Dashboard view;
    private final JPanel pnlCategory = new PnlCategory();
    private final CategoryController categoryController = new CategoryController(view, (PnlCategory) pnlCategory);
    private final ProductController productController = new ProductController();
    private final PnlProduct pnlProduct = new PnlProduct();

    public HomeController(Dashboard view) {
        this.view = view;
        view.setVisible(true);
        addEvent();
    }

    private void addEvent() {
        view.getBtnCategory().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setPnlBody(pnlCategory);
                categoryController.setPanel((PnlCategory) pnlCategory);
                categoryController.updateData();
            }
        });

    }
}
