package nhom8.controller;

import lombok.Setter;
import nhom8.dao.UserDAO;
import nhom8.model.User;
import nhom8.utils.Common;
import nhom8.view.home.Dashboard;
import nhom8.view.user.JDActionUser;
import nhom8.view.user.JDDeleteUser;
import nhom8.view.user.JDSearchUser;
import nhom8.view.user.PnlUser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class UserController implements ManagerController {
    private final UserDAO userDAO = new UserDAO();
    private final Dashboard view;

    @Setter
    private PnlUser panel;

    private JDActionUser jdAdd;
    private JDActionUser jdUpdate;
    private JDDeleteUser jdDelete;
    private JDSearchUser jdSearch;

    private ArrayList<User> users;
    private User user;

    public UserController(Dashboard view, PnlUser panel) {
        this.view = view;
        this.panel = panel;
        addEvent();
    }

    @Override
    public void actionAdd() {
        jdAdd.getBtnSubmit().addActionListener(ev -> {
            JLabel lblName = jdAdd.getLblName();
            JLabel lblEmail = jdAdd.getLblEmail();
            JLabel lblPassword = jdAdd.getLblPassword();
            JLabel lblNameError = jdAdd.getLblNameError();
            JLabel lblEmailError = jdAdd.getLblEmailError();
            JLabel lblPasswordError = jdAdd.getLblPasswordError();

            JTextField txtName = jdAdd.getTxtName();
            JTextField txtEmail = jdAdd.getTxtEmail();
            JTextField txtPassword = jdAdd.getTxtPassword();
            JComboBox cboPermission = jdAdd.getCboPermission();

            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();
            int role = cboPermission.getSelectedItem() == "Super Admin" ? 1 : 0;
            boolean validate = true;
            String email_regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            if (name.isEmpty()) {
                txtName.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblName.setForeground(new Color(240, 71, 71));
                lblNameError.setVisible(true);
                validate = false;
            }

            if (password.isEmpty()) {
                txtPassword.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblPassword.setForeground(new Color(240, 71, 71));
                lblPasswordError.setVisible(true);
                validate = false;
            }

            if (txtEmail.getText().trim().isEmpty()) {
                txtEmail.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblEmail.setText("Không được để trống");
                lblEmail.setForeground(new Color(240, 71, 71));
                lblEmailError.setVisible(true);
                validate = false;
            } else if (!email.matches(email_regex)) {
                txtEmail.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblEmail.setText("Email bạn nhập không đúng định dạng!");
                lblEmail.setForeground(new Color(240, 71, 71));
                lblEmailError.setVisible(true);
                validate = false;
            }

            if (validate) {
                lblNameError.setVisible(false);
                lblEmailError.setVisible(false);
                try {
                    User newUser = new User();
                    newUser.setName(name);
                    newUser.setRole(role);
                    newUser.setEmail(email);
                    newUser.setPassword(password);

                    Map<String, Object> result = userDAO.create(newUser);

                    if ((boolean) result.get("status")) {
                        JOptionPane.showMessageDialog(jdAdd, result.get("message"));
                        updateData();
                        jdAdd.dispose();
                    } else {
                        JOptionPane.showMessageDialog(jdAdd, result.get("message"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void actionEdit() {
        jdUpdate.getBtnSubmit().addActionListener(ev -> {
            JLabel lblName = jdUpdate.getLblName();
            JLabel lblEmail = jdUpdate.getLblEmail();
            JLabel lblPassword = jdUpdate.getLblPassword();
            JLabel lblNameError = jdUpdate.getLblNameError();
            JLabel lblEmailError = jdUpdate.getLblEmailError();
            JLabel lblPasswordError = jdUpdate.getLblPasswordError();

            JTextField txtName = jdUpdate.getTxtName();
            JTextField txtEmail = jdUpdate.getTxtEmail();
            JTextField txtPassword = jdUpdate.getTxtPassword();
            JComboBox cboPermission = jdUpdate.getCboPermission();

            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();
            int role = cboPermission.getSelectedItem() == "Super Admin" ? 1 : 0;
            boolean validate = true;
            String email_regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            if (name.isEmpty()) {
                txtName.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblName.setForeground(new Color(240, 71, 71));
                lblNameError.setVisible(true);
                validate = false;
            }

            if (password.isEmpty()) {
                txtPassword.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblPassword.setForeground(new Color(240, 71, 71));
                lblPasswordError.setVisible(true);
                validate = false;
            }

            if (txtEmail.getText().trim().isEmpty()) {
                txtEmail.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblEmail.setText("Không được để trống");
                lblEmail.setForeground(new Color(240, 71, 71));
                lblEmailError.setVisible(true);
                validate = false;
            } else if (!email.matches(email_regex)) {
                txtEmail.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblEmail.setText("Email bạn nhập không đúng định dạng!");
                lblEmail.setForeground(new Color(240, 71, 71));
                lblEmailError.setVisible(true);
                validate = false;
            }

            if (validate) {
                lblNameError.setVisible(false);
                lblEmailError.setVisible(false);
                try {
                    User newUser = new User();
                    newUser.setName(name);
                    newUser.setRole(role);
                    newUser.setEmail(email);
                    newUser.setPassword(password);
                    newUser.setId(this.user.getId());

                    Map<String, Object> result = userDAO.update(newUser);

                    if ((boolean) result.get("status")) {
                        JOptionPane.showMessageDialog(jdUpdate, result.get("message"));
                        updateData();
                        jdUpdate.dispose();
                    } else {
                        JOptionPane.showMessageDialog(jdUpdate, result.get("message"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void actionDelete() {
        jdDelete.getBtnDelete().addActionListener(e -> {
            if (!user.getId().equals(view.getUser().getId())) {
                try {
                    Map<String, Object> result = userDAO.delete(user.getId());
                    if ((Boolean) result.get("status")) {
                        JOptionPane.showMessageDialog(jdDelete, result.get("message"));
                        System.out.println(result.get("message"));
                        updateData();
                        jdDelete.dispose();
                    } else {
                        JOptionPane.showMessageDialog(jdDelete, result.get("message"));
                        jdDelete.dispose();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(jdDelete, "Currently logging in, cannot delete");
                jdDelete.dispose();
            }
        });
    }

    @Override
    public void actionSearch() {
        jdSearch.getBtnSearch().addActionListener(ev -> {
            JLabel lblEmail = jdSearch.getLblEmail();
            JLabel lblEmailError = jdSearch.getLblEmailError();

            JTextField txtName = jdSearch.getTxtName();
            JTextField txtEmail = jdSearch.getTxtEmail();
            JComboBox cboPermission = jdSearch.getCboPermission();

            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            boolean validate = true;
            String email_regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            lblEmailError.setVisible(false);

            if (!Common.isNullOrEmpty(email)) {
                if (!email.matches(email_regex)) {
                    txtEmail.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(240, 71, 71)),
                            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                    lblEmail.setForeground(new Color(240, 71, 71));
                    lblEmailError.setText("Email bạn nhập không đúng định dạng!");
                    lblEmailError.setVisible(true);
                    validate = false;
                }
            }

            if (validate) {
                user = new User();

                if (!Common.isNullOrEmpty(name)) {
                    user.setName(name);
                }

                if (!Common.isNullOrEmpty(email)) {
                    user.setEmail(email);
                }

                if (!"Không chọn".equals(cboPermission.getSelectedItem().toString())) {
                    user.setRole("Super Admin".equals(cboPermission.getSelectedItem().toString()) ? 1 : 0);
                }
                try {
                    users = userDAO.getWithCondition(user);
                    panel.getTblUser().removeAll();

                    String[] cols = {"Id", "Ten", "Email", "Quyen"};
                    DefaultTableModel dtm = new DefaultTableModel(cols, 0);
                    if (!Common.isNullOrEmpty(users)) {
                        users.forEach(obj -> {
                            dtm.addRow(new Object[]{obj.getId(), obj.getName(), obj.getEmail(), obj.getRole() == 0 ? "Nhan vien" : "Quan tri vien"});
                        });

                        panel.getTblUser().getSelectionModel().addListSelectionListener(e -> {
                            int position = panel.getTblUser().getSelectedRow();
                            if (position >= 0) {
                                user = users.get(position);
                            }
                        });

                        panel.getTblUser().changeSelection(0, 0, false, false);
                    }

                    panel.getTblUser().setModel(dtm);
                    jdSearch.dispose();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void updateData() {
        try {
            users = userDAO.getAll();
            panel.getTblUser().removeAll();

            String[] cols = {"Id", "Ten", "Email", "Quyen"};
            DefaultTableModel dtm = new DefaultTableModel(cols, 0);
            if (!Common.isNullOrEmpty(users)) {
                users.forEach(obj -> {
                    dtm.addRow(new Object[]{obj.getId(), obj.getName(), obj.getEmail(), obj.getRole() == 0 ? "Nhan vien" : "Quan tri vien"});
                });

                panel.getTblUser().getSelectionModel().addListSelectionListener(e -> {
                    int position = panel.getTblUser().getSelectedRow();
                    if (position >= 0) {
                        user = users.get(position);
                    }
                });

                panel.getTblUser().changeSelection(0, 0, false, false);
            }

            panel.getTblUser().setModel(dtm);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void addEvent() {
        panel.getLblAddUser().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jdAdd = new JDActionUser(view, false, null);
                jdAdd.setVisible(true);
                actionAdd();
            }
        });
        panel.getLblUpdateUser().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jdUpdate = new JDActionUser(view, false, user);
                jdUpdate.setVisible(true);
                actionEdit();
            }
        });

        panel.getLblDeleteUser().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jdDelete = new JDDeleteUser(view, false, user.getName());
                jdDelete.setVisible(true);
                actionDelete();
            }
        });

        panel.getLblSearchUser().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jdSearch = new JDSearchUser(view, false);
                jdSearch.setVisible(true);
                actionSearch();
            }
        });
        panel.getLblRefreshUser().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateData();
            }
        });
    }
}
