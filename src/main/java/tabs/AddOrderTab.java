/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tabs;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import orders.OrderDAO;
import orders.OrderDetail;
import products.Product;
import products.ProductDAO;
import utils.ValidationUtils;
import utils.DateUtils;
import cellrenderer.QuantityCellRenderer;

/**
 *
 * @author Marku
 */
public class AddOrderTab {

    private JTextField orderIdField;
    private JTextField orderDateField;
    private JTextField accountIdField;
    private JTextField productIdField;
    private JTextField productNameField;
    private JSpinner quantityField;
    private JTextField basePriceField;
    private JTextField productTotal;
    private JTextField orderTotal;
    private JTable table;
    private JTextField productSearchField;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;

    private DefaultTableModel model;

    private ValidationUtils validation;

//    This hashmap is for O(1) complexity when searching products on the table
//    HashMap<productId, rowNumber>
    private HashMap<Integer, Integer> productMap;

    public AddOrderTab() {
        this.productDAO = new ProductDAO();
        this.orderDAO = new OrderDAO();
        this.productMap = new HashMap<>();
        this.validation = new ValidationUtils();
    }

    private void setSpinnerMaximumValue(JSpinner spinner, int maxValue) {
        int minValue = 1;
        int initialValue = 1;
        int stepSize = 1;
        SpinnerNumberModel numberModel = new SpinnerNumberModel(initialValue, minValue, maxValue, stepSize);
        spinner.setModel(numberModel);
    }

    public void searchProductId() {
        if (validation.isPositiveInteger(this.productIdField.getText())) {
            int productId = Integer.parseInt(this.productIdField.getText());
            if (productDAO.doesProductExists(productId)) {
                if (productDAO.hasStock(productId)) {
                    this.quantityField.setEnabled(true);
                    Product selectedProduct = productDAO.getProduct(productId);
                    this.productNameField.setText(selectedProduct.getProductName());
                    this.basePriceField.setText(String.valueOf(selectedProduct.getPrice()));
                    this.productTotal.setText(String.valueOf(selectedProduct.getPrice()));
                    setSpinnerMaximumValue(this.quantityField, selectedProduct.getQuantityAvailable());
                    this.productIdField.setEnabled(false);
                } else {
                    validation.showErrorDialog("Product is out of stock.");
                }

            } else {
                validation.showErrorDialog("Product doesn't exist.");
            }

        }
    }

    public void insertProductToTable() {
        if (validation.isPositiveInteger(this.productIdField.getText())) {
            int productId = Integer.parseInt(this.productIdField.getText());
            if (!this.productMap.containsKey(productId)) {
//            Insert Selected productId in Table
                this.model.addRow(new Object[]{this.productIdField.getText(),
                    this.productNameField.getText(),
                    String.valueOf(this.quantityField.getValue()),
                    this.productTotal.getText()});
//           Insert Select ProductId in hashMap
//           subtract the row count into 1 because the rows in the table is zero-based indexed (first row is 0).
                this.productMap.put(productId, model.getRowCount() - 1);
                sumOrderTotal();

            } else {
                validation.showErrorDialog("Product is already in table");
            }

            clearProduct();
        } else {
            validation.showErrorDialog("No product input. Please Input a product.");
        }

    }

    public void deleteRecentProduct() {
        final int PRODUCTID_COLUMN = 0;
        final int QUANTITY_COLUMN = 3;

        int lastRow = this.model.getRowCount() - 1;
        if (!validation.isTableEmpty(this.table)) {
            // Get the productId of the last row of the table
            Integer productId = convertObjectToInt(this.model.getValueAt(lastRow, PRODUCTID_COLUMN));
            // remove productId from productMap and table            
            this.model.removeRow(lastRow);
            this.productMap.remove(productId);

            sumOrderTotal(); // Recalculate the order total upon removing a row

        } else {
            validation.showErrorDialog("There is no product to be removed.");
        }
    }

    public void clearProduct() {
        this.productIdField.setText("");
        this.productNameField.setText("");
        this.quantityField.setEnabled(false);
        this.basePriceField.setText("");
        this.productTotal.setText("");

        this.productIdField.setEnabled(true);
    }

