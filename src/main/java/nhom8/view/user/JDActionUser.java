/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nhom8.view.user;


import javax.swing.*;
import java.awt.*;
import java.util.Map;
import nhom8.model.User;
import nhom8.utils.Common;

/**
 * @author Minh
 */

public final class JDActionUser extends javax.swing.JDialog {

    private User user;

    /**
     * Creates new form JDCategoryCreate
     *
     * @param parent
     * @param modal
     * @param user
     */
    public JDActionUser(java.awt.Frame parent, boolean modal, User user) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        loadingRoles();
        
        if (!Common.isNullOrEmpty(user)) {
            lblTitle.setText("SỬA ĐỔI NGƯỜI DÙNG");
            btnSubmit.setText("Sửa đổi");
            this.user = user;
            loadingData();
        }

        // Custom Style
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                txtEmail.getBorder(),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        txtName.setBorder(BorderFactory.createCompoundBorder(
                txtName.getBorder(),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                txtPassword.getBorder(),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        lblNameError.setVisible(false);
        lblEmailError.setVisible(false);
        lblPasswordError.setVisible(false);
    }

    public JButton getBtnSubmit() {
        return btnSubmit;
    }

    public JComboBox getCboPermission() {
        return cboPermission;
    }

    public JLabel getLblEmail() {
        return lblEmail;
    }

    public JLabel getLblEmailError() {
        return lblEmailError;
    }

    public JLabel getLblName() {
        return lblName;
    }

    public JLabel getLblNameError() {
        return lblNameError;
    }

    public JLabel getLblPassword() {
        return lblPassword;
    }

    public JLabel getLblPasswordError() {
        return lblPasswordError;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JTextField getTxtName() {
        return txtName;
    }

    public JTextField getTxtPassword() {
        return txtPassword;
    }

    public void loadingData() {
        txtName.setText(this.user.getName());
        txtPassword.setText(this.user.getPassword());
        txtEmail.setText(this.user.getEmail());
        cboPermission.setSelectedItem(this.user.getRole() == 1 ? "Super Admin" : "Nhân viên");
    }

    public void loadingRoles() {
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        String permissions[] = {"Super Admin", "Nhân viên"};
        for (String permission : permissions) {
            dcbm.addElement(permission);
        }

        cboPermission.setModel(dcbm);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnSubmit = new javax.swing.JButton();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblPermission = new javax.swing.JLabel();
        cboPermission = new javax.swing.JComboBox();
        lblNameError = new javax.swing.JLabel();
        lblEmailError = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        lblPasswordError = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CẬP NHẬT NGƯỜI DÙNG");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI Semibold", 0, 36)); // NOI18N
        lblTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/add.png"))); // NOI18N
        lblTitle.setText("THÊM MỚI NGƯỜI DÙNG");
        jPanel1.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 547, 60));

        lblName.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblName.setText("Tên người dùng");
        jPanel1.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 89, 547, 30));
        jPanel1.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 547, 50));

        btnSubmit.setBackground(new java.awt.Color(0, 204, 106));
        btnSubmit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btnSubmit.setText("Thêm mới");
        btnSubmit.setBorderPainted(false);
        btnSubmit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmitActionPerformed(evt);
            }
        });
        jPanel1.add(btnSubmit, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 490, 120, 35));

        lblEmail.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblEmail.setText("Email");
        jPanel1.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 547, 30));
        jPanel1.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 547, 50));

        lblPermission.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblPermission.setText("Quyền");
        jPanel1.add(lblPermission, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 547, 30));

        jPanel1.add(cboPermission, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 547, 40));

        lblNameError.setFont(new java.awt.Font("Segoe UI Semibold", 0, 11)); // NOI18N
        lblNameError.setForeground(new java.awt.Color(240, 71, 71));
        lblNameError.setText("Không được để trống");
        jPanel1.add(lblNameError, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 547, -1));

        lblEmailError.setFont(new java.awt.Font("Segoe UI Semibold", 0, 11)); // NOI18N
        lblEmailError.setForeground(new java.awt.Color(240, 71, 71));
        lblEmailError.setText("Không được để trống");
        jPanel1.add(lblEmailError, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 547, -1));

        lblPassword.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblPassword.setText("Mật khẩu");
        jPanel1.add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 547, 30));
        jPanel1.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 547, 50));

        lblPasswordError.setFont(new java.awt.Font("Segoe UI Semibold", 0, 11)); // NOI18N
        lblPasswordError.setForeground(new java.awt.Color(240, 71, 71));
        lblPasswordError.setText("Không được để trổng");
        jPanel1.add(lblPasswordError, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 547, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitActionPerformed
        
    }//GEN-LAST:event_btnSubmitActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSubmit;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cboPermission;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblEmailError;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblNameError;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPasswordError;
    private javax.swing.JLabel lblPermission;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPassword;
    // End of variables declaration//GEN-END:variables
}
