package nhom8.controller;

import lombok.Setter;
import nhom8.dao.CategoryDAO;
import nhom8.model.Category;
import nhom8.utils.Common;
import nhom8.view.category.JDAddCategory;
import nhom8.view.category.JDDeleteCategory;
import nhom8.view.category.JDSearchCategory;
import nhom8.view.category.PnlCategory;
import nhom8.view.home.Dashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class CategoryController implements ManagerController {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final Dashboard view;

    @Setter
    private PnlCategory panel;

    private JDAddCategory jdAdd;
    private JDAddCategory jdUpdate;
    private JDDeleteCategory jdDelete;
    private JDSearchCategory jdSearch;

    private ArrayList<Category> categories;
    private Category category;

    public CategoryController(Dashboard view, PnlCategory panel) {
        this.view = view;
        this.panel = panel;
        addEvent();
    }

    @Override
    public void actionAdd() {
        jdAdd.getBtnSubmit().addActionListener(e -> {
            String name = jdAdd.getTxtName().getText().trim();
            boolean status = jdAdd.getRdoActive().isSelected();
            if (Common.isNullOrEmpty(name)) {
                jdAdd.getTxtName().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdAdd.getLblName().setForeground(new Color(240, 71, 71));
                jdAdd.getLblNameError().setText("Không được để trống");
            } else if (categories.stream().anyMatch(el -> el.getName().equalsIgnoreCase(name))) {
                jdAdd.getTxtName().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdAdd.getLblName().setForeground(new Color(240, 71, 71));
                jdAdd.getLblNameError().setText("Tên đã được sử dụng");
            } else {
                try {
                    Category category = new Category();
                    category.setName(name);
                    category.setStatus(status);

                    Map<String, Object> result = categoryDAO.create(category);
                    if ((Boolean) result.get("status")) {
                        JOptionPane.showMessageDialog(jdAdd, result.get("message"));
                        updateData();
                        jdAdd.dispose();
                    } else {
                        JOptionPane.showMessageDialog(jdAdd, result.get("message"));
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public void actionEdit() {
        jdUpdate.getBtnSubmit().addActionListener(e -> {
            String name = jdUpdate.getTxtName().getText().trim();
            boolean status = jdUpdate.getRdoActive().isSelected();
            if (Common.isNullOrEmpty(name)) {
                jdUpdate.getTxtName().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdUpdate.getLblName().setForeground(new Color(240, 71, 71));
                jdUpdate.getLblNameError().setText("Tên không được để trống");
            } else if (categories.stream().anyMatch(el -> el.getName().equalsIgnoreCase(name) && el.getId() != this.category.getId())) {
                jdUpdate.getTxtName().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdUpdate.getLblName().setForeground(new Color(240, 71, 71));
                jdUpdate.getLblNameError().setText("Tên đã được sử dụng");
            } else {
                try {
                    Category category = new Category();
                    category.setName(name);
                    category.setStatus(status);
                    category.setId(this.category.getId());

                    Map<String, Object> result = categoryDAO.update(category);
                    if ((Boolean) result.get("status")) {
                        JOptionPane.showMessageDialog(jdUpdate, result.get("message"));
                        updateData();
                        jdUpdate.dispose();
                    } else {
                        JOptionPane.showMessageDialog(jdUpdate, result.get("message"));
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public void actionDelete() {
        jdDelete.getBtnDelete().addActionListener(e -> {
            try {
                Map<String, Object> result = categoryDAO.delete(category.getId());
                if ((Boolean) result.get("status")) {
                    JOptionPane.showMessageDialog(jdDelete, result.get("message"));
                    System.out.println(result.get("message"));
                    updateData();
                    jdDelete.dispose();
                } else {
                    JOptionPane.showMessageDialog(jdDelete, result.get("message"));
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void actionSearch() {
        jdSearch.getBtnSearch().addActionListener(e -> {
            Category category = new Category();
            String name = jdSearch.getTxtName().getText().trim();
            String statusString = jdSearch.getCboStatus().getSelectedItem().toString();

            if (!Common.isNullOrEmpty(name)) {
                category.setName(name);
            }

            if (!statusString.equals("Không chọn")) {
                category.setStatus(statusString.equals("Hoạt động"));
            }

            try {
                categories = categoryDAO.getWithCondition(category);
                panel.getTblCategory().removeAll();
                String[] cols = {"Id", "Tên", "Trạng thái"};
                DefaultTableModel dtm = new DefaultTableModel(cols, 0);
                if (!Common.isNullOrEmpty(categories)) {
                    categories.forEach(obj -> {
                        dtm.addRow(new Object[]{obj.getId(), obj.getName(), obj.getStatus() ? "Hoạt động" : "Không hoạt động"});
                    });

                    panel.getTblCategory().getSelectionModel().addListSelectionListener(el -> {
                        int position = panel.getTblCategory().getSelectedRow();
                        if (position >= 0) {
                            this.category = categories.get(position);
                        }
                    });

                    panel.getTblCategory().changeSelection(0, 0, false, false);
                }

                panel.getTblCategory().setModel(dtm);
                jdSearch.dispose();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void updateData() {
        try {
            categories = categoryDAO.getAll();
            panel.getTblCategory().removeAll();

            String[] cols = {"Id", "Tên", "Trạng thái"};
            DefaultTableModel dtm = new DefaultTableModel(cols, 0);
            if (!Common.isNullOrEmpty(categories)) {
                categories.forEach(obj -> {
                    dtm.addRow(new Object[]{obj.getId(), obj.getName(), obj.getStatus() ? "Hoạt động" : "Không hoạt động"});
                });

                panel.getTblCategory().getSelectionModel().addListSelectionListener(e -> {
                    int position = panel.getTblCategory().getSelectedRow();
                    if (position >= 0) {
                        category = categories.get(position);
                    }
                });

                panel.getTblCategory().changeSelection(0, 0, false, false);
            }

            panel.getTblCategory().setModel(dtm);
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    @Override
    public void addEvent() {
        panel.getLblAddCategory().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jdAdd = new JDAddCategory(view, false, null);
                jdAdd.setVisible(true);
                actionAdd();
            }
        });
        panel.getLblUpdateCategory().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jdUpdate = new JDAddCategory(view, false, category);
                jdUpdate.setVisible(true);
                actionEdit();
            }
        });

        panel.getLblDeleteCategory().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jdDelete = new JDDeleteCategory(view, false, category.getName());
                jdDelete.setVisible(true);
                actionDelete();
            }
        });

        panel.getLblSearchCategory().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jdSearch = new JDSearchCategory(view, false);
                jdSearch.setVisible(true);
                actionSearch();
            }
        });
        panel.getLblRefreshCategory().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateData();
            }
        });
    }
}
