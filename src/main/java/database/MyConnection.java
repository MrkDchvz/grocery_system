/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.DriverManager;
import java.sql.Connection;

/**
 *
 * @author Marku
 */
public class MyConnection {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String SERVER = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE_NAME = "grocery_system";
    private static Connection con = getConnection();
    
    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            String dataconn = "jdbc:mysql://" + SERVER + ":" + PORT + "/" + DATABASE_NAME;
            con = DriverManager.getConnection(dataconn, USERNAME, PASSWORD);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return con;
    }
    
   
    
    
}

