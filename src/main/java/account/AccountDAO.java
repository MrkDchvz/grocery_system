/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package account;

/**
 *
 * @author Marku
 */
import database.MyConnection;
import java.sql.Connection;
import java.util.logging.Logger;
import products.ProductDAO;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class AccountDAO {


    private static final Logger LOGGER = Logger.getLogger(AccountDAO.class.getName());

    public AccountDAO() {
        
    }

    public boolean doesAccountExists(String name) {
        String sql = "SELECT account_name FROM accounts WHERE account_name = ?;";
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return true;
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error checking if account exists", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error checking if account exists", e);
        }
        return false;

    }

    
    public char[] getPassword(String accountName) {
        String sql = "SELECT password FROM accounts WHERE account_name = ?;";
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, accountName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("password").toCharArray();
                }
            }
            
        }catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error getting the password", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error getting the password", e);
        }
        return null;
    }
    
    public AccountTypes getAccountType(String accountName) {
        String sql = "SELECT account_type FROM accounts WHERE account_name = ?;";
        try (Connection con = MyConnection.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            preparedStatement.setString(1, accountName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String accType = resultSet.getString("account_type");
                    return AccountTypes.valueOf(accType.toUpperCase());
                    
                }
            }
            
        }catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error getting the account type", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error getting the account type", e);
        }
        return null;
    }
    
   
    
    
 

}
