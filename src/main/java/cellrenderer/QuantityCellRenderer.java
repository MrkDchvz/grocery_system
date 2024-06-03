/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cellrenderer;

import java.awt.Color;
import java.awt.Component;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Marku
 */
public class QuantityCellRenderer extends DefaultTableCellRenderer {

    private static final Color COLOR_EXPIRED = Color.RED;
    private static final Color COLOR_SELECT = Color.decode("#7E52A0");

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody

        Date expiryDate = (Date) table.getValueAt(row, table.getColumn("Expiry Date").getModelIndex());

        int quantity = (int) table.getValueAt(row, table.getColumn("Quantity").getModelIndex());

        if (quantity == 0) {
            cell.setBackground(COLOR_EXPIRED);
            
            if (isSelected) {
                cell.setBackground(table.getSelectionBackground());
              
            }

        } else {
            cell.setBackground(table.getBackground());
            cell.setForeground(table.getForeground());
        }
        
        if (isSelected) {
            cell.setBackground(table.getSelectionBackground());
            cell.setForeground(table.getSelectionForeground());
        }
        return cell;

    }

}
