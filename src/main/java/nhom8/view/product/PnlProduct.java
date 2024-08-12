/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package nhom8.view.product;

import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author dell
 */
public class PnlProduct extends javax.swing.JPanel {

    /**
     * Creates new form NewJPanel
     */
    public PnlProduct() {
        initComponents();
    }

    public JLabel getLblAddProduct() {
        return lblAddProduct;
    }

    public void setLblAddProduct(JLabel lblAddProduct) {
        this.lblAddProduct = lblAddProduct;
    }

    public JLabel getLblDeleteProduct() {
        return lblDeleteProduct;
    }

    public void setLblDeleteProduct(JLabel lblDeleteProduct) {
        this.lblDeleteProduct = lblDeleteProduct;
    }

    public JLabel getLblRefreshProduct() {
        return lblRefreshProduct;
    }

    public void setLblRefreshProduct(JLabel lblRefreshProduct) {
        this.lblRefreshProduct = lblRefreshProduct;
    }

    public JLabel getLblSearchProduct() {
        return lblSearchProduct;
    }

    public void setLblSearchProduct(JLabel lblSearchProduct) {
        this.lblSearchProduct = lblSearchProduct;
    }

    public JLabel getLblUpdateProduct() {
        return lblUpdateProduct;
    }

    public void setLblUpdateProduct(JLabel lblUpdateProduct) {
        this.lblUpdateProduct = lblUpdateProduct;
    }

    public JTable getTblProduct() {
        return tblProduct;
    }

    public void setTblProduct(JTable tblProduct) {
        this.tblProduct = tblProduct;
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
        lblAddProduct = new javax.swing.JLabel();
        lblUpdateProduct = new javax.swing.JLabel();
        lblDeleteProduct = new javax.swing.JLabel();
        lblSearchProduct = new javax.swing.JLabel();
        lblRefreshProduct = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduct = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.CardLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(152, 100));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 20));

        lblAddProduct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/add.png"))); // NOI18N
        lblAddProduct.setText("them");
        lblAddProduct.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblAddProduct.setPreferredSize(new java.awt.Dimension(68, 60));
        lblAddProduct.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(lblAddProduct);

        lblUpdateProduct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUpdateProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/edit.png"))); // NOI18N
        lblUpdateProduct.setText("sua");
        lblUpdateProduct.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblUpdateProduct.setPreferredSize(new java.awt.Dimension(68, 60));
        lblUpdateProduct.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(lblUpdateProduct);

        lblDeleteProduct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDeleteProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/delete.png"))); // NOI18N
        lblDeleteProduct.setText("xoa");
        lblDeleteProduct.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblDeleteProduct.setPreferredSize(new java.awt.Dimension(68, 60));
        lblDeleteProduct.setVerifyInputWhenFocusTarget(false);
        lblDeleteProduct.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(lblDeleteProduct);

        lblSearchProduct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSearchProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/loupe.png"))); // NOI18N
        lblSearchProduct.setText("tim kiem");
        lblSearchProduct.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSearchProduct.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(lblSearchProduct);

        lblRefreshProduct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblRefreshProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/assets/img/refresh.png"))); // NOI18N
        lblRefreshProduct.setText("lam moi");
        lblRefreshProduct.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblRefreshProduct.setPreferredSize(new java.awt.Dimension(68, 60));
        lblRefreshProduct.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel2.add(lblRefreshProduct);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblProduct);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAddProduct;
    private javax.swing.JLabel lblDeleteProduct;
    private javax.swing.JLabel lblRefreshProduct;
    private javax.swing.JLabel lblSearchProduct;
    private javax.swing.JLabel lblUpdateProduct;
    private javax.swing.JTable tblProduct;
    // End of variables declaration//GEN-END:variables
}
