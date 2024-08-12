package nhom8.controller;

import nhom8.dao.CategoryDAO;
import nhom8.model.Category;
import nhom8.view.category.PnlCategory;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class CategoryController implements ManagerController {
    private CategoryDAO categoryDAO = new CategoryDAO();
    private PnlCategory panel;

    public CategoryController() {
    }

    public PnlCategory getPanel() {
        return panel;
    }

    public void setPanel(PnlCategory panel) {
        this.panel = panel;
    }

    @Override
    public void actionAdd() {

    }

    @Override
    public void actionEdit() {

    }

    @Override
    public void actionDelete() {

    }

    @Override
    public void actionSearch() {

    }

    @Override
    public void updateData() {
        try {
            ArrayList<Category> categories = categoryDAO.getAll();
            panel.getTblCategory().removeAll();

            String cols[] = {"Id", "Ten", "Trang thai"};
            DefaultTableModel dtm = new DefaultTableModel(cols, 0);

            categories.forEach(obj -> {
                dtm.addRow(new Object[]{obj.getId(), obj.getName(), obj.getStatus() ? "Hoat dong" : "Khong hoat dong"});
            });
            panel.getTblCategory().setModel(dtm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
