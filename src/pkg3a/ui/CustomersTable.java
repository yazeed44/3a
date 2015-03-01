/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import pkg3a.utils.Customer;
import pkg3a.utils.DBUtil;
import pkg3a.utils.DBUtil.DeleteDbListener;
import pkg3a.utils.DBUtil.QueryDbListener;
import pkg3a.utils.Order;


/**
 *
 * @author yazeed44
 */
public class CustomersTable extends BaseTable  implements EditCustomerDialog.EditCustomerCallback{
    private  ArrayList<Customer> mCustomers;
    private final CustomersTableCallback mCallback;
    private final Frame mFrame;
    public CustomersTable(final Frame frame,final ArrayList<Customer> customers,CustomersTableCallback callback){
        super();
        mCustomers = customers;
        mCallback = callback;
        mFrame = frame;
        setupTable();
        setupMouseListener();
    }
    
    private void setupTable(){
    setModel(new CustomersModel());
    }

    @Override
    protected void setupMouseListener() {
        
        if (mCallback.shouldAllowClicks()){
            super.setupMouseListener();
        }
        else {
            return ;
        }
        
    }
    
    
    
    
    void updateTable(final ArrayList<Customer> customers){
    mCustomers = customers;
    setupTable();
    }

   

    @Override
    public void editedSuccessfully(EditCustomerDialog dialog) {
        mCallback.onUpdateCustomerTable(this);
        dialog.setVisible(false);
    }

    @Override
    public void failedToEdit(EditCustomerDialog dialog, Throwable error) {
        
    }

    @Override
    protected void onDoubleClickRow(int rowIndex) {
        
                showEditCustomerDialog(mCustomers.get(rowIndex));
            
    }
    
    private void showEditCustomerDialog(final Customer customer){
       SwingUtilities.invokeLater(() -> {
           EditCustomerDialog.showEditCustomerDialog(mFrame, customer, CustomersTable.this);
       });
     }

    @Override
    protected void onRightClickRow(int rowIndex,MouseEvent event) {
        showPopupItems(event,mCustomers.get(rowIndex));
    }
            
            private void showPopupItems(MouseEvent e,final Customer customer){
               JPopupMenu popup = createPopup(customer);
            popup.show(e.getComponent(), e.getX(), e.getY());     
        }
            
            
            private JPopupMenu createPopup(final Customer customer){
                final JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.add(createDeleteItem(customer));
                popupMenu.addSeparator();
                popupMenu.add(createDetailsItem(customer));
                
                return popupMenu;
            }
            
         private JMenuItem createDeleteItem(final Customer customer){
                final JMenuItem deleteItem = new JMenuItem("حذف");
                deleteItem.addActionListener((ActionEvent e) -> {
                    
                    DBUtil.loadOrdersFromCustomer(customer.id, new QueryDbListener<ArrayList<Order>>(){

                        @Override
                        public void queriedSuccessfully(final ArrayList<Order> ordersResult) {
                            
                            if (ordersResult.isEmpty()){
                                SwingUtilities.invokeLater(() -> {
                                    final String dialogMsg = "هل انت متأكد من حذف العميل " + customer.name;
                                    
                                    final int result = JOptionPane.showConfirmDialog(CustomersTable.this, dialogMsg, "تأكيد", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null);
                                    
                                    if (result == JOptionPane.YES_OPTION){
                                         DBUtil.deleteCustomer(customer.id, new OnDeleteOnlyCustomer());
                                    }
                                   
                                   
                                });
                                
                            }
                            
                            else {
                                SwingUtilities.invokeLater(new Runnable(){

                                    @Override
                                    public void run() {
                                        final String dialogMsg = customer.name + "مرتبط بطلبيات , هل تريد حذفها مع العميل ؟";
                                        
                                        final int result = JOptionPane.showConfirmDialog(CustomersTable.this, dialogMsg, "تأكيد", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null);
                                        
                                        if (result == JOptionPane.YES_OPTION){
                                            //Delete all orders related to customer
                                            
                                            DBUtil.deleteOrdersRelatedToCustomer(customer.id, new DeleteDbListener(){

                                                @Override
                                                public void deletedSuccessfully() {
                                                    mCallback.onUpdateOrdersTable();
                                                    DBUtil.deleteCustomer(customer.id, new OnDeleteOnlyCustomer());
                                                    
                                                }

                                                @Override
                                                public void failedToDelete(Throwable error) {
                                                }
                                            });
                                        }
                                        
                                        else {
                                            //Don't do anything
                                        }
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
         
           private JMenuItem createDetailsItem(final Customer customer){
                final JMenuItem detailsItem = new JMenuItem("التفاصيل");
                detailsItem.addActionListener((ActionEvent e) -> {
                    showEditCustomerDialog(customer);
                });
                       return detailsItem;
    }

    
    
    private class CustomersModel extends AbstractTableModel{

        private final String mNameCol = "الإسم";
        private final String mCompanyCol = "الشركة";
        private final String mMobileNumber = "رقم الجوال";
        
        private final String[] mColumns = {mNameCol,mCompanyCol,mMobileNumber};
        
        @Override
        public int getRowCount() {
            
            return mCustomers.size();
        }
        
        @Override
        public String getColumnName(int column) {
            return mColumns[column];
        }

        @Override
        public int getColumnCount() {
            return mColumns.length;
        }

        @Override
        public String getValueAt(int rowIndex, int columnIndex) {
            final Customer customer = mCustomers.get(rowIndex);
            
            switch(mColumns[columnIndex]){
                case mNameCol :return customer.name;
                case mCompanyCol : return customer.company;
                case mMobileNumber : return customer.mobileNumber;
                    
                 default : return "";
                    
            }
        }
    }
    
    private class OnDeleteOnlyCustomer implements DeleteDbListener {

        @Override
        public void deletedSuccessfully() {
            mCallback.onUpdateCustomerTable(CustomersTable.this);
        }

        @Override
        public void failedToDelete(Throwable error) {
        }
        
    }
    
    
    public static interface CustomersTableCallback{
        void onUpdateCustomerTable(CustomersTable customersTable);
        void onUpdateOrdersTable();
        boolean shouldAllowClicks();
    }
    
}
