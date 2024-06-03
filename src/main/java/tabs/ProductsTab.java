/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tabs;

import com.toedter.calendar.JDateChooser;
import filters.DecimalOnlyFilter;
import filters.IntegerOnlyFilter;
import filters.UpperCaseFilter;
import grocery.Admin;
import java.awt.Color;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import utils.DateUtils;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import products.Product;
import products.ProductDAO;
import utils.ValidationUtils;
import cellrenderer.QuantityCellRenderer;
import cellrenderer.ExpiryCellRenderer;
import javax.swing.JComboBox;

/**
 *
 * @author Marku
 */
public class ProductsTab {

    JTextField idField;
    JTextField nameField;
    JTextField quantityField;
    JTextField typeField;
    JTextField priceField;
    JTextField searchField;
    JTable table;
    JDateChooser deliveryField;
    JDateChooser expiryField;
    JComboBox displayLimitField;
    
    ProductDAO productDAO;
    
    IntegerOnlyFilter intFilter;
    UpperCaseFilter upperCaseFilter;
    DecimalOnlyFilter decimalFilter;
    
    ValidationUtils validation;

    public ProductsTab() {
        this.productDAO = new ProductDAO();
        this.intFilter = new IntegerOnlyFilter();
        this.upperCaseFilter = new UpperCaseFilter();
        this.decimalFilter = new DecimalOnlyFilter();
        this.validation = new ValidationUtils();
    }

    public void initTable() {
        this.table.setRowHeight(30);
        this.table.setShowGrid(true);
        this.table.setGridColor(Color.black);
        this.table.setBackground(Color.white);productDAO.setDisplay_limit(Integer.parseInt(this.displayLimitField.getSelectedItem().toString()));
        this.productDAO.setDisplay_limit(Integer.parseInt(this.displayLimitField.getSelectedItem().toString()));
        productDAO.lazySearchAndDisplayProducts(this.table, "");
    }
    
    public void addProduct() {
       
        if (validation.isValidProduct(this.nameField, this.quantityField, this.typeField, this.priceField, this.deliveryField, this.expiryField)
            && !validation.isDuplicateProduct(this.nameField.getText())) {
            
            String name = this.nameField.getText();
            Integer quantity = Integer.parseInt(this.quantityField.getText());
            String type = this.typeField.getText();
            double price = Double.parseDouble(this.priceField.getText());
            Date deliveryDate  = DateUtils.convertToSqlDate(this.deliveryField.getDate());
            Date expiryDate = DateUtils.convertToSqlDate(this.expiryField.getDate());
            
            if (productDAO.insertProduct(name, quantity, type, price, deliveryDate, expiryDate)) {
                validation.showDialog("Product added successfully");
                clearProductsTab();
                initTable();
            } else {
                validation.showErrorDialog("Failed to add product");
            }
            
        }
    }

    public void updateProduct() {
        int id = Integer.parseInt(this.idField.getText());
//            Check if all values on the Textfields are valid
        if (validation.isValidProduct(this.nameField, this.quantityField, this.typeField, this.priceField, this.deliveryField, this.expiryField)) {
            int quantity = Integer.parseInt(this.quantityField.getText());
            String type = this.typeField.getText();
            double price = Double.parseDouble(this.priceField.getText());
            Date deliveryDate = DateUtils.convertToSqlDate(this.deliveryField.getDate());
            Date expiryDate = DateUtils.convertToSqlDate(this.expiryField.getDate());

            if (productDAO.updateProduct(id, quantity, type, price, deliveryDate, expiryDate)) {
                validation.showDialog("Product updated successfully");
                clearProductsTab();
                initTable();
            } else {
                validation.showDialog("Failed to update product");
            }

        }

    }
    
    public void refreshSearch() {
        this.searchField.setText("");
        initTable();
    }
    
    public void deleteProduct() {
        int id = Integer.parseInt(this.idField.getText());
        
        if (productDAO.doesProductExists(id)) {
            String name = this.nameField.getText();
            String deleteMessage = "Are you sure you want to delete this product? \n " +
                               "Product Id: " + id + "\n " +
                               "Product Name: " + name + "\n ";
        
        
            int userInput = JOptionPane.showConfirmDialog(null, deleteMessage, "System", JOptionPane.YES_NO_OPTION);
        
            if (userInput == 0) {
                productDAO.deleteProduct(id);
                clearProductsTab();
                initTable();
            }
        } else {
            validation.showErrorDialog("Product doesn't exist");
        }
    }
    
