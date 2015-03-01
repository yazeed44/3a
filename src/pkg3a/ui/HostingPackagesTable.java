/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.ui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import pkg3a.utils.DBUtil;
import pkg3a.utils.HostingPackage;
import pkg3a.utils.Order;
import pkg3a.utils.ViewUtil;

/**
 *
 * @author yazeed44
 */
 public class HostingPackagesTable extends BaseTable implements EditHostingPackageDialog.EditHostingPackageCallback {
    
    private  ArrayList<HostingPackage> mHostingPackages;
    private final HostingPackagesCallback mCallback;
    private final Main mMainFrame;
    
    public HostingPackagesTable(final Main mainFrame,final ArrayList<HostingPackage> hostingPackages 
             , final HostingPackagesCallback callback){
        super();
        mHostingPackages = hostingPackages;
        mCallback = callback;
        mMainFrame = mainFrame;
        setupModel();
        setupMouseListener();
    
    }
    
    private void setupModel(){
        setModel(new HostingPackageModel());
    }
    
    @Override
    protected void setupMouseListener(){
        
        if (!mCallback.shouldAllowClick()){
            return;
        }
        
        else {
        super.setupMouseListener();
        }
        
     }
            
            private boolean isDoubleClick(MouseEvent e){
         return e.getClickCount() >= 2;
     }
     
     private boolean isRightClick(MouseEvent e){
         return e.isPopupTrigger();
     }
     
     private void showEditHostingPackageDialog(final HostingPackage selectedHostingPackage){
       SwingUtilities.invokeLater(() -> {
           EditHostingPackageDialog.showEditHostingPackageDialog(mMainFrame, selectedHostingPackage, HostingPackagesTable.this);
       });
     }
     
        
            private void showPopupItems(MouseEvent e,final HostingPackage hostingPackage){
               JPopupMenu popup = createPopup(hostingPackage);
            popup.show(e.getComponent(), e.getX(), e.getY());     
        }
            
            
            private JPopupMenu createPopup(final HostingPackage hostingPackage){
                final JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.add(createDeleteItem(hostingPackage));
                popupMenu.addSeparator();
                popupMenu.add(createDetailsItem(hostingPackage));
                
                return popupMenu;
            }
            
         private JMenuItem createDeleteItem(final HostingPackage hostingPackage){
                final JMenuItem deleteItem = new JMenuItem("حذف");
                deleteItem.addActionListener((ActionEvent e) -> {
                    
                    DBUtil.loadOrdersFromHostingPackage(hostingPackage.id, new DBUtil.QueryDbListener<ArrayList<Order>>(){

                        @Override
                        public void queriedSuccessfully(final ArrayList<Order> ordersResult) {
                            
                            if (ordersResult.isEmpty()){
                                SwingUtilities.invokeLater(() -> {
                                    final String dialogMsg = "هل انت متأكد من حذف الباقة " + hostingPackage.name;
                                    
                                    final int result = JOptionPane.showConfirmDialog(HostingPackagesTable.this, dialogMsg, "تأكيد", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null);
                                    
                                    if (result == JOptionPane.YES_OPTION){
                                         DBUtil.deleteHostingPackage(hostingPackage.id, new HostingPackagesTable.OnDeleteOnlyHostingPackage());
                                    }
                                   
                                   
                                });
                                
                            }
                            
                            else {
                                SwingUtilities.invokeLater(() -> {
                                    final String dialogMsg = hostingPackage.name + "مرتبط بطلبيات , هل تريد حذفها مع الباقة ؟";
                                    
                                    final int result = JOptionPane.showConfirmDialog(HostingPackagesTable.this, dialogMsg, "تأكيد", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null);
                                    
                                    if (result == JOptionPane.YES_OPTION){
                                        //Delete all orders related to customer
                                        
                                        DBUtil.deleteOrdersRelatedToHostingPackages(hostingPackage.id,new DBUtil.DeleteDbListener() {
                                            
                                            @Override
                                            public void deletedSuccessfully() {
                                                mCallback.onUpdateOrdersTable();
                                                DBUtil.deleteHostingPackage(hostingPackage.id, new HostingPackagesTable.OnDeleteOnlyHostingPackage());
                                            }
                                            
                                            @Override
                                            public void failedToDelete(Throwable error) {
                                            }
                                        });
                                        
                                       
                                    }
                                });
                            }
                        }

                        @Override
                        public void failedToQuery(Throwable throwable) {
                        }
                    });
                });
                
                return deleteItem;
            }
         
           private JMenuItem createDetailsItem(final HostingPackage hostingPackage){
                final JMenuItem detailsItem = new JMenuItem("التفاصيل");
                detailsItem.addActionListener((ActionEvent e) -> {
                    showEditHostingPackageDialog(hostingPackage);
                });
                       return detailsItem;
    }

    @Override
    protected void onDoubleClickRow(int rowIndex) {
        showEditHostingPackageDialog(mHostingPackages.get(rowIndex));
    }

    @Override
    protected void onRightClickRow(int rowIndex, MouseEvent e) {
        showPopupItems(e,mHostingPackages.get(rowIndex));
    }
    
   
    
    void updateTable(final ArrayList<HostingPackage> hostingPackages){
        mHostingPackages = hostingPackages;
        setupModel();
    }

    @Override
    public void editedSuccessfully(EditHostingPackageDialog dialog) {
        mCallback.onUpdateHostingPackagesTable(this);
        ViewUtil.disposeOfDialog(dialog);
    }

    @Override
    public void failedToEdit(EditHostingPackageDialog dialog, Throwable error) {
        
    }

    private  class OnDeleteOnlyHostingPackage implements DBUtil.DeleteDbListener {

        @Override
        public void deletedSuccessfully() {
            mCallback.onUpdateHostingPackagesTable(HostingPackagesTable.this);
        }

        @Override
        public void failedToDelete(Throwable error) {
        }

        
    }
    
    
    
    private class HostingPackageModel extends AbstractTableModel{
        
        private final String mPackageNameCol = "الإسم";
        private final String mStorageSpaceCol = "المساحة التخزينية";
        private final String mPriceCol = "السعر";

        private final String[] mColumns = {mPackageNameCol,mStorageSpaceCol,mPriceCol};
        @Override
        public int getRowCount() {
            
            return mHostingPackages.size();
        }

        @Override
        public int getColumnCount() {
            return mColumns.length;
        }
        
        @Override
        public String getColumnName(int index){
            return mColumns[index];
        }

        @Override
        public String getValueAt(int rowIndex, int columnIndex) {
            final HostingPackage hostingPackage = mHostingPackages.get(rowIndex);
            
            switch(mColumns[columnIndex]){
            
                case mPackageNameCol:return hostingPackage.name;
                case mStorageSpaceCol : return hostingPackage.storageSpace;
                case mPriceCol:return hostingPackage.yearCost + "";
                
                default : return null;
            }
        }
    
    
    }
    
    public static interface HostingPackagesCallback {
        void onUpdateHostingPackagesTable(final HostingPackagesTable table);
        void onUpdateOrdersTable();
        boolean shouldAllowClick();
    }
    
}
