/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg3a.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;

/**
 *
 * @author yazeed44
 */
public abstract class BaseTable extends JTable{
    
   
    
    protected void setupMouseListener(){
        
        addMouseListener(new MouseAdapter(){
        @Override
            public void mouseReleased(MouseEvent e) {
                //For linux
                handleClick(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //For mac os and windows
                handleClick(e);
            }
            
            private void handleClick(MouseEvent e){
           int r = rowAtPoint(e.getPoint());
        if (r >= 0 && r < getRowCount()) {
            setRowSelectionInterval(r, r);
        } else {
            clearSelection();
        }
        
        int rowIndex = getSelectedRow();
        if (rowIndex < 0)
            return;
        
        if (e.getComponent() instanceof JTable){
            
            if (isDoubleClick(e)){
                onDoubleClickRow(rowIndex);
            }
            
            else if (isRightClick(e)){
                onRightClickRow(rowIndex,e);
                
            }
            
        }
        }
        }
);
    }
    
            private boolean isDoubleClick(MouseEvent e){
         return e.getClickCount() >= 2;
     }
     
     private boolean isRightClick(MouseEvent e){
         return e.isPopupTrigger();
     }
         
    
    
    protected abstract void onDoubleClickRow(final int rowIndex);
    protected abstract void onRightClickRow(final int rowIndex,final MouseEvent e);
    
}
