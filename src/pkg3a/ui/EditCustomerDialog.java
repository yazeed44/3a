/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.ui;

import java.awt.Frame;
import pkg3a.utils.Customer;
import pkg3a.utils.DBUtil;
import pkg3a.utils.DBUtil.UpdateDbListener;

/**
 *
 * @author yazeed44
 */
public class EditCustomerDialog extends AddCustomerDialog implements UpdateDbListener<Customer> {

    private final Customer mCustomer;
    private final EditCustomerCallback mCallback;

    
    public EditCustomerDialog(Frame parent , Customer customer , final EditCustomerCallback callback){
        super(parent,true);
        mCustomer = customer;
        mCallback = callback;
        setupName();
        setupMobileNumber();
        setupPhoneNumber();
        setupAddress();
        setupCompanyName();
        submitButton.setText("تعديل");
    }
    
    public static void showEditCustomerDialog(Frame parent , Customer customer , final EditCustomerCallback callback){
        new EditCustomerDialog(parent,customer,callback).setVisible(true);
        
    }
    
    
    private void setupName(){
        nameText.setText(mCustomer.name);
    }
    
    private void setupMobileNumber(){
        mobileNumberText.setText(mCustomer.mobileNumber);
        
    }
    
    private void setupPhoneNumber() {
        phoneNumberText.setText(mCustomer.phoneNumber);
    }
    

    private void setupAddress(){
        addressText.setText(mCustomer.address);
    }
    
    private void setupCompanyName(){
        companyNameText.setText(mCustomer.company);
    }
    
    @Override
    protected void submit(){
    
        final Customer toUpdateCustomer = createBuilder(mCustomer.id).build();
        
        if (toUpdateCustomer == null){
            return;
        }
        
        DBUtil.updateCustomer(toUpdateCustomer, this);
    }
    
    

    @Override
    public void updatedSuccessfully(Customer result) {
        mCallback.editedSuccessfully(this);
    }

    @Override
    public void failedToUpdate(Throwable error) {
        mCallback.failedToEdit(this, error);
    }
    
    public static interface EditCustomerCallback {
        void editedSuccessfully(EditCustomerDialog dialog);
        void failedToEdit(EditCustomerDialog dialog, Throwable error);
    }
}
