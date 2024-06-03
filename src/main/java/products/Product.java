/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package products;

import java.time.LocalDate;
import java.sql.Date;

/**
 *
 * @author Marku
 */
public class Product {
    private int productId;
    private String productName;
    private int quantityAvailable;
    private String productType;
    private double price;
    private Date deliveryDate;
    private Date expiryDate;
    
    
    public Product(String name, int quantity, String type, double price, Date deliveryDate, Date expiryDate ) {
        this.productName = name;
        this.quantityAvailable = quantity;
        this.productType = type;
        this.price = price;
        this.deliveryDate = deliveryDate;
        this.expiryDate = expiryDate;
        
    }
//    Overload Constructor with ID
    public Product(int id, String name, int quantity, String type, double price, Date deliveryDate, Date expiryDate ) {
        this.productId = id;
        this.productName = name;
        this.quantityAvailable = quantity;
        this.productType = type;
        this.price = price;
        this.deliveryDate = deliveryDate;
        this.expiryDate = expiryDate;
        
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    
    
    
    
    
    
}
