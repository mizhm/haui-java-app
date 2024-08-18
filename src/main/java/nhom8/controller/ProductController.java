package nhom8.controller;

import lombok.Setter;
import nhom8.dao.CategoryDAO;
import nhom8.dao.ProductDAO;
import nhom8.model.Category;
import nhom8.model.Product;
import nhom8.utils.Common;
import nhom8.view.home.Dashboard;
import nhom8.view.product.JDActionProduct;
import nhom8.view.product.JDDeleteProduct;
import nhom8.view.product.JDSearchProduct;
import nhom8.view.product.PnlProduct;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class ProductController implements ManagerController {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final Dashboard view;

    @Setter
    private PnlProduct panel;

    private JDActionProduct jdAdd;
    private JDActionProduct jdUpdate;
    private JDDeleteProduct jdDelete;
    private JDSearchProduct jdSearch;

    private ArrayList<Product> products;
    private Product product;

    public ProductController(Dashboard view, PnlProduct panel) {
        this.view = view;
        this.panel = panel;
        addEvent();
    }

    @Override
    public void actionAdd() {
        jdAdd.getBtnSubmit().addActionListener(e -> {
            String name = jdAdd.getTxtName().getText().trim();
            Float price = Float.parseFloat(Common.isNullOrEmpty(jdAdd.getTxtPrice().getText().trim()) ? "0" :
                    jdAdd.getTxtPrice().getText().trim());
            Integer categoryId = ((Category) jdAdd.getCboCategory().getSelectedItem()).getId();
            boolean status = jdAdd.getRdoActive().isSelected();
            boolean validate = true;

            if (Common.isNullOrEmpty(name)) {
                jdAdd.getTxtName().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdAdd.getLblName().setForeground(new Color(240, 71, 71));
                jdAdd.getLblNameError().setText("Tên không được để trống");
                validate = false;
                jdAdd.getLblNameError().setVisible(true);
            }

            if (Common.isNullOrEmpty(price)) {
                jdAdd.getTxtPrice().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdAdd.getLblPrice().setForeground(new Color(240, 71, 71));
                jdAdd.getLblPriceError().setText("Giá không được để trống");
                validate = false;
                jdAdd.getLblPriceError().setVisible(true);
            } else if (price < 0) {
                jdAdd.getTxtPrice().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdAdd.getLblPrice().setForeground(new Color(240, 71, 71));
                jdAdd.getLblPriceError().setText("Giá phải lớn hơn hoặc bằng 0");
                validate = false;
                jdAdd.getLblPriceError().setVisible(true);
            }

            if (products.stream().anyMatch(el -> el.getName().equalsIgnoreCase(name))) {
                jdAdd.getTxtName().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdAdd.getLblName().setForeground(new Color(240, 71, 71));
                jdAdd.getLblNameError().setText("Tên đã được sử dụng");
                validate = false;
                jdAdd.getLblNameError().setVisible(true);
            }
            if (validate) {
                try {
                    Product product = new Product();
                    product.setName(name);
                    product.setPrice(price);
                    product.setStatus(status);
                    product.setCategoryId(categoryId);

                    Map<String, Object> result = productDAO.create(product);
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
            Float price = Float.parseFloat(Common.isNullOrEmpty(jdUpdate.getTxtPrice().getText().trim()) ? "0" :
                    jdUpdate.getTxtPrice().getText().trim());
            Integer categoryId = ((Category) jdUpdate.getCboCategory().getSelectedItem()).getId();
            boolean status = jdUpdate.getRdoActive().isSelected();
            boolean validate = true;

            if (Common.isNullOrEmpty(name)) {
                jdUpdate.getTxtName().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdUpdate.getLblName().setForeground(new Color(240, 71, 71));
                jdUpdate.getLblNameError().setText("Tên không được để trống");
                validate = false;
                jdUpdate.getLblNameError().setVisible(true);
            }

            if (Common.isNullOrEmpty(price)) {
                jdUpdate.getTxtPrice().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdUpdate.getLblPrice().setForeground(new Color(240, 71, 71));
                jdUpdate.getLblPriceError().setText("Giá không được để trống");
                validate = false;
                jdUpdate.getLblPriceError().setVisible(true);
            } else if (price < 0) {
                jdUpdate.getTxtPrice().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdUpdate.getLblPrice().setForeground(new Color(240, 71, 71));
                jdUpdate.getLblPriceError().setText("Giá phải là số lớn hơn hoặc bằng 0");
                validate = false;
                jdUpdate.getLblPriceError().setVisible(true);
            }

            if (products.stream().anyMatch(el -> el.getName().equalsIgnoreCase(name) && el.getId() != this.product.getId())) {
                jdUpdate.getTxtName().setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                jdUpdate.getLblName().setForeground(new Color(240, 71, 71));
                jdUpdate.getLblNameError().setText("Tên đã được sử dụng");
                validate = false;
                jdUpdate.getLblNameError().setVisible(true);
            }
            if (validate) {
                try {
                    Product product = new Product();
                    product.setId(this.product.getId());
                    product.setName(name);
                    product.setPrice(price);
                    product.setStatus(status);
                    product.setCategoryId(categoryId);

                    Map<String, Object> result = productDAO.update(product);
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
                Map<String, Object> result = productDAO.delete(product.getId());
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
            Product product = new Product();
            String name = jdSearch.getTxtName().getText().trim();
            String fromPrice = jdSearch.getTxtFromPrice().getText().trim();
            String toPrice = jdSearch.getTxtToPrice().getText().trim();
            String statusString = jdSearch.getCboStatus().getSelectedItem().toString();

            if (!Common.isNullOrEmpty(name)) {
                product.setName(name);
            }

            if (!Common.isNullOrEmpty(fromPrice)) {
                product.setFromPrice(Float.parseFloat(fromPrice));
            }

            if (!Common.isNullOrEmpty(toPrice)) {
                product.setToPrice(Float.parseFloat(toPrice));
            }

            if (!statusString.equals("Không chọn")) {
                product.setStatus(statusString.equals("Hoạt động"));
            }
            if (((Category) jdSearch.getCboCategory().getSelectedItem()).getId() != 0) {
                product.setCategoryId(((Category) jdSearch.getCboCategory().getSelectedItem()).getId());
            }

            System.out.println(product.toString());
            try {
                products = productDAO.getWithCondition(product);
                panel.getTblProduct().removeAll();

                String[] cols = {"Id", "Tên", "Giá tiền", "Trạng thái", "Danh mục"};
                DefaultTableModel dtm = new DefaultTableModel(cols, 0);
                if (!Common.isNullOrEmpty(products)) {
                    products.forEach(obj -> {
                        dtm.addRow(new Object[]{obj.getId(), obj.getName(), obj.getPrice(), obj.getStatus() ? "Hoạt động" :
                                "Không hoạt động", obj.getCategoryName()});
                    });

                    panel.getTblProduct().getSelectionModel().addListSelectionListener(el -> {
                        int position = panel.getTblProduct().getSelectedRow();
                        if (position >= 0) {
                            this.product = products.get(position);
                        }
                    });

                    panel.getTblProduct().changeSelection(0, 0, false, false);
                }

                panel.getTblProduct().setModel(dtm);
                jdSearch.dispose();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void updateData() {
        try {
            products = productDAO.getAll();
            panel.getTblProduct().removeAll();

            String[] cols = {"Id", "Tên", "Giá tiền", "Trạng thái", "Danh mục"};
            DefaultTableModel dtm = new DefaultTableModel(cols, 0);
            if (!Common.isNullOrEmpty(products)) {
                products.forEach(obj -> {
                    dtm.addRow(new Object[]{obj.getId(), obj.getName(), obj.getPrice(), obj.getStatus() ? "Hoạt động" :
                            "Không hoạt động", obj.getCategoryName()});
                });

                panel.getTblProduct().getSelectionModel().addListSelectionListener(e -> {
                    int position = panel.getTblProduct().getSelectedRow();
                    if (position >= 0) {
                        product = products.get(position);
                    }
                });

                panel.getTblProduct().changeSelection(0, 0, false, false);
            }

            panel.getTblProduct().setModel(dtm);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void addEvent() {
        panel.getLblAddProduct().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    JDActionProduct dialog = new JDActionProduct(view, false, null, categoryDAO.getAll());
                    dialog.setVisible(true);
                    jdAdd = dialog;
                    actionAdd();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        panel.getLblUpdateProduct().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    JDActionProduct dialog = new JDActionProduct(view, false, product, categoryDAO.getAll());
                    dialog.setVisible(true);
                    jdUpdate = dialog;
                    actionEdit();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        panel.getLblDeleteProduct().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JDDeleteProduct dialog = new JDDeleteProduct(view, false, product.getName());
                dialog.setVisible(true);
                jdDelete = dialog;
                actionDelete();
            }
        });

        panel.getLblSearchProduct().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    JDSearchProduct dialog = new JDSearchProduct(view, false, categoryDAO.getAll());
                    dialog.setVisible(true);
                    jdSearch = dialog;
                    actionSearch();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        panel.getLblRefreshProduct().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateData();
            }
        });
    }
}
