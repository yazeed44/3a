/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.ui;



import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import pkg3a.ui.AddOrderDialog.AddDialogListener;
import pkg3a.utils.DBUtil;
import pkg3a.utils.DBUtil.QueryDbListener;
import pkg3a.utils.Order;
import pkg3a.utils.ViewUtil;

/**
 *
 * @author yazeed44
 */
public class Main extends MainInterface {
    

    
    private final CustomersPanel mCustomersPanel = new CustomersPanel(this);
    private final OrdersPanel mOrdersPanel = new OrdersPanel(this);
    private final HostingPackagesPanel mHostingPackagesPanel = new HostingPackagesPanel(this);
    
    
    public Main() throws Exception{
        super();
        
        tabs.add("الطلبيات",mOrdersPanel);
        
        tabs.add("العملاء",mCustomersPanel);
        
        tabs.add("باقات الإستضافة",mHostingPackagesPanel);
        
      
        javax.swing.SwingUtilities.invokeLater(() -> {
            setVisible(true);
        });
        
       


        notifyAboutNearEndedDomain();
    }
    
    
    private void notifyAboutNearEndedDomain(){
         DBUtil.loadOrders(new QueryDbListener<ArrayList<Order>>(){

            @Override
            public void queriedSuccessfully(ArrayList<Order> result) {
                
                String toBeNotifiedOrders = "";
                for(final Order order : result){
                    if (order.isDomainNearExpire() || order.hasDomainExpired()){
                        toBeNotifiedOrders += order.getDomain() + "\n";
                    }
                }
                
                if (toBeNotifiedOrders.length() > 0){
                     String dialogMsg =  "اسماء الدومينات اللتي سوف تنتهي بعد 30 يوم أو اقل , أو انتهت فعلا"+ "\n";
                     dialogMsg += toBeNotifiedOrders;
                     
                     JOptionPane.showMessageDialog(Main.this, dialogMsg, "تنبيه", JOptionPane.INFORMATION_MESSAGE);
                     
                }
               
            }

            @Override
            public void failedToQuery(Throwable throwable) {
            }
        });
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        new Main();
    }
    
    public void addHostingPackage(){
        System.out.println("started adding hosting package ");
        AddHostingPackageDialog.showAddHostingPackageDialog(this, new AddHostingPackageDialog.AddHostingPackageCallback() {

            @Override
            public void addedSuccessfully(AddHostingPackageDialog dialog) {
                updateHostingPackagesTable();
                ViewUtil.disposeOfDialog(dialog);
            }

            @Override
            public void failedToAdd(AddHostingPackageDialog dialog, Throwable throwable) {
                
            }

            
        });
        
    }
    private void updateHostingPackagesTable() {
        
        mHostingPackagesPanel.onUpdateHostingPackagesTable((HostingPackagesTable) mHostingPackagesPanel.hostingPackagesTable);
            }
    
    public void addCustomer(){
        System.out.println("started adding Customer ");
        
        AddCustomerDialog.showAddCustomerDialog(this, new AddCustomerDialog.AddCustomerCallback() {

            @Override
            public void addedSuccessfully(AddCustomerDialog dialog) {
                updateCustomersTable();
                ViewUtil.disposeOfDialog(dialog);
            }

            @Override
            public void failedToAdd(AddCustomerDialog dialog, Throwable error) {
                System.out.println(error.getMessage());
            }
        });
        
    }
    
    private void updateCustomersTable(){
    mCustomersPanel.updateTable();
    }
    
    public void addOrder() {
        System.out.println("started adding Order");
        
        AddOrderDialog.showAddDialog(this,new AddDialogListener(){

            @Override
            public void addedSuccessfully(AddOrderDialog dialog) {
                
                System.out.println("Added order successfully");
                updateOrdersTable();
                ViewUtil.disposeOfDialog(dialog);
            }

            @Override
            public void failedToAdd(AddOrderDialog dialog , Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });
    }
    
   
    
     void updateOrdersTable(){
        mOrdersPanel.onUpdate();
    }

    @Override
    void onClickAddCustomer(ActionEvent evt) {
        addCustomer();
    }

    @Override
    void onClickAddOrder(ActionEvent evt) {
        addOrder();
    }

    @Override
    void onClickAddHostingPackage(ActionEvent evt) {
        addHostingPackage();
    }

    @Override
    void onClickFindEndingDomainDatesInRange(ActionEvent evt) {
        showFindEndDomainDatesDialog();
    }

    private void showFindEndDomainDatesDialog() {
        //TODO
        
       new FindExpiredDomainsInRangeDialog(this,true).setVisible(true);
    }

    
}
