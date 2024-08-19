package nhom8.controller;

import lombok.Setter;
import nhom8.dao.BillDAO;
import nhom8.dao.BillDetailDAO;
import nhom8.dao.ProductDAO;
import nhom8.model.Bill;
import nhom8.model.BillDetail;
import nhom8.model.Product;
import nhom8.utils.Common;
import nhom8.utils.Excel;
import nhom8.view.bill.*;
import nhom8.view.billdetail.JDDeleteBillDetail;
import nhom8.view.billdetail.JDEditBillDetail;
import nhom8.view.home.Dashboard;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private JDSearchBill jdSearch;

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
            bill.setUserId(view.getUser().getId());
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
                        lblProductAmountError.setText("Số lượng không được để trống");
                    } else if (Integer.parseInt(txtProductAmount.getText()) <= 0) {
                        txtProductAmount.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(240, 71, 71)),
                                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                        lblProductAmount.setForeground(new Color(240, 71, 71));
                        lblProductAmountError.setVisible(true);
                        lblProductAmountError.setText("Số lượng phải là số dương");
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
                            JOptionPane.showMessageDialog(jdAdd, "Chưa chọn sản phẩm");
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
            if (bill.getUserId().equals(view.getUser().getId()) || view.getUser().getRole() == 1) {
                try {
                    Map<String, Object> result = billDAO.delete(bill.getId());
                    if ((Boolean) result.get("status")) {
                        JOptionPane.showMessageDialog(jdDelete, result.get("message"));
                        System.out.println(result.get("message"));
                        updateData();
                    } else {
                        JOptionPane.showMessageDialog(jdDelete, result.get("message"));
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(jdDelete, "Bạn chỉ được phép xóa đơn bạn tạo");
            }
            jdDelete.dispose();
        });
    }

    @Override
    public void actionSearch() {
        jdSearch.getBtnSearch().addActionListener(e -> {
            Bill bill = new Bill();
            String id = jdSearch.getTxtBillId().getText().trim();
            String date = jdSearch.getTxtCreatedDate().getText().trim();
            Boolean validate = true;

            if (!Common.isNullOrEmpty(id)) {
                bill.setId(Integer.parseInt(id));
            }

            if (!Common.isNullOrEmpty(date)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    bill.setSearchDate(new Date(sdf.parse(date).getTime()));
                } catch (ParseException ex) {
                    validate = false;
                    jdSearch.getLblCreatedDateError().setVisible(true);
                }
            }

            if (validate) {
                try {
                    bills = billDAO.getWithCondition(bill);
                    panel.getTblBill().removeAll();
                    String[] cols = {"Id", "Người tạo", "Trạng thái", "Tổng tiền", "Ngày tạo", "Ngày cập nhật"};
                    DefaultTableModel dtm = new DefaultTableModel(cols, 0);
                    if (!Common.isNullOrEmpty(bills)) {
                        bills.forEach(obj -> {
                            dtm.addRow(new Object[]{obj.getId(), obj.getUserName(), obj.getStatus() ? "Đã thanh toán" : "Chưa thanh toán", Common.isNullOrEmpty(obj.getTotal()) ? 0 : obj.getTotal(), obj.getCreatedAt(),
                                    obj.getUpdatedAt()});
                        });

                        panel.getTblBill().getSelectionModel().addListSelectionListener(ev -> {
                            int position = panel.getTblBill().getSelectedRow();
                            if (position >= 0) {
                                this.bill = bills.get(position);
                            }
                        });

                        panel.getTblBill().changeSelection(0, 0, false, false);
                    }

                    panel.getTblBill().setModel(dtm);
                    jdSearch.dispose();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void actionExport() {
        if (!Common.isNullOrEmpty(bills)) {
            try {
                Excel excel = new Excel();
                String table = "bill";
                String excelFilePath = excel.getFileName(table.concat("_Export"));

                JFileChooser excelFileChooser = new JFileChooser(".");
                excelFileChooser.setDialogTitle("Lưu file ...");
                excelFileChooser.setSelectedFile(new File(excelFilePath));
                Action details = excelFileChooser.getActionMap().get("viewTypeDetails");
                details.actionPerformed(null);
                // Kiểu định dạng file xuất
                FileNameExtensionFilter fnef = new FileNameExtensionFilter("Files", "xls", "xlsx", "xlsm");
                //Setting extension for selected file names
                excelFileChooser.setFileFilter(fnef);

                int chooser = excelFileChooser.showSaveDialog(null);

                if (chooser == JFileChooser.APPROVE_OPTION) {
                    String sql = "select b.id N'Mã đơn hàng', IIF(b.status=1, N'Đã thanh toán', N'Chưa thanh toán') N'Trạng thái', b.created_at N'Ngày tạo', b.updated_at N'Ngày sửa đổi', sum(bd.price * bd.amount) N'Tổng tiền', u.name N'Người tạo'" +
                            " from bill b" +
                            " join [user] u on b.user_id = u.id" +
                            " left join bill_detail bd on b.id = bd.bill_id" +
                            " group by b.id, b.status, b.created_at, b.updated_at, b.user_id, u.name";
                    excel.export(sql, table, excelFileChooser.getSelectedFile().getPath());
                    JOptionPane.showMessageDialog(view, "Xuất hoá đơn thành công");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateData() {
        try {
            bills = billDAO.getAll();
            panel.getTblBill().removeAll();

            String[] cols = {"Id", "Người tạo", "Trạng thái", "Tổng tiền", "Ngày tạo", "Ngày cập nhật"};
            DefaultTableModel dtm = new DefaultTableModel(cols, 0);
            if (!Common.isNullOrEmpty(bills)) {
                bills.forEach(obj -> {
                    dtm.addRow(new Object[]{obj.getId(), obj.getUserName(), obj.getStatus() ? "Đã thanh toán" : "Chưa thanh toán", Common.isNullOrEmpty(obj.getTotal()) ? 0 : obj.getTotal(), obj.getCreatedAt(),
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
            System.out.println("Lỗi: " + e.getMessage());
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

        panel.getLblDetailBill().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    ArrayList<BillDetail> list = billDetailDAO.getAll(bill.getId());
                    jdDetail = new JDBill(view, false, bill, list);
                    jdDetail.setVisible(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
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

        panel.getLblSearchBill().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jdSearch = new JDSearchBill(view, false);
                jdSearch.setVisible(true);
                actionSearch();
            }
        });

        panel.getLblRefreshBill().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateData();
            }
        });

        panel.getLblExport().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                actionExport();
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
                jdAdd.getBtnCheckout().setVisible(true);
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
            } else {
                jdAdd.getBtnCheckout().setVisible(false);
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
                    JOptionPane.showMessageDialog(jdAdd, "Thanh toán thành công");
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
                    updateBillDetailTable(bill.getId());
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
                lblProductAmountError.setText("Số lượng không được để trống");
            } else if (Integer.parseInt(txtProductAmount.getText()) <= 0) {
                txtProductAmount.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblProductAmount.setForeground(new Color(240, 71, 71));
                lblProductAmountError.setVisible(true);
                lblProductAmountError.setText("Số lượng phải là số dương");
            } else {
                billDetail.setAmount(Integer.parseInt(amountString));
                try {
                    Map<String, Object> result = billDetailDAO.update(billDetail);
                    if ((Boolean) result.get("status")) {
                        JOptionPane.showMessageDialog(jdEditBillDetail, result.get("message"));
                        updateBillDetailTable(bill.getId());
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
