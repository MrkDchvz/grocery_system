package tabs;


import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextField;
import orders.Order;
import orders.OrderDAO;
import utils.DateUtils;
import utils.ValidationUtils;
import filters.IntegerOnlyFilter;
import grocery.Admin;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import pdf.GeneratePDF;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Marku
 */
public class OrderDetailsTab {
    JTextField orderIdField;
    JTextField orderDateField;
    JTextField accountIdField;
    JTextField totalField;
    JTable table;
    JButton printButton;
    
    OrderDAO orderDAO;
    
    ValidationUtils validation;
    
    GeneratePDF pdf;
    
    private static final Logger LOGGER = Logger.getLogger(OrderDetailsTab.class.getName());
    
    
    public OrderDetailsTab() {
        this.orderDAO = new OrderDAO();
        this.validation = new ValidationUtils();
        
        try {
            this.pdf = new GeneratePDF();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize GeneratePDF Class", e);
        }
       
    }
    

    public void searchOrderDetails() {
//        orderId is always guaranteed to be an Integer due to documentFilter but I added validation just incase
        try {
            int orderId = Integer.parseInt(this.orderIdField.getText());
            if (orderDAO.doesOrderExists(orderId)) {
                orderDAO.searchAndDisplayOrderDetails(table, orderId);
                populateFields();
                this.printButton.setEnabled(true);
                this.orderIdField.setEnabled(false);
            } else {
                validation.showErrorDialog("Order doesn't exists.");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "NumberFormatException: Can't parse orderIdField's text into an integer", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error searching for product", e);
        } 
    }
    
    public void clearOrderDetailsTab() {
        this.orderIdField.setText("");
        this.accountIdField.setText("");
        this.orderDateField.setText("");
        this.totalField.setText("");
        this.printButton.setEnabled(false);
        clearTable();
        
        
        this.orderIdField.setEnabled(true);
    }
    
    private void populateFields() {
        try {
            int orderId = Integer.parseInt(this.orderIdField.getText());
            Order order = orderDAO.getOrder(orderId);
 
         
            this.orderIdField.setText(String.valueOf(orderId));
            this.accountIdField.setText(String.valueOf(order.getAccountId()));
            this.orderDateField.setText(DateUtils.formatDate(order.getOrderDate(), "MMM d, yyyy") );
            this.totalField.setText(String.valueOf(order.getTotalAmount()));
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "NumberFormatException: Can't parse orderIdField's text into an integer", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error populating fields", e);
        }
        
    }
    public void printOrderDetails() {
        try {
//            MessageFormat headerFormat = new MessageFormat("Order Id: " + String.valueOf(orderId));
//            MessageFormat footerFormat = new MessageFormat("Page {0, number, integer}");
//            this.table.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
            pdf.header();
            pdf.divider();
            pdf.orderDetailsTable(this.table, Double.parseDouble(this.totalField.getText()));
            pdf.closeDocument();
            validation.showDialog("PDF Generated successfully");
        } catch (Exception ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void clearTable() {
       DefaultTableModel model = (DefaultTableModel) this.table.getModel();
       model.setRowCount(0);
    }

    public void setOrderIdField(JTextField orderIdField) {
        this.orderIdField = orderIdField;
        ((AbstractDocument) this.orderIdField.getDocument()).setDocumentFilter(new IntegerOnlyFilter());
        
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

    public void setTable(JTable table) {
        this.table = table;
        this.table.setRowHeight(30);
        this.table.setShowGrid(true);
        this.table.setGridColor(Color.black);
        this.table.setBackground(Color.white);
    }

    public void setPrintButton(JButton printButton) {
        this.printButton = printButton;
        this.printButton.setEnabled(false);
    }
    
    
    
    
    
    
    
   
    
    
    
    
    
}
