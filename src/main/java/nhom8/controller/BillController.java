package nhom8.controller;

import lombok.Setter;
import nhom8.dao.BillDAO;
import nhom8.dao.BillDetailDAO;
import nhom8.dao.ProductDAO;
import nhom8.model.Bill;
import nhom8.model.BillDetail;
import nhom8.model.Product;
import nhom8.utils.Common;
import nhom8.view.bill.*;
import nhom8.view.billdetail.JDDeleteBillDetail;
import nhom8.view.billdetail.JDEditBillDetail;
import nhom8.view.home.Dashboard;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class BillController implements ManagerController {
    private final BillDAO billDAO = new BillDAO();
    private final BillDetailDAO billDetailDAO = new BillDetailDAO();
    private final Dashboard view;

    @Setter
    private PnlBill panel;

    private JDAddBill jdAdd;
    private JDBill jdDetail;
    private JDDeleteBill jdDelete;
    private JDSearchBill jdSearchBill;

    private ArrayList<Bill> bills;
    private ArrayList<BillDetail> billDetails;
    private Bill bill;
    private Product product;
    private BillDetail billDetail;

    public BillController(Dashboard view, PnlBill panel) {
        this.view = view;
        this.panel = panel;
        addEvent();
    }

    @Override
    public void actionAdd() {
        try {
            Bill bill = new Bill();
            bill.setStatus(false);
            Map<String, Object> result = billDAO.create(bill);
            if ((boolean) result.get("status")) {
                this.bill = billDAO.getNewBill();
                jdAdd.getTxtBillId().setText(this.bill.getId().toString());
                jdAdd.getTxtBillTime().setText(this.bill.getCreatedAt());

                //After create bill successful
                updateProductTable();
                jdAddEvent();

                jdAdd.getBtnAddProduct().addActionListener(ev -> {
                    final JTextField txtProductAmount = jdAdd.getTxtProductAmount();
                    final JLabel lblProductAmount = jdAdd.getLblProductAmount();
                    final JLabel lblProductAmountError = jdAdd.getLblProductAmountError();
                    BillDetail billDetail = new BillDetail();
                    String amountString = txtProductAmount.getText();
                    if (Common.isNullOrEmpty(amountString)) {
                        txtProductAmount.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(240, 71, 71)),
                                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                        lblProductAmount.setForeground(new Color(240, 71, 71));
                        lblProductAmountError.setVisible(true);
                        lblProductAmountError.setText("So luong khong duoc de trong");
                    } else if (Integer.parseInt(txtProductAmount.getText()) <= 0) {
                        txtProductAmount.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(240, 71, 71)),
                                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                        lblProductAmount.setForeground(new Color(240, 71, 71));
                        lblProductAmountError.setVisible(true);
                        lblProductAmountError.setText("So luong phai la so duong");
                    } else {
                        billDetail.setAmount(Integer.parseInt(amountString));
                        if (!Common.isNullOrEmpty(product)) {
                            billDetail.setBillId(this.bill.getId());
                            billDetail.setProductId(product.getId());
                            Map<String, Object> resultBillDetail = null;
                            try {
                                resultBillDetail = billDetailDAO.create(billDetail);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            if ((boolean) resultBillDetail.get("status")) {
                                updateBillDetailTable(this.bill.getId());
                                product = null;
                                txtProductAmount.setBorder(BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(new Color(34, 36, 40)),
                                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                                lblProductAmount.setForeground(new Color(0, 0, 0));
                                lblProductAmountError.setVisible(false);
                                jdAdd.getTxtProductName().setText("");
                                jdAdd.getTxtProductAmount().setText("");
                                jdAdd.getTxtProductPrice().setText("");
                                jdAdd.getTxtProductAmount().setEditable(false);
                            }
                        } else {
                            JOptionPane.showMessageDialog(jdAdd, "Chua chon san pham");
                        }
                    }
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void actionEdit() {

    }

    @Override
    public void actionDelete() {
        jdDelete.getBtnDelete().addActionListener(e -> {
            try {
                Map<String, Object> result = billDAO.delete(bill.getId());
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

    }

    @Override
    public void updateData() {
        try {
            bills = billDAO.getAll();
            panel.getTblBill().removeAll();

            String[] cols = {"Id", "Trang thai", "Tong tien", "Ngay tao", "Ngay cap nhat"};
            DefaultTableModel dtm = new DefaultTableModel(cols, 0);
            if (!Common.isNullOrEmpty(bills)) {
                bills.forEach(obj -> {
                    dtm.addRow(new Object[]{obj.getId(), obj.getStatus() ? "Da thanh toan" : "Chua thanh toan", Common.isNullOrEmpty(obj.getTotal()) ? 0 : obj.getTotal(), obj.getCreatedAt(),
                            obj.getUpdatedAt()});
                });

                panel.getTblBill().getSelectionModel().addListSelectionListener(e -> {
                    int position = panel.getTblBill().getSelectedRow();
                    if (position >= 0) {
                        bill = bills.get(position);
                    }
                });

                panel.getTblBill().changeSelection(0, 0, false, false);
            }

            panel.getTblBill().setModel(dtm);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void addEvent() {
        panel.getLblAddBill().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jdAdd = new JDAddBill(view, false);
                jdAdd.setVisible(true);
                actionAdd();
            }
        });

        panel.getLblDeleteBill().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jdDelete = new JDDeleteBill(view, false, bill.getId());
                jdDelete.setVisible(true);
                actionDelete();
            }
        });

        panel.getLblRefreshBill().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateData();
            }
        });
    }

    private void updateProductTable() {
        try {
            final JTable tblProduct = jdAdd.getTblProduct();
            tblProduct.removeAll();
            jdAdd.getLblProductAmountError().setVisible(false);
            ProductDAO productDAO = new ProductDAO();
            ArrayList<Product> products = productDAO.getAll();

            String columns[] = {"Id", "Tên sản phẩm", "Giá", "Tên danh mục"};
            DefaultTableModel dtm = new DefaultTableModel(columns, 0);

            if (!Common.isNullOrEmpty(products)) {
                products.forEach(pro -> {
                    dtm.addRow(new Object[]{pro.getId(), pro.getName(), pro.getPrice(), pro.getCategoryName()});
                });

                tblProduct.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
                    int position = tblProduct.getSelectedRow();
                    if (position >= 0) {
                        product = products.get(position);

                        jdAdd.getTxtProductName().setText(String.valueOf(product.getName()));
                        jdAdd.getTxtProductPrice().setText(String.valueOf(product.getPrice()));
                        jdAdd.getTxtProductAmount().setEditable(true);
                        jdAdd.getTxtProductAmount().setText("");
                    }
                });

                tblProduct.changeSelection(0, 0, false, false);

            }

            tblProduct.setModel(dtm);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void updateBillDetailTable(Integer billId) {
        try {
            final JTable tblBillDetail = jdAdd.getTblBillDetail();
            tblBillDetail.removeAll();
            jdAdd.getLblProductAmountError().setVisible(false);
            billDetails = billDetailDAO.getAll(billId);

            String columns[] = {"Id", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"};
            DefaultTableModel dtm = new DefaultTableModel(columns, 0);
            Float total_bill = 0f;
            if (!Common.isNullOrEmpty(billDetails)) {
                for (BillDetail obj : billDetails) {
                    Float total = obj.getPrice() * obj.getAmount();
                    total_bill += total;
                    dtm.addRow(new Object[]{obj.getProductId(), obj.getProductName(), obj.getPrice(),
                            obj.getAmount(), total});
                }
                jdAdd.getTxtTotalPrice().setText(total_bill.toString());

                tblBillDetail.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
                    int position = tblBillDetail.getSelectedRow();
                    if (position >= 0) {
                        billDetail = billDetails.get(position);
                    }
                });

                tblBillDetail.changeSelection(0, 0, false, false);
            }

            tblBillDetail.setModel(dtm);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void jdAddEvent() {
        jdAdd.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    if (!bill.getStatus())
                        billDAO.delete(bill.getId());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        jdAdd.getBtnCheckout().addActionListener(ev -> {
            try {
                bill.setStatus(true);
                Map<String, Object> resultCheckOut = billDAO.update(bill);
                if ((boolean) resultCheckOut.get("status")) {
                    JOptionPane.showMessageDialog(jdAdd, "Thanh toan thanh cong");
                    updateData();
                    jdAdd.dispose();
                } else {
                    JOptionPane.showMessageDialog(jdAdd, resultCheckOut.get("message"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        jdAdd.getMenuItemDelete().addActionListener(ev -> {
            if (!Common.isNullOrEmpty(billDetail)) {
                JDDeleteBillDetail jdDeleteBillDetail = new JDDeleteBillDetail(jdAdd, false, billDetail.getProductName());
                jdDeleteBillDetail.setVisible(true);
                actionDeleteBillDetail(jdDeleteBillDetail);
            }
        });
        jdAdd.getMenuItemEdit().addActionListener(ev -> {
            if (!Common.isNullOrEmpty(billDetail)) {
                JDEditBillDetail jdEditBillDetail = new JDEditBillDetail(jdAdd, false, billDetail);
                jdEditBillDetail.setVisible(true);
                actionUpdateBillDetail(jdEditBillDetail);
            }
        });
    }

    private void actionDeleteBillDetail(JDDeleteBillDetail jdDeleteBillDetail) {
        jdDeleteBillDetail.getBtnDelete().addActionListener(ev -> {
            try {
                Map<String, Object> result = billDetailDAO.delete(billDetail.getBillId(), billDetail.getProductId());
                if ((Boolean) result.get("status")) {
                    JOptionPane.showMessageDialog(jdDeleteBillDetail, result.get("message"));
                    System.out.println(result.get("message"));
                    updateData();
                    jdDeleteBillDetail.dispose();
                } else {
                    JOptionPane.showMessageDialog(jdDelete, result.get("message"));
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void actionUpdateBillDetail(JDEditBillDetail jdEditBillDetail) {
        jdEditBillDetail.getBtnModify().addActionListener(ev -> {
            final JTextField txtProductAmount = jdEditBillDetail.getTxtProductAmount();
            final JLabel lblProductAmount = jdEditBillDetail.getLblProductAmount();
            final JLabel lblProductAmountError = jdEditBillDetail.getLblProductAmountError();
            String amountString = txtProductAmount.getText();
            if (Common.isNullOrEmpty(amountString)) {
                txtProductAmount.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblProductAmount.setForeground(new Color(240, 71, 71));
                lblProductAmountError.setVisible(true);
                lblProductAmountError.setText("So luong khong duoc de trong");
            } else if (Integer.parseInt(txtProductAmount.getText()) <= 0) {
                txtProductAmount.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblProductAmount.setForeground(new Color(240, 71, 71));
                lblProductAmountError.setVisible(true);
                lblProductAmountError.setText("So luong phai la so duong");
            } else {
                billDetail.setAmount(Integer.parseInt(amountString));
                try {
                    Map<String, Object> result = billDetailDAO.update(billDetail);
                    if ((Boolean) result.get("status")) {
                        JOptionPane.showMessageDialog(jdEditBillDetail, result.get("message"));
                        updateData();
                        jdEditBillDetail.dispose();
                    } else {
                        JOptionPane.showMessageDialog(jdEditBillDetail, result.get("message"));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
