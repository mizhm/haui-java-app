package nhom8.controller;

import lombok.Getter;
import lombok.Setter;
import nhom8.view.category.PnlCategory;
import nhom8.view.home.Dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeController {
    @Getter
    @Setter
    private Dashboard view;
    private final CategoryController categoryController = new CategoryController();
    private final ProductController productController = new ProductController();

    public HomeController(Dashboard view) {
        this.view = view;
        view.setVisible(true);
        addEvent();
    }

    private void addEvent() {
        view.getBtnCategory().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.getPnlBody().removeAll();
                PnlCategory pnlCategory = new PnlCategory();
                view.getPnlBody().add(pnlCategory);
                view.getPnlBody().repaint();
                view.getPnlBody().revalidate();

                categoryController.setPanel(pnlCategory);
                categoryController.updateData();
            }
        });
    }
}
