/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.ui;

import java.awt.Frame;
import pkg3a.utils.DBUtil;
import pkg3a.utils.HostingPackage;

/**
 *
 * @author yazeed44
 */
public class EditHostingPackageDialog extends AddHostingPackageDialog implements DBUtil.UpdateDbListener<HostingPackage> {

    private EditHostingPackageCallback mCallback;
    private HostingPackage mToEditHostingPackage;
    public EditHostingPackageDialog(Frame parent, boolean modal) {
        super(parent, modal);
    }
    
    public EditHostingPackageDialog(final Frame parent ,HostingPackage hostingPackage ,final EditHostingPackageCallback callback){
        this(parent,true);
        mCallback = callback;
        mToEditHostingPackage = hostingPackage;
        setTitle("تعديل باقة إستضافة");
        setupName();
        setupStorageSpace();
        setupMonthlyOffer();
        setupEmailsCount();
        setupYearCost();
        setupSubmitButton();
        
    }
    
    public static void showEditHostingPackageDialog(final Frame parent ,final HostingPackage hostingPackage, final EditHostingPackageCallback callback){
      new EditHostingPackageDialog(parent,hostingPackage,callback).setVisible(true);
        
    }
    
    private void setupName(){
        nameText.setText(mToEditHostingPackage.name);
    }
    
    private void setupStorageSpace(){
        storageSpaceText.setText(mToEditHostingPackage.getStorageSpaceWithoutUnit());
        
    }
    
    private void setupMonthlyOffer(){
        monthlyOfferText.setText(mToEditHostingPackage.getMonthlyPackageOfferWithoutUnit());
        
    }
    
    private void setupEmailsCount(){
        emailsCountText.setText(mToEditHostingPackage.emailsCount);
    }
    
    private void setupYearCost(){
        yearPriceText.setText(mToEditHostingPackage.getYearCostWithoutUnit());
        
    }
    
    private void setupSubmitButton(){
        submitButton.setText("تعديل");
    }
    
    @Override
    protected void submit(){
        final HostingPackage hostingPackage = createBuilder(mToEditHostingPackage.id).build();
        
        if (hostingPackage == null){
            return;
        }
        
        DBUtil.updateHostingPackage(hostingPackage, this);
    }

    @Override
    public void updatedSuccessfully(HostingPackage result) {
        mCallback.editedSuccessfully(this);
    }

    @Override
    public void failedToUpdate(Throwable error) {
        mCallback.failedToEdit(this, error);
    }
    
    
    
    
    
    public static interface EditHostingPackageCallback {
        void editedSuccessfully(final EditHostingPackageDialog dialog);
        void failedToEdit(final EditHostingPackageDialog dialog , final Throwable error);
    }
}
