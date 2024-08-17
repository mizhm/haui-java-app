package nhom8.controller;

import nhom8.dao.UserDAO;
import nhom8.model.User;
import nhom8.utils.Common;
import nhom8.utils.DataSource;
import nhom8.view.home.Dashboard;
import nhom8.view.home.JDLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class LoginController {
    private final UserDAO userDAO = new UserDAO();

    private final JDLogin jdLogin;
    private final Dashboard view;


    public LoginController(Dashboard view, JDLogin jdLogin) {
        this.jdLogin = jdLogin;
        this.view = view;
        addEvent();
    }

    private void addEvent() {
        jdLogin.addWindowListener(new WindowAdapter() {
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
        jdLogin.getBtnLogin().addActionListener(e -> {
            JTextField txtEmail = jdLogin.getTxtEmail();
            JPasswordField txtPassword = jdLogin.getTxtPassword();
            JLabel lblEmailError = jdLogin.getLblEmailError();
            JLabel lblPasswordError = jdLogin.getLblPasswordError();
            JLabel lblEmail = jdLogin.getLblEmail();
            JLabel lblPassword = jdLogin.getLblPassword();
            String email = txtEmail.getText().trim();
            String password = String.valueOf(txtPassword.getPassword()).trim();
            boolean validate = true;

            // Reset to default
            lblEmailError.setVisible(false);
            lblPasswordError.setVisible(false);
            txtEmail.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(34, 36, 40)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)));
            txtPassword.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(34, 36, 40)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)));
            lblEmail.setForeground(new Color(220, 221, 222));
            lblPassword.setForeground(new Color(220, 221, 222));

            if (email.equals("")) {
                txtEmail.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblEmail.setForeground(new Color(240, 71, 71));
                lblEmailError.setText("Email không được để trống");
                lblEmailError.setVisible(true);
                validate = false;
            } else {
                String email_regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                if (!email.matches(email_regex)) {
                    txtEmail.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(240, 71, 71)),
                            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                    lblEmail.setForeground(new Color(240, 71, 71));
                    lblEmailError.setText("Email không đúng định dạng");
                    lblEmailError.setVisible(true);
                    validate = false;
                }
            }

            if (password.equals("")) {
                txtPassword.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(240, 71, 71)),
                        BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                lblPassword.setForeground(new Color(240, 71, 71));
                lblPasswordError.setText("Mật khẩu không được để trống");
                lblPasswordError.setVisible(true);
                validate = false;
            }

            if (validate) {
                User obj = userDAO.auth(email, password);

                if (!Common.isNullOrEmpty(obj)) {
                    view.setUser(obj);
                    jdLogin.setVisible(false);
                    view.setVisible(true);
                    view.getLblEmail().setText(obj.getEmail());
                    view.getLblName().setText(obj.getName());
                    if (obj.getRole() != 1) {
                        view.getPnlUserMenu().setVisible(false);
                    } else {
                        view.getPnlUserMenu().setVisible(true);
                    }
                    view.getBtnDashboard().doClick();
                    txtPassword.setText("");
                    txtEmail.setText("");
                } else {
                    txtPassword.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(240, 71, 71)),
                            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
                    txtPassword.setText("");
                    lblPassword.setForeground(new Color(240, 71, 71));
                    lblPasswordError.setText("Email hoặc mật khẩu đăng nhập không chính xác.");
                    lblPasswordError.setVisible(true);
                }
            }
        });
    }
}
