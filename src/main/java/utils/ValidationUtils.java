/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import com.toedter.calendar.JDateChooser;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import products.Product;
import products.ProductDAO;
import account.AccountDAO;
import account.Account;
import account.AccountTypes;
import java.util.Arrays;

/**
 *
 * @author Marku
 */
public class ValidationUtils {

    private ProductDAO productDAO;
    private AccountDAO accountDAO;

    public ValidationUtils() {
        this.productDAO = new ProductDAO();
        this.accountDAO = new AccountDAO();
    }

    public boolean isPositiveInteger(String text) {
        try {
            double num = Double.parseDouble(text);
            return num > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTableEmpty(JTable table) {
        return table.getModel().getRowCount() == 0;
    
    }

    public boolean isValidDeliveryDate(Date deliveryDate) {
        if (deliveryDate == null) {
            return false;
        } else if (deliveryDate.after(new Date())) {
            return false;
        } else {
            return true;
        }

    }

    public boolean isValidExpirationDate(Date expirationDate) {
        if (expirationDate == null) {
            return false;
        } else if (expirationDate.before(new Date())) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isPostiveQuantity(JSpinner spinner) {
        if (isPositiveInteger(spinner.getValue().toString())) {
            return true;
        }
        return false;
    }

    public boolean isWithinMaxQuantityLimit(JSpinner spinner, int maxValue) {
        String value = spinner.getValue().toString();
//        Check if the value on Jspinner is a postive Integer and within the limits of maxValue
        if (isPositiveInteger(value) && Integer.parseInt(value) >= maxValue) {
            return true;
        }
        return false;
    }

    public boolean isDuplicateProduct(String text) {
        if (productDAO.doesProductExists(text)) {
            showErrorDialog("Product already exists.");
            return true;
        } else {
            return false;
        }
    }

    public boolean isDuplicateProduct(int id) {
        if (productDAO.doesProductExists(id)) {
            showErrorDialog("Product already exists.");
            return true;
        } else {
            return false;
        }
    }

    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean isValidProduct(JTextField name, JTextField quantity, JTextField type, JTextField price, JDateChooser deliveryDate, JDateChooser expiryDate) {
        if (name.getText().isBlank()) {
            showErrorDialog("Product name is empty. Please enter a product name");
            return false;
        }

        if (!isPositiveInteger(quantity.getText())) {
            showErrorDialog("Quantity must be a valid number");
            return false;
        }

        if (type.getText().isBlank()) {
            showErrorDialog("Product type is empty. Please enter a product type");
            return false;
        }

        if (!isPositiveInteger(price.getText())) {
            showErrorDialog("price must be a valid number");
            return false;
        }
        if (!isValidDeliveryDate(deliveryDate.getDate())) {
            showErrorDialog("Delivery date must be valid.");
            return false;
        }
        if (!isValidExpirationDate(expiryDate.getDate())) {
            showErrorDialog("Expiry date must be valid.");
            return false;
        }
        return true;
    }
    
    private boolean isPasswordCorrect(String accountName, char[] input) {
        boolean isCorrect = true;
        char[] correctPassword = accountDAO.getPassword(accountName);
        // Check length of both passwords       
        if (input.length != correctPassword.length) {           
            return false;
        // Check if both passwords match
        } else {
            isCorrect = Arrays.equals(input, correctPassword);
        }
          
        return isCorrect;
    }
    public boolean isValidAccount(JTextField nameField, JPasswordField passField) {
        if (!accountDAO.doesAccountExists(nameField.getText())) {
            showErrorDialog("Username not found");
            return false;
        } 
        if (!isPasswordCorrect(nameField.getText(), passField.getPassword())) {
            
            showErrorDialog("Password is incorrect");
            return false;
        }

        
        return true;

    }
    
    
    
   
    public String removeTrailingWhiteSpaces(String string) {
        return string.replaceAll("\\s+$", "");
    }

}
