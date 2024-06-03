/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package account;

/**
 *
 * @author Marku
 */
public class Account {
    private int accountId;
    private String accountType;
    private String accountName;
    private String password;
    
    public Account(int accountId, String accountType, String accountName, String password) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.accountName = accountName;
        this.password = password;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getPassword() {
        return password;
    }

    
    
   
    
}
