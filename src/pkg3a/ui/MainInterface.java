/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.ui;

import java.awt.event.ActionEvent;

/**
 *
 * @author yazeed44
 */
public abstract class MainInterface extends javax.swing.JFrame {

    /**
     * Creates new form MainInterface
     */
    public MainInterface() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        tabs = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        addOrderMenuItem = new javax.swing.JMenuItem();
        findEndDomainDatesMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        addCustomerMenuItem = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        addHostingPackageMenuItem = new javax.swing.JMenuItem();

        jMenu4.setText("File");
        jMenuBar2.add(jMenu4);

        jMenu5.setText("Edit");
        jMenuBar2.add(jMenu5);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabs.setFont(new java.awt.Font("KacstOne", 1, 12)); // NOI18N

        jMenu1.setText("إدارة الطلبيات");
        jMenu1.setFont(new java.awt.Font("KacstOne", 1, 12)); // NOI18N

        addOrderMenuItem.setFont(new java.awt.Font("KacstOne", 1, 12)); // NOI18N
        addOrderMenuItem.setText("إضافة");
        addOrderMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOrderMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(addOrderMenuItem);

        findEndDomainDatesMenuItem.setFont(new java.awt.Font("KacstOne", 1, 12)); // NOI18N
        findEndDomainDatesMenuItem.setText("بحث عن تواريخ التجديد");
        findEndDomainDatesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findEndDomainDatesMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(findEndDomainDatesMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("إدارة العملاء");
        jMenu2.setFont(new java.awt.Font("KacstOne", 1, 12)); // NOI18N

        addCustomerMenuItem.setFont(new java.awt.Font("KacstOne", 1, 12)); // NOI18N
        addCustomerMenuItem.setText("إضافة");
        addCustomerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(addCustomerMenuItem);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("إدارة الباقات");
        jMenu3.setFont(new java.awt.Font("KacstOne", 1, 12)); // NOI18N

        addHostingPackageMenuItem.setFont(new java.awt.Font("KacstOne", 1, 12)); // NOI18N
        addHostingPackageMenuItem.setText("إضافة");
        addHostingPackageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addHostingPackageMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(addHostingPackageMenuItem);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addOrderMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOrderMenuItemActionPerformed
        // TODO add your handling code here:
        onClickAddOrder(evt);
    }//GEN-LAST:event_addOrderMenuItemActionPerformed

    private void addCustomerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerMenuItemActionPerformed
        // TODO add your handling code here:
        onClickAddCustomer(evt);
        
    }//GEN-LAST:event_addCustomerMenuItemActionPerformed

    private void addHostingPackageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addHostingPackageMenuItemActionPerformed
        // TODO add your handling code here:
        onClickAddHostingPackage(evt);
    }//GEN-LAST:event_addHostingPackageMenuItemActionPerformed

    private void findEndDomainDatesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findEndDomainDatesMenuItemActionPerformed
        // TODO add your handling code here:
        onClickFindEndingDomainDatesInRange(evt);
    }//GEN-LAST:event_findEndDomainDatesMenuItemActionPerformed

    
    abstract void onClickAddCustomer(ActionEvent evt);
    abstract void onClickAddOrder(ActionEvent evt);
    abstract void onClickAddHostingPackage(ActionEvent evt);
    abstract void onClickFindEndingDomainDatesInRange(ActionEvent evt);

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addCustomerMenuItem;
    private javax.swing.JMenuItem addHostingPackageMenuItem;
    private javax.swing.JMenuItem addOrderMenuItem;
    private javax.swing.JMenuItem findEndDomainDatesMenuItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    protected javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables
}
