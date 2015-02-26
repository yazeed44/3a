/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.ui;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.table.TableCellRenderer;
import pkg3a.utils.DBUtil;
import pkg3a.utils.DBUtil.QueryDbListener;
import pkg3a.utils.DBUtil.UpdateDbListener;
import pkg3a.utils.Order;

/**
 *
 * @author yazeed44
 */
public class OrdersTable extends BaseTable implements  EditOrderDialog.EditDialogListener,DBUtil.DeleteDbListener{
    
    private ArrayList<Order> mOrders;
    private final Frame mFrame;
    public static final Color NEAR_EXPIRE_ROW_COLOR = Color.ORANGE;
    public static final Color EXPIRED_ROW_COLOR = Color.RED;
    public static final Color UNACTIVE_ROW_COLOR = Color.GRAY;
    private final UpdateListener mListener;
    public OrdersTable(final Frame frame , final ArrayList<Order> orders , final UpdateListener listener){
        super();
        mFrame = frame;
        mOrders = orders;
        mListener = listener;
        setupModel();
        setupMouseListener();
        
    }
    @Override
    protected void setupMouseListener() {
        
        if (mListener.shouldAllowClick()){
            super.setupMouseListener();
        }
        else {
            return ;
        }
        
    }
    
    private void setupModel(){
    setModel(new OrdersModel());
    }
    
   
     private void showEditOrderDialog(final Order result){
       SwingUtilities.invokeLater(() -> {
           EditOrderDialog.showEditDialog(mFrame, result, OrdersTable.this);
       });
     }
     
        
            private void showPopupItems(MouseEvent e,final Order result){
               JPopupMenu popup = createPopup(result);
            popup.show(e.getComponent(), e.getX(), e.getY());     
        }
            
            
            private JPopupMenu createPopup(final Order order){
                final JPopupMenu popupMenu = new JPopupMenu();
                popupMenu.add(createDeleteItem(order));
                popupMenu.add(createChangeStateItem(order));
                popupMenu.addSeparator();
                popupMenu.add(createDetailsItem(order));
                
                return popupMenu;
            }
            

            
            private JMenuItem createDeleteItem(final Order order){
                final JMenuItem deleteItem = new JMenuItem("حذف");
                deleteItem.addActionListener((ActionEvent e) -> {
                    SwingUtilities.invokeLater(() -> {
                        final String msg = "هل انت متأكد من حذف هذا الطلب ؟";
                        final int result = JOptionPane.showConfirmDialog(OrdersTable.this, msg, "تأكيد",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
                        
                        if (result == JOptionPane.YES_OPTION){
                            DBUtil.deleteOrder(order.id,OrdersTable.this);
                        }
                        
                        else {
                            
                        }
                    });
                    
                });
                
                return deleteItem;
            }
            
            private JMenuItem createChangeStateItem(final Order order){
                
                final String changeStateText;
                
                if (order.isActivated()){
                    changeStateText = "إيقاف";
                }
                else {
                    changeStateText = "تفعيل";
                }
            
                final JMenuItem changeStateItem = new JMenuItem(changeStateText);
                changeStateItem.addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        final Order toUpdateOrder = new Order.Builder(order)
                                .isActivated(!order.isActivated()).build();
                        
                        DBUtil.updateOrder(toUpdateOrder, new UpdateDbListener<Order>(){

                            @Override
                            public void updatedSuccessfully(Order result) {
                                mListener.onUpdateTable(OrdersTable.this);
                            }

                            @Override
                            public void failedToUpdate(Throwable error) {
                            }
                        });
                    }
                    
                });
                
                return changeStateItem;
            }
                
     
            
            private JMenuItem createDetailsItem(final Order order){
                final JMenuItem detailsItem = new JMenuItem("التفاصيل");
                detailsItem.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showEditOrderDialog(order);
                    }
                    }
                );
                       return detailsItem;
    }
    
     void updateTable(final ArrayList<Order> orders){
         mOrders = orders;
        setupModel();
    }

   

    @Override
    public void editedSuccessfully(EditOrderDialog dialog) {
        dialog.setVisible(false);
        mListener.onUpdateTable(this);
    }

    @Override
    public void failedToEdit(EditOrderDialog dialog, Throwable throwable) {
    }

    @Override
    public void deletedSuccessfully() {
        mListener.onUpdateTable(this);
    }

    @Override
    public void failedToDelete(Throwable error) {
    }
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
    Component stamp = super.prepareRenderer(renderer, row, column);
    stamp.setBackground(Color.WHITE);
    final Order order = mOrders.get(row);
    
    if (!order.isActivated()){
      stamp.setBackground(UNACTIVE_ROW_COLOR);
      return stamp;
     }
    
    if (!order.domainExists()){
         
         return stamp;
    }
     if (order.hasDomainExpired()){
        stamp.setBackground(EXPIRED_ROW_COLOR);
    }
    
    else if (order.isDomainNearExpire()){
        stamp.setBackground(NEAR_EXPIRE_ROW_COLOR);
    }
    
    return stamp;
    
    
}

    @Override
    protected void onDoubleClickRow(int rowIndex) {
        showEditOrderDialog(mOrders.get(rowIndex));
    }

    @Override
    protected void onRightClickRow(int rowIndex, MouseEvent e) {
        showPopupItems(e,mOrders.get(rowIndex));
    }
    
    
    
    
    
    private class OrdersModel extends AbstractTableModel{
        
        private final String mCustomerCol = "اسم العميل";
        private final String mDomainCol = "الدومين";
        private final String mBeginingDateCol = "تاريخ الطلبية";
        
        
        
        private final String[] mColumns = new String[]{mCustomerCol,mBeginingDateCol,mDomainCol};

        @Override
        public int getRowCount() {
            return mOrders.size();
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
            final Order order = mOrders.get(rowIndex);
            
            switch(mColumns[columnIndex]){
                
                case mCustomerCol:return order.getCustomer().name; 
                case mDomainCol:return order.getDomain();
                case mBeginingDateCol:return order.getBeginingDateFormatted();
                default:return "لايوجد";
                
            }
        }
        
    }
    
    
    public static interface UpdateListener {
        void onUpdateTable(OrdersTable ordersTable);
        boolean shouldAllowClick();
    }
    
    
        }
    
    
        