    public void setProductTotalPrice() {
        try {
            double basePrice = Double.parseDouble(this.basePriceField.getText());
            double multiplier = Double.parseDouble(this.quantityField.getValue().toString());
            double totalPrice = basePrice * multiplier;
            this.productTotal.setText(String.valueOf(totalPrice));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sumOrderTotal() {
        final int PRICE_COLUMN = 3;
        int lastRowIndex = this.model.getRowCount();
        double sumTotal = 0;
        for (int row = 0; row < lastRowIndex; row++) {
            double productPrice = convertObjectToDouble(model.getValueAt(row, PRICE_COLUMN));
            sumTotal += productPrice;
        }
        this.orderTotal.setText(String.valueOf(sumTotal));

    }

    public void clearOrder() {
        if (!validation.isTableEmpty(this.table)) {
            this.model.setRowCount(0);
            this.productMap.clear();

            sumOrderTotal();
            clearProduct();
        }
    }

    public void addOrderWithDetails() {
        if (!validation.isTableEmpty(this.table)) {
            //              Start the transaction
            orderDAO.openConnection();
            orderDAO.setAutoCommit(false);
            try {
                List<OrderDetail> orderDetails = generateListFromTable();
             
                int orderId = orderDAO.addOrder(Integer.parseInt(this.accountIdField.getText()), DateUtils.parseDate(this.orderDateField.getText()), Double.parseDouble(this.orderTotal.getText()));

                productDAO.batchSetQuantity(orderDetails);
                orderDAO.batchAddOrderDetails(orderId, orderDetails);

                 orderDAO.commitTransaction();
                 validation.showDialog("Order Successfully created.");
                 clearOrder();

            } catch (SQLException ex) {
                orderDAO.rollbackTransaction();
                validation.showErrorDialog("Failed to create order.");
                ex.printStackTrace();

            } finally {
                orderDAO.rollbackTransaction();
                orderDAO.setAutoCommit(true);       
                orderDAO.closeConnection();
            }

        } else {
            validation.showErrorDialog("Order list is empty.");
        }
        // Turn back commit behavior to default (autoCommit)
    }

    private List<OrderDetail> generateListFromTable() {
        final int PRODUCT_ID_COLUMN = 0;
        final int QUANTITY_COLUMN = 2;
        int rowCount = this.model.getRowCount();
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (int row = 0; row < rowCount; row++) {
            int productId = convertObjectToInt(model.getValueAt(row, PRODUCT_ID_COLUMN));
            int quantity = convertObjectToInt(model.getValueAt(row, QUANTITY_COLUMN));
            OrderDetail orderDetail = new OrderDetail(productId, quantity);
            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }

    private double convertObjectToDouble(Object obj) {
        return Double.parseDouble(obj.toString());
    }

    private int convertObjectToInt(Object obj) {
        return Integer.parseInt(obj.toString());
    }

// SETTERS
    public void setOrderIdField(JTextField orderIdField) {
        this.orderIdField = orderIdField;
        this.orderIdField.setText(String.valueOf(orderDAO.getMax()));

    }

    public void setOrderDateField(JTextField orderDateField) {
        this.orderDateField = orderDateField;
        String currentDate = DateUtils.formatDate(DateUtils.getCurrentDate(), "MMM d, yyyy");
        this.orderDateField.setText(currentDate);
    }

    public void setAccountIdField(JTextField accountIdField) {
        this.accountIdField = accountIdField;
    }

    public void setProductIdField(JTextField productIdField) {
        this.productIdField = productIdField;
    }

    public void setProductNameField(JTextField productNameField) {
        this.productNameField = productNameField;
    }

    public void setQuantityField(JSpinner quantityField) {
        this.quantityField = quantityField;
        ((JSpinner.DefaultEditor) this.quantityField.getEditor()).getTextField().setEditable(false);
        this.quantityField.setEnabled(false);

    }

    public void setBasePriceField(JTextField basePriceField) {
        this.basePriceField = basePriceField;
    }

    public void setProductTotal(JTextField productTotal) {
        this.productTotal = productTotal;
    }

    public void setOrderTotal(JTextField orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void setTable(JTable table) {
        this.table = table;
        this.table.setRowHeight(30);
        this.table.setShowGrid(true);
        this.table.setGridColor(Color.black);
        this.table.setBackground(Color.white);
        this.model = (DefaultTableModel) this.table.getModel();
        this.table.setShowGrid(false);
        
    }

    public void setProductSearchField(JTextField productSearchField) {
        this.productSearchField = productSearchField;
    }

}
