/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package orders;

import java.sql.Connection;
import database.MyConnection;
import java.util.logging.Logger;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Marku
 */
public class OrderDAO {

    private Connection con;
    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());
//    Constant for displaying all orders
    public static final int DISPLAY_ALL = -1;
    private int displayLimit = 10;

    public OrderDAO() {
        
    }

    public int getMax() {
        int id = 0;
        String sql = "SELECT max(order_id) as max_id FROM orders;";
        try (Connection con = MyConnection.getConnection()) {
             try (Statement statement = con.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                 while (resultSet.next()) {
                id = resultSet.getInt("max_id");
            }
             }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get the current ID of orders", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error searching for the current of the orders", e);
        }
        return id + 1;
       
    }
    
    public boolean doesOrderExists(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ? and is_void = 0;";
        
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, orderId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
                
            }
            
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to check if the product is available", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error searching product availability", e);

        }
        return false;
        
    }

    public int addOrder(int accountId, Date orderDate, double totalAmount) throws SQLException {
        String sql = "INSERT INTO orders(account_id, order_date, total_amount) VALUES (?,?,?);";
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, accountId);
            preparedStatement.setDate(2, orderDate);
            preparedStatement.setDouble(3, totalAmount);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Get the auto_incremented id from the databsae                
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
                JOptionPane.showMessageDialog(null, "Order added successfully");
            } else {
                JOptionPane.showMessageDialog(null, "Order input failed");
            }

        }
        throw new SQLException("Failed to insert order in orders table");
    }
    
    public int deleteOrder(int orderId) {
        String sql = "UPDATE orders SET is_void = 1 WHERE order_id = ?;";
        
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, orderId);
            return preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error deleting order", e);
        }
        
        return 0;
    }

    private PreparedStatement executeOrderSearchQuery(int searchValue, Connection con) throws SQLException {
//       Search orders via orderId (order must not a void order)
        String sql;

        if (searchValue == DISPLAY_ALL) {
            sql = "SELECT * FROM orders WHERE is_void = 0 ORDER BY order_id DESC LIMIT ?;";

        } else {
            sql = "SELECT * FROM orders "
                    + "WHERE order_id = ? AND is_void = 0 "
                    + "ORDER BY order_id DESC "
                    + "LIMIT ?;";
        }

//            If there is a searchValue Provided then set the placeholder to the searchValue.
        
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            if (searchValue != DISPLAY_ALL) {
                preparedStatement.setInt(1, searchValue);
                preparedStatement.setInt(2, this.displayLimit);
            } else {
                preparedStatement.setInt(1, this.displayLimit);
            }

            return preparedStatement;
        

    }
    
    private PreparedStatement executeOrderDetailsSearchQuery(int orderId, Connection con) throws SQLException {
        String sql = "SELECT\n"
                + "    products.product_id AS product_id,\n"
                + "    products.product_name AS product_name,\n"
                + "    order_details.product_quantity AS product_quantity,\n"
                + "    (order_details.product_quantity * products.price) AS price\n"
                + "FROM\n"
                + "    products\n"
                + "    INNER JOIN order_details ON order_details.product_id = products.product_id\n"
                + "WHERE\n"
                + "    order_details.order_id = ?;";       
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            return preparedStatement;       
   
    }

    public void searchAndDisplayOrders(JTable table, int searchValue) {
        
        

        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = executeOrderSearchQuery(searchValue, con); ResultSet resultSet = preparedStatement.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear Existing row before populating to avoid duplicates

            ResultSetMetaData metaData = resultSet.getMetaData(); // Get metadata to get column properties and count
            int columnCount = metaData.getColumnCount(); // Dynamically Calculate columnCount for object array

            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                row[0] = resultSet.getInt("order_id");
                row[1] = resultSet.getDate("order_date");
                row[2] = resultSet.getInt("account_id");
                row[3] = resultSet.getInt("total_amount");

                model.addRow(row);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL Error populating orders table", ex);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unexpected Error populating the orders  table", ex);
        }
    }
    
    public void searchAndDisplayOrderDetails(JTable table, int orderId) {
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = executeOrderDetailsSearchQuery(orderId, con); ResultSet resultSet = preparedStatement.executeQuery()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0); // Clear Existing row before populating to avoid duplicates
            
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                row[0] = resultSet.getInt("product_id");
                row[1] = resultSet.getString("product_name");
                row[2] = resultSet.getInt("product_quantity");
                row[3] = resultSet.getInt("price");

                model.addRow(row);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error populating order details table", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error populating the order details  table", e);
        }
    }

    public void batchAddOrderDetails(int orderId, List<OrderDetail> orderDetails) throws SQLException {
        String sql = "INSERT INTO order_details(order_id, product_id, product_quantity) VALUES(?, ?, ?);";
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            for (OrderDetail detail : orderDetails) {
                preparedStatement.setInt(1, orderId);
                preparedStatement.setInt(2, detail.getProductId());
                preparedStatement.setInt(3, detail.getProductQuantity());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

        }
    }
    
    public Order getOrder(int orderId) {
        String sql = "SELECT order_date, account_id, total_amount FROM orders WHERE order_id = ?;";
        
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setInt(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Date orderDate = resultSet.getDate("order_date");
                    int accountId = resultSet.getInt("account_id");
                    double totalAmount = resultSet.getDouble("total_amount");
                    return new Order(orderId, accountId, orderDate, totalAmount);
                    
                }
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error getting order", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error getting order", e);
        }
        
        return null;
        
    }

    public void setAutoCommit(boolean input) {
        try  {
            con.setAutoCommit(input);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to configure Connection.setAutoCommit() method", e);
        }
    }

    public void rollbackTransaction() {
        try {
            con.rollback();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to rollback transaction",e);
        }
    }

    public void commitTransaction() {
        try {
            con.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to commit transaction", e);
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setDisplayLimit(int displayLimit) {
        this.displayLimit = displayLimit;
    }
    
    public void closeConnection() {
        try {
            this.con.close();
        } catch (SQLException e) {
             LOGGER.log(Level.SEVERE, "Failed to close connection", e);
        }
        
    }
    
    public void openConnection() {
        this.con = MyConnection.getConnection();
    }
    
    

}
