/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package nhom8.view.bill;

import javax.swing.*;

/**
 * @author dell
 */
public class PnlBill extends javax.swing.JPanel {

    /**
     * Creates new form NewJPanel
     */
    public PnlBill() {
        initComponents();
    }

    public JLabel getLblExport() {
        return lblExport;
    }

    public JLabel getLblAddBill() {
        return lblAddBill;
    }

    public void setLblAddBill(JLabel lblAddBill) {
        this.lblAddBill = lblAddBill;
    }


    public JLabel getLblDeleteBill() {
        return lblDeleteBill;
    }

    public void setLblDeleteBill(JLabel lblDeleteBill) {
        this.lblDeleteBill = lblDeleteBill;
    }

    public JLabel getLblDetailBill() {
        return lblDetailBill;
    }

    public void setLblDetailBill(JLabel lblDetailBill) {
        this.lblDetailBill = lblDetailBill;
    }

    public JLabel getLblRefreshBill() {
        return lblRefreshBill;
    }

    public void setLblRefreshBill(JLabel lblRefreshBill) {
        this.lblRefreshBill = lblRefreshBill;
    }

    public JLabel getLblSearchBill() {
        return lblSearchBill;
    }

    public void setLblSearchBill(JLabel lblSearchBill) {
        this.lblSearchBill = lblSearchBill;
    }

    public JTable getTblBill() {
        return tblBill;
    }

    public void setTblBill(JTable tblBill) {
        this.tblBill = tblBill;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblAddBill = new javax.swing.JLabel();
        lblDetailBill = new javax.swing.JLabel();
        lblDeleteBill = new javax.swing.JLabel();
        lblSearchBill = new javax.swing.JLabel();
        lblRefreshBill = new javax.swing.JLabel();
        lblExport = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBill = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.CardLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(536, 100));
        jPanel2.setPreferredSize(new java.awt.Dimension(152, 100));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 20));

        lblAddBill.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAddBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/add.png"))); // NOI18N
        lblAddBill.setText("Thêm");
        lblAddBill.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblAddBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblAddBill.setPreferredSize(new java.awt.Dimension(68, 60));
        lblAddBill.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(lblAddBill);

        lblDetailBill.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDetailBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/website.png"))); // NOI18N
        lblDetailBill.setText("Chi tiết");
        lblDetailBill.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblDetailBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblDetailBill.setPreferredSize(new java.awt.Dimension(68, 60));
        lblDetailBill.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(lblDetailBill);

        lblDeleteBill.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDeleteBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/delete.png"))); // NOI18N
        lblDeleteBill.setText("Xóa");
        lblDeleteBill.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblDeleteBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblDeleteBill.setPreferredSize(new java.awt.Dimension(68, 60));
        lblDeleteBill.setVerifyInputWhenFocusTarget(false);
        lblDeleteBill.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(lblDeleteBill);

        lblSearchBill.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSearchBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/loupe.png"))); // NOI18N
        lblSearchBill.setText("Tìm kiếm");
        lblSearchBill.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSearchBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSearchBill.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(lblSearchBill);

        lblRefreshBill.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRefreshBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/refresh.png"))); // NOI18N
        lblRefreshBill.setText("Làm mới");
        lblRefreshBill.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRefreshBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblRefreshBill.setPreferredSize(new java.awt.Dimension(68, 60));
        lblRefreshBill.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(lblRefreshBill);

        lblExport.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/excel.png"))); // NOI18N
        lblExport.setText("Xuất Excel");
        lblExport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblExport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(lblExport);

        jPanel1.add(jPanel2, "card2");

        add(jPanel1, java.awt.BorderLayout.NORTH);

        tblBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblBill);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAddBill;
    private javax.swing.JLabel lblDeleteBill;
    private javax.swing.JLabel lblDetailBill;
    private javax.swing.JLabel lblExport;
    private javax.swing.JLabel lblRefreshBill;
    private javax.swing.JLabel lblSearchBill;
    private javax.swing.JTable tblBill;
    // End of variables declaration//GEN-END:variables
}
