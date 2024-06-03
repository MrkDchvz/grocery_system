/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package products;

import java.sql.ResultSetMetaData;
import database.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import orders.OrderDetail;

/**
 *
 * @author Marku
 */
// This class handles all data operations of product
public class ProductDAO {

    private int display_limit = 10;

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    public ProductDAO() {
        
    }

//    Simple processing does not requires seperation of concerns so its fine to have multiple resources in a single try-with-resources block
    public int getMax() {
        int id = 0;
        String sql = "SELECT max(product_id) as max_id from products;";
        try (Connection con = MyConnection.getConnection(); Statement statement = con.createStatement(); ResultSet result_set = statement.executeQuery(sql)) {
            while (result_set.next()) {
                id = result_set.getInt("max_id");
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to get the current ID of products");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected Error searching for the current of the products");
        }

        return id + 1;
    }
// Complex processing requires seperation of concerns (for readability and maintainability) nested try-with-resources block is required

    public boolean doesProductExists(String product_name) {

        String sql = "SELECT product_name FROM products WHERE product_name = ? and is_available = 1;";

        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, product_name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }
//    Overloaded method with productID

    public boolean doesProductExists(int productId) {
        String sql = "SELECT product_name FROM products WHERE product_id = ? and is_available = 1;";

        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, productId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public boolean insertProduct(String name, Integer quantity, String type, double price, Date delivery, Date expiry) {
        String sql = "INSERT INTO products(product_name, quantity_available, product_type, price, delivery_date, expiry_date)"
                + "VALUES (?,?,?,?,?,?)";

        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, quantity);
            preparedStatement.setString(3, type);
            preparedStatement.setDouble(4, price);
//            Note: java.sql.Date and java.util.Data are different for sql queries convert a date string into sql.Date via valueOf method
            preparedStatement.setDate(5, delivery);
            preparedStatement.setDate(6, expiry);
//          Check if there are rows affected (inserted in this instance) 
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unexpected SQL Error adding the product");

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected Error adding the product");
        }
        return false;
    }

    public boolean updateProduct(Integer id, Integer quantity, String type, double price, Date delivery, Date expiry) {
        String sql = "UPDATE products "
                + "SET quantity_available = ?, product_type = ?, price = ?, delivery_date = ?, expiry_date = ? "
                + "WHERE product_id = ?;";

        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setString(2, type);
            preparedStatement.setDouble(3, price);
            preparedStatement.setDate(4, delivery);
            preparedStatement.setDate(5, expiry);
            preparedStatement.setInt(6, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unexpected SQL Error updating the product", ex);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected Error updating the product");
        }

        return false;

    }

    public boolean deleteProduct(Integer id) {
        String sql = "UPDATE products "
                + "SET is_available = 0 "
                + "WHERE product_id = ?;";
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return true;
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected Error deleting the product");

        }
        return false;

    }

    private PreparedStatement executeSearchQuery(String searchValue, Connection con) throws SQLException {
        String sql = "SELECT * FROM products "
                + "WHERE (product_id LIKE ? OR product_name LIKE ? OR product_type LIKE ?) AND is_available = 1 "
                + "ORDER BY product_id DESC "
                + " LIMIT ?";

        PreparedStatement preparedStatement = con.prepareStatement(sql);
        String param = "%" + searchValue + "%";
        preparedStatement.setString(1, param);
        preparedStatement.setString(2, param);
        preparedStatement.setString(3, param);
        preparedStatement.setInt(4, this.display_limit);

        return preparedStatement;
    }

    public void lazySearchAndDisplayProducts(JTable table, String searchValue) {
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = executeSearchQuery(searchValue, con); ResultSet resultSet = preparedStatement.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear Existing row before populating to avoid duplicates

            ResultSetMetaData metaData = resultSet.getMetaData(); // Get metadata to get column properties and count
            int columnCount = metaData.getColumnCount(); // Dynamically Calculate columnCount for object array

            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                row[0] = resultSet.getInt("product_id");
                row[1] = resultSet.getString("product_name");
                row[2] = resultSet.getInt("quantity_available");
                row[3] = resultSet.getString("product_type");
                row[4] = resultSet.getDouble("price");
                row[5] = resultSet.getDate("delivery_date");
                row[6] = resultSet.getDate("expiry_date");

                model.addRow(row);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL Error populating product table", ex);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected Error populating the products table");
        }
    }

    // Overload method with JcomboBox
    public void lazySearchAndDisplayProducts(JComboBox comboBox, String searchValue) {

        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = executeSearchQuery(searchValue, con); ResultSet resultSet = preparedStatement.executeQuery()) {

            comboBox.removeAllItems();
            comboBox.addItem("Select Product Id");
            while (resultSet.next()) {
                comboBox.addItem(resultSet.getString("product_id"));
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL Error populating product combo box", ex);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected Error populating the products combo Box");

        }

    }

    public int getQuantity(String productName) {
        String sql = "SELECT quantity_available FROM products WHERE product_name = ?;";

        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, productName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("quantity_available");
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL Error getting the product's quantity", ex);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected Error getting the product's quantity");
        }

        return 0; // Return 0 if product doesn't exist is an error occured
    }

//    Overloaded method with productId
    public int getQuantity(int productId) {
        String sql = "SELECT quantity_available FROM products WHERE product_id = ?;";

        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("quantity_available");
                }
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL Error getting the product's quantity", ex);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected Error getting the product's quantity");
        }

        return 0; // Return 0 if product doesn't exist is an error occured
    }

    public boolean hasStock(int productId) {
        String sql = "SELECT quantity_available FROM products WHERE product_id = ?;";
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getInt("quantity_available") > 0) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error getting product's stock ", e);
        }
        return false;
    }

    public void batchSetQuantity(List<OrderDetail> orderDetails) throws SQLException {
        String sql = "UPDATE products SET quantity_available = quantity_available - ? WHERE product_id = ?;";

        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            for (OrderDetail detail : orderDetails) {
                preparedStatement.setInt(1, detail.getProductQuantity());
                preparedStatement.setInt(2, detail.getProductId());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

        }

    }

    public void setDisplay_limit(int display_limit) {
        this.display_limit = display_limit;
    }

    public Product getProduct(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?;";
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("product_id");
                    String name = resultSet.getString("product_name");
                    int quantity = resultSet.getInt("quantity_available");
                    String type = resultSet.getString("product_type");
                    double price = resultSet.getDouble("price");
                    Date delivery = resultSet.getDate("delivery_date");
                    Date expiry = resultSet.getDate("expiry_date");
                    return new Product(id, name, quantity, type, price, delivery, expiry);
                } else {
                    LOGGER.log(Level.SEVERE, "Product not Found");
                }

            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL Error getting the product", ex);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected Error getting the product");
        }

//        
        return null;
    }

}
