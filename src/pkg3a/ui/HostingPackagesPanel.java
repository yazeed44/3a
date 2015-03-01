/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.ui;

import java.util.ArrayList;
import pkg3a.utils.DBUtil;
import pkg3a.utils.DBUtil.QueryDbListener;
import pkg3a.utils.HostingPackage;

/**
 *
 * @author yazeed44
 */
public class HostingPackagesPanel extends javax.swing.JPanel implements HostingPackagesTable.HostingPackagesCallback {

    /**
     * Creates new form HostingPackagesPanel
     */
    
    private ArrayList<HostingPackage> mHostingPackages;
    private final Main mMainFrame;
    public HostingPackagesPanel(final Main mainFrame) {
        mMainFrame = mainFrame;
        DBUtil.loadHostingPackages(new QueryDbListener<ArrayList<HostingPackage>>(){

            @Override
            public void queriedSuccessfully(ArrayList<HostingPackage> result) {
                mHostingPackages = result;
                initComponents();
            }

            @Override
            public void failedToQuery(Throwable throwable) {
            }
        });
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        hostingPackagesTable = new HostingPackagesTable(mMainFrame,mHostingPackages,this);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(430, 318));

        hostingPackagesTable.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(hostingPackagesTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JTable hostingPackagesTable;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onUpdateHostingPackagesTable(HostingPackagesTable table) {
        DBUtil.loadHostingPackages(new QueryDbListener<ArrayList<HostingPackage>>(){

            @Override
            public void queriedSuccessfully(ArrayList<HostingPackage> result) {
                mHostingPackages = result;
                table.updateTable(mHostingPackages);
                
            }

            @Override
            public void failedToQuery(Throwable throwable) {
            }
        });
    }

    @Override
    public void onUpdateOrdersTable() {
        mMainFrame.updateOrdersTable();
    }

    @Override
    public boolean shouldAllowClick() {
        return true;
    }

    

    
}