    public void printProduct() {
        try {
            MessageFormat headerFormat = new MessageFormat("Products List");
            MessageFormat footerFormat = new MessageFormat("Page {0, number, integer}");
            this.table.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
        } catch (Exception ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void selectProductFromTable() {
        int rowIndex = this.table.getSelectedRow();
        
        if (rowIndex == -1) {
            // No row selected do nothing     
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        
        try {
            setTextFieldValue(this.idField,model.getValueAt(rowIndex, 0));
            setTextFieldValue(this.nameField,model.getValueAt(rowIndex, 1));
            setTextFieldValue(this.quantityField,model.getValueAt(rowIndex, 2));
            setTextFieldValue(this.typeField, model.getValueAt(rowIndex, 3));
            setTextFieldValue(this.priceField, model.getValueAt(rowIndex, 4));
                      
            setDateFromModel(this.deliveryField, model.getValueAt(rowIndex, 5).toString());
            setDateFromModel(this.expiryField, model.getValueAt(rowIndex, 6).toString());
            
            this.nameField.setEnabled(false);
       
        } catch (ParseException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
    
    

    public void clearProductsTab() {
        this.idField.setText(String.valueOf(productDAO.getMax()));
        this.nameField.setText("");
        this.quantityField.setText("");
        this.typeField.setText("");
        this.priceField.setText("");
        this.deliveryField.setDate(null);
        this.expiryField.setDate(null);
        this.nameField.setEnabled(true);
    }
    
    public void searchProduct() {
        productDAO.lazySearchAndDisplayProducts(this.table, this.searchField.getText());
    }
    
    public void updateLimit() {
        this.productDAO.setDisplay_limit(Integer.parseInt(this.displayLimitField.getSelectedItem().toString()));
        initTable();
    }
    

    
    private String formatDate(Date date, String format) {
        SimpleDateFormat date_format = new SimpleDateFormat(format);
        return date_format.format(date);
    } 
    
 
    
    private void setTextFieldValue(JTextField field, Object value) {
        if (value != null) {
            field.setText(value.toString());
        }
    }
    
    private void setDateFromModel(JDateChooser dateChooser, String dateString) throws ParseException {
        if (dateString != null && !dateString.isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = DateUtils.convertToSqlDate(simpleDateFormat.parse(dateString));
            dateChooser.setDate(date);
        }  
    }
    
  

    
// Setters and Getters for textfields and table
// Note: This is not the value of the textfields or table it is the component itself (Look at the return value of getters)

    public JTextField getIdField() {
        return idField;
    }

    public void setIdField(JTextField idField) {
        this.idField = idField;
        this.idField.setText(String.valueOf(productDAO.getMax()));
    }

    public JTextField getNameField() {
        return nameField;
    }

    public void setNameField(JTextField nameField) {
        this.nameField = nameField;
        ((AbstractDocument) this.nameField.getDocument()).setDocumentFilter(this.upperCaseFilter);
    }

    public JTextField getQuantityField() {
        return quantityField;
    }

    public void setQuantityField(JTextField quantityField) {
        this.quantityField = quantityField;
        ((AbstractDocument) this.quantityField.getDocument()).setDocumentFilter(this.intFilter);
    }

    public JTextField getTypeField() {
        return typeField;
    }

    public void setTypeField(JTextField typeField) {
        this.typeField = typeField;
        ((AbstractDocument) this.typeField.getDocument()).setDocumentFilter(this.upperCaseFilter);
    }

    public JTextField getPriceField() {
        return priceField;
        
    }

    public void setPriceField(JTextField priceField) {
        this.priceField = priceField;
        ((AbstractDocument) this.priceField.getDocument()).setDocumentFilter(this.decimalFilter);
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public void setSearchField(JTextField searchField) {
        this.searchField = searchField;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
        this.table.getColumnModel().getColumn(this.table.getColumn("Quantity").getModelIndex()).setCellRenderer(new QuantityCellRenderer());
        this.table.getColumnModel().getColumn(this.table.getColumn("Expiry Date").getModelIndex()).setCellRenderer(new ExpiryCellRenderer());
    }

    public JDateChooser getDeliveryField() {
        return deliveryField;
    }

    public void setDeliveryField(JDateChooser deliveryField) {
        this.deliveryField = deliveryField;
    }

    public JDateChooser getExpiryField() {
        return expiryField;
    }

    public void setExpiryField(JDateChooser expiryField) {
        this.expiryField = expiryField;
    }

    public JComboBox getDisplayLimitField() {
        return displayLimitField;
    }

    public void setDisplayLimitField(JComboBox displayLimitField) {
        this.displayLimitField = displayLimitField;
        this.productDAO.setDisplay_limit(Integer.parseInt(this.displayLimitField.getSelectedItem().toString()));
    }
    
    
   
}

