/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tabs;

import filters.IntegerOnlyFilter;
import grocery.Admin;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.sql.Date;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import orders.OrderDAO;
import utils.ValidationUtils;

/**
 *
 * @author Marku
 */
public class OrdersTab {
    JTextField orderIdField;
    JTextField orderDateField;
    JTextField accountIdField;
    JTextField totalField;
    JTextField searchField;
    JTable table;
    JComboBox displayLimit;
    
    OrderDAO orderDAO;
    ValidationUtils validation;
    
    
    
    public OrdersTab() {
        this.orderDAO = new OrderDAO();
        this.validation = new ValidationUtils();

    }
    
    public void clearOrdersTab() {
        this.orderIdField.setText("");
        this.orderDateField.setText("");
        this.accountIdField.setText("");
        this.totalField.setText("");
    } 
    
    
    
    public void deleteOrder() {
        try {
            if (orderDAO.deleteOrder(Integer.parseInt(this.orderIdField.getText())) > 0) {
            validation.showDialog("Order Successfully deleted.");
            clearOrdersTab();
            
            } else {
            validation.showDialog("Failed to delete Order.");
            }
            initTable();
            
        } catch (NumberFormatException e) {
            validation.showDialog("Failed to delete Order.");
        }
        
    }
    
    public void initTable() {
        
        orderDAO.searchAndDisplayOrders(table, orderDAO.DISPLAY_ALL);
        this.table.setRowHeight(30);
        this.table.setShowGrid(true);
        this.table.setGridColor(Color.black);
        this.table.setBackground(Color.white);
    }
    
    public void updateLimit() {
        orderDAO.setDisplayLimit(Integer.parseInt(this.displayLimit.getSelectedItem().toString()));
        initTable();
    }
    
    public void searchOrder() {
        orderDAO.searchAndDisplayOrders(table, Integer.parseInt(this.searchField.getText()));
    }
    
    public void selectOrderFromTable() {
        int rowIndex = this.table.getSelectedRow();
        
        if (rowIndex == -1) {
            // No row selected do nothing     
            return;
        }
       
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        
        try {
            setTextFieldValue(this.orderIdField,model.getValueAt(rowIndex, 0));
            setTextFieldDate(this.orderDateField,model.getValueAt(rowIndex, 1));
            setTextFieldValue(this.accountIdField,model.getValueAt(rowIndex, 2));
            setTextFieldValue(this.totalField, model.getValueAt(rowIndex, 3));

       
        } catch (Exception e) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void setTextFieldValue(JTextField field, Object value) {
        if (value != null) {
            field.setText(value.toString());
        }
    }
    
    private void setTextFieldDate(JTextField field, Object value) {
        LocalDate date = LocalDate.parse(value.toString(), DateTimeFormatter.ISO_DATE);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        String formattedDate = date.format(formatter);
        if (value != null) {
            field.setText(formattedDate);
        }
       
    }
    
    public void printOrder() {
        try {
            MessageFormat headerFormat = new MessageFormat("Orders List");
            MessageFormat footerFormat = new MessageFormat("Page {0, number, integer}");
            this.table.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
        } catch (Exception ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void setOrderIdField(JTextField orderIdField) {
        this.orderIdField = orderIdField;
    }

    public void setOrderDateField(JTextField orderDateField) {
        this.orderDateField = orderDateField;
    }

    public void setAccountIdField(JTextField accountIdField) {
        this.accountIdField = accountIdField;
    }

    public void setTotalField(JTextField totalField) {
        this.totalField = totalField;
    }

    public void setSearchField(JTextField searchField) {
        this.searchField = searchField;
        ((AbstractDocument) this.searchField.getDocument()).setDocumentFilter(new IntegerOnlyFilter());
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public void setDisplayLimit(JComboBox displayLimit) {
        this.displayLimit = displayLimit;
        orderDAO.setDisplayLimit(Integer.parseInt(this.displayLimit.getSelectedItem().toString()));
    }
    
    
    
    
    
    
    
}
