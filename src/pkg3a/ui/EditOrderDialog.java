/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.ui;

import java.awt.Frame;
import java.sql.Date;
import java.util.Calendar;
import pkg3a.utils.DBUtil;
import pkg3a.utils.DBUtil.QueryDbListener;
import pkg3a.utils.DBUtil.UpdateDbListener;
import pkg3a.utils.HostingPackage;
import pkg3a.utils.Order;

/**
 *
 * @author yazeed44
 */
public class EditOrderDialog extends AddOrderDialog implements UpdateDbListener<Order> {

    private EditDialogListener mListener;
    private Order mOrder;
    
    public EditOrderDialog(Frame parent, boolean modal) {
        super(parent, modal);
    }
    
    public EditOrderDialog(Frame parent , Order order,EditDialogListener listener ){
        this(parent,true);
        mListener = listener;
        mOrder = order;
        setupDomain();
        setupTotalCost();
        setupHostingPackage();
        setupCustomer();
        setupNote();
        setupEditButton();
        setupTitle();
        setupHostingPackageExpireDate();
    }
    public static void showEditDialog(Frame parent , final Order order , final EditDialogListener listener){
      new EditOrderDialog(parent,order,listener).setVisible(true);
    }
    
    private void setupDomain(){
        
        if (mOrder.domainExists()){
            domainNameTxt.setText(mOrder.getDomain());
            domainCostTxt.setText(mOrder.domainCost + "");
            
            final Calendar calendar = Calendar.getInstance();
          //  calendar.setTimeInMillis(mOrder.getEndDomainDateMillis());
            calendar.setTime(new Date(mOrder.getEndDomainDateMillis()));
            domainEndDate.setSelectedDate(calendar);
            
        }
        
        else {
            System.out.println("Started disabling domain");
            domainCheckbox.doClick();
        }
        
    }
    
    private void setupTotalCost(){
        totalCostText.setText(mOrder.getTotalCostWithoutCurrency() + "");
    }
    
    private void setupHostingPackage(){
        mOrder.loadHostingPackage(new QueryDbListener<HostingPackage>(){

            @Override
            public void queriedSuccessfully(HostingPackage result) {
                packageCombobox.setSelectedItem(result);
            }

            @Override
            public void failedToQuery(Throwable throwable) {
            }
        });
        
    }
    
    private void setupCustomer(){
        
        customerCombobox.setSelectedItem(mOrder.getCustomer());
        
        
        
        
    }
    
    private void setupNote(){
        noteText.setText(mOrder.note);
        
    }
    
    private void setupEditButton(){
        addButton.setText("تعديل");
    }
    
    private void setupTitle(){
        setTitle("تعديل طلبية");
    }
    
    private void setupHostingPackageExpireDate(){
        
        if (mOrder.getHostingPackageEndDateMillis() != -1){
             hostingPackageExpireDate.setEnabled(true);
            final Calendar expireCalendar = Calendar.getInstance();
            expireCalendar.setTime(new Date(mOrder.getHostingPackageEndDateMillis()));
            hostingPackageExpireDate.setSelectedDate(expireCalendar);
       
        }
        
        else {
            
            hostingPackageExpireDate.setEnabled(false);
            
        }
    
        
    }
    
    
    
    @Override
    protected void submitOrder() {
        final Order order = createOrderBuilder(mOrder.id).setBeginingDate(mOrder.getBeginingDateMillis())
                .isActivated(mOrder.isActivated())
                .build();
        
        DBUtil.updateOrder(order,this);
    }

    @Override
    public void updatedSuccessfully(Order result) {
         mListener.editedSuccessfully(this);
    }

    @Override
    public void failedToUpdate(Throwable error) {
         mListener.failedToEdit(this, error);
    }
    
    public static interface EditDialogListener {
        void editedSuccessfully(EditOrderDialog dialog);
        void failedToEdit(EditOrderDialog dialog , Throwable throwable);
    }
    
}
